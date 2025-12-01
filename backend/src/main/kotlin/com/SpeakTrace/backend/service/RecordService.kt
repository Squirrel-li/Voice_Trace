package com.SpeakTrace.backend.service

import com.SpeakTrace.backend.model.UploadRecord
import com.SpeakTrace.backend.repository.UploadRecordRepository
import com.SpeakTrace.backend.model.User // 假設 User 模型存在
import com.SpeakTrace.backend.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import com.SpeakTrace.backend.model.RecordStatus

@Service
class RecordService(
    private val uploadRecordRepository: UploadRecordRepository,
    private val userRepository: UserRepository // 假設有 UserRepository
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
        /*
        // 刪除用戶目錄
        val userDir = File("$uploadDir/$email")
        if (userDir.exists() && userDir.isDirectory) {
            val files = userDir.listFiles()
            if (files == null || files.isEmpty()) {
                if (!userDir.delete()) {
                    throw IllegalStateException("無法刪除用戶目錄：${userDir.absolutePath}")
                }
                println("用戶目錄已刪除：${userDir.absolutePath}")
            } else {
                println("用戶目錄未刪除，因為目錄不為空：${userDir.absolutePath}")
            }
        } */

        // 從資料庫中刪除記錄
        uploadRecordRepository.deleteAll(records)
    }

    fun getRecordFile(recordId: Long, email: String): File {
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
}