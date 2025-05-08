package recipe_management.assessment.dto;

public record UserDTO(
    String userID,
    String ratingID,
    String userName,
    String email,
    Boolean roles
) {
    
}
