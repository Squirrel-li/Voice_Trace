package com.SpeakTrace.backend.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.SpeakTrace.backend.util.JwtUtil
import com.SpeakTrace.backend.service.SpeechProcessingService

data class UploadRecordRequest(
    val id: Long
)

@RestController
@RequestMapping("/api/speech")
class SpeechController(
    private val jwtUtil: JwtUtil,
    private val speechProcessingService: SpeechProcessingService
) {
    @PostMapping("/tts")
    fun tts(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody request: UploadRecordRequest
    ): ResponseEntity<Map<String, String>> {
        try {
            val uploadRecordId = request.id
            val token = authHeader.removePrefix("Bearer ").trim()
            val email = jwtUtil.extractEmail(token)
            

            // 使用協程進行非同步處理
            CoroutineScope(Dispatchers.IO).launch {
                speechProcessingService.callSpeechApi(email, uploadRecordId)
            }
    		return ResponseEntity.ok(mapOf("message" to "TTS 請求已發送"))

        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(mapOf("error" to "發生錯誤: ${e.message}"))
        }
    }

    @PostMapping("/result")
    fun receiveResult(@RequestBody result: Map<String, Any>): ResponseEntity<String> {
        try {
            println("收到 Flask 的結果: $result")

            // 提取 result 和 file_id
            val resultData = result["results"] ?: return ResponseEntity.badRequest().body("缺少結果")
            val fileId = result["file_id"] ?: return ResponseEntity.badRequest().body("缺少 file_id")
            // 處理結果，例如存入資料庫
            println("處理結果: $resultData, 檔案 ID: $fileId")

            // 根據需求進行進一步處理
            speechProcessingService.uploadSpeechResult(result)

            return ResponseEntity.ok("結果已成功接收")
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body("發生錯誤: ${e.message}")
        }
    }
}
/*
{result=
{
    speakers=[SPEAKER_01, SPEAKER_00], 
    results=[   {speaker=SPEAKER_01, start=1.955, end=8.488, text= Dancing in the masquerade, idle truth and plain sight jaded, pop, roll, click, shot.}, 
                {speaker=SPEAKER_01, start=8.508, end=10.452, text=Who will I be today or not?}, 
                {speaker=SPEAKER_00, start=10.593, end=15.402, text=But such a tide as moving seems asleep, too full for sound and foam.}, 
                {speaker=SPEAKER_00, start=15.422, end=22.056, text=When that which drew from out the boundless deep turns again home, twilight and evening bell and after that}]}, 
    file_id=15
}
*/