package com.example.urlshortener.core;

import java.net.URI;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UrlShortener {
  private final UrlMappingRepository repository;

  public UrlShortener(UrlMappingRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public UrlMapping createOrGet(String rawUrl) {
    String normalized = normalizeAndValidate(rawUrl);

    Optional<UrlMapping> existing = repository.findFirstByLongUrl(normalized);
    if (existing.isPresent()) {
      return existing.get();
    }

    UrlMapping mapping = repository.saveAndFlush(new UrlMapping(normalized));
    mapping.setCode(Base62.encode(mapping.getId()));
    return repository.save(mapping);
  }

  @Transactional
  public UrlMapping resolveAndTrack(String code) {
    UrlMapping mapping =
        repository
            .findByCode(code)
            .orElseThrow(() -> new UrlNotFoundException("Unknown short code: " + code));
    mapping.incrementClickCount();
    return repository.save(mapping);
  }

  @Transactional(readOnly = true)
  public UrlMapping getStats(String code) {
    return repository
        .findByCode(code)
        .orElseThrow(() -> new UrlNotFoundException("Unknown short code: " + code));
  }

  private static String normalizeAndValidate(String rawUrl) {
    String trimmed = rawUrl == null ? "" : rawUrl.trim();
    if (trimmed.isEmpty()) {
      throw new IllegalArgumentException("url must not be blank");
    }
    URI uri;
    try {
      uri = URI.create(trimmed);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("url is not a valid URI");
    }
    String scheme = uri.getScheme();
    if (scheme == null || !(scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https"))) {
      throw new IllegalArgumentException("url must start with http:// or https://");
    }
    return uri.toString();
  }
}

