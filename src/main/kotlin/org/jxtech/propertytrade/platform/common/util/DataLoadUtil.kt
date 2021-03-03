package org.jxtech.propertytrade.platform.common.util

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.stream.Collectors

object DataLoadUtil {
    private val objectMapper = ObjectMapper()

    init {
        objectMapper.dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    }

    fun loadTestData(fileName: String): String {
        val inputStream = DataLoadUtil::class.java.classLoader.getResourceAsStream(fileName)
        return String(inputStream.readBytes(), Charsets.UTF_8)
    }

    fun loadTestDataAsLines(fileName: String): List<String> {
        val inputStream = DataLoadUtil::class.java.classLoader.getResourceAsStream(fileName)
        return BufferedReader(InputStreamReader(inputStream)).lines().collect(Collectors.toList()) as List<String>
    }

    fun <T> loadTestDataAsPojo(fileName: String, targetClass: Class<T>): T {
        val inputStream = DataLoadUtil::class.java.classLoader.getResourceAsStream(fileName)
        return objectMapper.readValue(inputStream, targetClass)
    }

    fun <T> loadTestDataAsPojoList(fileName: String, targetClass: Class<T>): List<T> {
        val inputStream = DataLoadUtil::class.java.classLoader.getResourceAsStream(fileName)
        val collectionType = objectMapper.typeFactory.constructCollectionType(List::class.java, targetClass)
        return objectMapper.readValue(inputStream, collectionType)
    }
}
