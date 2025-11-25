package org.globalds.mutantes.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "dna_records")
@Data
public class DnaRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String dnaHash;

    @Column(nullable = false)
    private boolean isMutant;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
