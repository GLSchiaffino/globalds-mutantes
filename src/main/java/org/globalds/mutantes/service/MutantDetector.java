package org.globalds.mutantes.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MutantDetector {

    private static final int SEQ = 4;

    public boolean isMutant(String[] dna) {

        // Validaciones básicas
        if (dna == null || dna.length < SEQ) {
            log.warn("DNA nulo o con menos de {} filas", SEQ);
            return false;
        }

        int n = dna.length;

        // Validar filas
        for (int i = 0; i < n; i++) {
            String row = dna[i];

            if (row == null) {
                log.warn("Fila {} es null", i);
                return false;
            }

            if (row.length() != n) {
                log.warn("Matriz no cuadrada: fila {} tiene longitud {}", i, row.length());
                return false;
            }

            if (!row.matches("[ATCG]+")) {
                log.warn("Fila {} contiene caracteres inválidos: {}", i, row);
                return false;
            }
        }

        // Convertir a matriz
        char[][] m = new char[n][n];
        for (int i = 0; i < n; i++) m[i] = dna[i].toCharArray();

        int count = 0;

        // Recorrer toda la matriz
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                char base = m[i][j];

                // Horizontal →
                if (j + 3 < n &&
                        isSeq(base, m[i][j+1], m[i][j+2], m[i][j+3])) {

                    log.debug("Secuencia HORIZONTAL encontrada en fila {} col {}", i, j);

                    if (++count > 1) return true;
                }

                // Vertical ↓
                if (i + 3 < n &&
                        isSeq(base, m[i+1][j], m[i+2][j], m[i+3][j])) {

                    log.debug("Secuencia VERTICAL encontrada en fila {} col {}", i, j);

                    if (++count > 1) return true;
                }

                // Diagonal ↘ descendente
                if (i + 3 < n && j + 3 < n &&
                        isSeq(base, m[i+1][j+1], m[i+2][j+2], m[i+3][j+3])) {

                    log.debug("Secuencia DIAGONAL ↘ encontrada en fila {} col {}", i, j);

                    if (++count > 1) return true;
                }

                // Diagonal ↗ ascendente
                if (i - 3 >= 0 && j + 3 < n &&
                        isSeq(base, m[i-1][j+1], m[i-2][j+2], m[i-3][j+3])) {

                    log.debug("Secuencia DIAGONAL ↗ encontrada en fila {} col {}", i, j);

                    if (++count > 1) return true;
                }
            }
        }

        return false;
    }

    // Helper para comparar 4 caracteres
    private boolean isSeq(char a, char b, char c, char d) {
        return a == b && a == c && a == d;
    }
}
