package org.globalds.mutantes.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidDnaSequenceValidator implements ConstraintValidator<ValidDnaSequence, String[]> {

    private static final int MAX_SIZE = 1000;

    @Override
    public boolean isValid(String[] dna, ConstraintValidatorContext context) {

        // No se acepta null o array vacío
        if (dna == null || dna.length == 0)
            return false;

        int n = dna.length;

        // Optativo 3: matriz demasiado grande
        if (n > MAX_SIZE)
            return false;

        for (String row : dna) {

            // No se aceptan filas nulas
            if (row == null)
                return false;

            // Debe ser cuadrada NxN
            if (row.length() != n)
                return false;

            // Optativo 3: fila demasiado larga
            if (row.length() > MAX_SIZE)
                return false;

            // Solo letras válidas
            if (!row.matches("[ATCG]+"))
                return false;
        }

        return true;
    }
}
