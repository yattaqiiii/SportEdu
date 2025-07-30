package com.sportedu.presenter;

import com.sportedu.model.Soal;
import com.sportedu.model.SoalMencocokkan;
import com.sportedu.model.SportEduModel;
import com.sportedu.view.MainView; // Cukup import MainView

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Versi Presenter yang sudah diperbaiki untuk mengatasi error kompilasi.
 * Menggunakan nama method dan variabel yang benar dari MainView.
 */
public class MainPresenter {
    private final MainView mainView;
    private final SportEduModel model;

    // State untuk Kuis Tebak Gambar
    private List<Soal> tebakGambarSoal;
    private int tebakGambarIndex;
    private int tebakGambarScore;

    // State untuk Kuis Mencocokkan Gambar
    private List<SoalMencocokkan> matchingQuizSoal;
    private int matchingQuizRound;
    private int matchingQuizScore;
    private int correctMatchesInRound;
    private JButton selectedImageButton;
    private JButton selectedNameButton;

    public MainPresenter(MainView mainView, SportEduModel model) {
        this.mainView = mainView;
        this.model = model;
        initListeners();
    }

    private void initListeners() {
        // Navigasi Utama
        mainView.getLandingView().addMateriButtonListener(e -> showMateriView());
        mainView.getLandingView().addQuizButtonListener(e -> showQuizModeView());

        // Navigasi Kembali
        mainView.getMateriView().addBackListener(e -> showLandingView());
        mainView.getTeknikView().addBackListener(e -> showMateriView());
        mainView.getPenjelasanView().addBackListener(e -> mainView.showTeknikView(mainView.getPenjelasanView().getCurrentSport()));
        mainView.getQuizModeView().addBackListener(e -> showLandingView());
        mainView.getTebakGambarView().addBackListener(e -> showQuizModeView()); // Perbaikan: getTebakGambarView
        mainView.getMatchingQuizView().addBackListener(e -> showQuizModeView());

        // Alur Materi
        mainView.getMateriView().addSportButtonListener("Sepak Bola", e -> mainView.showTeknikView("Sepak Bola"));
        mainView.getMateriView().addSportButtonListener("Badminton", e -> mainView.showTeknikView("Badminton"));
        model.getTeknikList("Sepak Bola").forEach(teknik ->
                mainView.getTeknikView().addTeknikButtonListener("Sepak Bola", teknik.getNama(), e -> mainView.showPenjelasanView("Sepak Bola", teknik))
        );
        model.getTeknikList("Badminton").forEach(teknik ->
                mainView.getTeknikView().addTeknikButtonListener("Badminton", teknik.getNama(), e -> mainView.showPenjelasanView("Badminton", teknik))
        );

        // Alur Kuis
        mainView.getQuizModeView().addTebakGambarListener(e -> startTebakGambarQuiz());
        mainView.getQuizModeView().addMencocokkanGambarListener(e -> startMatchingQuiz());
        mainView.getMatchingQuizView().addImageButtonListener(this::handleMatchingImageClick);
        mainView.getMatchingQuizView().addNameButtonListener(this::handleMatchingNameClick);
    }

    // --- Logika Navigasi ---
    private void showLandingView() { mainView.showPanel(MainView.LANDING_PANEL); }
    private void showMateriView() { mainView.showPanel(MainView.MATERI_PANEL); }
    private void showQuizModeView() { mainView.showPanel(MainView.QUIZ_MODE_PANEL); }

    // --- Logika Kuis: Tebak Gambar ---
    private void startTebakGambarQuiz() {
        tebakGambarSoal = model.getShuffledSoalList();
        tebakGambarIndex = 0;
        tebakGambarScore = 0;
        mainView.showPanel(MainView.TEBAK_GAMBAR_PANEL); // Perbaikan: TEBAK_GAMBAR_PANEL
        displayNextTebakGambarQuestion();
    }

