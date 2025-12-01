package com.SpeakTrace.backend.controller

import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.multipart.MultipartFile
import com.SpeakTrace.backend.util.JwtUtil
import com.SpeakTrace.backend.service.RecordService
import com.SpeakTrace.backend.repository.UploadRecordRepository

data class DeleteRecordsRequest(
    val ids: List<Long>
)

@RestController
@RequestMapping("/api/record")
class RecordController(
    private val jwtUtil: JwtUtil,
    private val recordService: RecordService
) {
    @PostMapping("/upload")
    fun uploadRecordInfo(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<Any> {
        return try {
            val token = authHeader.removePrefix("Bearer ").trim()
            val email = jwtUtil.extractEmail(token)

            // 儲存檔案並記錄到資料庫
            val record = recordService.saveRecord(file, email)

            ResponseEntity.ok(mapOf<String, Any>("message" to "影片已上傳", "recordId" to record.id))
        } catch (e: Exception) {
            e.printStackTrace() // 輸出完整的異常堆疊
            ResponseEntity.badRequest().body(e.message ?: "伺服器錯誤")
        }
    }

	@GetMapping("/list")
	fun getRecordList(
		@RequestHeader("Authorization") authHeader: String
	): ResponseEntity<Any> {
		return try {
			val token = authHeader.removePrefix("Bearer ").trim()
			val email = jwtUtil.extractEmail(token)
			
			// 根據 email 獲取上傳記錄列表
			val records = recordService.getRecordsByEmail(email)
			
			ResponseEntity.ok(records)
		} catch (e: Exception) {
			e.message?.let {
				return ResponseEntity.badRequest().body(it)
			}
			return ResponseEntity.badRequest().body("伺服器錯誤")
		}
	}

	@PostMapping("/delete")
	fun deleteRecords(
		@RequestBody request: DeleteRecordsRequest,
		@RequestHeader("Authorization") authHeader: String
	): ResponseEntity<Any> {
		return try {
			val token = authHeader.removePrefix("Bearer ").trim()
			val email = jwtUtil.extractEmail(token)

			// 調用服務層刪除記錄
			recordService.deleteRecords(request.ids, email)

			ResponseEntity.ok(mapOf("message" to "記錄已刪除"))
		} catch (e: Exception) {
			ResponseEntity.badRequest().body(mapOf("error" to e.message))
		}
	}

	@GetMapping("/download")
	fun downloadRecord(
		@RequestParam id: Long, // 從請求參數中獲取檔案 ID
		@RequestHeader("Authorization") authHeader: String // 從標頭中獲取 JWT Token
	): ResponseEntity<Any> {
    	return try {
        // 從標頭中提取用戶的 email
        val token = authHeader.removePrefix("Bearer ").trim()
        val email = jwtUtil.extractEmail(token)

        // 調用服務層獲取檔案
		println("下載請求的檔案 ID: $id, 用戶 Email: $email")
        val file = recordService.getRecordFile(id, email)

        // 構建下載回應
        val resource = file.inputStream().readBytes()
        val fileName = file.name

        ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=\"$fileName\"")
            .body(resource)
		} catch (e: Exception) {
			ResponseEntity.badRequest().body(mapOf("error" to e.message))
		}
	}
}

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<Map<String, String>> {
        // 記錄異常日誌
        e.printStackTrace()

        // 返回友好的錯誤訊息
        return ResponseEntity.badRequest().body(mapOf("error" to "伺服器發生錯誤，請稍後再試"))
    }
}