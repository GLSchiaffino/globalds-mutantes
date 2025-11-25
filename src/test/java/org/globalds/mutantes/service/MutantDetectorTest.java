package org.globalds.mutantes.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MutantDetectorTest {

    private final MutantDetector detector = new MutantDetector();

    // ==========================
    // 1) VALIDACIONES BÁSICAS
    // ==========================

    @Test
    void testNullDna() {
        assertFalse(detector.isMutant(null));
    }

    @Test
    void testEmptyDna() {
        assertFalse(detector.isMutant(new String[]{}));
    }

    @Test
    void testNonSquareMatrix() {
        String[] dna = {
                "ATGC",
                "CAGT",
                "TTAT"   // 3x4 -> no cuadrada
        };
        assertFalse(detector.isMutant(dna));
    }

    @Test
    void testInvalidCharacters() {
        String[] dna = {
                "ATGC",
                "CAGT",
                "TXAT",
                "AGGT"   // tiene X
        };
        assertFalse(detector.isMutant(dna));
    }

    @Test
    void testNullRow() {
        String[] dna = {
                "ATGC",
                null,
                "TTTT",
                "AGAG"
        };
        assertFalse(detector.isMutant(dna));
    }

    // ======================================
    // 2) MATRICES VÁLIDAS, NO MUTANTES (0 secuencias)
    // ======================================

    @Test
    void testValidNoSequences4x4_A() {
        String[] dna = {
                "CAAT",
                "GCCA",
                "GGGC",
                "ACAA"
        };
        assertFalse(detector.isMutant(dna));
    }

    @Test
    void testValidNoSequences5x5_A() {
        String[] dna = {
                "AGTCG",
                "CTTTC",
                "CAAAT",
                "CGAAT",
                "TAATC"
        };
        assertFalse(detector.isMutant(dna));
    }

    @Test
    void testValidNoSequences4x4_B() {
        String[] dna = {
                "TCCC",
                "TAGG",
                "CTAT",
                "CAGG"
        };
        assertFalse(detector.isMutant(dna));
    }

    @Test
    void testValidNoSequences5x5_B() {
        String[] dna = {
                "GAAGT",
                "CGAAT",
                "ATAAA",
                "GCGCG",
                "TCGTT"
        };
        assertFalse(detector.isMutant(dna));
    }

    @Test
    void testValidNoSequences6x6() {
        String[] dna = {
                "ACAAAT",
                "CCCCGC",
                "CTTCGT",
                "AACATC",
                "CAGATC",
                "GGACCT"
        };
        assertFalse(detector.isMutant(dna));
    }

    // ==========================
    // 3) CASOS MUTANTES (>= 2 secuencias)
    // ==========================

    @Test
    void testOfficialExampleMutant() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(detector.isMutant(dna));
    }

    @Test
    void testRandomMutant1() {
        String[] dna = {
                "ATCAAA",
                "CCAGCT",
                "CACGCC",
                "GATACC",
                "GAGTCC",
                "GTCAAC"
        };
        assertTrue(detector.isMutant(dna));
    }

    @Test
    void testRandomMutant2() {
        String[] dna = {
                "GCCAAT",
                "CACCAG",
                "ATCATT",
                "GGAGCT",
                "CAAGGG",
                "ACCTAT"
        };
        assertTrue(detector.isMutant(dna));
    }

    @Test
    void testRandomMutant3() {
        String[] dna = {
                "TACCC",
                "GCCAG",
                "GCACC",
                "CAGAC",
                "ACACG"
        };
        assertTrue(detector.isMutant(dna));
    }

    @Test
    void testRandomMutant4() {
        String[] dna = {
                "CTAGCA",
                "CCACCT",
                "TGTGCC",
                "CCGGCG",
                "CCACCT",
                "TCCGCC"
        };
        assertTrue(detector.isMutant(dna));
    }

    @Test
    void testRandomMutant5() {
        String[] dna = {
                "CAACTT",
                "AAGGCC",
                "CCGGAA",
                "CCCCAT",
                "GTGCAG",
                "ATTTCA"
        };
        assertTrue(detector.isMutant(dna));
    }
}
