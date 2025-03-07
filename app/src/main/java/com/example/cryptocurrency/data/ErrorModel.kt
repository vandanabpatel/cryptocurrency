package com.example.cryptocurrency.data

import com.google.gson.annotations.SerializedName

data class ErrorModel(
    val status: StatusModel,
)

data class StatusModel(
    @field:SerializedName("error_message") val errorMessage: String,
)

