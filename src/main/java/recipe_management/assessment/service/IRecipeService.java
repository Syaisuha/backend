package recipe_management.assessment.service;

import java.util.List;

import recipe_management.assessment.dto.PaginationResponseDTO;
import recipe_management.assessment.dto.RecipeDTO;

public interface IRecipeService {

    List<RecipeDTO> getRecipeList(String recipeID);

    RecipeDTO saveOrUpdateRecipe(RecipeDTO recipeDTO);

    String deleteRecipe(String name);

    List<RecipeDTO> searchRecipes(String searchKey, String searchColumn, Long page, Long pageSize, String sort, String sortDirection);

    PaginationResponseDTO searchRecipesPage(String searchKey, String searchColumn, Long page, Long pageSize, String sort, String sortDirection);
}