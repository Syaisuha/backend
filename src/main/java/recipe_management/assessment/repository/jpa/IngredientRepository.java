package recipe_management.assessment.repository.jpa;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import recipe_management.assessment.model.Ingredient;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findByRecipeId(Integer recipeId);
    
    @Modifying
    @Query("DELETE FROM Ingredient i WHERE i.recipeId = :recipeId")
    void deleteByRecipeId(@Param("recipeId") Integer recipeId);
}



