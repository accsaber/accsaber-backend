package de.ixsen.accsaber.business

import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface HasLogger {
    fun getLogger(): Logger = LoggerFactory.getLogger(this.javaClass)
}