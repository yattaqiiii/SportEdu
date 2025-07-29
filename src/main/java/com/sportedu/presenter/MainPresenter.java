// MainPresenter.java
package com.sportedu.presenter;

import com.sportedu.model.SportEduModel;
import com.sportedu.view.MainView;

public class MainPresenter implements MainView.MainViewListener {
    private MainView view;
    private SportEduModel model;

    // State management untuk navigasi
    private String currentSport = "";
    private String currentTeknik = "";
    private int currentPageIndex = 0;

    public MainPresenter(MainView view) {
        this.view = view;
        this.model = new SportEduModel();
        this.view.setListener(this);
    }

    public void showWelcomePage() {
        view.showWelcomePage();
    }

    @Override
    public void onMateriClicked() {
        view.showMateriPage();
    }

    @Override
    public void onQuizClicked() {
        view.showQuizPage();
    }

    @Override
    public void onSepakBolaClicked() {
        currentSport = "SepakBola";
        view.showSepakBolaPage();
    }

    @Override
    public void onBadmintonClicked() {
        currentSport = "Badminton";
        // Untuk saat ini, kita hanya fokus pada sepak bola
        // Nanti bisa dikembangkan untuk badminton
        view.showSepakBolaPage(); // Sementara menggunakan layout yang sama
    }

    @Override
    public void onTeknikClicked(String teknik) {
        currentTeknik = teknik;
        currentPageIndex = 0; // Reset ke halaman pertama

        if (teknik.equals("Passing")) {
            // Untuk passing, kita mulai dari halaman "Peralatan"
            view.showTeknikDetailPage("Passing");
        } else {
            view.showTeknikDetailPage(teknik);
        }
    }

    @Override
    public void onKembaliClicked() {
        // Navigasi kembali berdasarkan state saat ini
        if (!currentTeknik.isEmpty()) {
            // Jika sedang di detail teknik, kembali ke halaman olahraga
            currentTeknik = "";
            if (currentSport.equals("SepakBola")) {
                view.showSepakBolaPage();
            } else if (currentSport.equals("Badminton")) {
                view.showSepakBolaPage(); // Sementara
            }
        } else if (!currentSport.isEmpty()) {
            // Jika sedang di halaman olahraga, kembali ke halaman materi
            currentSport = "";
            view.showMateriPage();
        } else {
            // Kembali ke halaman utama
            view.showWelcomePage();
        }
    }

    @Override
    public void onMulaiQuizClicked() {
        // Implementasi untuk memulai quiz akan ditambahkan nanti
        System.out.println("Quiz dimulai!");
        // Sementara kembali ke halaman quiz
        view.showQuizPage();
    }

    @Override
    public void onPetunjukClicked() {
        view.showPetunjukPage();
    }

    @Override
    public void onTebakGambarClicked() {
        // Implementasi untuk tebak gambar
        System.out.println("Tebak Gambar dipilih!");
    }

    @Override
    public void onCocokkanGambarClicked() {
        // Implementasi untuk cocokkan gambar
        System.out.println("Cocokkan Gambar dipilih!");
    }

    // Method untuk navigasi dengan arrow (next/previous)
    public void navigateNext() {
        if (currentTeknik.equals("Passing")) {
            currentPageIndex++;
            if (currentPageIndex == 1) {
                view.showTeknikDetailPage("Animasi Passing");
            } else if (currentPageIndex == 2) {
                view.showTeknikDetailPage("Pengertian Passing");
            } else {
                // Loop kembali ke awal atau batasi navigasi
                currentPageIndex = 0;
                view.showTeknikDetailPage("Passing");
            }
        }
    }

    public void navigatePrevious() {
        if (currentTeknik.equals("Passing")) {
            currentPageIndex--;
            if (currentPageIndex < 0) {
                currentPageIndex = 2; // Loop ke halaman terakhir
                view.showTeknikDetailPage("Pengertian Passing");
            } else if (currentPageIndex == 0) {
                view.showTeknikDetailPage("Passing");
            } else if (currentPageIndex == 1) {
                view.showTeknikDetailPage("Animasi Passing");
            }
        }
    }

    // Getter methods untuk state management
    public String getCurrentSport() {
        return currentSport;
    }

    public String getCurrentTeknik() {
        return currentTeknik;
    }

    public int getCurrentPageIndex() {
        return currentPageIndex;
    }
}