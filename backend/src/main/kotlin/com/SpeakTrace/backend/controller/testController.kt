package com.SpeakTrace.backend.controller

import com.SpeakTrace.backend.model.*
import com.SpeakTrace.backend.repository.*
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController

@Profile("test_sql")
@RestController
@RequestMapping("/api/test/sql")
class TestController(
    private val userRepository: UserRepository,
    private val uploadRecordRepository: UploadRecordRepository,
    private val transcriptRepository: TranscriptRepository,
    private val speakerInfoRepository: SpeakerInfoRepository
) {
    // --- show ---
    @GetMapping("/show/user")
    fun showUsers(): List<User> = userRepository.findAll()

    @GetMapping("/show/uploadRecord")
    fun showUploadRecords(): List<UploadRecord> = uploadRecordRepository.findAll()

    @GetMapping("/show/transcript")
    fun showTranscripts(): List<Transcript> = transcriptRepository.findAll()

    @GetMapping("/show/speakerInfo")
    fun showSpeakerInfos(): List<SpeakerInfo> = speakerInfoRepository.findAll()

    @GetMapping("/show/all")
    fun showAll() = mapOf(
        "users" to userRepository.findAll(),
        "uploadRecords" to uploadRecordRepository.findAll(),
        "transcripts" to transcriptRepository.findAll(),
        "speakerInfos" to speakerInfoRepository.findAll()
    )

    // --- add ---
    data class CreateUserRequest(val username: String, val email: String, val password: String)
    @PostMapping("/add/user")
    fun addUser(@RequestBody body: CreateUserRequest): User =
        userRepository.save(User(username = body.username, email = body.email, password = body.password))

    data class CreateUploadRecordRequest(val userId: Long, val fileName: String, val filePath: String)
    @PostMapping("/add/uploadRecord")
    fun addUploadRecord(@RequestBody body: CreateUploadRecordRequest): UploadRecord {
        val user = userRepository.findById(body.userId)
            .orElseThrow { IllegalArgumentException("User ${body.userId} not found") }
        return uploadRecordRepository.save(
            UploadRecord(user = user, fileName = body.fileName, filePath = body.filePath)
        )
    }

    data class CreateTranscriptRequest(
        val uploadRecordId: Long,
        val speakerInfoId: Long?,
        val textContent: String,
        val startTime: Double,
        val endTime: Double
    )
    @PostMapping("/add/transcript")
    fun addTranscript(@RequestBody body: CreateTranscriptRequest): Transcript {
        val uploadRecord = uploadRecordRepository.findById(body.uploadRecordId)
            .orElseThrow { IllegalArgumentException("UploadRecord ${body.uploadRecordId} not found") }
        val speakerInfo = body.speakerInfoId?.let {
            speakerInfoRepository.findById(it)
                .orElseThrow { IllegalArgumentException("SpeakerInfo $it not found") }
        } ?: throw IllegalArgumentException("speakerInfoId is required")
        return transcriptRepository.save(
            Transcript(
                uploadRecord = uploadRecord,
                speakerInfo = speakerInfo,
                textContent = body.textContent,
                startTime = body.startTime,
                endTime = body.endTime
            )
        )
    }

    data class CreateSpeakerInfoRequest(val speakerLabel: String, val displayName: String)
    @PostMapping("/add/speakerinfo")
    fun addSpeakerInfo(@RequestBody body: CreateSpeakerInfoRequest): SpeakerInfo =
        speakerInfoRepository.save(SpeakerInfo(speakerLabel = body.speakerLabel, displayName = body.displayName))

    // --- clear ---
    @PostMapping("/clear/all")
    fun clearAll(): String {
        transcriptRepository.deleteAll()
        speakerInfoRepository.deleteAll()
        uploadRecordRepository.deleteAll()
        userRepository.deleteAll()
        return "All data cleared."
    }

    @PostMapping("/clear/user")
    fun clearUser(): String {
        userRepository.deleteAll()
        return "All users cleared."
    }

    @PostMapping("/clear/uploadRecord")
    fun clearUploadRecord(): String {
        uploadRecordRepository.deleteAll()
        return "All uploadRecords cleared."
    }

    @PostMapping("/clear/speakerInfo")
    fun clearSpeakerInfo(): String {
        speakerInfoRepository.deleteAll()
        return "All speakerInfos cleared."
    }

    @PostMapping("/clear/transcript")
    fun clearTranscript(): String {
        transcriptRepository.deleteAll()
        return "All transcripts cleared."
    }
}


@CrossOrigin(origins = ["http://localhost:5173"])
@Profile("test_sql")
@RestController
@RequestMapping("/api/test/connection")
class TestConnectionController {
    @GetMapping
    fun testConnection(): String = "backend connection successful!"
}