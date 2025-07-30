package com.sportedu.view;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.Objects;

/**
 * Kelas pabrik untuk membuat komponen UI yang sudah didesain sesuai tema.
 * Mengatur warna, font, dan gaya komponen secara terpusat.
 */
public class UIFactory {

    // Palet Warna dari Desain
    public static final Color COLOR_BACKGROUND_START = new Color(0, 77, 153); // Biru Tua
    public static final Color COLOR_BACKGROUND_END = new Color(51, 153, 255); // Biru Muda
    public static final Color COLOR_WHITE = Color.WHITE;
    public static final Color COLOR_TEXT_DARK = new Color(51, 51, 51);
    public static final Color COLOR_BUTTON_YELLOW = new Color(255, 193, 7);
    public static final Color COLOR_BUTTON_BLUE = new Color(30, 136, 229);

    // Font Kustom
    public static Font FONT_POPPINS_BOLD;
    public static Font FONT_POPPINS_REGULAR;

    // Blok statis untuk memuat font saat aplikasi pertama kali dijalankan
    static {
        try {
            InputStream boldStream = UIFactory.class.getResourceAsStream("/assets/Poppins/Poppins-Bold.ttf");
            InputStream regularStream = UIFactory.class.getResourceAsStream("/assets/Poppins/Poppins-Regular.ttf");

            if (boldStream == null || regularStream == null) {
                throw new RuntimeException("Font files not found in resources!");
            }

            FONT_POPPINS_BOLD = Font.createFont(Font.TRUETYPE_FONT, boldStream).deriveFont(Font.PLAIN);
            FONT_POPPINS_REGULAR = Font.createFont(Font.TRUETYPE_FONT, regularStream).deriveFont(Font.PLAIN);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(FONT_POPPINS_BOLD);
            ge.registerFont(FONT_POPPINS_REGULAR);

        } catch (Exception e) {
            e.printStackTrace();
            // Fallback ke font default jika Poppins gagal dimuat
            FONT_POPPINS_BOLD = new Font("Arial", Font.BOLD, 18);
            FONT_POPPINS_REGULAR = new Font("Arial", Font.PLAIN, 14);
        }
    }

    /**
     * Membuat panel utama dengan latar belakang gradien.
     */
    public static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            int w = getWidth();
            int h = getHeight();
            GradientPaint gp = new GradientPaint(0, 0, COLOR_BACKGROUND_START, 0, h, COLOR_BACKGROUND_END);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
        }
    }

    /**
     * Membuat tombol utama yang besar dan bergaya.
     * @param text Teks pada tombol.
     * @param iconPath Path ke ikon di dalam folder resources.
     * @return JButton yang sudah didesain.
     */
    public static JButton createMainButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(FONT_POPPINS_BOLD.deriveFont(24f));
        button.setForeground(COLOR_TEXT_DARK);
        button.setBackground(COLOR_WHITE);
        button.setIcon(new ImageIcon(Objects.requireNonNull(UIFactory.class.getResource(iconPath))));
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setPreferredSize(new Dimension(250, 150));
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setFocusPainted(false);
        return button;
    }

    /**
     * Membuat tombol pilihan (seperti pilihan teknik).
     * @param text Teks pada tombol.
     * @return JButton yang sudah didesain.
     */
    public static JButton createChoiceButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_POPPINS_BOLD.deriveFont(18f));
        button.setForeground(COLOR_WHITE);
        button.setBackground(COLOR_BUTTON_BLUE);
        button.setPreferredSize(new Dimension(200, 60));
        button.setBorder(BorderFactory.createLineBorder(COLOR_WHITE, 2));
        button.setFocusPainted(false);
        return button;
    }

    /**
     * Membuat tombol kembali (panah).
     * @return JButton yang sudah didesain.
     */
    public static JButton createBackButton() {
        JButton button = new JButton(new ImageIcon(Objects.requireNonNull(UIFactory.class.getResource("/assets/back_icon.png"))));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        return button;
    }

    /**
     * Membuat label judul utama.
     * @param text Teks judul.
     * @return JLabel yang sudah didesain.
     */
    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_POPPINS_BOLD.deriveFont(36f));
        label.setForeground(COLOR_WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }
}
```
        **Catatan:** Pastikan Anda memiliki ikon `back_icon.png` di dalam folder `src/main/resources/assets/`. Jika belum, Anda bisa menggunakan ikon panah kiri apa pun.

---

        ### **Langkah 2: Rombak Total Semua File `View`**

Sekarang, ganti **semua file** di dalam `src/main/java/com/sportedu/view/` dengan versi baru di bawah ini. Kode ini akan menggunakan `UIFactory` yang baru saja kita buat.

**1. `LandingView.java`**

        ```java
