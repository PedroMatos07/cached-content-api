package pedro.matos.cached_content_api.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import pedro.matos.cached_content_api.dto.ArticleRequestDTO;
import pedro.matos.cached_content_api.dto.ArticleResponseDTO;
import pedro.matos.cached_content_api.service.ArticleService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService service;

    public ArticleController(ArticleService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ArticleResponseDTO> create(@Valid @RequestBody ArticleRequestDTO request,
                                                     UriComponentsBuilder uriBuilder){
        ArticleResponseDTO created = service.save(request);

        URI location = uriBuilder
                .path("/articles/{slug}")
                .buildAndExpand(created.slug())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    public ResponseEntity<List<ArticleResponseDTO>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ArticleResponseDTO> findBySlug(@PathVariable String slug){
        return ResponseEntity.ok(service.findBySlug(slug));
    }

    @PutMapping("/{slug}")
    public ResponseEntity<ArticleResponseDTO> update(@PathVariable String slug,
                                                     @Valid @RequestBody ArticleRequestDTO request){
        return ResponseEntity.ok(service.update(request, slug));
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<Void> delete(@PathVariable String slug){
        service.delete(slug);
        return ResponseEntity.noContent().build();
    }
}
