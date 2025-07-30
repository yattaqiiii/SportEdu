package com.sportedu.view;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel untuk permainan Kuis Mencocokkan Gambar.
 */
public class MatchingQuizView extends JPanel {
    private final JLabel progressLabel;
    private final List<JButton> imageButtons = new ArrayList<>();
    private final List<JButton> nameButtons = new ArrayList<>();
    private final JButton backButton;

    public MatchingQuizView() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel Atas: Judul dan Progress
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Mencocokkan Gambar", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        progressLabel = new JLabel("Set 1 / 3", SwingConstants.CENTER);
        progressLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(progressLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // Panel Tengah: Area Permainan
        JPanel gamePanel = new JPanel(new GridLayout(1, 2, 20, 20));
        JPanel imagePanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JPanel namePanel = new JPanel(new GridLayout(4, 1, 10, 10));

        // Inisialisasi tombol gambar dan nama
        for (int i = 0; i < 4; i++) {
            JButton imgButton = new JButton();
            imgButton.setPreferredSize(new Dimension(200, 150));
            imageButtons.add(imgButton);
            imagePanel.add(imgButton);

            JButton nameButton = new JButton();
            nameButton.setFont(new Font("Arial", Font.BOLD, 16));
            nameButtons.add(nameButton);
            namePanel.add(nameButton);
        }
        gamePanel.add(imagePanel);
        gamePanel.add(namePanel);
        add(gamePanel, BorderLayout.CENTER);

        // Panel Bawah: Tombol Kembali
        backButton = new JButton("Kembali ke Menu");
        add(backButton, BorderLayout.SOUTH);
    }

    public void setRound(int round, int totalRounds, List<String> imagePaths, List<String> names) {
        progressLabel.setText("Set " + round + " / " + totalRounds);
        resetButtons();

        for (int i = 0; i < 4; i++) {
            // Set gambar
            try {
                ImageIcon originalIcon = new ImageIcon(getClass().getResource(imagePaths.get(i)));
                Image scaledImage = originalIcon.getImage().getScaledInstance(180, 130, Image.SCALE_SMOOTH);
                imageButtons.get(i).setIcon(new ImageIcon(scaledImage));
                imageButtons.get(i).setActionCommand(imagePaths.get(i)); // Simpan path sebagai identitas
            } catch (Exception e) {
                imageButtons.get(i).setText("Err");
            }
            // Set nama
            nameButtons.get(i).setText(names.get(i));
            nameButtons.get(i).setActionCommand(names.get(i)); // Simpan nama sebagai identitas
        }
    }

    public void resetButtons() {
        for (int i = 0; i < 4; i++) {
            imageButtons.get(i).setEnabled(true);
            imageButtons.get(i).setBorder(UIManager.getBorder("Button.border"));
            nameButtons.get(i).setEnabled(true);
            nameButtons.get(i).setBorder(UIManager.getBorder("Button.border"));
        }
    }

    public void highlightButton(JButton button, boolean selected) {
        if (selected) {
            button.setBorder(new LineBorder(Color.BLUE, 3));
        } else {
            button.setBorder(UIManager.getBorder("Button.border"));
        }
    }

    public void markCorrect(JButton imageButton, JButton nameButton) {
        imageButton.setEnabled(false);
        nameButton.setEnabled(false);
        imageButton.setBorder(new LineBorder(Color.GREEN, 3));
        nameButton.setBorder(new LineBorder(Color.GREEN, 3));
    }

    public void addImageButtonListener(ActionListener listener) {
        for (JButton button : imageButtons) {
            button.addActionListener(listener);
        }
    }

    public void addNameButtonListener(ActionListener listener) {
        for (JButton button : nameButtons) {
            button.addActionListener(listener);
        }
    }

    public void addBackListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    public void showResult(int score, int total) {
        String message = String.format("Kuis Selesai!\nAnda berhasil mencocokkan %d dari %d pasangan.", score, total);
        JOptionPane.showMessageDialog(this, message, "Hasil Kuis", JOptionPane.INFORMATION_MESSAGE);
    }
}