package com.sportedu.view;

import javax.swing.*;
        import java.awt.*;
        import java.awt.event.ActionListener;
import java.util.Objects;

public class LandingView extends UIFactory.GradientPanel {
    private final JButton materiButton;
    private final JButton quizButton;

    public LandingView() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Logo
        JLabel logoLabel = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/logo.png"))));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 20, 50, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        add(logoLabel, gbc);

        // Tombol Materi
        materiButton = UIFactory.createMainButton("Materi", "/assets/materi_icon.png");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 20, 10, 10);
        add(materiButton, gbc);

        // Tombol Quiz
        quizButton = UIFactory.createMainButton("Quiz", "/assets/quiz_icon.png");
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 10, 10, 20);
        add(quizButton, gbc);
    }

    public void addMateriButtonListener(ActionListener listener) { materiButton.addActionListener(listener); }
    public void addQuizButtonListener(ActionListener listener) { quizButton.addActionListener(listener); }
}
```
        **Catatan:** Pastikan ada `materi_icon.png` dan `quiz_icon.png` di folder `assets`.

        **2. `MateriView.java`**

        ```java
package com.sportedu.view;

import javax.swing.*;
        import java.awt.*;
        import java.awt.event.ActionListener;

public class MateriView extends UIFactory.GradientPanel {
    private final JButton sepakBolaButton;
    private final JButton badmintonButton;
    private final JButton backButton;

    public MateriView() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel Atas: Tombol Kembali dan Judul
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        backButton = UIFactory.createBackButton();
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(UIFactory.createTitleLabel("Materi"), BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // Panel Tengah: Pilihan Olahraga
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        sepakBolaButton = UIFactory.createMainButton("Sepak Bola", "/assets/sepakbola_icon.png");
        badmintonButton = UIFactory.createMainButton("Badminton", "/assets/badminton_icon.png");

        buttonPanel.add(sepakBolaButton, gbc);
        buttonPanel.add(badmintonButton, gbc);

        add(buttonPanel, BorderLayout.CENTER);
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
```

        **3. `TeknikView.java`**

        ```java
package com.sportedu.view;

import com.sportedu.model.SportEduModel;
import com.sportedu.model.Teknik;
import javax.swing.*;
        import java.awt.*;
        import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeknikView extends UIFactory.GradientPanel {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel sportPanelContainer = new JPanel(cardLayout);
    private final JButton backButton;
    private final Map<String, Map<String, JButton>> teknikButtons = new HashMap<>();

    public TeknikView(SportEduModel model) {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        sportPanelContainer.setOpaque(false);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        backButton = UIFactory.createBackButton();
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(UIFactory.createTitleLabel("Pilih Teknik"), BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        createSportPanel("Sepak Bola", model.getTeknikList("Sepak Bola"));
        createSportPanel("Badminton", model.getTeknikList("Badminton"));

        add(sportPanelContainer, BorderLayout.CENTER);
    }

    private void createSportPanel(String sportName, List<Teknik> teknikList) {
        JPanel panel = new JPanel(new GridLayout(4, 1, 15, 15));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));
        Map<String, JButton> buttons = new HashMap<>();
        for (Teknik teknik : teknikList) {
            JButton button = UIFactory.createChoiceButton(teknik.getNama());
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
```

        ... dan seterusnya untuk file-file view lainnya (`PenjelasanView`, `QuizModeView`, `QuizView`, `MatchingQuizView`). Saya akan melanjutkan di respons berikutnya jika Anda setuju dengan pendekatan ini, karena merombak semua file akan sangat panjang.

        ### **Langkah Selanjutnya**

        1.  **Buat file `UIFactory.java`** seperti di atas.
2.  **Ganti isi** dari `LandingView.java`, `MateriView.java`, dan `TeknikView.java` dengan kode baru.
3.  **Siapkan Ikon**: Pastikan Anda memiliki gambar `logo.png`, `back_icon.png`, `materi_icon.png`, `quiz_icon.png`, `sepakbola_icon.png`, dan `badminton_icon.png` di dalam `src/main/resources/assets/`.
        4.  **Jalankan `mvn clean package`**. Seharusnya aplikasi Anda sudah berubah drastis tampilannya di 3 halaman pertama.

Jika Anda suka dengan hasilnya, saya akan lanjutkan merombak sisa halaman kuis dan penjelasan agar sama persis dengan desa