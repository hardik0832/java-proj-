package com.example.urlshortener.api;

import com.example.urlshortener.core.UrlMapping;
import com.example.urlshortener.core.UrlShortener;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedirectController {
  private final UrlShortener shortener;

  public RedirectController(UrlShortener shortener) {
    this.shortener = shortener;
  }

  @GetMapping("/{code}")
  @Operation(summary = "Redirect short code to original URL")
  public void redirect(@PathVariable String code, HttpServletResponse response) {
    UrlMapping mapping = shortener.resolveAndTrack(code);
    response.setStatus(HttpServletResponse.SC_FOUND);
    response.setHeader("Location", mapping.getLongUrl());
  }
}

