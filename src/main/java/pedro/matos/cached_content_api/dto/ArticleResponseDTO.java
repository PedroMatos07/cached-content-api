package pedro.matos.cached_content_api.dto;

import java.time.LocalDateTime;

public record ArticleResponseDTO(
        Long id,
        String title,
        String slug,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
