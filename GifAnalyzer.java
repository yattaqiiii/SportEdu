import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.util.Iterator;

/**
 * Tool untuk menganalisis dan mengkonversi GIF yang bermasalah dengan JavaFX
 */
public class GifAnalyzer {
    
    public static void main(String[] args) {
        String[] problemGifs = {
            "dribbling.gif", "heading.gif", "shooting.gif", 
            "passing.gif", "smash.gif", "netting.gif", 
            "footwork.gif", "servis.gif"
        };
        
        String basePath = "src/main/resources/images/";
        
        for (String gifName : problemGifs) {
            analyzeGif(basePath + gifName, gifName);
        }
    }
    
    public static void analyzeGif(String filePath, String fileName) {
        try {
            File gifFile = new File(filePath);
            if (!gifFile.exists()) {
                System.out.println("❌ File not found: " + filePath);
                return;
            }
            
            System.out.println("\n=== ANALYZING " + fileName + " ===");
            System.out.println("File size: " + gifFile.length() + " bytes");
            
            // Use ImageIO to read GIF details
            ImageInputStream iis = ImageIO.createImageInputStream(gifFile);
            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
            
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                reader.setInput(iis);
                
                int numImages = reader.getNumImages(true);
                System.out.println("Number of frames: " + numImages);
                
                if (numImages > 0) {
                    BufferedImage firstFrame = reader.read(0);
                    System.out.println("Dimensions: " + firstFrame.getWidth() + "x" + firstFrame.getHeight());
                    System.out.println("Color model: " + firstFrame.getColorModel());
                    System.out.println("Image type: " + firstFrame.getType());
                    
                    // Check for transparency
                    boolean hasAlpha = firstFrame.getColorModel().hasAlpha();
                    System.out.println("Has transparency: " + hasAlpha);
                    
                    // Analyze all frames
                    System.out.println("Frame analysis:");
                    for (int i = 0; i < Math.min(numImages, 5); i++) { // Check first 5 frames
                        try {
                            BufferedImage frame = reader.read(i);
                            System.out.println("  Frame " + i + ": " + frame.getWidth() + "x" + frame.getHeight() + 
                                             " Type: " + frame.getType());
                        } catch (Exception frameEx) {
                            System.out.println("  Frame " + i + ": ERROR - " + frameEx.getMessage());
                        }
                    }
                    
                    // Check if this GIF might be problematic for JavaFX
                    if (hasAlpha) {
                        System.out.println("⚠️  WARNING: GIF has transparency - might cause issues in JavaFX");
                    }
                    if (numImages > 50) {
                        System.out.println("⚠️  WARNING: Too many frames (" + numImages + ") - might cause performance issues");
                    }
                    if (firstFrame.getType() == BufferedImage.TYPE_CUSTOM) {
                        System.out.println("⚠️  WARNING: Custom color model - might not be compatible with JavaFX");
                    }
                    
                } else {
                    System.out.println("❌ No frames found in GIF");
                }
                
                reader.dispose();
            } else {
                System.out.println("❌ No image readers found for GIF");
            }
            
            iis.close();
            
        } catch (Exception e) {
            System.out.println("❌ Error analyzing " + fileName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
