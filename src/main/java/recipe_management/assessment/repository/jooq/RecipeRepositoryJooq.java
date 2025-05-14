package recipe_management.assessment.repository.jooq;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.SelectLimitPercentStep;
import org.springframework.stereotype.Repository;
import recipe_management.assessment.dto.PaginationRequestDTO;
import recipe_management.assessment.dto.RecipeDTO;
import recipe_management.assessment.dto.RecipeRequestDTO;
import recipe_management.assessment.util.LogUtil;

@Repository
@Slf4j
@AllArgsConstructor
public class RecipeRepositoryJooq {
  private final DSLContext dsl;

  public Long getRecipeCount(RecipeRequestDTO requestDTO) {
    try {
      log.info(LogUtil.ENTRY, "getRecipeCount");
      
      Condition condition = noCondition();
      
      if (requestDTO.name() != null && !requestDTO.name().isBlank()) {
        condition = condition.and(field("r.name").containsIgnoreCase(requestDTO.name()));
      }
      
      if (requestDTO.category() != null && !requestDTO.category().isBlank()) {
        condition = condition.and(field("r.category").containsIgnoreCase(requestDTO.category()));
      }
      
      return dsl.selectCount()
          .from(table("recipe").as("r"))
          .where(condition)
          .fetchOneInto(Long.class);
    } catch (Exception e) {
      log.error("Error in getRecipeCount: {}", e.getMessage(), e);
      throw new RuntimeException("Error retrieving recipe count", e);
    }
  }

  public List<String> searchRecipeName(String searchParam) {
    try {
      log.info(LogUtil.ENTRY, "searchRecipeName");
      
      Field<String> recipeName = field("name", String.class);
      Field<BigDecimal> similarity =
          field("word_similarity({0},{1})", BigDecimal.class, recipeName, searchParam);
      
      SelectLimitPercentStep<Record1<String>> select =
          dsl.select(recipeName)
              .from(table("recipe"))
              .where(similarity.ge(new BigDecimal(0.1)))
              .groupBy(recipeName)
              .orderBy(similarity.desc())
              .limit(10);
              
      return select.fetchInto(String.class);
    } catch (Exception e) {
      log.error("Error in searchRecipeName: {}", e.getMessage(), e);
      throw new RuntimeException("Error searching recipe names", e);
    }
  }

  public List<RecipeDTO> getRecipeList(
      RecipeRequestDTO requestDTO, PaginationRequestDTO paginationRequestDTO) {
    try {
      log.info(LogUtil.ENTRY, "getRecipeList");
      
      Field<Integer> recipeIdField = field("r.recipe_id", Integer.class).as("recipeId");
      Field<String> nameField = field("r.name", String.class).as("name");
      Field<String> descriptionField = field("r.description", String.class).as("description");
      Field<String> categoryField = field("r.category", String.class).as("category");
      Field<String> instructionField = field("r.instruction", String.class).as("instruction");
      Field<String> ingredientNameField = field("ing.ingredient_name", String.class).as("ingredientName");
      Field<String> preparationField = 
          field("concat(ing.quantity, ' ', ing.unit)", String.class).as("preparation");
      Field<LocalDateTime> createdDateField = 
          field("r.created_date", LocalDateTime.class).as("createdDate");
      
      Condition condition = noCondition();
      
      if (requestDTO.name() != null && !requestDTO.name().isBlank()) {
        condition = condition.and(field("r.name").containsIgnoreCase(requestDTO.name()));
      }
      
      if (requestDTO.category() != null && !requestDTO.category().isBlank()) {
        condition = condition.and(field("r.category").containsIgnoreCase(requestDTO.category()));
      }
      
      return dsl.select(
              recipeIdField,
              nameField,
              descriptionField,
              categoryField,
              instructionField,
              ingredientNameField,
              preparationField,
              createdDateField)
          .from(table("recipe").as("r"))
          .leftJoin(table("ingredient").as("ing"))
          .on("r.recipe_id = ing.recipe_id")
          .where(condition)
          .orderBy(CoreUtilsRepositoryJooq.getOrderByField(
              paginationRequestDTO.sort(), 
              paginationRequestDTO.sortDirection()))
          .offset((paginationRequestDTO.page() - 1) * paginationRequestDTO.size())
          .limit(paginationRequestDTO.size())
          .fetchInto(RecipeDTO.class);
    } catch (Exception e) {
      log.error("Error in getRecipeList: {}", e.getMessage(), e);
      throw new RuntimeException("Error retrieving recipe list", e);
    }
  }
  
