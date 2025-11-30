package com.SpeakTrace.backend.controller

import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import com.SpeakTrace.backend.service.callSpeechApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody


@RestController
@RequestMapping("/api/speech")
class SpeechController {
    data class SpeechResult(val result: Any) // 用 Any 可接收 dict 或 string

    @PostMapping("/result")
    fun updateSpeechResult(@RequestBody req: SpeechResult): ResponseEntity<String> {
        println("收到語音辨識結果: ${req.result}")
        // TODO: 這裡可以更新資料庫或狀態
        return ResponseEntity.ok("更新完成")
    }
}
/*
req.result = 
{
    speakers=
    [
        SPEAKER_01, 
        SPEAKER_00
    ], 
    results=
    [
        {
            speaker=SPEAKER_01, 
            start=1.955, 
            end=8.488, 
            text= Dancing in the masquerade, idle truth and plain sight jaded, pop, roll, click, shot.
        }, 
        {
            speaker=SPEAKER_01, 
            start=8.508, 
            end=10.452, 
            text=Who will I be today or not?
        }, 
        {
            speaker=SPEAKER_00, 
            start=10.593, 
            end=15.402, 
            text=But such a tide as moving seems asleep, too full for sound and foam.
        }, 
        {
            speaker=SPEAKER_00, 
            start=15.422, 
            end=22.056, 
            text=When that which drew from out the boundless deep turns again home, twilight and evening bell and after that
        }
    ]
}
*/






/* 
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
                "path_file_input": "${req.path_file_input}"
            }
        """.trimIndent()

        val client = OkHttpClient.Builder()
            .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(120, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .build()
        val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        System.out.println("requestBody: $requestBody")
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
*/