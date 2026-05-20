package eu.codeloop.ai.wjug

import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.show
import assertk.fail
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification
import io.modelcontextprotocol.spec.McpSchema
import io.modelcontextprotocol.spec.McpSchema.CallToolResult
import tools.jackson.databind.json.JsonMapper

private val jsonMapper = JsonMapper.builder().build()

fun Assert<CallToolResult>.isSuccess(): Assert<String> {
    return transform { actual ->
        if (actual.isError == true) {
            expected("successful result but was error:${show(actual.content())}")
        }
        textOrStructured(actual)
    }
}

fun Assert<CallToolResult>.isError(): Assert<String> {
    return transform { actual ->
        if (actual.isError != true) {
            expected("error result but was success:${show(actual.content())}")
        }
        textOrStructured(actual)
    }
}

private fun textOrStructured(result: CallToolResult): String {
    val text = result.content().filterIsInstance<McpSchema.TextContent>().firstOrNull()?.text()
    if (text != null) return text
    val structured = result.structuredContent() ?: return ""
    return jsonMapper.writeValueAsString(structured)
}

fun Assert<SyncToolSpecification>.hasDescription(expected: String) {
    given { actual ->
        val actualDescription = actual.tool().description()
        if (actualDescription == expected) return@given
        expected("description:${show(expected)} but was description:${show(actualDescription)}")
    }
}

fun List<SyncToolSpecification>.firstByName(name: String): SyncToolSpecification {
    return firstOrNull { it.tool().name() == name } ?: fail("Tool name not found name:$name tools:${map { it.tool().name }}")
}

fun SyncToolSpecification.call(vararg arguments: Pair<String, Any>): CallToolResult {
    val request = McpSchema.CallToolRequest.builder()
        .name(tool().name())
        .arguments(arguments.toMap())
        .build()
    return callHandler().apply(null, request)
}
