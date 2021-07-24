package de.ixsen.accsaber.api.controllers;

import de.ixsen.accsaber.api.dtos.CategoryDto;
import de.ixsen.accsaber.api.mapping.CategoryMapper;
import de.ixsen.accsaber.business.CategoryService;
import de.ixsen.accsaber.database.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories() {
        List<Category> allCategories = this.categoryService.getAllLeaderboards();
        List<CategoryDto> categoryDtos = this.categoryMapper.map(allCategories);
        return ResponseEntity.ok(categoryDtos);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewLeaderboard(CategoryDto categoryDto) {
        Category newCategory = this.categoryMapper.map(categoryDto);
        this.categoryService.createNewLeaderboard(newCategory);
    }

    @DeleteMapping("{leaderboardName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLeaderboard(@PathVariable String leaderboardName) {
        this.categoryService.deleteLeaderboard(leaderboardName);
    }

    @PutMapping("{leaderboardName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateLeaderboardDescription(@PathVariable String leaderboardName, @RequestBody CategoryDto categoryDto) {
        this.categoryService.updateDescription(leaderboardName, categoryDto.getDescription());
    }
}
