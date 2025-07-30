package com.sportedu.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Panel ini berfungsi sebagai halaman untuk memilih mode kuis.
 */
public class QuizModeView extends JPanel {
    private final JButton tebakGambarButton;
    private final JButton mencocokkanGambarButton;
    private final JButton backButton;

    public QuizModeView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Pilih Mode Kuis", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        tebakGambarButton = new JButton("Tebak Gambar");
        tebakGambarButton.setFont(new Font("Arial", Font.BOLD, 18));
        mencocokkanGambarButton = new JButton("Mencocokkan Gambar");
        mencocokkanGambarButton.setFont(new Font("Arial", Font.BOLD, 18));

        buttonPanel.add(tebakGambarButton);
        buttonPanel.add(mencocokkanGambarButton);
        add(buttonPanel, BorderLayout.CENTER);

        backButton = new JButton("Kembali ke Menu Utama");
        add(backButton, BorderLayout.SOUTH);
    }

    public void addTebakGambarListener(ActionListener listener) {
        tebakGambarButton.addActionListener(listener);
    }

    public void addMencocokkanGambarListener(ActionListener listener) {
        mencocokkanGambarButton.addActionListener(listener);
    }

    public void addBackListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }
}
