package com.SpeakTrace.backend.service

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody


fun callSpeechApi(
    inputPath: String,
    outputPath: String
): String? {
    val client = OkHttpClient() // all path is absolute path
    val json = """ 
        {
            "path_file_input": "$inputPath",
            "path_file_output": "$outputPath",
        }
    """.trimIndent()

	System.out.println("path_file_input: $inputPath")
	System.out.println("path_file_output: $outputPath")

    val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    val request = Request.Builder()
        .url("http://127.0.0.1:5000/api/tts")
        .post(requestBody)
        .build()

    client.newCall(request).execute().use { response ->
        return if (response.isSuccessful) {
            response.body?.string()
        } else {
            null
        }
    }
}

/* 
Invoke-RestMethod -Method POST -Uri http://localhost:5000/api/tts `
  -Headers @{ "Content-Type" = "application/json" } `
  -Body '{
    "path_file_input":"D:/!universitiy/3-1/ApplicationSoftwareDesignPractice/final_project/project_kotlin/database/upload_audio/test01_20s.wav",
    "path_file_output":"D:/!universitiy/3-1/ApplicationSoftwareDesignPractice/final_project/project_kotlin/database/upload_audio/results/result.txt"}'
*/