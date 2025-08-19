package com.sportedu.model;

import java.util.Map;

/**
 * Kelas ini adalah cetakan untuk satu set soal mencocokkan.
 * Isinya adalah sebuah Map yang berisi pasangan path gambar dan nama tekniknya.
 */
public class SoalMencocokkan {
    private final Map<String, String> pasanganJawaban; // Key: imagePath, Value: namaTeknik

    public SoalMencocokkan(Map<String, String> pasanganJawaban) {
        this.pasanganJawaban = pasanganJawaban;
    }

    public Map<String, String> getPasanganJawaban() {
        return pasanganJawaban;
    }
}
