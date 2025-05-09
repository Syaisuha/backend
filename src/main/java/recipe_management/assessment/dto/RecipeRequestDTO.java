package recipe_management.assessment.dto;

public record RecipeRequestDTO(
    String name, String category, String instruction, String description) {}
