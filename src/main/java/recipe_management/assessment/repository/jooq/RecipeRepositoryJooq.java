package recipe_management.assessment.repository.jooq;

import static org.jooq.impl.DSL.*;

import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record9;
import org.jooq.SelectConditionStep;
import org.springframework.stereotype.Repository;
import recipe_management.assessment.dto.RecipeDTO;
import recipe_management.assessment.dto.RecipeRequestDTO;

@Repository
@Slf4j
public class RecipeRepositoryJooq {
  private final DSLContext dsl;

  public RecipeRepositoryJooq(DSLContext dsl) {
    this.dsl = dsl;
  }

  public List<RecipeDTO> getRecipeList(String reqRecipeId) {
    Condition condition = noCondition();
    condition = JooqUtil.andCondition(condition, field("r.recipe_id"), Field::eq, reqRecipeId);

    Field<String> recipeId = field("r.recipe_id", String.class);
    Field<String> name = field("r.name", String.class);
    Field<String> description = field("r.description", String.class);
    Field<String> category = field("r.category", String.class);
    Field<String> instruction = field("r.instruction", String.class);
    Field<String> ingredientName = field("ing.name", String.class);
    Field<String> quantity =
        concat(field("ing.quantity", String.class), inline(" "), field("ing.unit", String.class))
            .as("Quantity");
    Field<String> rating = field("rat.rating", String.class);
    Field<LocalDateTime> createdDate = field("r.created_date", LocalDateTime.class);

    SelectConditionStep<
            Record9<String, String, String, String, String, String, String, String, LocalDateTime>>
        query =
            dsl.select(
                    recipeId,
                    name,
                    description,
                    category,
                    instruction,
                    ingredientName,
                    quantity,
                    rating,
                    createdDate)
                .from(table("recipe").as("r"))
                .leftJoin(table("ingredient").as("ing"))
                .on(field("r.recipe_id").eq(field("ing.recipe_id")))
                .leftJoin(table("rating").as("rat"))
                .on(field("r.recipe_id").eq(field("rat.recipe_id")))
                .where();

    log.info("Executing query: " + query);

    return query.fetchInto(RecipeDTO.class);
  }

  public void deleteRecipe(String name) {
    log.info("Deleting recipe: {}", name);
    dsl.deleteFrom(table("recipe")).where(field("name").eq(name)).execute();
  }

  public void addRecipe(RecipeRequestDTO recipeRequestDTO) {
    log.info("Adding new recipe: {}", "addRecipe");
    dsl.insertInto(table("recipe"))
        .set(field("name"), recipeRequestDTO.name())
        .set(field("category"), recipeRequestDTO.category())
        .set(field("instruction"), recipeRequestDTO.instruction())
        .set(field("description"), recipeRequestDTO.description())
        .execute();
  }

  public List<RecipeDTO> searchRecipes(String name) {
    log.info("Searching for recipes: {}", "searchRecipes");
    return dsl.select(
            field("r.recipeId", String.class),
            field("r.name", String.class),
            field("r.description", String.class),
            field("r.instruction", String.class),
            field("r.category", String.class),
            field("ing.name", String.class).as("ingredientName"),
            field("CONCAT(ING.quantity, ' ', ING.unit)", String.class).as("quantity"),
            field("rat.rating", String.class),
            field("r.createdDate", String.class))
        .from(table("recipe").as("r"))
        .leftJoin(table("ingredient").as("ing"))
        .on(field("r.recipeId").eq(field("ing.recipeId")))
        .leftJoin(table("rating").as("rat"))
        .on(field("r.recipeId").eq(field("rat.recipeId")))
        .where(field("r.name").likeIgnoreCase("%" + name + "%"))
        .fetchInto(RecipeDTO.class);
  }
}
