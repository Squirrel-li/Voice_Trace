package com.SpeakTrace.backend.model

import jakarta.persistence.*
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore

@Entity
@Table(name = "speaker_info")
data class SpeakerInfo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @OneToMany(mappedBy = "speakerInfo", cascade = [CascadeType.ALL], orphanRemoval = true)
    @JsonIgnore
    var transcripts: MutableList<Transcript> = mutableListOf(),

    @Column(nullable = false)
    var speakerLabel: String,

    @Column(nullable = true)
    var displayName: String? = null
)