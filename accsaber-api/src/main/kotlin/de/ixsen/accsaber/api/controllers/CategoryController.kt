package de.ixsen.accsaber.api.controllers

import de.ixsen.accsaber.api.dtos.CategoryDto
import de.ixsen.accsaber.api.dtos.PlayerDto
import de.ixsen.accsaber.api.mapping.CategoryMapper
import de.ixsen.accsaber.api.mapping.PlayerMapper
import de.ixsen.accsaber.business.CategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("categories")
class CategoryController @Autowired constructor(
    private val categoryService: CategoryService,
    private val categoryMapper: CategoryMapper
) {
    @GetMapping
    fun getCategories(): ResponseEntity<List<CategoryDto>> {
        val allCategories = categoryService.getAllCategories()
        val categoryDtos = categoryMapper.map(allCategories)
        return ResponseEntity.ok(categoryDtos)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createNewCategory(@RequestBody categoryDto: CategoryDto) {
        val newCategory = categoryMapper.map(categoryDto)
        categoryService.createNewCategory(newCategory)
    }

    @GetMapping("{categoryName}/standings")
    fun getStandingsForCategory(@PathVariable categoryName: String): ResponseEntity<List<PlayerDto>> {
        val accSaberPlayers = categoryService.getStandingsForCategory(categoryName)
        val playerDtos: ArrayList<PlayerDto> = PlayerMapper.INSTANCE.playersToPlayerDtos(accSaberPlayers)
        return ResponseEntity.ok(playerDtos)
    }
}