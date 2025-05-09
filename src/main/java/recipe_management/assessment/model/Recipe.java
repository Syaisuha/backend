package recipe_management.assessment.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "recipe_id")
  private Integer recipeId;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "instruction")
  private String instruction;

  @Column(name = "category")
  private String category;

  @Column(name = "created_date")
  private LocalDateTime createdDate;

  @PrePersist
  protected void onCreate() {
    createdDate = LocalDateTime.now();
  }
}