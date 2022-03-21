package de.ixsen.accsaber.api.dtos

data class RankedMapsStatisticsDto(
    val mapCount: Int,
    val trueAccMapCount: Long,
    val standardAccMapCount: Long,
    val techAccMapCount: Long,
    val complexityToMapCount: Map<Double, Int>
)