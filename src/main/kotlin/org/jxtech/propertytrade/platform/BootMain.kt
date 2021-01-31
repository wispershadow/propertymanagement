package org.jxtech.propertytrade.platform

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["org.jxtech.propertytrade.platform"])
class BootMain

fun main(args: Array<String>) {
    runApplication<BootMain>(*args)
}

