package eu.codeloop.ai.wjug.boundary.inbound.common

data class JSendSuccess<T>(val status: String = "success", val data: T)

data class JSendFail<T>(val status: String = "fail", val data: T)

data class JSendError(val status: String = "error", val message: String)

data class FailReason(val message: String)
