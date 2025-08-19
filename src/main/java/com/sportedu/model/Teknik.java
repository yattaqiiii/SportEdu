package com.sportedu.model;

/**
 * Model untuk data sebuah teknik olahraga.
 * File ini diperbaiki dengan menambahkan semua getter yang dibutuhkan.
 */
public class Teknik {
    private final String nama;
    private final String deskripsi;
    private final String imagePath;
    private final String gifPath;

    public Teknik(String nama, String deskripsi, String imagePath, String gifPath) {
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.imagePath = imagePath;
        this.gifPath = gifPath;
    }

    // --- GETTER METHODS (YANG SEBELUMNYA HILANG) ---

    public String getNama() {
        return nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getGifPath() {
        return gifPath;
    }
}
