package org.jxtech.propertytrade.platform.common.service.data

class Schema(
    val name: String,
    val columns: MutableList<Column> = mutableListOf()
) {
    fun addColumn(column: Column) {
        columns.add(column)
    }
}

class Column(
    val name: String,
    var required: Boolean = false,
    val dataType: String = "String"
)
