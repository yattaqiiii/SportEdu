# PowerShell script to remove GIF fallback and improve MP4 consistency
# This script will replace the entire showAnimationOverlay method with MP4-only implementation

$filePath = "src\main\java\com\sportedu\view\PenjelasanView.java"
$content = Get-Content $filePath -Raw -Encoding UTF8

# Find the start and end of showAnimationOverlay method
$startPattern = "public void showAnimationOverlay\(Teknik teknik\) \{"
$endPattern = "^\s*\}\s*$"

# Read the file line by line to find method boundaries
$lines = Get-Content $filePath -Encoding UTF8
$startLine = -1
$endLine = -1
$braceCount = 0
$inMethod = $false

for ($i = 0; $i -lt $lines.Length; $i++) {
    if ($lines[$i] -match "public void showAnimationOverlay") {
        $startLine = $i
        $inMethod = $true
        $braceCount = 0
    }
    
    if ($inMethod) {
        # Count opening and closing braces
        $openBraces = ($lines[$i] -split "\{").Length - 1
        $closeBraces = ($lines[$i] -split "\}").Length - 1
        $braceCount += $openBraces - $closeBraces
        
        # If we're back to 0 braces, we've found the end
        if ($braceCount -eq 0 -and $i -gt $startLine) {
            $endLine = $i
            break
        }
    }
}

if ($startLine -eq -1 -or $endLine -eq -1) {
    Write-Host "Could not find showAnimationOverlay method boundaries"
    exit 1
}

Write-Host "Found showAnimationOverlay method from line $($startLine + 1) to $($endLine + 1)"

