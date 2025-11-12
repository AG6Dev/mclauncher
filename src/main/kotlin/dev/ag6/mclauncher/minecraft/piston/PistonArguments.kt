package dev.ag6.mclauncher.minecraft.piston

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

data class PistonArguments(val arguments: Arguments)

data class Arguments(
    val game: List<GameArgument>, val jvm: List<JvmArgument>
)

sealed class GameArgument {
    data class Simple(val value: String) : GameArgument()
    data class Conditional(val argument: ConditionalArgument) : GameArgument()
}

sealed class JvmArgument {
    data class Simple(val value: String) : JvmArgument()
    data class Conditional(val argument: ConditionalArgument) : JvmArgument()
}

data class ConditionalArgument(
    val rules: List<Rule>, val value: ArgumentValue
)

sealed class ArgumentValue {
    data class Single(val value: String) : ArgumentValue()
    data class Multiple(val values: List<String>) : ArgumentValue()
}

data class Rule(
    val action: String,

    val features: Map<String, Boolean>? = null,

    val os: OsRule? = null
)

data class OsRule(
    val name: String? = null, val arch: String? = null
)

class GameArgumentDeserializer : JsonDeserializer<GameArgument> {
    override fun deserialize(
        json: JsonElement, typeOfT: Type, context: JsonDeserializationContext
    ): GameArgument {
        return if (json.isJsonPrimitive) {
            GameArgument.Simple(json.asString)
        } else {
            GameArgument.Conditional(context.deserialize(json, ConditionalArgument::class.java))
        }
    }
}

class JvmArgumentDeserializer : JsonDeserializer<JvmArgument> {
    override fun deserialize(
        json: JsonElement, typeOfT: Type, context: JsonDeserializationContext
    ): JvmArgument {
        return if (json.isJsonPrimitive) {
            JvmArgument.Simple(json.asString)
        } else {
            JvmArgument.Conditional(context.deserialize(json, ConditionalArgument::class.java))
        }
    }
}

class ArgumentValueDeserializer : JsonDeserializer<ArgumentValue> {
    override fun deserialize(
        json: JsonElement, typeOfT: Type, context: JsonDeserializationContext
    ): ArgumentValue {
        return if (json.isJsonPrimitive) {
            ArgumentValue.Single(json.asString)
        } else {
            val listType = object : TypeToken<List<String>>() {}.type
            ArgumentValue.Multiple(context.deserialize(json, listType))
        }
    }
}



