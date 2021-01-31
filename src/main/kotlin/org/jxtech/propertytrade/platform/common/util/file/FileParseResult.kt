package org.jxtech.propertytrade.platform.common.util.file

class FileParseResult {
    val fileParseLineResults = mutableListOf<FileParseLineResult>()

    fun addFileParseLineResult(fileParseLineResult: FileParseLineResult) {
        fileParseLineResults.add(fileParseLineResult)
    }
}

class FileParseLineResult (
    val lineNumber: Long,
    val status: FileParseLineResultStatus
) {
    var lineDataId: Any? = null
    var lineErrors = listOf<FileParseLineError>()
}

class FileParseLineError(
    errorCode: String,
    errorDescription: String
)

enum class FileParseLineResultStatus {
    CREATED,
    UPDATED,
    ERROR
}
