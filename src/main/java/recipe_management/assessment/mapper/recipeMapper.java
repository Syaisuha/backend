package my.com.recipeapi.mapper;

import java.util.LinkedHashMap;
import java.util.Map;

public class RecipeMapper extends ColumnMapper {

    public RecipeMapper() {
        // Map DTO fields to column names
        COLUMN_MAP.put("name", "recipe_name");
        COLUMN_MAP.put("description", "recipe_description");
        COLUMN_MAP.put("category", "recipe_category");
        COLUMN_MAP.put("ingredients", "recipe_ingredients");
        COLUMN_MAP.put("instructions", "recipe_instructions");
        COLUMN_MAP.put("createdDate", "recipe_created_date");
        COLUMN_MAP.put("updatedDate", "recipe_updated_date");
    }
}
