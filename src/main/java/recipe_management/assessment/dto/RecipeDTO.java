package recipe_management.assessment.dto;

import java.time.LocalDateTime;

public record RecipeDTO(
    String recipeId,
    String name,
    String description,
    String category,
    String instruction,
    String ingredientName,
    String preparation,
    LocalDateTime createdDate) {
}
