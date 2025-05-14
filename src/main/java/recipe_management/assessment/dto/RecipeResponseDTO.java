package recipe_management.assessment.dto;

import java.time.LocalDateTime;
import java.util.List;
import recipe_management.assessment.model.Recipe;
import recipe_management.assessment.model.Ingredient;

public record RecipeResponseDTO(
    Integer recipeID,
    String name,
    String description,
    String category,
    String instruction,
    LocalDateTime createdAt,
    List<IngredientResponseDTO> ingredients
) {
    public static RecipeResponseDTO fromEntity(Recipe recipe, List<Ingredient> ingredients) {
        List<IngredientResponseDTO> ingredientDTOs = ingredients.stream()
            .map(ingredient -> new IngredientResponseDTO(
                ingredient.getIngredientId(),
                ingredient.getIngredientName(),
                ingredient.getQuantity(),
                ingredient.getUnit()
            ))
            .toList();
            
        return new RecipeResponseDTO(
            recipe.getRecipeID(),
            recipe.getName(),
            recipe.getDescription(),
            recipe.getCategory(),
            recipe.getInstruction(),
            recipe.getCreatedAt(),
            ingredientDTOs
        );
    }
}




