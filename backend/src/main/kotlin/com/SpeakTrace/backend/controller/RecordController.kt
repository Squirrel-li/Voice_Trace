package com.SpeakTrace.backend.controller

import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.multipart.MultipartFile
import com.SpeakTrace.backend.util.JwtUtil
import com.SpeakTrace.backend.service.RecordService

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
            
            ResponseEntity.ok(mapOf("message" to "影片已上傳", "recordId" to record.id))
        } catch (e: Exception) {
            e.message?.let {
                return ResponseEntity.badRequest().body(it)
            }
            return ResponseEntity.badRequest().body("伺服器錯誤")
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
}