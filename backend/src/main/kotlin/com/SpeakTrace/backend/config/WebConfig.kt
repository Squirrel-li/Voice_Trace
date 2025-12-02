package com.SpeakTrace.backend.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.beans.factory.annotation.Value

@Configuration
class WebConfig : WebMvcConfigurer {

    @Value("\${videos.path}") // 從 application.properties 或 application.yml 中讀取路徑
    private lateinit var videosPath: String

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/videos/**")
            .addResourceLocations("file:$videosPath") // 動態設置路徑
    }
}