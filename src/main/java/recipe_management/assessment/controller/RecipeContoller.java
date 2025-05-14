package recipe_management.assessment.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import recipe_management.assessment.dto.PaginationResponseDTO;
import recipe_management.assessment.dto.RecipeDTO;
import recipe_management.assessment.dto.RecipeRequestDTO;
import recipe_management.assessment.service.IRecipeService;
import recipe_management.assessment.util.LogUtil;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api/v1/recipe")
public class RecipeContoller {
  private final IRecipeService recipeService;

  @GetMapping("/list")
  public List<RecipeDTO> getRecipeList(@RequestParam(required = false) String recipeId) {
    log.info(LogUtil.ENTRY, "getRecipeList");
    return recipeService.getRecipeList(recipeId);
  }

  @PostMapping("/add")
  public void addRecipe(@RequestBody RecipeRequestDTO recipeRequestDTO) {
    log.info(LogUtil.ENTRY, "addRecipe");
    recipeService.addRecipe(recipeRequestDTO);
  }

  @DeleteMapping("/delete") // change to recipeId
  public String deleteRecipe(@RequestParam String name) {
    log.info(LogUtil.ENTRY, "deleteRecipe");
    return recipeService.deleteRecipe(name);
  }

  @GetMapping("/search")
  public List<RecipeDTO> searchRecipes(
      @RequestParam String searchKey,
      @RequestParam String searchColumn,
      @RequestParam(name = "page", defaultValue = "1") Long page,
      @RequestParam(name = "pageSize", defaultValue = "10") Long pageSize,
      @RequestParam(name = "sort", defaultValue = "name") String sort,
      @RequestParam(name = "sortDirection", defaultValue = "asc") String sortDirection) {
    log.info(LogUtil.ENTRY, "searchRecipes");
    return recipeService.searchRecipes(
        searchKey, searchColumn, page, pageSize, sort, sortDirection);
  }

  @GetMapping("/search/page")
  public PaginationResponseDTO searchRecipesPage(
      @RequestParam String searchKey,
      @RequestParam String searchColumn,
      @RequestParam(name = "page", defaultValue = "1") Long page,
      @RequestParam(name = "pageSize", defaultValue = "10") Long pageSize,
      @RequestParam(name = "sort", defaultValue = "name") String sort,
      @RequestParam(name = "sortDirection", defaultValue = "asc") String sortDirection) {
    log.info(LogUtil.ENTRY, "searchRecipesPage");
    return recipeService.searchRecipesPage(
        searchKey, searchColumn, page, pageSize, sort, sortDirection);
  }
}
