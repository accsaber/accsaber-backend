package de.ixsen.accsaber.api.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.servers.Server
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.view.InternalResourceViewResolver
import springfox.documentation.oas.web.OpenApiTransformationContext
import springfox.documentation.oas.web.WebMvcOpenApiTransformationFilter
import springfox.documentation.spi.DocumentationType
import java.util.*
import javax.servlet.http.HttpServletRequest


@Configuration
@EnableWebMvc
class WebConfig(@Value("\${accsaber.allowed-origins}") private val allowedOrigins: Array<String>) : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**").allowedOrigins(*allowedOrigins)
    }

    @Bean
    fun defaultViewResolver(): InternalResourceViewResolver {
        return InternalResourceViewResolver()
    }

    @Component
    class SpringfoxSwaggerHostResolver : WebMvcOpenApiTransformationFilter {
        @Value("\${accsaber.swagger-host}")
        var hostUri: String? = null
        override fun transform(context: OpenApiTransformationContext<HttpServletRequest>): OpenAPI {
            val swagger = context.specification
            val server = Server()
            server.url = hostUri
            swagger.servers = Arrays.asList(server)
            return swagger
        }

        override fun supports(docType: DocumentationType): Boolean {
            return docType == DocumentationType.OAS_30
        }
    }
}