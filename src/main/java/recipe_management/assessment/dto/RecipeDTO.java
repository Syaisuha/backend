package recipe_management.assessment.dto;

import java.time.LocalDateTime;

public record RecipeDTO(
    String recipeId,
    String name,
    String description,
    String instruction,
    String category,
    String ingredientName,
    String quantity,
    String rating,
    LocalDateTime createdDate) {}
