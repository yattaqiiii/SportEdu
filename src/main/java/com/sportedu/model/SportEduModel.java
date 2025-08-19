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
        sepakBolaTekniks.add(new Teknik("Passing", "Passing adalah teknik mengoper bola ke teman satu tim. Passing dilakukan supaya bola bisa bergerak cepat dari satu pemain ke pemain lain. Passing dapat dilakukan dengan berbagai bagian kaki, seperti kaki bagian dalam untuk operan yang lebih akurat, atau punggung kaki untuk operan yang lebih jauh.", "/images/soccer_passing.jpg", "/images/passing.gif"));
        sepakBolaTekniks.add(new Teknik("Dribbling", "Dribbling adalah teknik menggiring bola sambil berjalan atau berlari. Tujuan dribbling adalah untuk melewati pemain lawan, menjaga bola tetap dikuasai sendiri, atau mencari posisi yang lebih baik sebelum mengoper atau menembak. Saat dribbling, bola harus selalu dekat dengan kaki agar tidak mudah direbut lawan.", "/images/soccer_dribble.jpg", "/images/dribbling.gif"));
        sepakBolaTekniks.add(new Teknik("Shooting", "Shooting adalah teknik menendang bola ke arah gawang lawan dengan tujuan mencetak gol. Shooting biasanya dilakukan dengan tendangan yang kuat dan terarah supaya kiper lawan kesulitan menangkap bola. Pemain bisa menggunakan punggung kaki atau kaki bagian dalam saat melakukan shooting.", "/images/soccer_shoot.jpg", "/images/shooting.gif"));
        sepakBolaTekniks.add(new Teknik("Heading", "Heading adalah teknik menyundul bola menggunakan dahi atau kening. Heading sering digunakan untuk mengoper bola ke teman, mencetak gol saat menerima umpan lambung, atau untuk membuang bola menjauh dari gawang sendiri. Heading harus dilakukan dengan posisi badan yang seimbang supaya sundulan kuat dan terarah.", "/images/soccer_heading.jpg", "/images/heading.gif"));

        // Data untuk materi Badminton - 4 teknik sesuai permintaan
        badmintonTekniks = new ArrayList<>();
        badmintonTekniks.add(new Teknik("Servis", "Servis adalah pukulan pertama untuk memulai sebuah rally atau permainan. Servis dilakukan dari belakang garis servis dan harus diarahkan ke area lawan sesuai aturan. Dalam badminton, servis bisa dilakukan secara pendek (agar shuttlecock jatuh dekat net) atau panjang (agar shuttlecock jatuh jauh di belakang lapangan lawan).", "/images/badminton_service.jpg", "/images/servis.gif"));
        badmintonTekniks.add(new Teknik("Smash", "Smash adalah pukulan yang dilakukan dengan keras dan cepat ke arah bawah lapangan lawan. Smash biasanya dilakukan saat shuttlecock datang tinggi di atas kepala kita. Gerakan ini bertujuan supaya shuttlecock menukik tajam dan lawan sulit untuk mengembalikannya.", "/images/badminton_smash.jpg", "/images/smash.gif"));
        badmintonTekniks.add(new Teknik("Footwork", "Footwork adalah gerakan kaki yang teratur dan cepat saat bermain badminton. Footwork membantu kita untuk bergerak ke segala arah, seperti ke depan, belakang, kiri, dan kanan, agar bisa memukul shuttlecock dengan baik. Pemain yang punya footwork bagus akan lebih mudah menguasai permainan.", "/images/badminton_footwork.jpg", "/images/footwork.gif"));
        badmintonTekniks.add(new Teknik("Netting", "Netting adalah teknik memukul shuttlecock secara pelan dan halus di dekat net. Tujuannya agar shuttlecock jatuh tipis di sisi net lawan, sehingga lawan kesulitan menjangkau dan terpaksa mengangkat shuttlecock. Dengan begitu, kita bisa menyerang pada pukulan berikutnya.", "/images/badminton_netting.jpg", "/images/netting.gif"));

        // Data untuk Kuis Tebak Gambar - 8 soal dengan path gambar yang benar dan optimized
        tebakGambarSoal = new ArrayList<>();
        // Gunakan gambar yang sudah ada saja, hindari duplikasi untuk menghemat memory
        tebakGambarSoal.add(new Soal("/images/soccer_dribble.jpg", new String[]{"Shooting", "Dribbling", "Passing", "Heading"}, 1));
        tebakGambarSoal.add(new Soal("/images/badminton_smash.jpg", new String[]{"Servis", "Netting", "Smash", "Footwork"}, 2));
        tebakGambarSoal.add(new Soal("/images/soccer_heading.jpg", new String[]{"Heading", "Passing", "Shooting", "Dribbling"}, 0));
        tebakGambarSoal.add(new Soal("/images/soccer_shoot.jpg", new String[]{"Dribbling", "Passing", "Shooting", "Heading"}, 2));
        tebakGambarSoal.add(new Soal("/images/badminton_service.jpg", new String[]{"Servis", "Smash", "Netting", "Footwork"}, 0));
        tebakGambarSoal.add(new Soal("/images/soccer_passing.jpg", new String[]{"Shooting", "Passing", "Dribbling", "Heading"}, 1));
        tebakGambarSoal.add(new Soal("/images/badminton_netting.jpg", new String[]{"Smash", "Servis", "Footwork", "Netting"}, 3));
        tebakGambarSoal.add(new Soal("/images/badminton_footwork.jpg", new String[]{"Footwork", "Netting", "Servis", "Smash"}, 0));
        // Reduce total questions to 8 to save memory instead of duplicating images
        //tebakGambarSoal.add(new Soal("/images/soccer_dribble.jpg", new String[]{"Shooting", "Goalkeeping", "Passing", "Dribbling"}, 3));
        //tebakGambarSoal.add(new Soal("/images/badminton_smash.jpg", new String[]{"Passing", "Smash", "Shooting", "Heading"}, 1));
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
