package de.ixsen.accsaber.api.dtos

data class CreateRankedMapDto(
    val id: String,
    val difficulty: String,
    val complexity: Double,
    val categoryName: String,
)