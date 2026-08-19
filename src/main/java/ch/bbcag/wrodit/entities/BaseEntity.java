package ch.bbcag.wrodit.entities;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@MappedSuperclass
public abstract class BaseEntity {

  @Id
  @Column(nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt = now();

  @Column(name = "updated_at", nullable = false)
  private OffsetDateTime updatedAt = createdAt;

  @PrePersist
  protected void onCreate() {
    OffsetDateTime timestamp = now();
    if (createdAt == null) {
      createdAt = timestamp;
    }
    updatedAt = timestamp;
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = now();
  }

  public Integer getId() {
    return id;
  }

  public void setId(final Integer id) {
    this.id = id;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  private static OffsetDateTime now() {
    return OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MICROS);
  }
}
