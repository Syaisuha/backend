package recipe_management.assessment.dto;

import java.util.List;
import lombok.Data;

@Data
public class RecipeCreateDTO {
    private String name;
    private String description;
    private String category;
    private String instruction;
    private List<IngredientDTO> ingredients;
}