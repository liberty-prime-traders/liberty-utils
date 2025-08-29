package me.ezrahome.libertyutils.configuration.cache

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.springframework.cache.annotation.CachingConfigurer
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@EnableCaching
class CacheConfig : CachingConfigurer {

    companion object {
        private val GSON: Gson = GsonBuilder()
            .registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY)
            .create()
    }

    @Bean
    override fun keyGenerator(): KeyGenerator {
        return KeyGenerator { target, method, params ->
            val jsonParams = GSON.toJson(params)
            "${target::class.simpleName ?: "Unknown"}:${method.name}:${jsonParams}"
        }
    }
}
