package com.sportedu.model;

/**
 * Kelas ini adalah cetakan (blueprint) untuk sebuah objek soal kuis.
 * Ini hanya menyimpan data, tidak ada logika di sini.
 */
public class Soal {
    private final String imagePath;
    private final String[] options;
    private final String correctAnswer;

    public Soal(String imagePath, String[] options, String correctAnswer) {
        this.imagePath = imagePath;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String[] getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
