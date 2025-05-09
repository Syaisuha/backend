package recipe_management.assessment.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import recipe_management.assessment.model.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {}