  public List<RecipeDTO> getRecipeList(String recipeId) {
    try {
      log.info(LogUtil.ENTRY, "getRecipeList by recipeId");
      
      Field<Integer> recipeIdField = field("r.recipe_id", Integer.class).as("recipeId");
      Field<String> nameField = field("r.name", String.class).as("name");
      Field<String> descriptionField = field("r.description", String.class).as("description");
      Field<String> categoryField = field("r.category", String.class).as("category");
      Field<String> instructionField = field("r.instruction", String.class).as("instruction");
      Field<String> ingredientNameField = field("ing.ingredient_name", String.class).as("ingredientName");
      Field<String> preparationField = 
          field("concat(ing.quantity, ' ', ing.unit)", String.class).as("preparation");
      Field<LocalDateTime> createdDateField = 
          field("r.created_date", LocalDateTime.class).as("createdDate");
      
      Condition condition = noCondition();
      
      if (recipeId != null && !recipeId.isBlank()) {
        condition = condition.and(field("r.recipe_id").eq(Integer.parseInt(recipeId)));
      }
      
      return dsl.select(
              recipeIdField,
              nameField,
              descriptionField,
              categoryField,
              instructionField,
              ingredientNameField,
              preparationField,
              createdDateField)
          .from(table("recipe").as("r"))
          .leftJoin(table("ingredient").as("ing"))
          .on("r.recipe_id = ing.recipe_id")
          .where(condition)
          .fetchInto(RecipeDTO.class);
    } catch (Exception e) {
      log.error("Error in getRecipeList by recipeId: {}", e.getMessage(), e);
      throw new RuntimeException("Error retrieving recipe", e);
    }
  }
  
  public List<RecipeDTO> searchRecipes(String name) {
    try {
      log.info(LogUtil.ENTRY, "searchRecipes by name");
      
      Field<Integer> recipeIdField = field("r.recipe_id", Integer.class).as("recipeId");
      Field<String> nameField = field("r.name", String.class).as("name");
      Field<String> descriptionField = field("r.description", String.class).as("description");
      Field<String> categoryField = field("r.category", String.class).as("category");
      Field<String> instructionField = field("r.instruction", String.class).as("instruction");
      Field<String> ingredientNameField = field("ing.ingredient_name", String.class).as("ingredientName");
      Field<String> preparationField = 
          field("concat(ing.quantity, ' ', ing.unit)", String.class).as("preparation");
      Field<LocalDateTime> createdDateField = 
          field("r.created_date", LocalDateTime.class).as("createdDate");
      
      Condition condition = field("r.name").containsIgnoreCase(name);
      
      return dsl.select(
              recipeIdField,
              nameField,
              descriptionField,
              categoryField,
              instructionField,
              ingredientNameField,
              preparationField,
              createdDateField)
          .from(table("recipe").as("r"))
          .leftJoin(table("ingredient").as("ing"))
          .on("r.recipe_id = ing.recipe_id")
          .where(condition)
          .fetchInto(RecipeDTO.class);
    } catch (Exception e) {
      log.error("Error in searchRecipes by name: {}", e.getMessage(), e);
      throw new RuntimeException("Error searching for recipe", e);
    }
  }
  
  public void deleteRecipe(String name) {
    try {
      log.info(LogUtil.ENTRY, "deleteRecipe");
      dsl.deleteFrom(table("ingredient"))
         .where(field("recipe_id").in(
             dsl.select(field("recipe_id"))
                .from(table("recipe"))
                .where(field("name").eq(name))
         ))
         .execute();
      dsl.deleteFrom(table("recipe"))
         .where(field("name").eq(name))
         .execute();
         
    } catch (Exception e) {
      log.error("Error in deleteRecipe: {}", e.getMessage(), e);
      throw new RuntimeException("Error deleting recipe", e);
    }
  }
}
