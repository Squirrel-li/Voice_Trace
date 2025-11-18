package com.SpeakTrace.backend.controller

import com.SpeakTrace.backend.model.User
import com.SpeakTrace.backend.repository.UserRepository
import com.SpeakTrace.backend.model.UploadRecord
import com.SpeakTrace.backend.repository.UploadRecordRepository
import com.SpeakTrace.backend.model.Transcript
import com.SpeakTrace.backend.repository.TranscriptRepository
import com.SpeakTrace.backend.model.SpeakerInfo
import com.SpeakTrace.backend.repository.SpeakerInfoRepository

import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


// for test SQL fun
@Profile("test_sql")
@RestController
@RequestMapping("/api/test_sql/show/user")
class UserController_show(
    private val userRepository: UserRepository
) {
    @GetMapping
    fun list(): List<User> = userRepository.findAll()
}


@Profile("test_sql")
@RestController
@RequestMapping("/api/test_sql/show/uploadRecord")
class UploadRecordController_show(
    private val uploadRecordRepository: UploadRecordRepository
) {
    @GetMapping
    fun list(): List<UploadRecord> = uploadRecordRepository.findAll()
}


@Profile("test_sql")
@RestController
@RequestMapping("/api/test_sql/show/transcript")
class tranScriptionsController_show(
    private val transcriptRepository: TranscriptRepository
) {
    @GetMapping
    fun list(): List<Transcript> = transcriptRepository.findAll()
}


@Profile("test_sql")
@RestController
@RequestMapping("/api/test_sql/show/speakerInfo")
class SpeakerinfosController_show(
    private val speakerInfoRepository: SpeakerInfoRepository
) {
    @GetMapping
    fun list(): List<SpeakerInfo> = speakerInfoRepository.findAll()
}


@Profile("test_sql")
@RestController
@RequestMapping("/api/test_sql/show/all")
class AggregateController_show(
    private val userRepository: UserRepository,
    private val uploadRecordRepository: UploadRecordRepository,
    private val transcriptRepository: TranscriptRepository,
    private val speakerInfoRepository: SpeakerInfoRepository
) {
    @GetMapping
    fun list() = mapOf(
        "users" to userRepository.findAll(),
        "uploadRecords" to uploadRecordRepository.findAll(),
        "transcripts" to transcriptRepository.findAll(),
        "speakerInfos" to speakerInfoRepository.findAll()
    )
}



@Profile("test_sql")
@RestController
@RequestMapping("/api/test_sql/add/user")
class UserController_add(
    private val userRepository: UserRepository
) {
    data class CreateUserRequest(val username: String, val email: String, val password: String)

    @PostMapping
    fun create(@RequestBody body: CreateUserRequest): User =
        userRepository.save(User(username = body.username, email = body.email, password = body.password))
}
/* 
curl -Method POST -Uri http://localhost:8080/api/test_sql/add/user `
  -Headers @{ "Content-Type"="application/json" } `
  -Body '{"username":"demo","email":"demo@example.com","password":"secret"}'
*/

@Profile("test_sql")
@RestController
@RequestMapping("/api/test_sql/add/uploadRecord")
class UploadRecordController_add(
    private val uploadRecordRepository: UploadRecordRepository,
    private val userRepository: UserRepository
) {
    data class CreateUploadRecordRequest(
        val userId: Long,
        val fileName: String,
        val filePath: String
    )

    @PostMapping
    fun create(@RequestBody body: CreateUploadRecordRequest): UploadRecord {
        val user = userRepository.findById(body.userId)
            .orElseThrow { IllegalArgumentException("User ${body.userId} not found") }

        return uploadRecordRepository.save(
            UploadRecord(
                user = user,
                fileName = body.fileName,
                filePath = body.filePath
            )
        )
    }
}
/*
curl -Method POST -Uri http://localhost:8080/api/test_sql/add/uploadRecord `
  -Headers @{ "Content-Type"="application/json" } `
  -Body '{"userId":1,"fileName":"sample.wav","filePath":"/tmp/sample.wav"}'
*/

