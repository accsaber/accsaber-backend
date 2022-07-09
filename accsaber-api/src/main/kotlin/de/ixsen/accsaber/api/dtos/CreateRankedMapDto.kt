package de.ixsen.accsaber.api.dtos

data class CreateRankedMapDto(
    val id: String? = null,
    val difficulty: String? = null,
    val complexity: Double? = null,
    val categoryName: String? = null,
)