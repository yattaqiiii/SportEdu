package com.sportedu.view;

import com.sportedu.model.Teknik;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PenjelasanView extends JPanel {
    private final JLabel titleLabel;
    private final JLabel gifLabel;
    private final JTextArea descriptionArea;
    private final JButton backButton;
    private String currentSport;

    public PenjelasanView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        titleLabel = new JLabel("Nama Teknik", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        gifLabel = new JLabel();
        gifLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(gifLabel);

        descriptionArea = new JTextArea("Deskripsi akan muncul di sini.");
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 16));
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setEditable(false);
        descriptionArea.setOpaque(false);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        contentPanel.add(scrollPane);

        add(contentPanel, BorderLayout.CENTER);

        backButton = new JButton("Kembali");
        add(backButton, BorderLayout.SOUTH);
    }

    public void setTeknik(String sport, Teknik teknik) {
        this.currentSport = sport;
        titleLabel.setText(teknik.getNama());
        descriptionArea.setText(teknik.getDeskripsi());
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(teknik.getGifPath()));
            gifLabel.setIcon(icon);
        } catch (Exception e) {
            gifLabel.setIcon(null);
            gifLabel.setText("Animasi tidak ditemukan");
        }
        descriptionArea.setCaretPosition(0);
    }

    public String getCurrentSport() {
        return currentSport;
    }

    public void addBackListener(ActionListener listener) { backButton.addActionListener(listener); }
}
