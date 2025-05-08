package recipe_management.assessment.dto;

public record RecipeRequestDTO(
    Long recipeId,
    String name,
    String serving,
    String category) {
}
