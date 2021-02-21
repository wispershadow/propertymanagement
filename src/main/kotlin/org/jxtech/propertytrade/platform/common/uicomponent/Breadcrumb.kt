package org.jxtech.propertytrade.platform.common.uicomponent

class Breadcrumb {
    var items: List<BreadcrumbItem> = emptyList()
}

class BreadcrumbItem {
    lateinit var name: String
    lateinit var link: String
    var active: Boolean = false
}

