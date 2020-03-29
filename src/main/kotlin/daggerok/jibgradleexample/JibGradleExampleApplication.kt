package daggerok.jibgradleexample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.router
import reactor.kotlin.core.publisher.toMono
import java.util.function.Supplier

fun ServerResponse.BodyBuilder.toBody(map: Any) = this.body(map.toMono())

@SpringBootApplication
class JibGradleExampleApplication {

  @Bean
  fun routes(env: Environment): RouterFunction<ServerResponse> {
    return router {
      "/api".nest {
        path("/**") {
          ok().toBody(mapOf("message" to env.getProperty("greeting.message", "fck...")))
        }
      }
      path("/**") {
        ok().toBody(mapOf(
            "_self ${it.methodName()}" to it.uri(),
            "hello GET" to it.uri().run { "$scheme://$authority/api/" }
        ))
      }
    }
  }
}

fun main(args: Array<String>) {
  runApplication<JibGradleExampleApplication>(*args)
}
