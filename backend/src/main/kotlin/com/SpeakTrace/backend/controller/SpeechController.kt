package com.SpeakTrace.backend.controller

import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import com.SpeakTrace.backend.service.callSpeechApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

data class SpeechRequest(
    val path_file_input: String
)

@RestController
@RequestMapping("/api")
class SpeechController {

    @PostMapping("/tts")
    fun tts(@RequestBody req: SpeechRequest) {
        println("req: $req")
        println("path_file_input: ${req.path_file_input}")

        val json = """
            {
                "path_file_input": "${req.path_file_input}",
            }
        """.trimIndent()

        val client = OkHttpClient()
        val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder()
            .url("http://127.0.0.1:5000/api/tts")
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            val result = if (response.isSuccessful) {
                response.body?.string()
            } else {
                null
            }
            println("API 回傳結果：$result")
        }
    }
}