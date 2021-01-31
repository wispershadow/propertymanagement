package org.jxtech.propertytrade.platform.listing.controller.config

// import org.eclipse.jetty.websocket.api.WebSocketBehavior
// import org.eclipse.jetty.websocket.api.WebSocketPolicy
import org.jxtech.propertytrade.platform.listing.controller.buyer.ListingEventPushController
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy
import org.springframework.web.socket.server.support.DefaultHandshakeHandler
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor

@Configuration
@EnableWebSocket
class WebSocketConfig: WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(listingEventPushController(), "/listingEventPush/**")
            .addInterceptors(HttpSessionHandshakeInterceptor()).setHandshakeHandler(
                handshakeHandler()
            )
    }

    //jetty only
    /*
    @Bean
    fun handshakeHandler(): DefaultHandshakeHandler {
        val policy = WebSocketPolicy(WebSocketBehavior.SERVER)
        policy.inputBufferSize = 8192
        policy.idleTimeout = 600000

        return DefaultHandshakeHandler(
            JettyRequestUpgradeStrategy(policy)
        )
    }
     */

    @Bean
    fun handshakeHandler(): DefaultHandshakeHandler {
        return DefaultHandshakeHandler(
            TomcatRequestUpgradeStrategy()
        )
    }


    @Bean
    fun listingEventPushController(): ListingEventPushController {
        return ListingEventPushController()
    }
}
