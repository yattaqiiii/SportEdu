package com.sportedu.presenter;

import com.sportedu.model.Soal;
import com.sportedu.model.SportEduModel;
import com.sportedu.model.Teknik;
import com.sportedu.view.*;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;

public class MainPresenter {
    private final MainView mainView;
    private final SportEduModel model;

    // State untuk kuis
    private int skorSaatIni;
    private int indeksSoalSaatIni;
    private List<Soal> daftarSoalKuis;

    // State untuk matching quiz
    private int currentMatchingRound = 1;
    private int matchingScore = 0;

    public MainPresenter(MainView mainView, SportEduModel model) {
        this.mainView = mainView;
        this.model = model;
        attachLandingPageEvents();
    }

    // --- Bagian Navigasi Utama ---

    private void attachLandingPageEvents() {
        mainView.materiButton.setOnAction(e -> showMateriPage());
        mainView.quizButton.setOnAction(e -> showQuizModePage());

        // Attach navbar events
        attachNavbarEvents();
    }

    /**
     * Attach event handlers to navbar buttons
     */
    private void attachNavbarEvents() {
        if (mainView.navHomeButton != null) {
            mainView.navHomeButton.setOnAction(e -> showLandingPage());
        }
        if (mainView.navMateriButton != null) {
            mainView.navMateriButton.setOnAction(e -> showMateriPage());
        }
        if (mainView.navQuizButton != null) {
            mainView.navQuizButton.setOnAction(e -> showQuizModePage());
        }
    }

    private void attachMateriPageEvents() {
        mainView.kembaliButton.setOnAction(e -> showLandingPage());
        mainView.sepakBolaButton.setOnAction(e -> showTeknikPage("Sepak Bola"));
        mainView.badmintonButton.setOnAction(e -> showTeknikPage("Badminton"));
    }

    private void attachQuizModePageEvents() {
        // Attach navbar events untuk halaman quiz - ini yang paling penting
        attachNavbarEvents();

        // Attach event handlers untuk tombol Mulai dan Petunjuk yang ada di halaman quiz
        if (mainView.mulaiButton != null) {
            mainView.mulaiButton.setOnAction(e -> showQuizModeSelectionPage());
        }
        if (mainView.petunjukButton != null) {
            mainView.petunjukButton.setOnAction(e -> showInstructionsPage());
        }

        // Tombol-tombol ini hanya ada di halaman pemilihan mode quiz, bukan di halaman quiz utama
        if (mainView.tebakGambarButton != null) {
            mainView.tebakGambarButton.setOnAction(e -> startTebakGambarQuiz());
        }
        if (mainView.cocokGambarButton != null) {
            mainView.cocokGambarButton.setOnAction(e -> startMencocokkanGambarQuiz());
        }
        if (mainView.kembaliButton != null) {
            mainView.kembaliButton.setOnAction(e -> showLandingPage());
        }
    }

    private void showLandingPage() {
        mainView.showLandingPage();
        attachLandingPageEvents();
    }

    private void showMateriPage() {
        mainView.showMateriPilihanPage();
        attachMateriPageEvents();
        // Tambahkan navbar events untuk halaman materi
        attachNavbarEvents();
    }

    private void showQuizModePage() {
        mainView.showQuizModePage();
        attachQuizModePageEvents();
    }

    private void showTeknikPage(String olahraga) {
        TeknikView teknikView = new TeknikView();
        List<Teknik> teknikList;

        if ("Sepak Bola".equals(olahraga)) {
            teknikList = model.getSepakBolaTekniks();
        } else {
            teknikList = model.getBadmintonTekniks();
        }

        teknikView.displayTeknik(teknikList, olahraga);

        // Event handler untuk memilih teknik -> ke halaman penjelasan
        teknikView.setOnTeknikSelected(teknik -> showPenjelasanPage(teknik, olahraga));

        teknikView.getKembaliButton().setOnAction(e -> showMateriPage());
        mainView.setView(teknikView.getView());
    }

    // --- Bagian Alur Materi: Penjelasan -> Animasi ---

    private void showPenjelasanPage(Teknik teknik, String olahraga) {
        PenjelasanView penjelasanView = new PenjelasanView();
        penjelasanView.displayTeknik(teknik);

        penjelasanView.getKembaliButton().setOnAction(e -> showTeknikPage(olahraga));
        penjelasanView.getNextButton().setOnAction(e -> showAnimasiPage(teknik, olahraga));

        mainView.setView(penjelasanView.getView());
    }

    private void showAnimasiPage(Teknik teknik, String olahraga) {
        AnimasiView animasiView = new AnimasiView();
        animasiView.displayAnimasi(teknik);

        animasiView.getKembaliButton().setOnAction(e -> showTeknikPage(olahraga));

        mainView.setView(animasiView.getView());
    }

    // --- Bagian Logika Kuis Tebak Gambar (10 soal) ---

    private void startTebakGambarQuiz() {
        this.daftarSoalKuis = model.getTebakGambarSoal();
        this.skorSaatIni = 0;
        this.indeksSoalSaatIni = 0;
        tampilkanSoalBerikutnya();
    }

