package org.jxtech.propertytrade.platform.common.uicomponent

class Carousel {
    var items: List<BreadcrumbItem> = emptyList()
}

class CarouselItem {
    lateinit var name: String
    lateinit var link: String
    var active: Boolean = false
}
