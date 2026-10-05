package com.example.data.model

enum class SenderType {
    USER,
    LUXYS
}

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: SenderType,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val recommendedPerfumeId: String? = null,
    val alternativePerfumeIds: List<String> = emptyList(),
    val whyPoints: List<String> = emptyList(),
    val bestForTags: List<String> = emptyList(),
    val suggestionChips: List<String> = emptyList(),
    val isStreaming: Boolean = false
)
