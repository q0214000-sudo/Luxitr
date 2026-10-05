package com.example.data.model

data class QuizStep(
    val stepNumber: Int,
    val title: String,
    val subtitle: String,
    val options: List<QuizOption>
)

data class QuizOption(
    val id: String,
    val label: String,
    val icon: String,
    val description: String = ""
)

data class QuizResult(
    val primaryMatch: Perfume,
    val matchPercentage: Int,
    val secondaryMatch: Perfume,
    val luxysVerdict: String,
    val selectedVibes: List<String>
)
