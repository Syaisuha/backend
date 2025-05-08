package recipe_management.assessment.dto;

public record PaginationRequestDTO(String sort, String sortDirection, Long page, Long size) {}
