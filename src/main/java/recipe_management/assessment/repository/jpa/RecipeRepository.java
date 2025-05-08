package recipe_management.assessment.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import recipe_management.assessment.model.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe,Long>{}