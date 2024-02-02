package com.boki.bokiapiclienttest

import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import java.net.InetAddress

@Component
class ServerStartedListener : ApplicationListener<ApplicationReadyEvent> {
    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        val environment = event.applicationContext.environment
        val port = environment.getProperty("local.server.port")
        val hostName = InetAddress.getLocalHost().hostName
        println("Server is running on: http://$hostName:$port")
    }
}