package recipe_management.assessment.service.impl;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import recipe_management.assessment.dto.PaginationResponseDTO;
import recipe_management.assessment.dto.RecipeDTO;
import recipe_management.assessment.repository.jooq.RecipeRepositoryJooq;
import recipe_management.assessment.service.IRecipeService;

import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class RecipeService implements IRecipeService {

  private RecipeRepositoryJooq recipeRepositoryJooq;

  public List<RecipeDTO> getRecipeList(String recipeID) {
    log.info("getRecipeList: {}", recipeID);
    try {
      return recipeRepositoryJooq.getRecipeList(recipeID);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Error fetching recipe", e);
    }
  }
  public List<RecipeDTO> searchRecipesByName(String name) {
    log.info("search Recipes: {}", name);
    try {
      return recipeRepositoryJooq.searchRecipes(name);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Error searching for recipe", e);
    }
  }

  public String addNewRecipe(RecipeDTO recipeDTO) {
    log.info("add new recipe: {}", recipeDTO);
    try {
      if (recipeDTO == null) {
        throw new RuntimeException("recipe cannot be null");
      }
      recipeRepositoryJooq.addRecipe(recipeDTO);
      return "Recipe added successfully";
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Error adding new recipes", e);
    }
  }

  public String deleteRecipe(String name) {
    log.info("delete Recipe: {}", name);
    try {
      if (name == null || name.isEmpty()) {
        return "Recipe name is required";
      }
      recipeRepositoryJooq.deleteRecipe(name);
      return "Recipe deleted successfully";
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Error deleting recipe", e);
    }
  }
  @Override
  public RecipeDTO saveOrUpdateRecipe(RecipeDTO recipeDTO) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'saveOrUpdateRecipe'");
  }
  @Override
  public List<RecipeDTO> searchRecipes(String searchKey, String searchColumn, Long page, Long pageSize, String sort,
      String sortDirection) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'searchRecipes'");
  }
  @Override
  public PaginationResponseDTO searchRecipesPage(String searchKey, String searchColumn, Long page, Long pageSize,
      String sort, String sortDirection) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'searchRecipesPage'");
  }
}