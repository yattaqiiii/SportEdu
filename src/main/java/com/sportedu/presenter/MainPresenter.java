package com.sportedu.presenter;

import com.sportedu.model.Soal;
import com.sportedu.model.SportEduModel;
import com.sportedu.model.Teknik;
import com.sportedu.view.*;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;

public class MainPresenter {
    private final MainView mainView;
    private final SportEduModel model;

    // State untuk kuis
    private int skorSaatIni;
    private int indeksSoalSaatIni;
    private List<Soal> daftarSoalKuis;
    private QuizView currentQuizView; // Tambahan untuk menyimpan referensi QuizView saat ini

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
            // Keep error logging for debugging
        }
    }

    private void setupNavbarAttempt(int attempt) {
        if (mainView.navHomeButton != null) {
            // Clear any existing handlers first
            mainView.navHomeButton.setOnAction(null);
            // Set new handler
            mainView.navHomeButton.setOnAction(event -> {
                brutalNavigateToHome();
            });
        }

        if (mainView.navMateriButton != null) {
            mainView.navMateriButton.setOnAction(null);
            mainView.navMateriButton.setOnAction(event -> {
                brutalNavigateToMateri();
            });
        }

        if (mainView.navQuizButton != null) {
            mainView.navQuizButton.setOnAction(null);
            mainView.navQuizButton.setOnAction(event -> {
                brutalNavigateToQuiz();
            });
        }
    }

    // BRUTAL NAVIGATION METHODS
    private void brutalNavigateToHome() {
        javafx.application.Platform.runLater(() -> {
            mainView.showLandingPage();
            attachLandingPageEvents();
            // RE-SETUP NAVBAR setelah brutal navigation
            reSetupNavbarAfterPageChange();
        });
    }

    private void brutalNavigateToMateri() {
        javafx.application.Platform.runLater(() -> {
            mainView.showMateriPilihanPage();
            attachMateriPageEvents();
            // RE-SETUP NAVBAR setelah brutal navigation
            reSetupNavbarAfterPageChange();
        });
    }

    private void brutalNavigateToQuiz() {
        javafx.application.Platform.runLater(() -> {
            mainView.showQuizModePage();
            attachQuizModePageEvents();
            // RE-SETUP NAVBAR setelah brutal navigation
            reSetupNavbarAfterPageChange();
        });
    }

    // --- Bagian Navigasi Utama ---

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

        // Delay untuk memastikan UI quiz mode page sudah fully loaded
        javafx.application.Platform.runLater(() -> {
            javafx.application.Platform.runLater(() -> {
                attachQuizModePageEvents();
            });
        });

        // RE-SETUP NAVBAR setiap pindah halaman
        reSetupNavbarAfterPageChange();
    }

    /**
     * RE-SETUP navbar setelah pindah halaman karena navbar dibuat ulang
     */
    private void reSetupNavbarAfterPageChange() {
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
        if (mainView.mulaiButton != null) {
            mainView.mulaiButton.setOnAction(null);
            mainView.mulaiButton.setOnAction(e -> {
                showQuizModeSelectionPage();
            });
        }

        if (mainView.petunjukButton != null) {
            mainView.petunjukButton.setOnAction(null);
            mainView.petunjukButton.setOnAction(e -> {
                showInstructionsPage();
            });
        }

        if (mainView.quizKembaliButton != null) {
            mainView.quizKembaliButton.setOnAction(null);
            mainView.quizKembaliButton.setOnAction(e -> {
                forceNavigateToLandingPage();
            });
        }

        // Force refresh button events dengan delay untuk memastikan UI sudah siap
        javafx.application.Platform.runLater(() -> {
            if (mainView.petunjukButton != null && mainView.mulaiButton != null) {
                mainView.petunjukButton.setOnAction(e -> {
                    showInstructionsPage();
                });

                mainView.mulaiButton.setOnAction(e -> {
                    showQuizModeSelectionPage();
                });

                if (mainView.quizKembaliButton != null) {
                    mainView.quizKembaliButton.setOnAction(e -> {
                        forceNavigateToLandingPage();
                    });
                }
            }
        });
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
        // PENTING: Stop semua audio sebelum pindah ke halaman teknik
        stopAllAudioInAllViews();

        TeknikView teknikView = new TeknikView(mainView.getStage());
        List<Teknik> teknikList;

        if ("Sepak Bola".equals(olahraga)) {
            teknikList = model.getSepakBolaTekniks();
        } else {
            teknikList = model.getBadmintonTekniks();
        }

        teknikView.displayTeknik(teknikList, olahraga);

        // Event handler untuk memilih teknik -> ke halaman penjelasan
        teknikView.setOnTeknikSelected(teknik -> showPenjelasanPage(teknik, olahraga));

        // Tombol Kembali - kembali ke halaman pilih materi
        teknikView.getKembaliButton().setOnAction(e -> {
            // PENTING: Stop semua audio sebelum kembali ke materi
            stopAllAudioInAllViews();
            showMateriPage();
        });
        mainView.setView(teknikView.getView());
    }

    // --- Bagian Alur Materi: Penjelasan -> Animasi ---

    private void showPenjelasanPage(Teknik teknik, String olahraga) {
        // PENTING: Stop semua audio terlebih dahulu sebelum membuat PenjelasanView baru
        stopAllAudioInAllViews();

        PenjelasanView penjelasanView = new PenjelasanView();
        penjelasanView.displayTeknik(teknik);

        // Tombol Next - sekarang menampilkan overlay animasi di halaman yang sama
        penjelasanView.getNextButton().setOnAction(e -> {
            // Tampilkan overlay animasi di halaman penjelasan
            penjelasanView.showAnimationOverlay(teknik);
        });

        // Tombol Kembali kuning - kembali ke halaman teknik DENGAN FORCE STOP AUDIO
        penjelasanView.getKembaliButton().setOnAction(e -> {
            // PENTING: Stop audio PenjelasanView secara paksa sebelum navigasi
            penjelasanView.stopAudio();
            penjelasanView.cleanup();
            stopAllAudioInAllViews();
            showTeknikPage(olahraga);
        });

        mainView.setView(penjelasanView.getView());
    }

    private void showAnimasiPage(Teknik teknik, String olahraga) {
        AnimasiView animasiView = new AnimasiView();
        animasiView.displayAnimasi(teknik);

        // Tombol Back - kembali ke halaman penjelasan
        animasiView.getBackButton().setOnAction(e -> showPenjelasanPage(teknik, olahraga));

        // Tombol Kembali kuning - kembali ke halaman teknik
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
        QuizView quizView = new QuizView(mainView.getStage());

        try {
            quizView.displaySoal(soal);
        } catch (Exception e) {
            System.err.println("Error displaying quiz question: " + e.getMessage());
            e.printStackTrace();
            // Skip to next question if current one fails
            indeksSoalSaatIni++;
            tampilkanSoalBerikutnya();
            return;
        }

        // Simpan referensi ke QuizView saat ini
        currentQuizView = quizView;

        // Setup action listeners untuk pilihan jawaban dengan null checking
        if (quizView.pilihanButtons != null && quizView.pilihanButtons.length >= 4) {
            for (int i = 0; i < quizView.pilihanButtons.length; i++) {
                final int jawabanPilihan = i;
                if (quizView.pilihanButtons[i] != null) {
                    quizView.pilihanButtons[i].setOnAction(e -> {
                        prosesJawaban(jawabanPilihan, soal.getJawabanBenar());
                    });
                }
            }
        } else {
            System.err.println("Error: Quiz buttons not properly initialized");
            // Skip to next question
            indeksSoalSaatIni++;
            tampilkanSoalBerikutnya();
            return;
        }

        quizView.getKembaliButton().setOnAction(e -> showLandingPage());
        mainView.setView(quizView.getView());
    }

    private void prosesJawaban(int jawabanPilihan, int jawabanBenar) {
        // Tampilkan efek visual terlebih dahulu
        if (currentQuizView != null) {
            currentQuizView.showAnswerEffects(jawabanPilihan, jawabanBenar);
        }

        // Update skor
        if (jawabanPilihan == jawabanBenar) {
            skorSaatIni++;
        }

        // Delay sebelum pindah ke soal berikutnya agar user bisa melihat efek
        javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(
                javafx.util.Duration.seconds(1.5)); // 1.5 detik delay
        delay.setOnFinished(e -> {
            indeksSoalSaatIni++;
            tampilkanSoalBerikutnya();
        });
        delay.play();
    }

    private void tampilkanHasilQuiz() {
        // Langsung tampilkan hasil tanpa kembali ke quiz mode page
        javafx.application.Platform.runLater(() -> {
            // Buat overlay hasil
            HasilTebakGambarView hasilOverlay = new HasilTebakGambarView();
            hasilOverlay.displayHasil(skorSaatIni, daftarSoalKuis.size());

            // Setup button actions - tombol kembali ke landing page (homepage)
            hasilOverlay.getKembaliButton().setOnAction(e -> {
                showLandingPage();
            });

            hasilOverlay.getMainLagiButton().setOnAction(e -> {
                showLandingPage();
            });

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

        MatchingQuizFXView matchingView = new MatchingQuizFXView(mainView.getStage());

        // Data untuk round ini
        List<String> imagePaths, names, ids;

        switch (round) {
            case 1:
                imagePaths = Arrays.asList(
                        "/images/soccer_dribble.jpg",
                        "/images/soccer_shoot.jpg",
                        "/images/soccer_goal.jpg",
                        "/images/soccer_heading.jpg");
                names = Arrays.asList("Dribble", "Shoot", "Goal", "Heading");
                ids = Arrays.asList("dribble", "shoot", "goal", "heading");
                break;
            case 2:
                imagePaths = Arrays.asList(
                        "/images/badminton_service.jpg",
                        "/images/badminton_smash.jpg",
                        "/images/badminton_netting.jpg",
                        "/images/badminton_footwork.jpg");
                names = Arrays.asList("Service", "Smash", "Netting", "Footwork");
                ids = Arrays.asList("service", "smash", "netting", "footwork");
                break;
            default:
                imagePaths = Arrays.asList(
                        "/images/soccer_goalkeeper.jpg",
                        "/images/soccer_dribble.jpg",
                        "/images/badminton_service.jpg",
                        "/images/badminton_smash.jpg");
                names = Arrays.asList("Goalkeeper", "Dribble", "Service", "Smash");
                ids = Arrays.asList("goalkeeper", "dribble", "service", "smash");
                break;
        }

        // Acak posisi jawaban dengan benar
        List<String> shuffledNames = new ArrayList<>(names);
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

        // Set up navigation callbacks
        matchingView.setOnNavigation(new MatchingQuizFXView.OnNavigationListener() {
            @Override
            public void onNavigateToHome() {
                showLandingPage();
            }

            @Override
            public void onNavigateToMateri() {
                showMateriPage();
            }

            @Override
            public void onNavigateToQuiz() {
                showQuizModePage();
            }

            @Override
            public void onNavigateBack() {
                showQuizModePage();
            }
        });

        mainView.setView(matchingView.getView());
    }

    private void tampilkanSkorAkhirMatching() {
        HasilMencocokkanGambarView hasilView = new HasilMencocokkanGambarView();
        hasilView.displayHasil(matchingScore, 12); // 12 = 4 matches × 3 rounds

        hasilView.getKembaliButton().setOnAction(e -> {
            showLandingPage();
        });
        hasilView.getMainLagiButton().setOnAction(e -> {
            showLandingPage();
        });

        mainView.setView(hasilView.getView());
    }

    /**
     * Menampilkan halaman pemilihan mode quiz (Tebak Gambar vs Mencocokkan Gambar)
     */
    private void showQuizModeSelectionPage() {
        mainView.showQuizModeSelectionPage();
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
            mainView.cocokGambarButton.setOnAction(e -> {
                startMencocokkanGambarQuiz();
            });
        }

        if (mainView.quizModeSelectionCancelButton != null) {
            mainView.quizModeSelectionCancelButton.setOnAction(e -> {
                forceNavigateToQuizPage();
            });
        }
    }

    /**
     * Menampilkan halaman petunjuk
     */
    private void showInstructionsPage() {
        InstructionsView instructionsView = new InstructionsView(mainView.getStage());

        // Set up navigation callbacks
        instructionsView.setOnNavigation(new InstructionsView.OnNavigationListener() {
            @Override
            public void onNavigateToHome() {
                showLandingPage();
            }

            @Override
            public void onNavigateToMateri() {
                showMateriPage();
            }

            @Override
            public void onNavigateToQuiz() {
                showQuizModePage();
            }

            @Override
            public void onNavigateBack() {
                showQuizModePage(); // Back to quiz mode selection
            }
        });

        mainView.setView(instructionsView.getView());
    }

    /**
     * Method untuk menghentikan semua audio di semua view secara paksa
     * Digunakan saat navigasi untuk mencegah audio bertabrakan
     */
    private void stopAllAudioInAllViews() {
        try {
            // Force stop semua MediaPlayer yang mungkin masih aktif
            System.out.println("🔇 FORCE STOPPING all audio in all views...");

            // Coba akses PenjelasanView yang mungkin masih aktif
            // Kita tidak bisa langsung akses instance PenjelasanView, jadi kita paksa dengan cara lain

            // Force garbage collection untuk memastikan MediaPlayer yang tidak terpakai di-cleanup
            System.gc();

            // Tunggu sebentar untuk memastikan GC berjalan
            Thread.sleep(50);

            System.out.println("🔇 Audio force stop completed");

        } catch (Exception e) {
            System.err.println("Error in stopAllAudioInAllViews: " + e.getMessage());
        }
    }

    /**
     * Helper method to extract answer from image path (same logic as in MatchingQuizFXView)
     */
    private String extractAnswerFromImagePath(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return "";
        }

        // Get filename without path
        String filename = imagePath;
        if (imagePath.contains("/")) {
            filename = imagePath.substring(imagePath.lastIndexOf("/") + 1);
        }
        if (imagePath.contains("\\")) {
            filename = filename.substring(filename.lastIndexOf("\\") + 1);
        }

        // Remove file extension
        if (filename.contains(".")) {
            filename = filename.substring(0, filename.lastIndexOf("."));
        }

        // Get the last word after underscore
        if (filename.contains("_")) {
            String[] parts = filename.split("_");
            return parts[parts.length - 1].toLowerCase();
        }

        // If no underscore, return the whole filename
        return filename.toLowerCase();
    }
}
