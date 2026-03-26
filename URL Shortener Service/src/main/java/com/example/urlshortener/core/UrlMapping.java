package com.example.urlshortener.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(
    name = "url_mappings",
    indexes = {
      @Index(name = "idx_url_mappings_code", columnList = "code", unique = true),
      @Index(name = "idx_url_mappings_long_url", columnList = "longUrl")
    })
public class UrlMapping {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = true, unique = true, length = 32)
  private String code;

  @Column(nullable = false, length = 2048)
  private String longUrl;

  @Column(nullable = false)
  private long clickCount;

  @Column(nullable = false)
  private Instant createdAt;

  protected UrlMapping() {}

  public UrlMapping(String longUrl) {
    this.longUrl = longUrl;
    this.createdAt = Instant.now();
    this.clickCount = 0;
  }

  public Long getId() {
    return id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getLongUrl() {
    return longUrl;
  }

  public long getClickCount() {
    return clickCount;
  }

  public void incrementClickCount() {
    this.clickCount += 1;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}

