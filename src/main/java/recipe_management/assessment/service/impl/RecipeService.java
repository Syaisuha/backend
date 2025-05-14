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
    private final RecipeRepositoryJooq recipeRepositoryJooq;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

  @Override
  public List<String> searchRecipeName(String searchParam) {
    log.info(LogUtil.ENTRY, "searchRecipeName");
    try {
      return recipeRepositoryJooq.searchRecipeName(searchParam);
    } catch (Exception e) {
      log.error("Error searching recipe names: {}", e.getMessage(), e);
      throw new RuntimeException("Error searching recipe names", e);
    }
  }

  @Override
  @Transactional
  public Recipe saveOrUpdateRecipe(Recipe recipe) {
    log.info(LogUtil.ENTRY, "saveOrUpdateRecipe");
    try {
      return recipeRepository.save(recipe);
    } catch (Exception e) {
      log.error("Error saving recipe: {}", e.getMessage(), e);
      throw new RuntimeException("Error saving recipe", e);
    }
  }

  public List<RecipeDTO> getRecipeList(String recipeId) {
    log.info("getRecipeList: {}", recipeId);
    try {
      return recipeRepositoryJooq.getRecipeList(recipeId);
    } catch (Exception e) {
      log.error("Error fetching recipe: {}", e.getMessage(), e);
      throw new RuntimeException("Error fetching recipe", e);
    }
  }

  @Override
  public Recipe findRecipeByName(String recipeName) {
    log.info(LogUtil.ENTRY, "findRecipeByName");
    try {
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
    } catch (Exception e) {
      log.error("Error finding recipe by name: {}", e.getMessage(), e);
      throw new RuntimeException("Error finding recipe by name", e);
    }
  }

  @Override
  @Transactional
  public String deleteRecipe(Long recipeId) {
    log.info(LogUtil.ENTRY, "deleteRecipe");
    try {
      if (recipeId == null) {
        return "Recipe ID not found";
      }
      
      Optional<Recipe> recipeOpt = recipeRepository.findById(recipeId);
      if (recipeOpt.isEmpty()) {
        return "Recipe not found with ID: " + recipeId;
      }
      
      Recipe recipe = recipeOpt.get();
      
      ingredientRepository.deleteByRecipeId(recipe.getRecipeID());
      recipeRepository.deleteById(recipeId);
      
      return "Recipe deleted successfully";
    } catch (Exception e) {
      log.error("Error deleting recipe: {}", e.getMessage(), e);
      throw new RuntimeException("Error deleting recipe", e);
    }
  }

  @Override
  public List<RecipeDTO> getRecipeList(
      RecipeRequestDTO requestDTO, PaginationRequestDTO paginationRequestDTO) {
    log.info(LogUtil.ENTRY, "getRecipeList");
    try {
      return recipeRepositoryJooq.getRecipeList(requestDTO, paginationRequestDTO);
    } catch (Exception e) {
      log.error("Error retrieving recipe list: {}", e.getMessage(), e);
      throw new RuntimeException("Error retrieving recipe list", e);
    }
  }
  
  @Override
  public PaginationResponseDTO getRecipeListPage(
      RecipeRequestDTO requestDTO, PaginationRequestDTO paginationRequestDTO) {
    log.info(LogUtil.ENTRY, "getRecipeListPage");
    try {
      Long totalRecipes = recipeRepositoryJooq.getRecipeCount(requestDTO);
      Long pageSize = paginationRequestDTO.size();
      Long totalPages = (long) Math.ceil((double) totalRecipes / pageSize);
      return new PaginationResponseDTO(totalPages, totalRecipes, pageSize);
    } catch (Exception e) {
      log.error("Error retrieving recipe page info: {}", e.getMessage(), e);
      throw new RuntimeException("Error retrieving recipe page info", e);
    }
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
    try {
      return ingredientRepository.findByRecipeId(recipeId);
    } catch (Exception e) {
      log.error("Error retrieving ingredients: {}", e.getMessage(), e);
      throw new RuntimeException("Error retrieving ingredients", e);
    }
  }

  @Override
  public List<RecipeDTO> searchRecipesByName(
      String name, Long page, Long pageSize, String sort, String sortDirection) {
    log.info(LogUtil.ENTRY, "searchRecipesByName");
    try {
      PaginationRequestDTO paginationRequestDTO = new PaginationRequestDTO(sort, sortDirection, page, pageSize);
      RecipeRequestDTO requestDTO = new RecipeRequestDTO(null, name, null, null, null);
      return recipeRepositoryJooq.getRecipeList(requestDTO, paginationRequestDTO);
    } catch (Exception e) {
      log.error("Error searching recipes by name: {}", e.getMessage(), e);
      throw new RuntimeException("Error searching recipes by name", e);
    }
  }
}
