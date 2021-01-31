package org.jxtech.propertytrade.platform.listing.controller.buyer

import com.fasterxml.jackson.databind.ObjectMapper
import org.jxtech.propertytrade.platform.listing.controller.ListingCreatedEvent
import org.jxtech.propertytrade.platform.listing.service.ListingEventListener
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.MessageHandler
import org.springframework.messaging.SubscribableChannel
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.concurrent.CopyOnWriteArraySet
import javax.annotation.PostConstruct

class ListingEventPushController: TextWebSocketHandler() {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ListingEventPushController::class.java)
    }
    private val objectMapper = ObjectMapper()
    private val sessions = CopyOnWriteArraySet<WebSocketSession>()

    @Autowired
    lateinit var listingCreateEventChannel: SubscribableChannel

    @PostConstruct
    fun init() {
        listingCreateEventChannel.subscribe(MessageHandler {message ->
            logger.info("Received event from listing created event channel: {}", message)
            val eventString = message.payload as String
            sessions.forEach {session ->
                logger.info("Sending listing created event to session: {}", session.id)
                session.sendMessage(TextMessage(eventString))
            }
        })
    }


    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        logger.info("Websocket session connection closed: {}", session.id)
        sessions.remove(session)
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        logger.info("Websocket session connection established: {}", session.id)
        sessions.add(session)
    }
}
