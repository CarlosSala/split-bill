package data

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import io.devexpert.splitbill.ImageConverter
import kotlinx.serialization.json.Json

class MLTicketDataSource : TicketDataSource {

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun processTicket(imageByte: ByteArray): TicketData {

        // Real implementation using Firebase Generative AI
        Log.d("TicketProcessor", "initialize image processing...")

        val bitmap = ImageConverter.toBitmap(imageByte)

        // Define schema for JSON response (without requiredProperties)
        val jsonSchema = Schema.obj(
            mapOf(
                "items" to Schema.array(
                    Schema.obj(
                        mapOf(
                            "name" to Schema.string(),
                            "count" to Schema.integer(),
                            "price_per_unit" to Schema.double()
                        )
                    )
                ),
                "total" to Schema.double()
            )
        )

        val prompt = """
                    Analiza esta imagen de un ticket de restaurante y extrae:
                    1. Lista de items con nombre, cantidad y precio individual
                    2. Total de la cuenta
                    Si no puedes leer algún precio, ponlo como 0.0
                """.trimIndent()

        val inputContent = content {
            image(bitmap)
            text(prompt)
        }

        val model = Firebase.ai.generativeModel(
            modelName = "gemini-2.5-flash-lite-preview-06-17",
            generationConfig = generationConfig {
                responseMimeType = "application/json"
                responseSchema = jsonSchema
            }
        )

        val response = model.generateContent(inputContent)
        val responseText =
            response.text ?: throw Exception("There is no IA response text")
        Log.d("TicketProcessor", "Response of IA: $responseText")

        // Parse the JSON using kotlinx.serialization
        val ticketData =
            Json { ignoreUnknownKeys = true }.decodeFromString<TicketData>(responseText)
        return ticketData
    }

}