package eu.codeloop.ai.wjug

import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.show
import io.modelcontextprotocol.server.McpServerFeatures
import io.modelcontextprotocol.spec.McpSchema

fun Assert<McpSchema.CallToolResult>.isSuccess(): Assert<String> {
    return transform { actual ->
        if (actual.isError == true) {
            expected("successful result but was error:${show(actual.content())}")
        }
        (actual.content().first() as McpSchema.TextContent).text()
    }
}

fun Assert<McpSchema.CallToolResult>.isError(): Assert<String> {
    return transform { actual ->
        if (actual.isError != true) {
            expected("error result but was success:${show(actual.content())}")
        }
        (actual.content().first() as McpSchema.TextContent).text()
    }
}

fun List<McpServerFeatures.SyncToolSpecification>.firstByName(name: String): McpServerFeatures.SyncToolSpecification {
    return first { it.tool().name() == name }
}

fun Assert<McpServerFeatures.SyncToolSpecification>.hasDescription(expected: String) {
    given { actual ->
        val actualDescription = actual.tool().description()
        if (actualDescription == expected) return@given
        expected("description:${show(expected)} but was description:${show(actualDescription)}")
    }
}

fun McpServerFeatures.SyncToolSpecification.call(arguments: Map<String, Any> = emptyMap()): McpSchema.CallToolResult {
    val request = McpSchema.CallToolRequest.builder()
        .name(tool().name())
        .arguments(arguments)
        .build()
    return callHandler().apply(null, request)
}
