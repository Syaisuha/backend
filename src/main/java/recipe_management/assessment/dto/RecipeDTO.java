package recipe_management.assessment.dto;

import java.time.LocalDateTime;

public record RecipeDTO(
    Long recipeID,
    String name,
    String description,
    String category,
    String instruction,
    String ingredientName,
    String preparation,
    LocalDateTime createdDate) {
}
