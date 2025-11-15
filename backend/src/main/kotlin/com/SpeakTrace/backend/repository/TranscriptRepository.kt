package com.SpeakTrace.backend.repository

import com.SpeakTrace.backend.model.Transcript
import org.springframework.data.jpa.repository.JpaRepository

interface TranscriptRepository : JpaRepository<Transcript, Long> {
    fun findByUploadRecord_Id(uploadRecordId: Long): List<Transcript>
    fun findBySpeakerInfo_Id(speakerInfoId: Long): List<Transcript>
}