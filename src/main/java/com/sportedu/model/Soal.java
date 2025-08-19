package com.sportedu.model;

/**
 * Model untuk data sebuah soal kuis.
 * File ini mendefinisikan struktur data soal yang benar.
 */
public class Soal {
    private final String pertanyaan; // Path gambar untuk tebak gambar
    private final String[] pilihanJawaban;
    private final int jawabanBenar; // Index dari jawaban yang benar

    public Soal(String pertanyaan, String[] pilihanJawaban, int jawabanBenar) {
        this.pertanyaan = pertanyaan;
        this.pilihanJawaban = pilihanJawaban;
        this.jawabanBenar = jawabanBenar;
    }

    // --- GETTER METHODS ---
    // Metode ini dibutuhkan oleh QuizView dan MainPresenter

    public String getPertanyaan() {
        return pertanyaan;
    }

    public String[] getPilihanJawaban() {
        return pilihanJawaban;
    }

    public int getJawabanBenar() {
        return jawabanBenar;
    }
}
