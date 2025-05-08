package recipe_management.assessment.service.impl;

import recipe_management.assessment.dto.PaginationRequestDTO;
import recipe_management.assessment.dto.RecipeDTO;
import recipe_management.assessment.dto.RecipeRequestDTO;
import recipe_management.assessment.model.Recipe;
import recipe_management.assessment.repository.jooq.RecipeRepositoryJooq;
import recipe_management.assessment.repository.jpa.RecipeRepository;
import recipe_management.assessment.service.IRecipeService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import recipe_management.assessment.util.LogUtil;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class RecipeService implements IRecipeService{

    RecipeRepositoryJooq recipeRepositoryJooq;
    RecipeRepository recipeRepository;

  @Override
  public List<String> searchRecipeName(String searchParam) {
    log.info(LogUtil.ENTRY, "searchRecipeName");
      List<String> searchResult = recipeRepositoryJooq.searchRecipeName(searchParam);
      return searchResult;
  }

  @Override
  public Recipe saveOrUpdateRecipe(Recipe Recipes) {
    log.info(LogUtil.ENTRY, "saveOrUpdateRecipe");
      return recipeRepository.save(Recipes);
  }

  @Override
public Recipe findRecipeById(Long recipeID) {
    log.info(LogUtil.ENTRY, "findRecipeById");
    return recipeRepository
        .findById(recipeID)
        .orElseThrow(() -> new RuntimeException("Recipe not found"));
}

//  @Override
//  public PaginationResponseDTO<Recipe> findAllRecipePage(
//      String searchKey,
//      String searchColumn,
//      Long page,
//      Long pageSize,
//      String sort,
//      String sortDirection) {
//      log.info(LogUtil.ENTRY, "findAllRecipePage");
//      Long total =
//          recipeRepositoryJooq.searchRecipeListPage(searchKey, searchColumn, page, pageSize, sort, sortDirection);
 
//      Long totalPage = (long) Math.ceil((double) total / pageSize);
//      List<Recipe> content = recipeRepositoryJooq.searchRecipePageContent(searchKey, searchColumn, page, pageSize, sort, sortDirection);
//      return new PaginationResponseDTO<>(totalPage, total, (long) pageSize, content);
//  }

  @Override
  public String deleteRecipe(Long recipeID) {
    log.info(LogUtil.ENTRY, "deleteRecipe");
      if (recipeID == null) {
        return "Key no found";
      }
      recipeRepository.deleteById(recipeID);
      return "DELETED";
  }

  @Override
  public List<RecipeDTO> getRecipeList(
      RecipeRequestDTO requestDTO, PaginationRequestDTO paginationRequestDTO) {
    log.info(LogUtil.ENTRY, "getRecipeMasterList");
    return recipeRepositoryJooq.getRecipeList(requestDTO, paginationRequestDTO);
  }
}

