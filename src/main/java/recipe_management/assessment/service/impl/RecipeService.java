package recipe_management.assessment.service.impl;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import recipe_management.assessment.dto.PaginationResponseDTO;
import recipe_management.assessment.dto.RecipeDTO;
import recipe_management.assessment.dto.RecipeRequestDTO;
import recipe_management.assessment.model.Recipe;
import recipe_management.assessment.repository.jooq.RecipeRepositoryJooq;
import recipe_management.assessment.repository.jpa.RecipeRepository;
import recipe_management.assessment.service.IRecipeService;

@Slf4j
@AllArgsConstructor
@Service
public class RecipeService implements IRecipeService {

  private RecipeRepositoryJooq recipeRepositoryJooq;
  private final RecipeRepository recipeRepository;

  public List<RecipeDTO> getRecipeList(String recipeId) {
    log.info("getRecipeList: {}", recipeId);
    try {
      return recipeRepositoryJooq.getRecipeList(recipeId);
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
  @Transactional
  public void addRecipe(RecipeRequestDTO recipeDTO) {
    log.info("add Recipe: {}", "addRecipe");
    try {
      if (recipeDTO.name() == null) {
        throw new RuntimeException("Recipe name cannot be null");
      }

      Recipe recipe = new Recipe();
      recipe.setName(recipeDTO.name());
      recipe.setCategory(recipeDTO.category());
      recipe.setInstruction(recipeDTO.instruction());
      recipe.setDescription(recipeDTO.description());
      recipeRepository.save(recipe);

      // recipeRepositoryJooq.addRecipe(recipeDTO);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Error adding recipe", e);
    }
  }

  @Override
  public List<RecipeDTO> searchRecipes(
      String searchKey,
      String searchColumn,
      Long page,
      Long pageSize,
      String sort,
      String sortDirection) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'searchRecipes'");
  }

  @Override
  public PaginationResponseDTO searchRecipesPage(
      String searchKey,
      String searchColumn,
      Long page,
      Long pageSize,
      String sort,
      String sortDirection) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'searchRecipesPage'");
  }
}
