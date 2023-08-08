package com.rickg.iprating.api

import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.boolean

object BooleanAsStringSerializer: JsonTransformingSerializer<String>(String.serializer()) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        return if (element is JsonPrimitive && element.boolean) {
            JsonPrimitive(element.boolean.toString())
        } else {
            element
        }
    }
}

