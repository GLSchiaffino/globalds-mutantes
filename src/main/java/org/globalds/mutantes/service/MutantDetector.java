package org.globalds.mutantes.service;

import org.springframework.stereotype.Component;

@Component
public class MutantDetector {

    private static final int SEQ = 4;

    public boolean isMutant(String[] dna) {

        // Validaciones básicas
        if (dna == null || dna.length < SEQ) return false;
        int n = dna.length;

        for (String row : dna)
            if (row == null || row.length() != n || !row.matches("[ATCG]+"))
                return false;

        // Convertir a matriz
        char[][] m = new char[n][n];
        for (int i = 0; i < n; i++) m[i] = dna[i].toCharArray();

        int count = 0;

        // Recorrer toda la matriz
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                // Horizontal →
                if (j + 3 < n &&
                        isSeq(m[i][j], m[i][j+1], m[i][j+2], m[i][j+3])) {
                    if (++count > 1) return true;
                }

                // Vertical ↓
                if (i + 3 < n &&
                        isSeq(m[i][j], m[i+1][j], m[i+2][j], m[i+3][j])) {
                    if (++count > 1) return true;
                }

                // Diagonal descendente ↘
                if (i + 3 < n && j + 3 < n &&
                        isSeq(m[i][j], m[i+1][j+1], m[i+2][j+2], m[i+3][j+3])) {
                    if (++count > 1) return true;
                }

                // Diagonal ascendente ↗
                if (i - 3 >= 0 && j + 3 < n &&
                        isSeq(m[i][j], m[i-1][j+1], m[i-2][j+2], m[i-3][j+3])) {
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
