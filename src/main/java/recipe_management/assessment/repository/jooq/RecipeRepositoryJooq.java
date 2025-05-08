package recipe_management.assessment.repository.jooq;

import static org.jooq.impl.DSL.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Record7;
import org.jooq.SelectConditionStep;
import org.jooq.SelectForUpdateStep;
import org.jooq.SelectLimitPercentStep;
import org.springframework.stereotype.Repository;
import org.jooq.Condition;
import lombok.extern.slf4j.Slf4j;
import recipe_management.assessment.dto.PaginationRequestDTO;
import recipe_management.assessment.dto.RecipeDTO;
import recipe_management.assessment.dto.RecipeRequestDTO;
import recipe_management.assessment.model.Recipe;

@Repository
@Slf4j
public class RecipeRepositoryJooq {
    private final DSLContext dsl;

    public RecipeRepositoryJooq(DSLContext dsl){
        this.dsl=dsl;
    }

    public List<String> searchRecipeName(String searchParam) {
    Field<String> recipeName = field("name", String.class);
    Field<BigDecimal> similarity =
        field("word_similarity({0},{1})", BigDecimal.class, recipeName, searchParam);
    SelectLimitPercentStep<Record1<String>> select =
        dsl.select(recipeName)
            .from(table("Recipe"))
            .where(similarity.ge(new BigDecimal(0.1)))
            .groupBy(recipeName)
            .orderBy(similarity.desc())
            .limit(10);
    List<String> result = select.fetchInto(String.class);
    return result;
  }

  public List<RecipeDTO> getRecipeList(
      RecipeRequestDTO requestDTO, PaginationRequestDTO paginationRequestDTO) {
    Condition condition = noCondition();
        JooqUtil.andCondition(
            condition, field("RE.Name"), Field::containsIgnoreCase, requestDTO.name());
    condition =
        JooqUtil.andCondition(
            condition, field("RE.Category"), Field::containsIgnoreCase, requestDTO.category());

    Field<Long> recipeID = field("RE.recipeID", Long.class).as("recipeID");
    Field<String> name = field("RE.Name", String.class).as("name");
    Field<String> description = field("RE.Description", String.class).as("description");
    Field<String> preparation =
        field("CONCAT(ING.Name, ' ', ING.Quantity)", String.class).as("preparation");
    Field<String> category = field("RE.Category", String.class).as("category");
    Field<LocalDateTime> createdDate =
        field("RE.createdDate", LocalDateTime.class).as("createdDate");
    Field<LocalDateTime> updatedDate =
        field("RE.updatedDate", LocalDateTime.class).as("updatedDate");

    SelectForUpdateStep<
            Record7<
                Long,
                String,
                String,
                String,
                String,
                LocalDateTime,
                LocalDateTime>>
        query =
            dsl.select(
                    recipeID,
                    name,
                    description,
                    preparation,
                    category,
                    createdDate,
                    updatedDate)
                .from(table("Recipe RE"))
                .leftJoin(table("Ingredient ING"))
                .on(field("RE.ingredientID").eq(field("ING.ingredientID")))
                .where(condition)
                .orderBy(
                    CoreUtilsRepositoryJooq.getOrderByField(
                        paginationRequestDTO.sort(), paginationRequestDTO.sortDirection()))
                .offset((paginationRequestDTO.page() - 1) * paginationRequestDTO.size())
                .limit(paginationRequestDTO.size());
    List<RecipeDTO> result = query.fetchInto(RecipeDTO.class);
    return result;
}
//   public Long searchRecipeListPage(
//     String searchKey,
//     String searchColumn,
//     Long page,
//     Long pageSize,
//     String sort,
//     String sortDirection
// ) {
//         Field<Long> recipeID = field("recipeID", Long.class);
//         Field<String> name = field("name", String.class);
//         Field<String> description = field("description", String.class);
//         Field<String> category = field("category", String.class);

//         Condition condition = noCondition();

//         if (searchKey != null && !searchKey.isBlank()) {
//             if (searchColumn != null && !searchColumn.isBlank()) {
//                 if (searchColumn.equals("name")) {
//                     condition = condition.and(field(name).likeIgnoreCase("%" + searchKey + "%"));
//                 } else if (searchColumn.equals("description")) {
//                     condition = condition.and(field(description).likeIgnoreCase("%" + searchKey + "%"));
//                 } else if (searchColumn.equals("category")) {
//                     condition = condition.and(field(category).likeIgnoreCase("%" + searchKey + "%"));
//                 } else {
//                     condition = condition.and(
//                         field(name).likeIgnoreCase("%" + searchKey + "%")
//                             .or(field(description).likeIgnoreCase("%" + searchKey + "%"))
//                             .or(field(category).likeIgnoreCase("%" + searchKey + "%"))
//                     );
//                 }
//             } else {
//                 condition = condition.and(
//                     field(name).likeIgnoreCase("%" + searchKey + "%")
//                         .or(field(description).likeIgnoreCase("%" + searchKey + "%"))
//                         .or(field(category).likeIgnoreCase("%" + searchKey + "%"))
//                 );
//             }
//         }

//         SelectConditionStep<Record4<Long, String, String, String>> query =
//             dsl.select(
//                 Recipe.RecipeID,
//                 Recipe.Name,
//                 Recipe.Description,
//                 Recipe.Category
//             )
//             .from(Recipe)
//             .where(condition);

//         if (sort != null && !sort.isBlank()) {
//             if (sortDirection.equals("asc")) {
//                 query = query.orderBy(field(sort).asc());
//             } else {
//                 query = query.orderBy(field(sort).desc());
//             }
//         }

//         return query.fetch();
//   }

}