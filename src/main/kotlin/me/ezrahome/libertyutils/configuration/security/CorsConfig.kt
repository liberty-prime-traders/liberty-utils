package me.ezrahome.libertyutils.configuration.security

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import kotlin.collections.toTypedArray
import kotlin.text.split

@Configuration
@EnableWebMvc
class CorsConfig: WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        val originPatterns = System.getenv("CORS_PATTERNS").split(',').toTypedArray()
        registry.addMapping("/**")
            .allowedOrigins(*originPatterns)
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
    }
}
