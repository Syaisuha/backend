package recipe_management.assessment.repository.jpa;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import recipe_management.assessment.model.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe,Long> {
    @Query("SELECT r FROM Recipe r WHERE LOWER(r.name) = LOWER(:name)")
    Optional<Recipe> findByName(@Param("name") String name);
    @Query("SELECT r FROM Recipe r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Recipe> findBySimilarName(@Param("name") String name);
    @Query("SELECT r FROM Recipe r WHERE LOWER(r.name) LIKE LOWER(CONCAT(:name, '%'))")
    List<Recipe> findByNameStartingWith(@Param("name") String name);
}