@Profile("test_sql")
@RestController
@RequestMapping("/api/test_sql/add/transcript")
class TranscriptController_add(
    private val transcriptRepository: TranscriptRepository,
    private val uploadRecordRepository: UploadRecordRepository,
    private val speakerInfoRepository: SpeakerInfoRepository
) {
    data class CreateTranscriptRequest(
        val uploadRecordId: Long,
        val speakerInfoId: Long?,
        val textContent: String,
        val startTime: Double,
        val endTime: Double
    )

    @PostMapping
    fun create(@RequestBody body: CreateTranscriptRequest): Transcript {
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
}
/*
Invoke-RestMethod -Method POST -Uri http://localhost:8080/api/test_sql/add/transcript `
  -Headers @{ "Content-Type"="application/json" } `
  -Body '{"uploadRecordId":1,"speakerInfoId":1,"content":"sample","startTime":0.0,"endTime":3.2}'
*/


@Profile("test_sql")
@RestController
@RequestMapping("/api/test_sql/add/speakerinfo")
class SpeakerInfoController_add(
    private val speakerInfoRepository: SpeakerInfoRepository
) {
    data class CreateSpeakerInfoRequest(
        val speakerLabel: String,
        val displayName: String
    )

    @PostMapping
    fun create(@RequestBody body: CreateSpeakerInfoRequest): SpeakerInfo {
        return speakerInfoRepository.save(
            SpeakerInfo(
                speakerLabel = body.speakerLabel,
                displayName = body.displayName
            )
        )
    }
}

/*
curl -Method POST -Uri http://localhost:8080/api/test_sql/add/transcript `
-Headers @{ "Content-Type"="application/json" } `
-Body '{"uploadRecordId":1,"speakerInfoId":1,"content":"sample","startTime":0.0,"endTime":3.2}'
*/


@Profile("test_sql")
@RestController
@RequestMapping("/api/test_sql/clear/all")
class ClearAllController(
    private val transcriptRepository: TranscriptRepository,
    private val speakerInfoRepository: SpeakerInfoRepository,
    private val uploadRecordRepository: UploadRecordRepository,
    private val userRepository: UserRepository
) {
    @PostMapping
    fun clearAll(): String {
        transcriptRepository.deleteAll()
        speakerInfoRepository.deleteAll()
        uploadRecordRepository.deleteAll()
        userRepository.deleteAll()
        return "All data cleared."
    }
}
/*
Invoke-RestMethod -Method POST -Uri http://localhost:8080/api/test_sql/clear/all
*/

@Profile("test_sql")
@RestController
@RequestMapping("/api/test_sql/clear/user")
class ClearUserController(
    private val userRepository: UserRepository
) {
    @PostMapping
    fun clear(): String {
        userRepository.deleteAll()
        return "All users cleared."
    }
}
/*
Invoke-RestMethod -Method POST -Uri http://localhost:8080/api/test_sql/clear/user
*/

@Profile("test_sql")
@RestController
@RequestMapping("/api/test_sql/clear/uploadRecord")
class ClearUploadRecordController(
    private val uploadRecordRepository: UploadRecordRepository
) {
    @PostMapping
    fun clear(): String {
        uploadRecordRepository.deleteAll()
        return "All uploadRecords cleared."
    }
}
/*
Invoke-RestMethod -Method POST -Uri http://localhost:8080/api/test_sql/clear/uploadRecord
*/

@Profile("test_sql")
@RestController
@RequestMapping("/api/test_sql/clear/speakerInfo")
class ClearSpeakerInfoController(
    private val speakerInfoRepository: SpeakerInfoRepository
) {
    @PostMapping
    fun clear(): String {
        speakerInfoRepository.deleteAll()
        return "All speakerInfos cleared."
    }
}
/*
Invoke-RestMethod -Method POST -Uri http://localhost:8080/api/test_sql/clear/speakerInfo
*/

@Profile("test_sql")
@RestController
@RequestMapping("/api/test_sql/clear/transcript")
class ClearTranscriptController(
    private val transcriptRepository: TranscriptRepository
) {
    @PostMapping
    fun clear(): String {
        transcriptRepository.deleteAll()
        return "All transcripts cleared."
    }
}
/*
Invoke-RestMethod -Method POST -Uri http://localhost:8080/api/test_sql/clear/transcript
*/