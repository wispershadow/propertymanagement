package org.jxtech.propertytrade.platform.common.util.excel

import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.jxtech.propertytrade.platform.common.DataSet
import org.jxtech.propertytrade.platform.common.util.file.FileParseErrors
import org.springframework.validation.Errors
import org.springframework.validation.MapBindingResult
import org.springframework.validation.Validator
import java.io.InputStream

object ExcelParsingUtil {
    fun parseExcelStream(inputStream: InputStream,
        validator: Validator,
        parseSuccessLineAcceptor: (Int, List<Any?>) -> Unit,
        parseFailLineAcceptor: (Int, Errors) -> Unit) {
        iterateExcelWorksheet(inputStream, false) { rowNum, rowData ->
            val rowErrors = MapBindingResult(mutableMapOf<String, Any>(), "row_$rowNum")
            validator.validate(rowData, rowErrors)
            if (rowErrors.hasErrors()) {
                parseFailLineAcceptor.invoke(rowNum, rowErrors)
            }
            else {
                parseSuccessLineAcceptor.invoke(rowNum, rowData)
            }
        }
    }

    fun parseExcelStream(inputStream: InputStream,
        headerRow: List<String>,
        validator: Validator,
        parseSuccessLineAcceptor: (Int, List<Pair<String, Any?>>) -> Unit,
        parseFailLineAcceptor: (Int, Errors) -> Unit) {
        iterateExcelWorksheet(inputStream, true) { rowNum, rowData ->
            val rowErrors = MapBindingResult(mutableMapOf<String, Any>(), "row_$rowNum")
            validator.validate(rowData, rowErrors)
            if (rowErrors.hasErrors()) {
                parseFailLineAcceptor.invoke(rowNum, rowErrors)
            }
            else {
                val actualRow = mutableListOf<Pair<String, Any?>>()
                headerRow.forEachIndexed { index, colName ->
                    if (index < rowData.size) {
                        actualRow.add(Pair(colName, rowData[index]))
                    }
                    else {
                        actualRow.add(Pair(colName, null))
                    }
                }
                parseSuccessLineAcceptor.invoke(rowNum, actualRow)
            }
        }
    }


    fun parseExcelStream(inputStream: InputStream,
        headerRow: List<String>,
        validator: Validator): Pair<DataSet, FileParseErrors> {
        val resultDataSet = DataSet(headerRow)
        val fileParseErrors = FileParseErrors()
        iterateExcelWorksheet(inputStream, false) { rowNum, rowData ->
            val rowErrors = MapBindingResult(mutableMapOf<String, Any>(), "row_$rowNum")
            validator.validate(rowData, rowErrors)
            if (rowErrors.hasErrors()) {
            }
            else {
                resultDataSet.addDataRow(rowData)
            }
        }
        return Pair(resultDataSet, fileParseErrors)
    }

    private fun iterateExcelWorksheet(inputStream: InputStream, hasHeaderRow: Boolean,
        processFun: (Int, List<Any?>) -> Unit) {
        val workBook = XSSFWorkbook(inputStream)
        val firstSheet = workBook.getSheetAt(0)
        val rowIterator = firstSheet.rowIterator()
        var rowNum = 1
        rowIterator.forEachRemaining {row ->
            if (!hasHeaderRow || rowNum > 1) {
                val rowValues = mutableListOf<Any?>()
                row.cellIterator().forEachRemaining {cell ->
                    val cellValue = when (cell.cellType) {
                        CellType.BLANK -> null
                        CellType.BOOLEAN -> cell.booleanCellValue
                        CellType.STRING -> cell.stringCellValue
                        CellType.NUMERIC -> cell.numericCellValue
                        else -> null
                    }
                    rowValues.add(cellValue)
                }
                processFun.invoke(rowNum, rowValues)
            }
            rowNum ++
        }
    }



}
