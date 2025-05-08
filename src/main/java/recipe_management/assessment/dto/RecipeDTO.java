package recipe_management.assessment.dto;

import java.time.LocalDateTime;

public record RecipeDTO(
    Long recipeID,
    String name,
    String description,
    String preparation,
    String serving,
    String category,
    LocalDateTime createdDate,
    LocalDateTime updatedDate) {
}
