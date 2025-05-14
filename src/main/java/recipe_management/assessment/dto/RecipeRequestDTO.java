package recipe_management.assessment.dto;

public record RecipeRequestDTO(
    String recipeId,
    String name, 
    String category, 
    String instruction, 
    String description) {}