# Create the new MP4-only method implementation
$newMethod = @'
    public void showAnimationOverlay(Teknik teknik) {
        try {
            // Buat overlay container yang menutupi seluruh view
            StackPane animationOverlay = new StackPane();
            animationOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);"); // Lebih transparan
            animationOverlay.setPrefSize(view.getWidth(), view.getHeight());
            animationOverlay.prefWidthProperty().bind(view.widthProperty());
            animationOverlay.prefHeightProperty().bind(view.heightProperty());

            // Container untuk konten animasi - DIKECILKAN
            VBox animationContent = new VBox(15); // Spacing dikurangi dari 20 ke 15
            animationContent.setAlignment(Pos.CENTER);
            animationContent.setPadding(new Insets(30)); // Padding dikurangi dari 50 ke 30
            animationContent.setStyle(
                    "-fx-background-color: #F5E6A3; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 12, 0, 0, 6);");
            animationContent.setMaxWidth(600); // Dikurangi dari 800 ke 600
            animationContent.setMaxHeight(450); // Dikurangi dari 600 ke 450

            // Title animasi - DIKECILKAN
            ImageView titleImageView = new ImageView();
            try {
                String titlePath = "/images/" + teknik.getNama() + "_title.png";
                Image titleImage = new Image(getClass().getResourceAsStream(titlePath));
                titleImageView.setImage(titleImage);
                titleImageView.setFitHeight(30); // Dikurangi dari 40 ke 30
                titleImageView.setPreserveRatio(true);
            } catch (Exception e) {
                Label titleLabel = new Label("Animasi " + teknik.getNama());
                titleLabel.setStyle(
                        "-fx-font-family: 'Poppins', Arial; -fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1A1E2C;");
                animationContent.getChildren().add(titleLabel);
            }

            if (titleImageView.getImage() != null) {
                animationContent.getChildren().add(titleImageView);
            }

            // Container untuk video animasi - DIKECILKAN
            VBox videoContainer = new VBox();
            videoContainer.setAlignment(Pos.CENTER);
            videoContainer.setPadding(new Insets(15)); // Dikurangi dari 20 ke 15
            videoContainer.setStyle(
                    "-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 6, 0, 0, 3);");
            videoContainer.setMaxWidth(450); // Dikurangi dari 600 ke 450

            // Load MP4 video ONLY - NO GIF FALLBACK
            javafx.scene.media.MediaView videoView = new javafx.scene.media.MediaView();
            javafx.scene.media.MediaPlayer animationPlayer = null;

            // MP4 video path only
            String videoPath = "/videos/" + teknik.getNama().toLowerCase() + ".mp4";
            System.out.println("📹 MP4-ONLY: Loading VIDEO " + videoPath + " for " + teknik.getNama());

            boolean videoLoaded = false;

            // Try MP4 video with improved consistency and error handling
            try {
                var videoUrl = getClass().getResource(videoPath);
                if (videoUrl != null) {
                    System.out.println("Video resource found: " + videoUrl.toString());

                    // Create media with proper error handling
                    javafx.scene.media.Media media = new javafx.scene.media.Media(videoUrl.toExternalForm());
                    animationPlayer = new javafx.scene.media.MediaPlayer(media);

                    // Configure video playback with better stability
                    animationPlayer.setAutoPlay(false); // Manual start for better control
                    animationPlayer.setCycleCount(javafx.scene.media.MediaPlayer.INDEFINITE); // Loop forever
                    animationPlayer.setMute(true); // Mute video audio (we have separate technique audio)
                    
                    // Add ready listener for consistent loading
                    final javafx.scene.media.MediaPlayer playerRef = animationPlayer;
                    animationPlayer.setOnReady(() -> {
                        Platform.runLater(() -> {
                            try {
                                if (playerRef.getStatus() != javafx.scene.media.MediaPlayer.Status.DISPOSED) {
                                    playerRef.play(); // Start playback when ready
                                    System.out.println("🎬 MP4 playback started for " + teknik.getNama());
                                }
                            } catch (Exception e) {
                                System.err.println("❌ Error starting MP4 playback: " + e.getMessage());
                            }
                        });
                    });
                    
                    // Add error listener
                    animationPlayer.setOnError(() -> {
                        System.err.println("❌ MP4 MediaPlayer error: " + animationPlayer.getError());
                    });

                    // Add status change listener for debugging
                    animationPlayer.statusProperty().addListener((obs, oldStatus, newStatus) -> {
                        System.out.println("🎥 MP4 Status changed from " + oldStatus + " to " + newStatus);
                    });

                    videoView.setMediaPlayer(animationPlayer);
                    videoView.setFitWidth(350);
                    videoView.setFitHeight(200);
                    videoView.setPreserveRatio(true);
                    videoView.setSmooth(true);

                    // Add video to container
                    videoContainer.getChildren().clear();
                    videoContainer.getChildren().add(videoView);
                    videoView.setVisible(true);
                    videoView.setOpacity(1.0);

                    videoLoaded = true;
                    System.out.println("🎬 SUCCESS: MP4 video loaded for " + teknik.getNama());

                    // Store player reference for cleanup
                    videoView.setUserData(animationPlayer);

                } else {
                    System.err.println("❌ MP4 resource not found: " + videoPath);
                }
            } catch (Exception e) {
                System.err.println("❌ MP4 loading error: " + e.getMessage());
                e.printStackTrace();
            }

            // Show error if MP4 not loaded (NO GIF FALLBACK)
            if (!videoLoaded) {
                System.err.println("❌ CRITICAL: MP4 video not available for " + teknik.getNama());

                // Show placeholder text indicating MP4 issue
                Label errorLabel = new Label("Video MP4 tidak dapat dimuat\nuntuk teknik " + teknik.getNama());
                errorLabel.setStyle(
                        "-fx-font-family: 'Poppins', Arial; -fx-font-size: 14px; -fx-text-fill: #666; -fx-text-alignment: center;");
                errorLabel.setAlignment(Pos.CENTER);

                videoContainer.getChildren().clear();
                videoContainer.getChildren().add(errorLabel);
            }

            animationContent.getChildren().add(videoContainer);

            // Instruksi - DIKECILKAN
            Label instructionLabel = new Label(
                    "Perhatikan animasi di atas untuk memahami gerakan teknik " + teknik.getNama());
            instructionLabel.setStyle(
                    "-fx-font-family: 'Poppins', Arial; -fx-font-size: 12px; -fx-text-fill: #1A1E2C; -fx-text-alignment: center;");
            instructionLabel.setWrapText(true);
            instructionLabel.setMaxWidth(400); // Dikurangi dari 500 ke 400
            instructionLabel.setAlignment(Pos.CENTER);
            animationContent.getChildren().add(instructionLabel);

            // Tombol kembali - DIKECILKAN
            Button closeButton = UIFactory.createNavButton("/images/Kembali_kuning.png", 150, 45);
            closeButton.setOnAction(e -> hideAnimationOverlay());
            animationContent.getChildren().add(closeButton);

            // Animasi entrance LEBIH SMOOTH dan RINGAN
            animationOverlay.setOpacity(0);
            animationContent.setScaleX(0.9); // Start scale lebih dekat ke 1.0 untuk smooth
            animationContent.setScaleY(0.9);

            // Fade in yang lebih smooth
            FadeTransition fadeIn = new FadeTransition(Duration.millis(250), animationOverlay);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            // Scale transition yang lebih smooth
            ScaleTransition scaleIn = new ScaleTransition(Duration.millis(300), animationContent);
            scaleIn.setFromX(0.9);
            scaleIn.setFromY(0.9);
            scaleIn.setToX(1.0);
            scaleIn.setToY(1.0);
            scaleIn.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

            ParallelTransition entranceAnimation = new ParallelTransition(fadeIn, scaleIn);

            animationOverlay.getChildren().add(animationContent);

            // Tambahkan overlay ke view utama - perbaiki struktur layout
            if (view.getChildren().get(0) instanceof javafx.scene.control.ScrollPane) {
                javafx.scene.control.ScrollPane scrollPane = (javafx.scene.control.ScrollPane) view.getChildren()
                        .get(0);

                // Buat StackPane wrapper untuk menggabungkan konten scroll dengan overlay
                StackPane wrapperPane = new StackPane();
                wrapperPane.prefWidthProperty().bind(view.widthProperty());
                wrapperPane.prefHeightProperty().bind(view.heightProperty());

                // Pindahkan scrollPane ke wrapper
                view.getChildren().remove(scrollPane);
                wrapperPane.getChildren().add(scrollPane);
                wrapperPane.getChildren().add(animationOverlay);

                // Tambahkan wrapper ke view
                view.getChildren().add(wrapperPane);

                // Simpan referensi overlay untuk bisa dihapus nanti
                animationOverlay.setUserData("animationOverlay");
                wrapperPane.setUserData("wrapperPane");

                System.out.println("🎬 OVERLAY DEBUG: Animation overlay added to view structure");
                System.out.println("  - Wrapper children count: " + wrapperPane.getChildren().size());
                System.out.println("  - Main view children count: " + view.getChildren().size());

            } else {
                // Fallback jika struktur tidak sesuai ekspektasi
                System.err.println("❌ CRITICAL: Unexpected view structure in PenjelasanView");
                System.err.println(
                        "  - Expected: ScrollPane, Got: " + view.getChildren().get(0).getClass().getSimpleName());
                return;
            }

            entranceAnimation.play();
            System.out.println("🎬 ANIMATION: Entrance animation started for " + teknik.getNama() + " overlay");

        } catch (Exception e) {
            System.err.println("❌ CRITICAL ERROR showing animation overlay: " + e.getMessage());
            e.printStackTrace();

            // Print stack trace untuk debugging yang lebih detail
            System.err.println("❌ OVERLAY CREATION STACK TRACE:");
            for (StackTraceElement element : e.getStackTrace()) {
                System.err.println("  " + element.toString());
            }
        }
    }
'@

# Replace the method in the file
$beforeMethod = $lines[0..($startLine-1)]
$afterMethod = $lines[($endLine+1)..($lines.Length-1)]

$newContent = $beforeMethod + $newMethod + $afterMethod

# Write back to file with proper encoding
$newContent | Out-File -FilePath $filePath -Encoding UTF8

Write-Host "✅ Successfully replaced showAnimationOverlay method with MP4-only implementation"
Write-Host "🎬 Removed GIF fallback code completely"
Write-Host "⚡ Improved MP4 loading consistency and error handling"
