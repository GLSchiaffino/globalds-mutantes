package org.globalds.mutantes.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import org.globalds.mutantes.validation.ValidDnaSequence;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DnaRequest {

    @NotNull(message = "El campo dna no puede ser null")
    @ValidDnaSequence
    private String[] dna;
}
