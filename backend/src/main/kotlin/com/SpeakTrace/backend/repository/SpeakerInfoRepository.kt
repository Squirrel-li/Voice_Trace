package com.SpeakTrace.backend.repository

import com.SpeakTrace.backend.model.SpeakerInfo
import org.springframework.data.jpa.repository.JpaRepository

interface SpeakerInfoRepository : JpaRepository<SpeakerInfo, Long> {
    fun findByTranscripts_Id(transcriptId: Long): List<SpeakerInfo>
}