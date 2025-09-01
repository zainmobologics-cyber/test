package com.example.test.network

data class AudioFile(
    val id:Long?=null,
    val title: String,
    val duration: Long,
    val filePath: String,
    val uri: String,

)
