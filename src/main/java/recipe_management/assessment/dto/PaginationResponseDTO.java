package recipe_management.assessment.dto;

public record PaginationResponseDTO(
    Long totalPages,
    Long total,
    Long size
) {}

