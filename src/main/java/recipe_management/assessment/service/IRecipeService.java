package recipe_management.assessment.service;

import java.util.List;

import recipe_management.assessment.dto.PaginationRequestDTO;
// import recipe_management.assessment.dto.PaginationResponseDTO;
import recipe_management.assessment.dto.RecipeDTO;
import recipe_management.assessment.dto.RecipeRequestDTO;
import recipe_management.assessment.model.Recipe;

public interface IRecipeService {
    List<String> searchRecipeName(String searchParam);
  
    Recipe saveOrUpdateRecipe(Recipe Recipe);
  
    Recipe findRecipeById(Long RecipeSeqno);
  
    // List<Recipe> findAllRecipe(
    //     String searchKey,
    //     String searchColumn,
    //     Long page,
    //     Long pageSize,
    //     String sort,
    //     String sortDirection);
  
    // PaginationResponseDTO<Recipe> findAllRecipePage(
    //     String searchKey,
    //     String searchColumn,
    //     Long page,
    //     Long pageSize,
    //     String sort,
    //     String sortDirection);
  
    String deleteRecipe(Long recipeID);
  

    List<RecipeDTO> getRecipeList(
        RecipeRequestDTO requestDTO, PaginationRequestDTO paginationRequestDTO);
  
    // PaginationResponseDTO<RecipeDTO> getRecipeListPage(
    //     RecipeRequestDTO requestDTO, PaginationRequestDTO paginationRequestDTO);
  }
  