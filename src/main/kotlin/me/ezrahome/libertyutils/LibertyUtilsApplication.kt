package me.ezrahome.libertyutils

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
@EnableMethodSecurity
class LibertyUtilsApplication

fun main(args: Array<String>) {
	runApplication<LibertyUtilsApplication>(*args)
}
