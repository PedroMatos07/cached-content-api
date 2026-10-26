package pedro.matos.cached_content_api.dto;

import jakarta.validation.constraints.NotBlank;

public record ArticleRequestDTO(
        @NotBlank String title,
        @NotBlank String content
) {
}
