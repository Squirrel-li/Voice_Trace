package com.SpeakTrace.backend.service

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import com.SpeakTrace.backend.repository.TranscriptRepository
import com.SpeakTrace.backend.repository.SpeakerInfoRepository
import com.SpeakTrace.backend.repository.UploadRecordRepository
import com.SpeakTrace.backend.model.Transcript
import com.SpeakTrace.backend.model.SpeakerInfo
import com.SpeakTrace.backend.model.UploadRecord
import com.SpeakTrace.backend.model.RecordStatus


@Service
class SpeechProcessingService(
    private val uploadRecordRepository: UploadRecordRepository,
    private val speakerInfoRepository: SpeakerInfoRepository,
    private val transcriptRepository: TranscriptRepository
) {
    fun callSpeechApi(email: String, uploadRecordId: Long): String? {
        val client = OkHttpClient()

        // 從資料庫中獲取 uploadRecord
        val uploadRecord = uploadRecordRepository.findByIdAndUserEmail(uploadRecordId, email)
            ?: throw IllegalArgumentException("找不到對應的上傳記錄")

        if (uploadRecord.status != RecordStatus.PENDING) {
            throw IllegalStateException("上傳記錄的狀態不是 PENDING，無法進行處理")
        }
        updateUploadRecordStatus(uploadRecordId, RecordStatus.PROCESSING)

        val inputPath = uploadRecord.filePath
        if (inputPath.isNullOrBlank()) {
            throw IllegalArgumentException("上傳記錄中缺少檔案路徑")
        }

        val outputPath = uploadRecord.filePath.replaceAfterLast(".", "txt")
        // 構建 JSON 請求體
        val json = """ 
            {
                "path_file_input": "$inputPath",
                "path_file_output": "$outputPath",
                "file_id": "$uploadRecordId"
            }
        """.trimIndent()

        println("path_file_input: $inputPath")
        println("path_file_output: $outputPath")

        val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder()
            .url("http://127.0.0.1:5000/api/tts")
            .post(requestBody)
            .build()

        // 僅發送請求，不處理回應
        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    println("Flask API 回應失敗，狀態碼: ${response.code}")
                }
            }
        } catch (e: Exception) {
            println("呼叫\"識別伺服器\"時發生錯誤：${e.message}")
        }
        return null
    }

    fun uploadSpeechResult(results: Map<String, Any>): ResponseEntity<String> {
        try {
            println("收到的結果資料：$results")

            // 確保 file_id 存在
            val fileId = results["file_id"]?.toString()?.toLong()
                ?: throw IllegalArgumentException("結果中缺少 file_id")

            updateUploadRecordStatus(fileId, RecordStatus.COMPLETED)

            // 查詢對應的 UploadRecord
            val uploadRecord = uploadRecordRepository.findById(fileId)
                .orElseThrow { IllegalArgumentException("找不到對應的 UploadRecord，file_id: $fileId") }

            // 保存講者資訊
            val speakerInfoMap = mutableMapOf<String, SpeakerInfo>()
            for (speaker in results["speakers"] as List<*>) {
                println("講者資訊: $speaker")
                val speakerInfo = SpeakerInfo(
                    speakerLabel = speaker.toString(),
                    displayName = speaker.toString()
                )
                speakerInfoRepository.save(speakerInfo)
                speakerInfoMap[speaker.toString()] = speakerInfo
            }

            // 保存文字轉錄結果
            for (result in results["results"] as List<Map<String, Any>>) {
                println("結果項目: $result")

                val speakerLabel = result["speaker"]?.toString()
                    ?: throw IllegalArgumentException("結果中缺少 speaker 資訊")
                val speakerInfo = speakerInfoMap[speakerLabel]
                    ?: throw IllegalArgumentException("找不到對應的 SpeakerInfo，speakerLabel: $speakerLabel")

                val transcript = Transcript(
                    uploadRecord = uploadRecord,
                    speakerInfo = speakerInfo,
                    textContent = result["text"].toString(),
                    startTime = result["start"].toString().toDouble(),
                    endTime = result["end"].toString().toDouble()
                )
                transcriptRepository.save(transcript)
            }
            println("結果資料已成功處理並存入資料庫")

            return ResponseEntity.ok("結果資料已成功處理")
        } catch (e: Exception) {
            println("處理結果資料時發生錯誤：${e.message}")
            return ResponseEntity.badRequest().body("處理結果資料時發生錯誤：${e.message}")
        }
    }

    fun updateUploadRecordStatus(fileId: Long, newStatus: RecordStatus): ResponseEntity<String> {
        return try {
            val uploadRecord = uploadRecordRepository.findById(fileId)
                .orElseThrow { IllegalArgumentException("找不到對應的 UploadRecord，file_id: $fileId") }

            // 修改 status
            uploadRecord.status = newStatus

            // 保存更改
            uploadRecordRepository.save(uploadRecord)

            ResponseEntity.ok("UploadRecord 狀態已更新為 $newStatus")
        } catch (e: Exception) {
            println("更新 UploadRecord 狀態時發生錯誤：${e.message}")
            ResponseEntity.badRequest().body("更新狀態失敗：${e.message}")
        }
    }
}