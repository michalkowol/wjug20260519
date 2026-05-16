package eu.codeloop.ai.wjug.boundary.inbound.jsonapi

const val JSON_API_MEDIA_TYPE = "application/vnd.api+json"

data class JsonApiDocument<T>(val data: T)

data class JsonApiResource<A>(
    val type: String,
    val id: String?,
    val attributes: A
)

fun <A> singleDocument(type: String, id: String, attributes: A): JsonApiDocument<JsonApiResource<A>> {
    return JsonApiDocument(JsonApiResource(type = type, id = id, attributes = attributes))
}

fun <A> listDocument(type: String, items: List<Pair<String, A>>): JsonApiDocument<List<JsonApiResource<A>>> {
    return JsonApiDocument(items.map { (id, attrs) -> JsonApiResource(type = type, id = id, attributes = attrs) })
}
