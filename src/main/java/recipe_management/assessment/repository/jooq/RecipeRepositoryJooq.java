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
    dsl.deleteFrom(table("name")).where(field("name").eq(name)).execute();
  }

  public void addRecipe(RecipeDTO recipeDTO) {
    log.info("Adding new recipe: {}", recipeDTO.name());
    dsl.insertInto(
            table("recipe"),
            field("recipeID"),
            field("name"),
            field("description"),
            field("instruction"),
            field("category"),
            field("createdDate"))
        .values(
            recipeDTO.recipeID(),
            recipeDTO.name(),
            recipeDTO.description(),
            recipeDTO.instruction(),
            recipeDTO.category(),
            recipeDTO.createdDate())
        .execute();
  }

  public List<RecipeDTO> searchRecipes(String name) {
    log.info("Searching for recipes: {}", name);
    return dsl.select(
            field("r.recipeID", String.class),
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
        .on(field("r.recipeID").eq(field("ing.recipeID")))
        .leftJoin(table("rating").as("rat"))
        .on(field("r.recipeID").eq(field("rat.recipeID")))
        .where(field("r.name").likeIgnoreCase("%" + name + "%")) // Added search by name
        .fetchInto(RecipeDTO.class);
  }
}