package de.ixsen.accsaber.integration.connector

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

abstract class AbstractConnector  {

    protected val logger: Logger = LoggerFactory.getLogger(this.javaClass)
    private val webClient: WebClient = WebClient.create()

    @Value("\${accsaber.connector-user-agent}")
    private var userAgent: String = ""

    protected fun getRequest(requestUrl: String): WebClient.ResponseSpec {
        return webClient.get()
            .uri(requestUrl)
            .header("User-Agent", this.userAgent)
            .retrieve()
    }

    protected fun getRequest(requestUrl: String, mediaType: MediaType): WebClient.ResponseSpec {
        return webClient.get()
            .uri(requestUrl)
            .header("User-Agent", this.userAgent)
            .accept(mediaType)
            .retrieve()
    }
}