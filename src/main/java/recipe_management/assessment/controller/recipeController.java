package recipe_management.assessment.controller;

import java.util.List;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/recipe")
public class RecipeController {

    private static final Logger log = LoggerFactory.getLogger(RecipeController.class);
    private final IRecipeService recipeService;

    @GetMapping("/list")
    public ResponseEntity<List<RecipeDTO>> getRecipeList(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String serving,
            @RequestParam(name = "sort", defaultValue = "recipeID") String sort,
            @RequestParam(name = "sortDirection", defaultValue = "asc") String sortDirection,
            @RequestParam(name = "page", defaultValue = "1") Long page,
            @RequestParam(name = "pageSize", defaultValue = "10") Long pageSize) {
        
        log.info(LogUtil.ENTRY, "getRecipeList");
        RecipeRequestDTO requestDTO = new RecipeRequestDTO(null, name, serving, category);
        PaginationRequestDTO paginationRequestDTO = new PaginationRequestDTO(sort, sortDirection, page, pageSize);
        List<RecipeDTO> recipes = recipeService.getRecipeList(requestDTO, paginationRequestDTO);
        return ResponseEntity.ok(recipes);
    }
    
    @GetMapping("/list/page")
    public ResponseEntity<PaginationResponseDTO> getRecipeListPage(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String serving,
            @RequestParam(name = "page", defaultValue = "1") Long page,
            @RequestParam(name = "pageSize", defaultValue = "10") Long pageSize) {
        
        log.info(LogUtil.ENTRY, "getRecipeListPage");
        RecipeRequestDTO requestDTO = new RecipeRequestDTO(null, name, serving, category);
        PaginationRequestDTO paginationRequestDTO = new PaginationRequestDTO(null, null, page, pageSize);
        
        PaginationResponseDTO paginationResponse = recipeService.getRecipeListPage(requestDTO, paginationRequestDTO);
        return ResponseEntity.ok(paginationResponse);
    }
    
    @GetMapping("/recipe")
    public ResponseEntity<?> getRecipeByName(@RequestParam String name) {
        log.info(LogUtil.ENTRY, "getRecipeByName");
        try {
            Recipe recipe = recipeService.findRecipeByName(name);
            
            List<Ingredient> ingredients = recipeService.getIngredientsByRecipeId(recipe.getRecipeID());
            
            RecipeResponseDTO responseDTO = RecipeResponseDTO.fromEntity(recipe, ingredients);
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("Recipe not found with name: " + name);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<RecipeResponseDTO> createRecipe(@RequestBody RecipeCreateDTO recipeCreateDTO) {
        log.info(LogUtil.ENTRY, "createRecipe");
        
        if (recipeCreateDTO.getName() == null || recipeCreateDTO.getName().isBlank()) {
            return ResponseEntity.badRequest().body(null);
        }
        Recipe recipe = new Recipe();
        recipe.setName(recipeCreateDTO.getName());
        recipe.setDescription(recipeCreateDTO.getDescription());
        recipe.setCategory(recipeCreateDTO.getCategory());
        recipe.setInstruction(recipeCreateDTO.getInstruction());

        Recipe savedRecipe = recipeService.saveOrUpdateRecipe(recipe);
        
        List<Ingredient> savedIngredients = new ArrayList<>();
        
        if (recipeCreateDTO.getIngredients() != null && !recipeCreateDTO.getIngredients().isEmpty()) {
            recipeService.saveIngredients(savedRecipe.getRecipeID(), recipeCreateDTO.getIngredients());

            savedIngredients = recipeService.getIngredientsByRecipeId(savedRecipe.getRecipeID());
        }
        
        return ResponseEntity.ok(RecipeResponseDTO.fromEntity(savedRecipe, savedIngredients));
    }

    @DeleteMapping("/delete/{recipeId}")
    public ResponseEntity<String> deleteRecipe(@PathVariable Long recipeId) {
        log.info(LogUtil.ENTRY, "deleteRecipe");
        String result = recipeService.deleteRecipe(recipeId);
        return ResponseEntity.ok(result);
    }
}
