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
    
    Recipe saveOrUpdateRecipe(Recipe recipe);
  
    String deleteRecipe(Long recipeId);
  
    List<RecipeDTO> getRecipeList(
        RecipeRequestDTO requestDTO, PaginationRequestDTO paginationRequestDTO);
  
    PaginationResponseDTO getRecipeListPage(
        RecipeRequestDTO requestDTO, PaginationRequestDTO paginationRequestDTO);

    void saveIngredients(Integer recipeId, List<IngredientDTO> ingredients);

    List<Ingredient> getIngredientsByRecipeId(Integer recipeId);
    
    List<RecipeDTO> searchRecipesByName(
        String name, Long page, Long pageSize, String sort, String sortDirection);
    
    Recipe findRecipeByName(String recipeName);
}
  
