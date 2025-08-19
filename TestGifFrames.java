import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;

/**
 * Test program untuk mengecek bagaimana JavaFX menangani GIF frames
 * dengan berbagai method loading
 */
public class TestGifFrames extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            VBox root = new VBox(10);
            root.setAlignment(Pos.CENTER);
            root.setPadding(new Insets(20));

            Label title = new Label("GIF Frame Test - JavaFX Loading Methods");
            title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            // Test 1: Simple URL loading (seperti AnimasiView)
            System.out.println("=== TEST 1: Simple URL Loading ===");
            ImageView gifView1 = new ImageView();
            try {
                var gifUrl = getClass().getResource("/images/dribbling.gif");
                if (gifUrl != null) {
                    System.out.println("URL found: " + gifUrl.toExternalForm());
                    Image gifImage1 = new Image(gifUrl.toExternalForm());
                    System.out.println("Image loaded - Error: " + gifImage1.isError());
                    System.out.println("Image width: " + gifImage1.getWidth());
                    System.out.println("Image height: " + gifImage1.getHeight());
                    
                    gifView1.setImage(gifImage1);
                    gifView1.setFitWidth(200);
                    gifView1.setFitHeight(150);
                    gifView1.setPreserveRatio(true);
                } else {
                    System.out.println("URL not found!");
                }
            } catch (Exception e) {
                System.out.println("URL method failed: " + e.getMessage());
            }

            // Test 2: InputStream loading (seperti yang kita coba)
            System.out.println("\n=== TEST 2: InputStream Loading ===");
            ImageView gifView2 = new ImageView();
            try {
                var gifStream = getClass().getResourceAsStream("/images/dribbling.gif");
                if (gifStream != null) {
                    System.out.println("InputStream obtained");
                    Image gifImage2 = new Image(gifStream);
                    System.out.println("Image loaded - Error: " + gifImage2.isError());
                    System.out.println("Image width: " + gifImage2.getWidth());
                    System.out.println("Image height: " + gifImage2.getHeight());
                    
                    gifView2.setImage(gifImage2);
                    gifView2.setFitWidth(200);
                    gifView2.setFitHeight(150);
                    gifView2.setPreserveRatio(true);
                    gifStream.close();
                } else {
                    System.out.println("InputStream not found!");
                }
            } catch (Exception e) {
                System.out.println("InputStream method failed: " + e.getMessage());
            }

            // Test 3: Dengan background loading
            System.out.println("\n=== TEST 3: Background Loading ===");
            ImageView gifView3 = new ImageView();
            try {
                var gifUrl = getClass().getResource("/images/dribbling.gif");
                if (gifUrl != null) {
                    System.out.println("Background loading URL: " + gifUrl.toExternalForm());
                    Image gifImage3 = new Image(gifUrl.toExternalForm(), true); // background loading = true
                    System.out.println("Image created with background loading");
                    
                    // Set listener untuk progress
                    gifImage3.progressProperty().addListener((obs, oldVal, newVal) -> {
                        System.out.println("Loading progress: " + (newVal.doubleValue() * 100) + "%");
                    });
                    
                    gifView3.setImage(gifImage3);
                    gifView3.setFitWidth(200);
                    gifView3.setFitHeight(150);
                    gifView3.setPreserveRatio(true);
                } else {
                    System.out.println("URL not found for background loading!");
                }
            } catch (Exception e) {
                System.out.println("Background loading failed: " + e.getMessage());
            }

            Label label1 = new Label("Method 1: URL toExternalForm()");
            Label label2 = new Label("Method 2: InputStream");
            Label label3 = new Label("Method 3: Background Loading");

            root.getChildren().addAll(title, 
                                     label1, gifView1,
                                     label2, gifView2,
                                     label3, gifView3);

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("GIF Frame Test");
            primaryStage.setScene(scene);
            primaryStage.show();

            // Test frames after loading
            javafx.application.Platform.runLater(() -> {
                try {
                    Thread.sleep(2000); // Wait for loading
                    System.out.println("\n=== AFTER 2 SECONDS ===");
                    
                    if (gifView1.getImage() != null) {
                        System.out.println("Method 1 final - Error: " + gifView1.getImage().isError());
                    }
                    if (gifView2.getImage() != null) {
                        System.out.println("Method 2 final - Error: " + gifView2.getImage().isError());
                    }
                    if (gifView3.getImage() != null) {
                        System.out.println("Method 3 final - Error: " + gifView3.getImage().isError());
                        System.out.println("Method 3 progress: " + gifView3.getImage().getProgress());
                    }
                } catch (Exception e) {
                    System.out.println("Post-loading check failed: " + e.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
