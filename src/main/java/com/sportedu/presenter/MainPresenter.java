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

        // Tampilkan landing page dulu
        showLandingPage();

        // Delay setup navbar untuk memastikan UI sudah ready
        javafx.application.Platform.runLater(() -> {
            javafx.application.Platform.runLater(() -> {
                forceSetupNavbar();
            });
        });
    }

    // --- KONSEP NAVBAR BARU: BRUTAL FORCE SETUP ---

    /**
     * BRUTAL FORCE setup navbar - paksa setup dengan cara yang lebih aggressive
     */
    private void forceSetupNavbar() {
        System.out.println("🔥 BRUTAL FORCE NAVBAR SETUP 🔥");

        try {
            // Force setup dengan multiple attempts
            setupNavbarAttempt(1);

            // Backup setup dengan delay
            javafx.animation.PauseTransition delay1 = new javafx.animation.PauseTransition(
                    javafx.util.Duration.millis(500));
            delay1.setOnFinished(e -> setupNavbarAttempt(2));
            delay1.play();

            // Triple backup setup
            javafx.animation.PauseTransition delay2 = new javafx.animation.PauseTransition(
                    javafx.util.Duration.millis(1000));
            delay2.setOnFinished(e -> setupNavbarAttempt(3));
            delay2.play();

        } catch (Exception e) {
            System.out.println("❌ Error in brutal force setup: " + e.getMessage());
        }
    }

    private void setupNavbarAttempt(int attempt) {
        System.out.println("🎯 NAVBAR SETUP ATTEMPT #" + attempt);

        if (mainView.navHomeButton != null) {
            System.out.println("✅ Attempt " + attempt + ": navHomeButton found");
            // Clear any existing handlers first
            mainView.navHomeButton.setOnAction(null);
            // Set new handler
            mainView.navHomeButton.setOnAction(event -> {
                System.out.println("🏠 NAVBAR HOME CLICKED!");
                brutalNavigateToHome();
            });
        } else {
            System.out.println("❌ Attempt " + attempt + ": navHomeButton is NULL");
        }

        if (mainView.navMateriButton != null) {
            System.out.println("✅ Attempt " + attempt + ": navMateriButton found");
            mainView.navMateriButton.setOnAction(null);
            mainView.navMateriButton.setOnAction(event -> {
                System.out.println("📚 NAVBAR MATERI CLICKED!");
                brutalNavigateToMateri();
            });
        } else {
            System.out.println("❌ Attempt " + attempt + ": navMateriButton is NULL");
        }

        if (mainView.navQuizButton != null) {
            System.out.println("✅ Attempt " + attempt + ": navQuizButton found");
            mainView.navQuizButton.setOnAction(null);
            mainView.navQuizButton.setOnAction(event -> {
                System.out.println("🎮 NAVBAR QUIZ CLICKED!");
                brutalNavigateToQuiz();
            });
        } else {
            System.out.println("❌ Attempt " + attempt + ": navQuizButton is NULL");
        }
    }

    // BRUTAL NAVIGATION METHODS
    private void brutalNavigateToHome() {
        System.out.println("🔥 BRUTAL NAVIGATE TO HOME");
        javafx.application.Platform.runLater(() -> {
            mainView.showLandingPage();
            attachLandingPageEvents();
            // RE-SETUP NAVBAR setelah brutal navigation
            reSetupNavbarAfterPageChange();
        });
    }

    private void brutalNavigateToMateri() {
        System.out.println("🔥 BRUTAL NAVIGATE TO MATERI");
        javafx.application.Platform.runLater(() -> {
            mainView.showMateriPilihanPage();
            attachMateriPageEvents();
            // RE-SETUP NAVBAR setelah brutal navigation
            reSetupNavbarAfterPageChange();
        });
    }

    private void brutalNavigateToQuiz() {
        System.out.println("🔥 BRUTAL NAVIGATE TO QUIZ");
        javafx.application.Platform.runLater(() -> {
            mainView.showQuizModePage();
            attachQuizModePageEvents();
            // RE-SETUP NAVBAR setelah brutal navigation
            reSetupNavbarAfterPageChange();
        });
    }

    // --- Bagian Navigasi Utama ---

    /**
     * Setup navbar universal - berfungsi untuk navigasi paksa ke halaman manapun
     */
    private void setupUniversalNavbar() {
        System.out.println("=== SETUP UNIVERSAL NAVBAR ===");

        // Navbar sebagai tombol navigasi universal - tidak peduli halaman apa yang
        // sedang aktif
        if (mainView.navHomeButton != null) {
            System.out.println("✓ navHomeButton - setup for universal navigation");
            mainView.navHomeButton.setOnAction(e -> {
                System.out.println(">>> NAVBAR: Force navigate to LANDING PAGE");
                forceNavigateToLandingPage();
            });
        }

        if (mainView.navMateriButton != null) {
            System.out.println("✓ navMateriButton - setup for universal navigation");
            mainView.navMateriButton.setOnAction(e -> {
                System.out.println(">>> NAVBAR: Force navigate to MATERI PAGE");
                forceNavigateToMateriPage();
            });
        }

        if (mainView.navQuizButton != null) {
            System.out.println("✓ navQuizButton - setup for universal navigation");
            mainView.navQuizButton.setOnAction(e -> {
                System.out.println(">>> NAVBAR: Force navigate to QUIZ PAGE");
                forceNavigateToQuizPage();
            });
        }

        System.out.println("=== UNIVERSAL NAVBAR READY ===");
    }

    /**
     * Force navigate ke landing page - dari halaman manapun
     */
    private void forceNavigateToLandingPage() {
        mainView.showLandingPage();
        attachLandingPageEvents();
        // RE-SETUP NAVBAR setiap pindah halaman
        reSetupNavbarAfterPageChange();
    }

    /**
     * Force navigate ke materi page - dari halaman manapun
     */
    private void forceNavigateToMateriPage() {
        mainView.showMateriPilihanPage();
        attachMateriPageEvents();
        // RE-SETUP NAVBAR setiap pindah halaman
        reSetupNavbarAfterPageChange();
    }

    /**
     * Force navigate ke quiz page - dari halaman manapun
     */
    private void forceNavigateToQuizPage() {
        mainView.showQuizModePage();
        attachQuizModePageEvents();
        // RE-SETUP NAVBAR setiap pindah halaman
        reSetupNavbarAfterPageChange();
    }

    /**
     * RE-SETUP navbar setelah pindah halaman karena navbar dibuat ulang
     */
    private void reSetupNavbarAfterPageChange() {
        System.out.println("🔄 RE-SETUP NAVBAR AFTER PAGE CHANGE");

        // Delay setup untuk memastikan UI halaman baru sudah ready
        javafx.application.Platform.runLater(() -> {
            javafx.application.Platform.runLater(() -> {
                setupNavbarAttempt(99); // Attempt 99 = re-setup
            });
        });
    }

    private void attachLandingPageEvents() {
        mainView.materiButton.setOnAction(e -> forceNavigateToMateriPage());
        mainView.quizButton.setOnAction(e -> forceNavigateToQuizPage());
    }

    private void attachMateriPageEvents() {
        mainView.kembaliButton.setOnAction(e -> forceNavigateToLandingPage());
        mainView.sepakBolaButton.setOnAction(e -> showTeknikPage("Sepak Bola"));
        mainView.badmintonButton.setOnAction(e -> showTeknikPage("Badminton"));
    }

    private void attachQuizModePageEvents() {
        // Event handlers untuk tombol Mulai dan Petunjuk
        if (mainView.mulaiButton != null) {
            mainView.mulaiButton.setOnAction(e -> showQuizModeSelectionPage());
        }
        if (mainView.petunjukButton != null) {
            mainView.petunjukButton.setOnAction(e -> showInstructionsPage());
        }
    }

    // Method navigasi sederhana untuk kompatibilitas
    private void showLandingPage() {
        forceNavigateToLandingPage();
    }

    private void showMateriPage() {
        forceNavigateToMateriPage();
    }

    private void showQuizModePage() {
        forceNavigateToQuizPage();
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

        // Tombol kembali ke daftar teknik
        penjelasanView.getBackToTeknikButton().setOnAction(e -> showTeknikPage(olahraga));

        // Tombol navigasi ke animasi
        penjelasanView.getToAnimasiPrevButton().setOnAction(e -> showAnimasiPage(teknik, olahraga));
        penjelasanView.getToAnimasiNextButton().setOnAction(e -> showAnimasiPage(teknik, olahraga));

        mainView.setView(penjelasanView.getView());
    }

    private void showAnimasiPage(Teknik teknik, String olahraga) {
        AnimasiView animasiView = new AnimasiView();
        animasiView.displayAnimasi(teknik);

        // Kedua tombol kembali ke penjelasan
        animasiView.getToPenjelasanPrevButton().setOnAction(e -> showPenjelasanPage(teknik, olahraga));
        animasiView.getToPenjelasanNextButton().setOnAction(e -> showPenjelasanPage(teknik, olahraga));

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

        // Setup navbar di QuizView dengan BRUTAL FORCE
        setupNavbarForQuizView(quizView);

        for (int i = 0; i < quizView.pilihanButtons.length; i++) {
            final int jawabanPilihan = i;
            quizView.pilihanButtons[i].setOnAction(e -> {
                prosesJawaban(jawabanPilihan, soal.getJawabanBenar());
            });
        }

        quizView.getKembaliButton().setOnAction(e -> showQuizModePage());
        mainView.setView(quizView.getView());
    }

    /**
     * Setup navbar untuk QuizView dengan BRUTAL FORCE
     */
    private void setupNavbarForQuizView(QuizView quizView) {
        System.out.println("🎯 SETUP NAVBAR FOR QUIZ VIEW");

        // Delay setup untuk memastikan UI QuizView sudah ready
        javafx.application.Platform.runLater(() -> {
            javafx.application.Platform.runLater(() -> {
                if (quizView.navHomeButton != null) {
                    System.out.println("✅ QuizView navHomeButton found");
                    quizView.navHomeButton.setOnAction(null);
                    quizView.navHomeButton.setOnAction(event -> {
                        System.out.println("🏠 QUIZ VIEW NAVBAR HOME CLICKED!");
                        brutalNavigateToHome();
                    });
                }

                if (quizView.navMateriButton != null) {
                    System.out.println("✅ QuizView navMateriButton found");
                    quizView.navMateriButton.setOnAction(null);
                    quizView.navMateriButton.setOnAction(event -> {
                        System.out.println("📚 QUIZ VIEW NAVBAR MATERI CLICKED!");
                        brutalNavigateToMateri();
                    });
                }

                if (quizView.navQuizButton != null) {
                    System.out.println("✅ QuizView navQuizButton found");
                    quizView.navQuizButton.setOnAction(null);
                    quizView.navQuizButton.setOnAction(event -> {
                        System.out.println("🎮 QUIZ VIEW NAVBAR QUIZ CLICKED!");
                        brutalNavigateToQuiz();
                    });
                }
            });
        });
    }

    private void prosesJawaban(int jawabanPilihan, int jawabanBenar) {
        if (jawabanPilihan == jawabanBenar) {
            skorSaatIni++;
        }
        indeksSoalSaatIni++;
        tampilkanSoalBerikutnya();
    }

    private void tampilkanHasilQuiz() {
        // JANGAN ganti halaman! Tampilkan overlay di atas halaman quiz-pilih mode yang sudah ada

        // Kembali ke quiz mode page dulu (jika belum ada)
        mainView.showQuizModeSelectionPage();
        attachQuizModeSelectionPageEvents();

        // Delay sedikit untuk memastikan halaman quiz mode sudah ready
        javafx.application.Platform.runLater(() -> {
            // Buat overlay hasil di atas halaman quiz mode
            HasilTebakGambarView hasilOverlay = new HasilTebakGambarView();
            hasilOverlay.displayHasil(skorSaatIni, daftarSoalKuis.size());

            // Setup button actions - tombol kembali ke quiz mode selection page
            hasilOverlay.getKembaliButton().setOnAction(e -> {
                // Tutup overlay dan kembali ke quiz mode selection page
                showQuizModeSelectionPage();
            });

            hasilOverlay.getMainLagiButton().setOnAction(e -> {
                // Tutup overlay dan mulai game lagi
                startTebakGambarQuiz();
            });

            // Setup navbar actions untuk HasilTebakGambarView
            if (hasilOverlay.getNavHomeButton() != null) {
                hasilOverlay.getNavHomeButton().setOnAction(e -> brutalNavigateToHome());
            }
            if (hasilOverlay.getNavMateriButton() != null) {
                hasilOverlay.getNavMateriButton().setOnAction(e -> brutalNavigateToMateri());
            }
            if (hasilOverlay.getNavQuizButton() != null) {
                hasilOverlay.getNavQuizButton().setOnAction(e -> brutalNavigateToQuiz());
            }

            // HasilTebakGambarView sekarang adalah popup overlay kecil, tidak ada navbar
            // Jadi tidak perlu setup navbar actions

            // TAMPILKAN OVERLAY DI ATAS HALAMAN YANG SUDAH ADA
            mainView.setView(hasilOverlay.getView());
        });
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
                        "/images/soccer_heading.jpg");
                names = Arrays.asList("Dribbling", "Shooting", "Goal Keeping", "Heading");
                ids = Arrays.asList("dribble", "shoot", "goal", "heading");
                break;
            case 2:
                imagePaths = Arrays.asList(
                        "/images/badminton_service.jpg",
                        "/images/badminton_smash.jpg",
                        "/images/badminton_netting.jpg",
                        "/images/soccer_tackle.jpg");
                names = Arrays.asList("Service", "Smash", "Netting", "Tackle");
                ids = Arrays.asList("service", "smash", "netting", "tackle");
                break;
            default:
                imagePaths = Arrays.asList(
                        "/images/soccer_goalkeeper.jpg",
                        "/images/soccer_dribble.jpg",
                        "/images/badminton_service.jpg",
                        "/images/badminton_smash.jpg");
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
        // Navbar sudah di-setup sekali di constructor, tidak perlu setup ulang
        attachQuizModeSelectionPageEvents();
    }

    /**
     * Attach event handlers untuk halaman pemilihan mode quiz
     */
    private void attachQuizModeSelectionPageEvents() {

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
        // Implementasi akan dibuat nanti - untuk sekarang fokus ke halaman quiz utama
        // dulu
        System.out.println("Tombol Petunjuk ditekan - akan mengarah ke halaman petunjuk");
        // Placeholder - akan dibuat halaman petunjuk
    }
}
