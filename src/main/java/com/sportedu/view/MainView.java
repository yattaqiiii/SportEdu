package com.sportedu.view;

import com.sportedu.model.SportEduModel;
import com.sportedu.model.Teknik;
import javax.swing.*;
import java.awt.*;

/**
 * Jendela utama yang menggunakan CardLayout untuk beralih antar panel (halaman).
 * Ini adalah file "wadah" utama yang hilang dan menyebabkan error kompilasi.
 */
public class MainView extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    // Nama-nama panel untuk CardLayout
    public static final String LANDING_PANEL = "LandingPanel";
    public static final String MATERI_PANEL = "MateriPanel";
    public static final String TEKNIK_PANEL = "TeknikPanel";
    public static final String PENJELASAN_PANEL = "PenjelasanPanel";
    public static final String QUIZ_MODE_PANEL = "QuizModePanel";
    public static final String TEBAK_GAMBAR_PANEL = "TebakGambarPanel";
    public static final String MATCHING_QUIZ_PANEL = "MatchingQuizPanel";

    // Referensi ke semua panel
    private final LandingView landingView;
    private final MateriView materiView;
    private final TeknikView teknikView;
    private final PenjelasanView penjelasanView;
    private final QuizModeView quizModeView;
    private final QuizView tebakGambarView;
    private final MatchingQuizView matchingQuizView;

    public MainView(SportEduModel model) {
        setTitle("SportEdu v1.0");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Inisialisasi semua panel dari kelas mereka masing-masing
        landingView = new LandingView();
        materiView = new MateriView();
        teknikView = new TeknikView(model);
        penjelasanView = new PenjelasanView();
        quizModeView = new QuizModeView();
        tebakGambarView = new QuizView();
        matchingQuizView = new MatchingQuizView();

        // Tambahkan semua panel ke mainPanel
        mainPanel.add(landingView, LANDING_PANEL);
        mainPanel.add(materiView, MATERI_PANEL);
        mainPanel.add(teknikView, TEKNIK_PANEL);
        mainPanel.add(penjelasanView, PENJELASAN_PANEL);
        mainPanel.add(quizModeView, QUIZ_MODE_PANEL);
        mainPanel.add(tebakGambarView, TEBAK_GAMBAR_PANEL);
        mainPanel.add(matchingQuizView, MATCHING_QUIZ_PANEL);

        add(mainPanel);
    }

    /**
     * Method untuk beralih panel.
     * @param panelName Nama panel yang ingin ditampilkan.
     */
    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    /**
     * Menampilkan panel pilihan teknik untuk olahraga tertentu.
     * @param sport Nama olahraga.
     */
    public void showTeknikView(String sport) {
        teknikView.showSport(sport);
        showPanel(TEKNIK_PANEL);
    }

    /**
     * Menampilkan panel penjelasan untuk teknik tertentu.
     * @param sport Nama olahraga (untuk tombol kembali).
     * @param teknik Objek teknik yang akan ditampilkan.
     */
    public void showPenjelasanView(String sport, Teknik teknik) {
        penjelasanView.setTeknik(sport, teknik);
        showPanel(PENJELASAN_PANEL);
    }

    // Getters untuk diakses oleh Presenter
    public LandingView getLandingView() { return landingView; }
    public MateriView getMateriView() { return materiView; }
    public TeknikView getTeknikView() { return teknikView; }
    public PenjelasanView getPenjelasanView() { return penjelasanView; }
    public QuizModeView getQuizModeView() { return quizModeView; }
    public QuizView getTebakGambarView() { return tebakGambarView; }
    public MatchingQuizView getMatchingQuizView() { return matchingQuizView; }
}
