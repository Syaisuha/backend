package recipe_management.assessment.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import recipe_management.assessment.model.Ingredient;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findByRecipeId(Integer recipeId);
    
    @Modifying
    @Query(value = "INSERT INTO ingredient (recipe_id, ingredient_name, quantity, unit) VALUES (:recipeId, :name, :quantity, :unit)", nativeQuery = true)
    void insertIngredient(
        @Param("recipeId") String recipeId, 
        @Param("name") String name, 
        @Param("quantity") String quantity, 
        @Param("unit") String unit
    );
}

