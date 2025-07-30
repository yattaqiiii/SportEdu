package com.sportedu;

import com.formdev.flatlaf.FlatLightLaf;
import com.sportedu.model.SportEduModel;
import com.sportedu.presenter.MainPresenter;
import com.sportedu.view.MainView;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException; // <-- IMPORT YANG HILANG SUDAH DITAMBAHKAN

public class Main {
    public static void main(String[] args) {
        // Mengatur Look and Feel FlatLaf agar tampilan modern
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            // Sebaiknya cetak error trace untuk debugging jika terjadi masalah
            e.printStackTrace();
            System.err.println("Failed to initialize LaF");
        }

        SwingUtilities.invokeLater(() -> {
            SportEduModel model = new SportEduModel();
            MainView view = new MainView(model); // Kirim model ke view untuk inisialisasi data
            new MainPresenter(view, model);

            view.setVisible(true);
        });
    }
}