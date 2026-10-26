package pedro.matos.cached_content_api.service;

import org.springframework.stereotype.Service;
import pedro.matos.cached_content_api.dto.ArticleRequestDTO;
import pedro.matos.cached_content_api.dto.ArticleResponseDTO;
import pedro.matos.cached_content_api.exception.ConflictException;
import pedro.matos.cached_content_api.exception.ResourceNotFoundException;
import pedro.matos.cached_content_api.model.Article;
import pedro.matos.cached_content_api.repository.ArticleRepository;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    private ArticleRepository repository;

    public ArticleService(ArticleRepository repository){
        this.repository = repository;
    }

    private Article toEntity(ArticleRequestDTO request) {
        Article article = new Article();
        article.setTitle(request.title());
        article.setContent(request.content());
        article.setSlug(generateSlug(request.title()));
        return article;
    }

    private ArticleResponseDTO toResponse(Article a) {
        return new ArticleResponseDTO(a.getId(), a.getTitle(), a.getSlug(),
                a.getContent(), a.getCreatedAt(), a.getUpdatedAt());
    }

    private String generateSlug(String title) {
        String semAcento = Normalizer.normalize(title, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return semAcento.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }

    public ArticleResponseDTO save(ArticleRequestDTO request){
        Article article = toEntity(request);

        if (repository.findBySlug(article.getSlug()).isPresent()){
            throw new ConflictException("An article with this title already exists");
        }

        return toResponse(repository.save(article));
    }


    public ArticleResponseDTO update(ArticleRequestDTO request,String slug){
        Article articleFound = repository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));

        articleFound.setContent(request.content());
        articleFound.setTitle(request.title());

        return toResponse(repository.save(articleFound));
    }

    public void delete(String slug){
        Article article = repository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        repository.delete(article);
    }

    public List<ArticleResponseDTO> findAll(){
        List<Article> articleList = repository.findAll();
        List<ArticleResponseDTO> articleResponseDTOS = new ArrayList<>();
        for (Article article : articleList ){
            articleResponseDTOS.add(toResponse(article));
        }
        return articleResponseDTOS;
    }

    public ArticleResponseDTO findBySlug(String slug){
        Article articleFound = repository.findBySlug(slug)
                .orElseThrow(()-> new ResourceNotFoundException("Article not found"));
        return toResponse(articleFound);
    }


}
