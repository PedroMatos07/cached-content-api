package pedro.matos.cached_content_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pedro.matos.cached_content_api.model.Article;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository <Article,Long> {

    Optional<Article> findBySlug(String slug);
}
