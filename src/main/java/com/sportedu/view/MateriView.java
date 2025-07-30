package com.sportedu.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MateriView extends JPanel {
    private final JButton sepakBolaButton;
    private final JButton badmintonButton;
    private final JButton backButton;

    public MateriView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Pilih Cabang Olahraga", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        sepakBolaButton = new JButton("Sepak Bola");
        sepakBolaButton.setFont(new Font("Arial", Font.BOLD, 18));
        badmintonButton = new JButton("Badminton");
        badmintonButton.setFont(new Font("Arial", Font.BOLD, 18));
        buttonPanel.add(sepakBolaButton);
        buttonPanel.add(badmintonButton);
        add(buttonPanel, BorderLayout.CENTER);

        backButton = new JButton("Kembali");
        add(backButton, BorderLayout.SOUTH);
    }

    public void addSportButtonListener(String sport, ActionListener listener) {
        if ("Sepak Bola".equals(sport)) {
            sepakBolaButton.addActionListener(listener);
        } else if ("Badminton".equals(sport)) {
            badmintonButton.addActionListener(listener);
        }
    }
    public void addBackListener(ActionListener listener) { backButton.addActionListener(listener); }
}
