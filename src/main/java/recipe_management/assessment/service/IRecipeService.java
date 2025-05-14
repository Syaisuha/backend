package recipe_management.assessment.service;

import java.util.List;

import recipe_management.assessment.dto.IngredientDTO;
import recipe_management.assessment.dto.PaginationRequestDTO;
import recipe_management.assessment.dto.PaginationResponseDTO;
import recipe_management.assessment.dto.RecipeDTO;
import recipe_management.assessment.dto.RecipeRequestDTO;
import recipe_management.assessment.model.Ingredient;
import recipe_management.assessment.model.Recipe;

public interface IRecipeService {
    List<String> searchRecipeName(String searchParam);
  
    Recipe saveOrUpdateRecipe(Recipe Recipe);
  
    Recipe findRecipeByName(String recipeName);
  
    String deleteRecipe(Long recipeID);
  

    List<RecipeDTO> getRecipeList(
        RecipeRequestDTO requestDTO, PaginationRequestDTO paginationRequestDTO);
  
    PaginationResponseDTO getRecipeListPage(
        RecipeRequestDTO requestDTO, PaginationRequestDTO paginationRequestDTO);

    void saveIngredients(Integer recipeId, List<IngredientDTO> ingredients);

    List<Ingredient> getIngredientsByRecipeId(Integer recipeId);
}
  
