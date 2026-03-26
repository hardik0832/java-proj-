package com.example.urlshortener.api;

import jakarta.validation.constraints.NotBlank;

public record CreateShortUrlRequest(@NotBlank String url) {}

