package com.sportedu.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LandingView extends JPanel {
    private final JButton materiButton;
    private final JButton quizButton;

    public LandingView() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Selamat Datang di SportEdu");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        materiButton = new JButton("Materi");
        materiButton.setFont(new Font("Arial", Font.PLAIN, 18));
        materiButton.setPreferredSize(new Dimension(200, 50));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(materiButton, gbc);

        quizButton = new JButton("Quiz");
        quizButton.setFont(new Font("Arial", Font.PLAIN, 18));
        quizButton.setPreferredSize(new Dimension(200, 50));
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(quizButton, gbc);
    }

    public void addMateriButtonListener(ActionListener listener) { materiButton.addActionListener(listener); }
    public void addQuizButtonListener(ActionListener listener) { quizButton.addActionListener(listener); }
}
