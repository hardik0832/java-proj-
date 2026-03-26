package com.example.urlshortener.api;

import java.time.Instant;

public record ShortUrlResponse(
    String code, String shortUrl, String longUrl, Instant createdAt, long clickCount) {}

