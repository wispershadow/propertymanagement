package org.jxtech.propertytrade.platform.property.persistence

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.jxtech.propertytrade.platform.property.persistence.config.PropertyJpaConfig
import org.jxtech.propertytrade.platform.property.persistence.entity.ImageResource
import org.jxtech.propertytrade.platform.property.persistence.repository.BuildingRepository
import org.jxtech.propertytrade.platform.property.persistence.repository.ImageResourceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime

@SpringBootTest(classes = [ImageResourcePersistenceTestConfig::class, PropertyJpaConfig::class])
@ExtendWith(SpringExtension::class)
class ImageResourcePersistenceTest {
    @Autowired
    private lateinit var imageResourceRepository: ImageResourceRepository

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun performTest() {
        val imageResource = ImageResource().apply {
            this.name = "image1"
            this.description = "Description"
            this.label = "LOBBY,LARGE"
            this.imageFullPath = "/image/lobby-3111.png"
            this.belongToEntityType = "PROPERTY"
            this.referenceId = 1L
            this.version = 1L
            this.createdBy = "james"
            this.createdOn = LocalDateTime.now()
            this.lastUpdatedBy = "james"
            this.lastUpdatedOn = LocalDateTime.now()
        }
        imageResourceRepository.save(imageResource)
        val imageResourceOptional = imageResourceRepository.findById(1L)
        imageResourceOptional.map {
            println(it.name)
            println(it.imageFullPath)
            println(it.belongToEntityType)
        }
    }

}

@Configuration
@EnableAutoConfiguration
class ImageResourcePersistenceTestConfig {
}
