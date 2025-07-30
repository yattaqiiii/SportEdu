package com.sportedu.model;

/**
 * Kelas ini adalah cetakan (blueprint) untuk sebuah objek materi teknik.
 */
public class Teknik {
    private final String nama;
    private final String deskripsi;
    private final String gifPath;

    public Teknik(String nama, String deskripsi, String gifPath) {
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.gifPath = gifPath;
    }

    public String getNama() {
        return nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getGifPath() {
        return gifPath;
    }
}
