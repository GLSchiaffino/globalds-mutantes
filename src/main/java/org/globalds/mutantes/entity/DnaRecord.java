package org.globalds.mutantes.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class DnaRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String dnaHash;

    private boolean isMutant;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
