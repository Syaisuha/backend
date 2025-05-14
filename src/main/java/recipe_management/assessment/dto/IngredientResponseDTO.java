package recipe_management.assessment.dto;

public record IngredientResponseDTO(
    Long id,
    String name,
    String quantity,
    String unit
) {
    // concationate quantity and unit (like SQL)
    public String getPreparation() {
        return quantity + " " + unit;
    }
}