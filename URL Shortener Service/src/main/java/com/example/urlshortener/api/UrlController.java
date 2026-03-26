package com.example.urlshortener.api;

import com.example.urlshortener.core.UrlMapping;
import com.example.urlshortener.core.UrlShortener;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/urls")
@Tag(name = "URLs", description = "Create short URLs and inspect stats")
public class UrlController {
  private final UrlShortener shortener;

  public UrlController(UrlShortener shortener) {
    this.shortener = shortener;
  }

  @PostMapping
  @Operation(summary = "Create a short URL")
  public ResponseEntity<ShortUrlResponse> create(@Valid @RequestBody CreateShortUrlRequest req) {
    UrlMapping mapping = shortener.createOrGet(req.url());
    return ResponseEntity.ok(toResponse(mapping));
  }

  @GetMapping("/{code}")
  @Operation(summary = "Get stats for a short code")
  public ResponseEntity<ShortUrlResponse> stats(@PathVariable String code) {
    UrlMapping mapping = shortener.getStats(code);
    return ResponseEntity.ok(toResponse(mapping));
  }

  private static ShortUrlResponse toResponse(UrlMapping mapping) {
    String shortUrl =
        ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/{code}")
            .buildAndExpand(mapping.getCode())
            .toUriString();
    return new ShortUrlResponse(
        mapping.getCode(), shortUrl, mapping.getLongUrl(), mapping.getCreatedAt(), mapping.getClickCount());
  }
}

