package com.SpeakTrace.backend.service

import com.SpeakTrace.backend.model.UploadRecord
import com.SpeakTrace.backend.repository.UploadRecordRepository
import com.SpeakTrace.backend.model.User // 假設 User 模型存在
import com.SpeakTrace.backend.repository.UserRepository
import com.SpeakTrace.backend.repository.TranscriptRepository
import com.SpeakTrace.backend.repository.SpeakerInfoRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import com.SpeakTrace.backend.model.RecordStatus

@Service
class RecordService(
    private val transcriptRepository: TranscriptRepository,
    private val userRepository: UserRepository,
    private val uploadRecordRepository: UploadRecordRepository,
    private val speakerInfoRepository: SpeakerInfoRepository
) {
    private val uploadDir = "D:/!universitiy/3-1/ApplicationSoftwareDesignPractice/final_project/project_kotlin/database/upload_audio" // 本地資料夾路徑

    init {
        val dir = File(uploadDir)
        if (!dir.exists()) dir.mkdirs()
    }

    fun saveRecord(file: MultipartFile, email: String): UploadRecord {
        val filename = file.originalFilename ?: "unknown"
        val dir = File("$uploadDir/$email")
        if (!dir.exists()) dir.mkdirs()
        val filepath = "$uploadDir/$email/$filename"

        // 儲存檔案到本地
        Files.copy(file.inputStream, Paths.get(filepath))

        // 取得用戶
        val user = userRepository.findByEmail(email) ?: throw IllegalArgumentException("用戶不存在")

        // 儲存到資料庫
        val record = UploadRecord(
            user = user,
            fileName = filename,
            filePath = filepath,
            uploadedAt = LocalDateTime.now()
        )
        return uploadRecordRepository.save(record)
    }

    fun getRecordsByEmail(email: String): List<Map<String, Any>> {
        val records = uploadRecordRepository.findByUserEmail(email)
        val results = mutableListOf<Map<String, Any>>() // 使用可變列表

        if (records.isNotEmpty()) {
            for (record in records) {
                val result = mapOf(
                    "id" to record.id,
                    "filename" to record.fileName,
                    "time" to record.uploadedAt.toString(),
                    "language" to "中文",
                    "status" to record.status
                )
                results.add(result) // 將結果加入列表
            }
        }

        return results // 返回處理後的結果
    }

    fun deleteRecords(ids: List<Long>, email: String) {
        val records = uploadRecordRepository.findAllById(ids)

        if (records.isEmpty()) {
            throw IllegalArgumentException("沒有找到對應的記錄")
        }

        for (record in records) {
            // 驗證該記錄是否屬於該用戶
            if (record.user.email != email) {
                throw IllegalArgumentException("無權限刪除記錄 ID: ${record.id}")
            }

            // 刪除與該記錄相關的 Transcript
            val transcripts = transcriptRepository.findByUploadRecord_Id(record.id)
            for (transcript in transcripts) {
                val speakerInfo = transcript.speakerInfo

                // 刪除 Transcript
                transcriptRepository.delete(transcript)
                println("已刪除 Transcript: ${transcript.id}")

                // 檢查是否有其他 Transcript 引用該 SpeakerInfo
                val remainingTranscripts = transcriptRepository.findBySpeakerInfo_Id(speakerInfo.id)
                if (remainingTranscripts.isEmpty()) {
                    speakerInfoRepository.delete(speakerInfo)
                    println("已刪除孤立的 SpeakerInfo: ${speakerInfo.id}")
                }
            }

            // 刪除本地檔案
            val recordFile = File(record.filePath)
            val resultFile = File(record.filePath.toString().split(".").first() + ".txt")
            if (recordFile.exists()) {
                if (!recordFile.delete()) {
                    throw IllegalStateException("無法刪除檔案：${record.filePath}")
                }
                println("檔案已刪除：${record.filePath}")
            }
            if (resultFile.exists()) {
                if (!resultFile.delete()) {
                    throw IllegalStateException("無法刪除檔案：${resultFile.absolutePath}")
                }
                println("檔案已刪除：${resultFile.absolutePath}")
            }
        }

        // 從資料庫中刪除 UploadRecord
        uploadRecordRepository.deleteAll(records)
        println("已刪除 UploadRecord: ${records.map { it.id }}")
    }

    fun getscriptsFile(recordId: Long, email: String): File {
        // 查詢 UploadRecord
        val uploadRecord = uploadRecordRepository.findByIdAndUserEmail(recordId, email)
            ?: throw IllegalArgumentException("找不到對應的 UploadRecord，recordId: $recordId，email: $email")
        if (uploadRecord.status != RecordStatus.COMPLETED) {
            throw IllegalStateException("上傳記錄的狀態不是 COMPLETED，無法下載檔案")
        }
        println("uploadRecord: $uploadRecord")
        // 獲取檔案路徑
        val filePath = uploadRecord.filePath
        val fileName = filePath.toString().split(".").first() + ".txt"
        println("fileName: $fileName")
        val file = File(fileName)

        // 檢查檔案是否存在
        if (!file.exists()) {
            throw IllegalArgumentException("檔案不存在，路徑: ${file.absolutePath}")
        }

        return file
    }

    fun getDisplayData(displayID: Long, email: String): Map<String, Any> {
        // 查詢資料庫，獲取影片檔案名稱
        val uploadRecord = uploadRecordRepository.findByIdAndUserEmail(displayID, email)
            ?: throw IllegalArgumentException("未找到對應的記錄")

        val videoUrl = "http://localhost:8080/videos/$email/${uploadRecord.fileName}"

        // 查詢 Transcript 資料
        val transcriptList = transcriptRepository.findByUploadRecord_Id(displayID).map {
            mapOf(
                "speaker" to (it.speakerInfo.displayName ?: it.speakerInfo.speakerLabel), // 處理 displayName 為 null 的情況
                "speakerID" to it.speakerInfo.id,
                "time" to it.startTime,
                "text" to it.textContent
            )
        }

        return mapOf(
            "uploadrecord" to mapOf("videoUrl" to videoUrl), // 影片 URL
            "transcriptList" to transcriptList // 文本資訊
        )
    }

    fun updateSpeakerLabel(speakerId: Long, email: String, newLabel: String) {
        println("嘗試查找 speakerId: $speakerId")

        // 查詢對應的 SpeakerInfo
        val speakerInfoOptional = speakerInfoRepository.findById(speakerId)
        if (speakerInfoOptional.isEmpty) {
            throw IllegalArgumentException("找不到對應的 Speaker 資料，speakerId: $speakerId")
        }
        val speakerInfo = speakerInfoOptional.get()
        println("找到的 SpeakerInfo: $speakerInfo")

        // 查詢與 SpeakerInfo 關聯的 Transcript
        val transcripts = transcriptRepository.findBySpeakerInfo_Id(speakerId)
        if (transcripts.isEmpty()) {
            throw IllegalArgumentException("找不到與該 Speaker 資料關聯的 Transcript，speakerId: $speakerId")
        }
        println("找到的 Transcripts: $transcripts")

        // 驗證用戶是否有權限
        val transcript = transcripts.first() // 假設所有關聯的 Transcript 都屬於同一用戶
        if (transcript.uploadRecord.user.email != email) {
            throw IllegalAccessException("無權限更新該 Speaker 的標籤")
        }
        println("驗證通過，原本的 SpeakerInfo: ${speakerInfo.displayName}")

        // 更新 Speaker 的標籤
        speakerInfo.displayName = newLabel
        println("更新後的 SpeakerInfo: ${speakerInfo.displayName}")

        // 保存更新後的 SpeakerInfo
        speakerInfoRepository.save(speakerInfo)
        println("Speaker 標籤更新成功")
    }
}