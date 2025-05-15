package recipe_management.assessment.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import recipe_management.assessment.dto.PaginationRequestDTO;
import recipe_management.assessment.dto.PaginationResponseDTO;
import recipe_management.assessment.dto.RecipeCreateDTO;
import recipe_management.assessment.dto.RecipeDTO;
import recipe_management.assessment.dto.RecipeRequestDTO;
import recipe_management.assessment.dto.RecipeResponseDTO;
import recipe_management.assessment.model.Ingredient;
import recipe_management.assessment.model.Recipe;
import recipe_management.assessment.service.IRecipeService;
import recipe_management.assessment.util.LogUtil;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("api/v1/recipe")
public class RecipeController {

  private final IRecipeService recipeService;

  @GetMapping("/list")
  public List<RecipeDTO> getRecipeList(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String category,
      @RequestParam(name = "sort", defaultValue = "recipeId") String sort,
      @RequestParam(name = "sortDirection", defaultValue = "asc") String sortDirection,
      @RequestParam(name = "page", defaultValue = "1") Long page,
      @RequestParam(name = "pageSize", defaultValue = "10") Long pageSize) {
    
    log.info(LogUtil.ENTRY, "getRecipeList");
    RecipeRequestDTO requestDTO = new RecipeRequestDTO(null, name, category, null, null);
    PaginationRequestDTO paginationRequestDTO = new PaginationRequestDTO(sort, sortDirection, page, pageSize);
    return recipeService.getRecipeList(requestDTO, paginationRequestDTO);
  }
  @GetMapping("/list/page")
  public PaginationResponseDTO getRecipeListPage(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String category,
      @RequestParam(name = "page", defaultValue = "1") Long page,
      @RequestParam(name = "pageSize", defaultValue = "10") Long pageSize) {
    
    log.info(LogUtil.ENTRY, "getRecipeListPage");
    RecipeRequestDTO requestDTO = new RecipeRequestDTO(null, name, category, null, null);
    PaginationRequestDTO paginationRequestDTO = new PaginationRequestDTO(null, null, page, pageSize);
    return recipeService.getRecipeListPage(requestDTO, paginationRequestDTO);
  }
  @PostMapping("/create")
  public RecipeResponseDTO createRecipe(@RequestBody RecipeCreateDTO recipeCreateDTO) {
    log.info(LogUtil.ENTRY, "createRecipe");
    
    Recipe recipe = new Recipe();
    recipe.setName(recipeCreateDTO.getName());
    recipe.setDescription(recipeCreateDTO.getDescription());
    recipe.setCategory(recipeCreateDTO.getCategory());
    recipe.setInstruction(recipeCreateDTO.getInstruction());

    Recipe savedRecipe = recipeService.saveOrUpdateRecipe(recipe);
    
    List<Ingredient> savedIngredients = List.of();
    
    if (recipeCreateDTO.getIngredients() != null && !recipeCreateDTO.getIngredients().isEmpty()) {
      recipeService.saveIngredients(savedRecipe.getRecipeID(), recipeCreateDTO.getIngredients());
      savedIngredients = recipeService.getIngredientsByRecipeId(savedRecipe.getRecipeID());
    }
    
    return RecipeResponseDTO.fromEntity(savedRecipe, savedIngredients);
  }

  @DeleteMapping("/delete/{recipeId}")
  public String deleteRecipe(@PathVariable Long recipeId) {
    log.info(LogUtil.ENTRY, "deleteRecipe");
    return recipeService.deleteRecipe(recipeId);
  }
  @GetMapping("/search")
  public List<RecipeDTO> searchRecipesByName(
      @RequestParam String name,
      @RequestParam(name = "page", defaultValue = "1") Long page,
      @RequestParam(name = "pageSize", defaultValue = "10") Long pageSize,
      @RequestParam(name = "sort", defaultValue = "name") String sort,
      @RequestParam(name = "sortDirection", defaultValue = "asc") String sortDirection) {
    log.info(LogUtil.ENTRY, "searchRecipesByName");
    return recipeService.searchRecipesByName(name, page, pageSize, sort, sortDirection);
  }
}
