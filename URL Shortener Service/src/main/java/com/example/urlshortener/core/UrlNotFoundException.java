package com.example.urlshortener.core;

public class UrlNotFoundException extends RuntimeException {
  public UrlNotFoundException(String message) {
    super(message);
  }
}