    private void tampilkanSoalBerikutnya() {
        if (indeksSoalSaatIni >= daftarSoalKuis.size()) {
            tampilkanHasilQuiz();
            return;
        }

        Soal soal = daftarSoalKuis.get(indeksSoalSaatIni);
        QuizView quizView = new QuizView();
        quizView.displaySoal(soal);

        for (int i = 0; i < quizView.pilihanButtons.length; i++) {
            final int jawabanPilihan = i;
            quizView.pilihanButtons[i].setOnAction(e -> {
                prosesJawaban(jawabanPilihan, soal.getJawabanBenar());
            });
        }

        quizView.getKembaliButton().setOnAction(e -> showQuizModePage());
        mainView.setView(quizView.getView());
    }

    private void prosesJawaban(int jawabanPilihan, int jawabanBenar) {
        if (jawabanPilihan == jawabanBenar) {
            skorSaatIni++;
        }
        indeksSoalSaatIni++;
        tampilkanSoalBerikutnya();
    }

    private void tampilkanHasilQuiz() {
        HasilQuizView hasilView = new HasilQuizView();
        hasilView.displayHasil(skorSaatIni, daftarSoalKuis.size());

        hasilView.getKembaliButton().setOnAction(e -> showLandingPage());
        hasilView.getMainLagiButton().setOnAction(e -> startTebakGambarQuiz());

        mainView.setView(hasilView.getView());
    }

    // --- Bagian Kuis Mencocokkan Gambar ---

    private void startMencocokkanGambarQuiz() {
        this.skorSaatIni = 0;
        this.currentMatchingRound = 1;
        this.matchingScore = 0;
        tampilkanMencocokkanGambarRound(1);
    }

    private void tampilkanMencocokkanGambarRound(int round) {
        if (round > 3) { // 3 rounds total
            tampilkanSkorAkhirMatching();
            return;
        }

        MatchingQuizFXView matchingView = new MatchingQuizFXView();

        // Data untuk round ini
        List<String> imagePaths, names, ids;

        switch (round) {
            case 1:
                imagePaths = Arrays.asList(
                    "/images/soccer_dribble.jpg",
                    "/images/soccer_shoot.jpg",
                    "/images/soccer_goal.jpg",
                    "/images/soccer_heading.jpg"
                );
                names = Arrays.asList("Dribbling", "Shooting", "Goal Keeping", "Heading");
                ids = Arrays.asList("dribble", "shoot", "goal", "heading");
                break;
            case 2:
                imagePaths = Arrays.asList(
                    "/images/badminton_service.jpg",
                    "/images/badminton_smash.jpg",
                    "/images/badminton_netting.jpg",
                    "/images/soccer_tackle.jpg"
                );
                names = Arrays.asList("Service", "Smash", "Netting", "Tackle");
                ids = Arrays.asList("service", "smash", "netting", "tackle");
                break;
            default:
                imagePaths = Arrays.asList(
                    "/images/soccer_goalkeeper.jpg",
                    "/images/soccer_dribble.jpg",
                    "/images/badminton_service.jpg",
                    "/images/badminton_smash.jpg"
                );
                names = Arrays.asList("Goalkeeping", "Dribble", "Badminton Service", "Badminton Smash");
                ids = Arrays.asList("goalkeeper", "dribble", "service", "smash");
                break;
        }

        List<String> shuffledNames = Arrays.asList(names.get(0), names.get(1), names.get(2), names.get(3));
        Collections.shuffle(shuffledNames);

        matchingView.setRound(round, 3, imagePaths, shuffledNames, ids);

        matchingView.setOnMatchAttempt(isCorrect -> {
            if (isCorrect) {
                matchingScore++;
            }
        });

        matchingView.setOnNextRound(() -> {
            currentMatchingRound++;
            tampilkanMencocokkanGambarRound(currentMatchingRound);
        });

        matchingView.getKembaliButton().setOnAction(e -> showQuizModePage());

        mainView.setView(matchingView.getView());
    }

    private void tampilkanSkorAkhirMatching() {
        HasilQuizView hasilView = new HasilQuizView();
        hasilView.displayHasil(matchingScore, 12); // 12 = 4 matches × 3 rounds

        hasilView.getKembaliButton().setOnAction(e -> showLandingPage());
        hasilView.getMainLagiButton().setOnAction(e -> startMencocokkanGambarQuiz());

        mainView.setView(hasilView.getView());
    }

    /**
     * Menampilkan halaman pemilihan mode quiz (Tebak Gambar vs Mencocokkan Gambar)
     */
    private void showQuizModeSelectionPage() {
        mainView.showQuizModeSelectionPage();
        attachNavbarEvents(); // Pastikan event handler navbar di-attach
        attachQuizModeSelectionPageEvents();
    }

    /**
     * Attach event handlers untuk halaman pemilihan mode quiz
     */
    private void attachQuizModeSelectionPageEvents() {
        // Attach navbar events
        attachNavbarEvents();

        // Event handlers untuk tombol mode quiz
        if (mainView.tebakGambarButton != null) {
            mainView.tebakGambarButton.setOnAction(e -> startTebakGambarQuiz());
        }
        if (mainView.cocokGambarButton != null) {
            mainView.cocokGambarButton.setOnAction(e -> startMencocokkanGambarQuiz());
        }
    }

    /**
     * Menampilkan halaman petunjuk
     */
    private void showInstructionsPage() {
        // Implementasi akan dibuat nanti - untuk sekarang fokus ke halaman quiz utama dulu
        System.out.println("Tombol Petunjuk ditekan - akan mengarah ke halaman petunjuk");
        // Placeholder - akan dibuat halaman petunjuk
    }
}
