package org.jxtech.propertytrade.platform.common

class DataSet(val columnNames: List<String> ) {
    private val rows = mutableListOf<DataRow>()

    fun addDataRow(rowData: List<Any?>) {
        rows.add(DataRow(rowData, this))
    }

    fun getRowIterator(): Iterator<DataRow> {
        return rows.iterator()
    }

    fun getColIndex(colName: String?): Int {
        return columnNames.indexOf(colName)
    }

    fun size(): Int {
        return rows.size
    }
}

class DataRow(val values: List<Any?>, val owner: DataSet) {
    fun getValue(index: Int): Any? {
        return values[index]
    }

    fun getValue(colName: String): Any? {
        val index = owner.getColIndex(colName)
        require(index >= 0) { "Invalid column name: $colName" }
        return getValue(index)
    }
}