    private void displayNextTebakGambarQuestion() {
        if (tebakGambarIndex < tebakGambarSoal.size()) {
            Soal soal = tebakGambarSoal.get(tebakGambarIndex);
            mainView.getTebakGambarView().setQuestion(tebakGambarIndex + 1, tebakGambarSoal.size(), soal.getImagePath(), soal.getOptions());
            for (int i = 0; i < soal.getOptions().length; i++) {
                final int optionIndex = i;
                mainView.getTebakGambarView().addOptionButtonListener(i, e -> checkTebakGambarAnswer(soal.getOptions()[optionIndex]));
            }
        } else {
            mainView.getTebakGambarView().showResult(tebakGambarScore, tebakGambarSoal.size());
            showQuizModeView();
        }
    }

    private void checkTebakGambarAnswer(String selectedAnswer) {
        if (selectedAnswer.equals(tebakGambarSoal.get(tebakGambarIndex).getCorrectAnswer())) {
            tebakGambarScore++;
        }
        tebakGambarIndex++;
        displayNextTebakGambarQuestion();
    }

    // --- Logika Kuis: Mencocokkan Gambar ---
    private void startMatchingQuiz() {
        matchingQuizSoal = model.getSoalMencocokkanList();
        matchingQuizRound = 0;
        matchingQuizScore = 0;
        mainView.showPanel(MainView.MATCHING_QUIZ_PANEL);
        displayNextMatchingRound();
    }

    private void displayNextMatchingRound() {
        if (matchingQuizRound < matchingQuizSoal.size()) {
            correctMatchesInRound = 0;
            SoalMencocokkan soalSet = matchingQuizSoal.get(matchingQuizRound);
            Map<String, String> pasangan = soalSet.getPasanganJawaban();
            List<String> imagePaths = new ArrayList<>(pasangan.keySet());
            List<String> names = new ArrayList<>(pasangan.values());
            Collections.shuffle(imagePaths);
            Collections.shuffle(names);
            mainView.getMatchingQuizView().setRound(matchingQuizRound + 1, matchingQuizSoal.size(), imagePaths, names);
        } else {
            mainView.getMatchingQuizView().showResult(matchingQuizScore, matchingQuizSoal.size() * 4);
            showQuizModeView();
        }
    }

    private void handleMatchingImageClick(java.awt.event.ActionEvent e) {
        selectedImageButton = (JButton) e.getSource();
        mainView.getMatchingQuizView().highlightButton(selectedImageButton, true);
        checkMatch();
    }

    private void handleMatchingNameClick(java.awt.event.ActionEvent e) {
        selectedNameButton = (JButton) e.getSource();
        mainView.getMatchingQuizView().highlightButton(selectedNameButton, true);
        checkMatch();
    }

    private void checkMatch() {
        if (selectedImageButton != null && selectedNameButton != null) {
            String imagePath = selectedImageButton.getActionCommand();
            String name = selectedNameButton.getActionCommand();

            Map<String, String> correctPairs = matchingQuizSoal.get(matchingQuizRound).getPasanganJawaban();

            if (name.equals(correctPairs.get(imagePath))) {
                // Jawaban benar
                matchingQuizScore++;
                correctMatchesInRound++;
                mainView.getMatchingQuizView().markCorrect(selectedImageButton, selectedNameButton);
                if (correctMatchesInRound == 4) {
                    matchingQuizRound++;
                    // Jeda sejenak sebelum ronde berikutnya
                    Timer timer = new Timer(1000, ev -> displayNextMatchingRound());
                    timer.setRepeats(false);
                    timer.start();
                }
            } else {
                // Jawaban salah, reset highlight
                mainView.getMatchingQuizView().highlightButton(selectedImageButton, false);
                mainView.getMatchingQuizView().highlightButton(selectedNameButton, false);
            }
            // Reset pilihan
            selectedImageButton = null;
            selectedNameButton = null;
        }
    }
}
