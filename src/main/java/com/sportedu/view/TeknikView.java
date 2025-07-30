package com.sportedu.view;

import com.sportedu.model.SportEduModel;
import com.sportedu.model.Teknik;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeknikView extends JPanel {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel sportPanelContainer = new JPanel(cardLayout);
    private final JButton backButton;
    private final Map<String, Map<String, JButton>> teknikButtons = new HashMap<>();

    public TeknikView(SportEduModel model) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Pilih Teknik Dasar", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        createSportPanel("Sepak Bola", model.getTeknikList("Sepak Bola"));
        createSportPanel("Badminton", model.getTeknikList("Badminton"));

        add(sportPanelContainer, BorderLayout.CENTER);

        backButton = new JButton("Kembali");
        add(backButton, BorderLayout.SOUTH);
    }

    private void createSportPanel(String sportName, List<Teknik> teknikList) {
        JPanel panel = new JPanel(new GridLayout(2, 2, 15, 15));
        Map<String, JButton> buttons = new HashMap<>();
        for (Teknik teknik : teknikList) {
            JButton button = new JButton(teknik.getNama());
            button.setFont(new Font("Arial", Font.PLAIN, 16));
            panel.add(button);
            buttons.put(teknik.getNama(), button);
        }
        teknikButtons.put(sportName, buttons);
        sportPanelContainer.add(panel, sportName);
    }

    public void showSport(String sportName) {
        cardLayout.show(sportPanelContainer, sportName);
    }

    public void addTeknikButtonListener(String sport, String teknik, ActionListener listener) {
        if (teknikButtons.containsKey(sport) && teknikButtons.get(sport).containsKey(teknik)) {
            teknikButtons.get(sport).get(teknik).addActionListener(listener);
        }
    }
    public void addBackListener(ActionListener listener) { backButton.addActionListener(listener); }
}
