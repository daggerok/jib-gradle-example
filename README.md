# jib it
jib gradle plugin

```bash
# build and push without docker!
.gradlew jib

# turn on docker and test it!
docker run --rm -it -p 8080:8080 -e GREETING_MESSAGE='привет!' daggerok/daggerok-jib-gradle-example
http :8080/ololo/trololo
```

## resources

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/gradle-plugin/reference/html/)
* [Coroutines section of the Spring Framework Documentation](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/languages.html#coroutines)
* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)
