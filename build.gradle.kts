plugins {
  kotlin("jvm") version "1.3.71"
  kotlin("plugin.spring") version "1.3.71"
  id("org.ajoberstar.reckon") version "0.12.0"
  id("com.google.cloud.tools.jib") version "2.1.0"
  id("org.springframework.boot") version "2.2.6.RELEASE"
  id("io.spring.dependency-management") version "1.0.9.RELEASE"
  id("com.github.ben-manes.versions") version  "0.28.0"
}

val gradleWrapperVersion = "6.3"
val javaVersion = JavaVersion.VERSION_1_8

group = "daggerok"
java.sourceCompatibility = javaVersion

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
  testImplementation("org.springframework.boot:spring-boot-starter-test") {
    exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
  }
  testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    showCauses = true
    showExceptions = true
    showStackTraces = true
    showStandardStreams = true
    events(
        org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
        org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
        org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
    )
  }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "$javaVersion"
  }
}

tasks {
  withType(Wrapper::class.java) {
    gradleVersion = gradleWrapperVersion
    distributionType = Wrapper.DistributionType.BIN
    // distributionType = Wrapper.DistributionType.ALL
  }
}

reckon {
  scopeFromProp()
  // stageFromProp()
  snapshotFromProp()
}

tasks {
  register("version") {
    println(project.version.toString())
  }
  register("status") {
    doLast {
      val status = grgit.status()
      status?.let { s ->
        println("workspace is clean: ${s.isClean}")
        if (!s.isClean) {
          if (s.unstaged.allChanges.isNotEmpty()) {
            println("""all unstaged changes: ${s.unstaged.allChanges.joinToString(separator = "") { i -> "\n - $i" }}""")
          }
        }
      }
    }
  }
  jib { // reckon version pass in jib tags workaround
    this.to.tags = setOf(project.version.toString())
  }
}

jib {
  from {
    image = "openjdk:8u242-jre"
  }
  to {
    image = "daggerok/$group-$name"
  }
}

defaultTasks("clean", "build")
