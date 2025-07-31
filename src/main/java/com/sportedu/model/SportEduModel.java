package com.sportedu.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model utama yang menyimpan semua data aplikasi.
 * File ini diperbaiki dengan menambahkan semua getter yang dibutuhkan.
 */
public class SportEduModel {

    private final List<Teknik> sepakBolaTekniks;
    private final List<Teknik> badmintonTekniks;
    private final List<Soal> tebakGambarSoal;

    public SportEduModel() {
        // Data untuk materi Sepak Bola - 4 teknik sesuai permintaan
        sepakBolaTekniks = new ArrayList<>();
        sepakBolaTekniks.add(new Teknik("Passing", "Mengoper bola kepada rekan satu tim dengan akurat.", "/images/soccer_passing.jpg", "/images/passing.gif"));
        sepakBolaTekniks.add(new Teknik("Dribbling", "Menggiring bola sambil bergerak menggunakan kaki.", "/images/soccer_dribble.jpg", "/images/dribbling.gif"));
        sepakBolaTekniks.add(new Teknik("Shooting", "Menendang bola ke arah gawang untuk mencetak gol.", "/images/soccer_shoot.jpg", "/images/shooting.gif"));
        sepakBolaTekniks.add(new Teknik("Heading", "Menyundul bola menggunakan kepala untuk mengoper atau mencetak gol.", "/images/soccer_heading.jpg", "/images/heading.gif"));

        // Data untuk materi Badminton - 4 teknik sesuai permintaan
        badmintonTekniks = new ArrayList<>();
        badmintonTekniks.add(new Teknik("Servis", "Pukulan awal untuk memulai permainan badminton.", "/images/badminton_service.jpg", "/images/servis.gif"));
        badmintonTekniks.add(new Teknik("Smash", "Pukulan keras dan menukik ke area lawan untuk mencetak poin.", "/images/badminton_smash.jpg", "/images/smash.gif"));
        badmintonTekniks.add(new Teknik("Footwork", "Gerakan kaki untuk berpindah posisi dengan cepat dan efisien.", "/images/badminton_footwork.jpg", "/images/footwork.gif"));
        badmintonTekniks.add(new Teknik("Netting", "Pukulan halus di dekat net agar shuttlecock jatuh tipis di area lawan.", "/images/badminton_netting.jpg", "/images/netting.gif"));

        // Data untuk Kuis Tebak Gambar - 10 soal
        tebakGambarSoal = new ArrayList<>();
        tebakGambarSoal.add(new Soal("/images/soccer_dribble.jpg", new String[]{"Shooting", "Dribbling", "Passing", "Heading"}, 1));
        tebakGambarSoal.add(new Soal("/images/badminton_smash.jpg", new String[]{"Servis", "Netting", "Smash", "Footwork"}, 2));
        tebakGambarSoal.add(new Soal("/images/soccer_heading.jpg", new String[]{"Heading", "Goalkeeping", "Passing", "Dribbling"}, 0));
        tebakGambarSoal.add(new Soal("/images/soccer_shoot.jpg", new String[]{"Dribbling", "Passing", "Shooting", "Heading"}, 2));
        tebakGambarSoal.add(new Soal("/images/badminton_service.jpg", new String[]{"Servis", "Smash", "Netting", "Footwork"}, 0));
        tebakGambarSoal.add(new Soal("/images/soccer_goal.jpg", new String[]{"Shooting", "Goalkeeping", "Passing", "Dribbling"}, 1));
        tebakGambarSoal.add(new Soal("/images/badminton_netting.jpg", new String[]{"Smash", "Servis", "Footwork", "Netting"}, 3));
        tebakGambarSoal.add(new Soal("/images/soccer_tackle.jpg", new String[]{"Tackling", "Passing", "Shooting", "Heading"}, 0));
        tebakGambarSoal.add(new Soal("/images/soccer_goalkeeper.jpg", new String[]{"Shooting", "Goalkeeping", "Passing", "Dribbling"}, 1));
        tebakGambarSoal.add(new Soal("/images/soccer_dribble.jpg", new String[]{"Passing", "Dribbling", "Shooting", "Heading"}, 1));
    }

    // --- GETTER METHODS (YANG SEBELUMNYA HILANG) ---

    public List<Teknik> getSepakBolaTekniks() {
        return Collections.unmodifiableList(sepakBolaTekniks);
    }

    public List<Teknik> getBadmintonTekniks() {
        return Collections.unmodifiableList(badmintonTekniks);
    }

    public List<Soal> getTebakGambarSoal() {
        return Collections.unmodifiableList(tebakGambarSoal);
    }
}
