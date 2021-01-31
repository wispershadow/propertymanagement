package org.jxtech.propertytrade.platform.listing.controller.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.channel.DirectChannel
import org.springframework.messaging.SubscribableChannel
import org.springframework.web.context.support.HttpRequestHandlerServlet

@Configuration
class ChannelConfig {
    @Bean
    @Qualifier("listingCreateEventChannel")
    fun listingCreateEventChannel(): SubscribableChannel {
        return DirectChannel()
    }

    //@Bean
    fun customServlet(): ServletRegistrationBean<HttpRequestHandlerServlet> {
        return ServletRegistrationBean(httpRequestHandlerServlet(), "/web")
    }

    //@Bean
    fun httpRequestHandlerServlet(): HttpRequestHandlerServlet {
        return HttpRequestHandlerServlet()
    }
}
