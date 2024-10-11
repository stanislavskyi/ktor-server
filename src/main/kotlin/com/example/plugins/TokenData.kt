package com.example.plugins

import kotlinx.serialization.Serializable

@Serializable
data class TokenData(val token: String, val userId: String)
