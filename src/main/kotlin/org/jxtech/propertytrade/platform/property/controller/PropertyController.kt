package org.jxtech.propertytrade.platform.property.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.jxtech.propertytrade.platform.property.api.PropertyDetailPageData
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/property")
class PropertyController {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(PropertyController::class.java)
    }


    @GetMapping("/{propertyId}")
    @ResponseBody
    fun getPropertyDetail(@PathVariable("propertyId") propertyId: String):  PropertyDetailPageData {
        val objectMapper = ObjectMapper()
        val contentStr = "{" +
            "            \"breadcrumb\": { \"items\":\n" +
            "            [{\"active\":false, \"link\":\"\", \"name\":\"New York\"},\n" +
            "                {\"active\":false, \"link\":\"\", \"name\":\"Queens\"},\n" +
            "                {\"active\":false, \"link\":\"\", \"name\":\"Astrolia\"},\n" +
            "                {\"active\":false, \"link\":\"\", \"name\":\"Ditmars-Steinway\"},\n" +
            "                {\"active\":true, \"link\":\"\", \"name\": \"21 -21 31st Street #2F\"}\n" +
            "            ]\n" +
            "        },\n" +
            "            \"carousel\": { \"items\":\n" +
            "            [\n" +
            "                {\"active\":false, \"link\":\"../image/418737234.jpg\", \"name\":\"Main Lobby\"},\n" +
            "                {\"active\":false, \"link\":\"../image/418737268.jpg\", \"name\":\"Bedroom\"},\n" +
            "                {\"active\":true, \"link\":\"../image/418737285.jpg\", \"name\":\"Bath room\"}\n" +
            "            ]\n" +
            "        }\n" +
            "        }"
        return objectMapper.readValue(contentStr, PropertyDetailPageData::class.java)
    }

}
