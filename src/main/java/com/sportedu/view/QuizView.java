package com.sportedu.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class QuizView extends JPanel {
    private final JLabel questionCounterLabel;
    private final JLabel imageLabel;
    private final List<JButton> optionButtons;
    private final JButton backButton;

    public QuizView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel Atas: Judul dan Counter Soal
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Kuis Tebak Gambar", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        questionCounterLabel = new JLabel("Soal 1 / 10", SwingConstants.CENTER);
        questionCounterLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(questionCounterLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // Panel Tengah: Gambar Soal
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(400, 300));
        add(imageLabel, BorderLayout.CENTER);

        // Panel Bawah: Pilihan Jawaban dan Tombol Kembali
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel optionsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        optionButtons = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            JButton button = new JButton("Opsi " + (i + 1));
            button.setFont(new Font("Arial", Font.BOLD, 16));
            optionButtons.add(button);
            optionsPanel.add(button);
        }
        bottomPanel.add(optionsPanel, BorderLayout.CENTER);

        backButton = new JButton("Kembali ke Menu");
        bottomPanel.add(backButton, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void setQuestion(int questionNumber, int totalQuestions, String imagePath, String[] options) {
        questionCounterLabel.setText("Soal " + questionNumber + " / " + totalQuestions);

        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource(imagePath));
            Image scaledImage = originalIcon.getImage().getScaledInstance(400, 300, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            imageLabel.setIcon(null);
            imageLabel.setText("Gambar tidak ditemukan: " + imagePath);
        }

        for (int i = 0; i < optionButtons.size(); i++) {
            optionButtons.get(i).setText(options[i]);
            // Hapus listener lama sebelum menambahkan yang baru
            for (ActionListener al : optionButtons.get(i).getActionListeners()) {
                optionButtons.get(i).removeActionListener(al);
            }
        }
    }

    public void addOptionButtonListener(int index, ActionListener listener) {
        if (index < optionButtons.size()) {
            optionButtons.get(index).addActionListener(listener);
        }
    }

    public void addBackListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    public void showResult(int score, int totalQuestions) {
        String message = String.format("Kuis Selesai!\nSkor Anda: %d dari %d", score, totalQuestions);
        JOptionPane.showMessageDialog(this, message, "Hasil Kuis", JOptionPane.INFORMATION_MESSAGE);
    }
}
