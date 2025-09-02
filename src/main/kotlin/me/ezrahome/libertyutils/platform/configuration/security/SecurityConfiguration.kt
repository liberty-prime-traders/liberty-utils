package me.ezrahome.libertyutils.platform.configuration.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@EnableWebSecurity
@Configuration
class SecurityConfiguration {

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeHttpRequests {
                it.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .requestMatchers("/secured/**").authenticated()
                    .anyRequest().permitAll()
            }
            .csrf { it.disable() }
            .cors(Customizer.withDefaults())
            .oauth2ResourceServer { it.jwt(Customizer.withDefaults()) }
            .build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val originPatterns = System.getenv("CORS_PATTERNS")?.split(",") ?: listOf()
        val configuration = CorsConfiguration().apply {
            this.allowedOriginPatterns = originPatterns
            this.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
            this.allowedHeaders = listOf("*")
            this.allowCredentials = true
        }

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/secured/**", configuration)
        return source
    }
}
