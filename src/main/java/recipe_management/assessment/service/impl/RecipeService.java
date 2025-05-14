package recipe_management.assessment.service.impl;

import recipe_management.assessment.dto.IngredientDTO;
import recipe_management.assessment.dto.PaginationRequestDTO;
import recipe_management.assessment.dto.PaginationResponseDTO;
import recipe_management.assessment.dto.RecipeDTO;
import recipe_management.assessment.dto.RecipeRequestDTO;
import recipe_management.assessment.model.Ingredient;
import recipe_management.assessment.model.Recipe;
import recipe_management.assessment.repository.jooq.RecipeRepositoryJooq;
import recipe_management.assessment.repository.jpa.IngredientRepository;
import recipe_management.assessment.repository.jpa.RecipeRepository;
import recipe_management.assessment.service.IRecipeService;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import recipe_management.assessment.util.LogUtil;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class RecipeService implements IRecipeService {

    private static final Logger log = LoggerFactory.getLogger(RecipeService.class);
    RecipeRepositoryJooq recipeRepositoryJooq;
    RecipeRepository recipeRepository;
    IngredientRepository ingredientRepository;

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

  @Override
  public Recipe findRecipeByName(String recipeName) {
    log.info(LogUtil.ENTRY, "findRecipeByName");
    Optional<Recipe> exactMatch = recipeRepository.findByName(recipeName);
    if (exactMatch.isPresent()) {
        return exactMatch.get();
    }
    List<Recipe> prefixMatches = recipeRepository.findByNameStartingWith(recipeName);
    if (!prefixMatches.isEmpty()) {
        return prefixMatches.get(0);
    }
    List<Recipe> containsMatches = recipeRepository.findBySimilarName(recipeName);
    if (!containsMatches.isEmpty()) {
        return containsMatches.get(0);
    }
    throw new RuntimeException("Recipe not found with name: " + recipeName);
  }

  @Override
  public String deleteRecipe(Long recipeID) {
    log.info(LogUtil.ENTRY, "deleteRecipe");
      if (recipeID == null) {
        return "Key no found";
      }
      recipeRepositoryJooq.deleteRecipe(name);
      return "Recipe deleted successfully";
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Error deleting recipe", e);
    }
  }

  @Override
  public List<RecipeDTO> getRecipeList(
      RecipeRequestDTO requestDTO, PaginationRequestDTO paginationRequestDTO) {
    log.info(LogUtil.ENTRY, "getRecipeList");
    return recipeRepositoryJooq.getRecipeList(requestDTO, paginationRequestDTO);
  }
  
  @Override
  public PaginationResponseDTO getRecipeListPage(
      RecipeRequestDTO requestDTO, PaginationRequestDTO paginationRequestDTO) {
    log.info(LogUtil.ENTRY, "getRecipeListPage");

    Long totalRecipes = recipeRepositoryJooq.getRecipeCount(requestDTO);
    Long pageSize = paginationRequestDTO.size();
    Long totalPages = (long) Math.ceil((double) totalRecipes / pageSize);
    return new PaginationResponseDTO(totalPages, totalRecipes, pageSize);
  }

  @Override
  @Transactional
  public void saveIngredients(Integer recipeId, List<IngredientDTO> ingredientDTOs) {
    log.info(LogUtil.ENTRY, "saveIngredients");
    
    try {
        for (IngredientDTO dto : ingredientDTOs) {
            Ingredient ingredient = new Ingredient();
            ingredient.setRecipeId(recipeId);
            ingredient.setIngredientName(dto.name());
            ingredient.setQuantity(dto.quantity());
            ingredient.setUnit(dto.unit());
            
            log.info("Saving ingredient: {}", ingredient);
            ingredientRepository.save(ingredient);
        }
    } catch (Exception e) {
        log.error("Error saving ingredients: {}", e.getMessage(), e);
        throw new RuntimeException("Failed to save ingredients: " + e.getMessage(), e);
    }
  }

  @Override
  public List<Ingredient> getIngredientsByRecipeId(Integer recipeId) {
    log.info(LogUtil.ENTRY, "getIngredientsByRecipeId");
    return ingredientRepository.findByRecipeId(recipeId);
  }
}
