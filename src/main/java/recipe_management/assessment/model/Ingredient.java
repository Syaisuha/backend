package recipe_management.assessment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ingredient")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id")
    private Long ingredientId;

    @Column(name = "recipe_id")
    private Integer recipeId;

    @Column(name = "ingredient_name")
    private String ingredientName;

    @Column(name = "quantity")
    private String quantity;

    @Column(name = "unit")
    private String unit;
}

