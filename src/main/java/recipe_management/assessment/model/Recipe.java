package recipe_management.assessment.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recipe")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name="recipeID")
    private Integer recipeID;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="preparation")
    private String preparation;

    @Column(name="category")
    private String category;

    @Column(name="serving")
    private String serving;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
protected void onCreate() {
  createdAt = LocalDateTime.now();
  updatedAt = createdAt;
}

@PreUpdate
protected void onUpdate() {
  updatedAt = LocalDateTime.now();
}



    

}