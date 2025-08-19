package com.sportedu.util;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.util.HashMap;
import java.util.Map;
import java.lang.ref.WeakReference;
import java.util.Iterator;

/**
 * Utility class untuk optimasi penggunaan memory/RAM
 * Menggunakan cache dengan WeakReference dan lazy loading
 */
public class MemoryOptimizer {

    // Cache untuk gambar dengan WeakReference untuk auto garbage collection
    private static final Map<String, WeakReference<Image>> imageCache = new HashMap<>();

    // Cache untuk media dengan WeakReference
    private static final Map<String, WeakReference<Media>> mediaCache = new HashMap<>();

    // Ukuran maksimal cache
    private static final int MAX_CACHE_SIZE = 20;

    /**
     * Load image dengan cache dan optimasi memory
     */
    public static Image loadOptimizedImage(String path, double requestedWidth, double requestedHeight) {
        try {
            // Buat key unik berdasarkan path dan ukuran
            String cacheKey = path + "_" + (int)requestedWidth + "x" + (int)requestedHeight;

            // Cek cache terlebih dahulu
            WeakReference<Image> cachedImageRef = imageCache.get(cacheKey);
            if (cachedImageRef != null) {
                Image cachedImage = cachedImageRef.get();
                if (cachedImage != null && !cachedImage.isError()) {
                    return cachedImage;
                }
            }

            // Clean up cache jika terlalu besar
            cleanUpImageCache();

            // Load image dengan ukuran yang dioptimasi
            Image image = new Image(
                MemoryOptimizer.class.getResourceAsStream(path),
                requestedWidth,
                requestedHeight,
                true,  // preserveRatio
                true   // smooth (tapi akan menggunakan lebih banyak memory)
            );

            // Simpan ke cache dengan WeakReference
            imageCache.put(cacheKey, new WeakReference<>(image));

            return image;

        } catch (Exception e) {
            System.err.println("Error loading optimized image: " + path + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * Load image sederhana tanpa resize
     */
    public static Image loadOptimizedImage(String path) {
        try {
            // Cek cache terlebih dahulu
            WeakReference<Image> cachedImageRef = imageCache.get(path);
            if (cachedImageRef != null) {
                Image cachedImage = cachedImageRef.get();
                if (cachedImage != null && !cachedImage.isError()) {
                    return cachedImage;
                }
            }

            // Clean up cache jika terlalu besar
            cleanUpImageCache();

            // Load image dengan loading background untuk hemat memory
            Image image = new Image(
                    MemoryOptimizer.class.getResourceAsStream(path)
            );

            // Simpan ke cache dengan WeakReference
            imageCache.put(path, new WeakReference<>(image));

            return image;

        } catch (Exception e) {
            System.err.println("Error loading optimized image: " + path + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * Load media dengan cache dan optimasi
     */
    public static Media loadOptimizedMedia(String path) {
        try {
            // Cek cache terlebih dahulu
            WeakReference<Media> cachedMediaRef = mediaCache.get(path);
            if (cachedMediaRef != null) {
                Media cachedMedia = cachedMediaRef.get();
                if (cachedMedia != null) {
                    return cachedMedia;
                }
            }

            // Clean up cache jika terlalu besar
            cleanUpMediaCache();

            // Load media
            var mediaUrl = MemoryOptimizer.class.getResource(path);
            if (mediaUrl != null) {
                Media media = new Media(mediaUrl.toExternalForm());

                // Simpan ke cache dengan WeakReference
                mediaCache.put(path, new WeakReference<>(media));

                return media;
            }

            return null;

        } catch (Exception e) {
            System.err.println("Error loading optimized media: " + path + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * Load GIF animasi tanpa optimasi memory untuk mempertahankan animasi
     * BYPASS semua caching dan optimasi untuk GIF animasi
     */
    public static Image loadAnimatedGif(String path) {
        try {
            System.out.println("🎬 MEMORY OPTIMIZER: Loading animated GIF without optimization - " + path);

            // METODE 1: Coba load via URL terlebih dahulu
            var gifURL = MemoryOptimizer.class.getResource(path);
            if (gifURL != null) {
                try {
                    // Load dengan setting optimal untuk GIF animasi
                    Image gifImage = new Image(gifURL.toExternalForm(),
                                             0, 0,           // requestedWidth, requestedHeight (0 = original size)
                                             true,           // preserveRatio
                                             false,          // smooth = false untuk animasi crisp
                                             false);         // backgroundLoading = false untuk load lengkap

                    // Validasi yang lebih longgar - GIF dengan minor error tetap diterima
                    if (gifImage != null) {
                        // Jika ada error minor, tetap coba gunakan GIF
                        if (!gifImage.isError()) {
                            System.out.println("✅ MEMORY OPTIMIZER: GIF loaded successfully via URL");
                            return gifImage;
                        } else {
                            // GIF memiliki error tapi mungkin masih bisa digunakan
                            System.out.println("⚠️ GIF has minor errors but attempting to use - " + path);
                            return gifImage; // Return despite error
                        }
                    }
                } catch (Exception urlEx) {
                    System.out.println("🔄 URL method failed, trying stream method - " + path);
                }
            }

            // METODE 2: Fallback ke InputStream jika URL gagal
            var gifStream = MemoryOptimizer.class.getResourceAsStream(path);
            if (gifStream != null) {
                try {
                    // Load dengan InputStream - biasanya lebih stabil untuk GIF bermasalah
                    Image gifImage = new Image(gifStream);

                    // Validasi yang sangat longgar untuk InputStream
                    if (gifImage != null) {
                        if (!gifImage.isError()) {
                            System.out.println("✅ MEMORY OPTIMIZER: GIF loaded successfully via InputStream");
                            return gifImage;
                        } else {
                            // Return GIF meskipun ada error - biarkan JavaFX handle
                            System.out.println("🎬 GIF loaded with minor errors - continuing anyway");
                            return gifImage;
                        }
                    }
                } catch (Exception streamEx) {
                    System.out.println("🔄 InputStream method failed - " + path);
                } finally {
                    try {
                        gifStream.close();
                    } catch (Exception e) {
                        // Ignore close exception
                    }
                }
            }

            System.out.println("❌ MEMORY OPTIMIZER: Could not load GIF - " + path);
            return null;

        } catch (Exception e) {
            System.out.println("❌ MEMORY OPTIMIZER: Exception loading GIF " + path + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Validate if the loaded GIF image is valid and has proper dimensions
     */
    private static boolean validateGifImage(Image image, String path) {
        if (image == null) {
            System.err.println("🔍 GIF VALIDATION: Image is null - " + path);
            return false;
        }

        if (image.isError()) {
            System.err.println("🔍 GIF VALIDATION: Image has error - " + path +
                             (image.getException() != null ? ": " + image.getException().getMessage() : ""));
            return false;
        }

        // PERBAIKAN: Jangan terlalu ketat dengan validasi dimensi untuk GIF animasi
        // GIF animasi mungkin perlu waktu untuk load dimensinya
        double width = image.getWidth();
        double height = image.getHeight();

        System.out.println("🔍 GIF VALIDATION: Current dimensions " + width + "x" + height + " - " + path);

        // Jika dimensi masih 0, tunggu sebentar untuk background loading
        if (width == 0.0 && height == 0.0) {
            System.out.println("🔍 GIF VALIDATION: Dimensions not ready yet, allowing GIF to load - " + path);
            return true; // BIARKAN GIF DIMUAT meskipun dimensi belum siap
        }

        // Hanya tolak jika benar-benar ada masalah dengan dimensi
        if (width < 0 || height < 0) {
            System.err.println("🔍 GIF VALIDATION: Negative dimensions " + width + "x" + height + " - " + path);
            return false;
        }

        System.out.println("🔍 GIF VALIDATION: Valid GIF " + width + "x" + height + " - " + path);
        return true;
    }

    /**
     * Cleanup cache gambar yang sudah tidak terpakai
     */
    private static void cleanUpImageCache() {
        if (imageCache.size() > MAX_CACHE_SIZE) {
            // Hapus entry yang sudah null (garbage collected)
            Iterator<Map.Entry<String, WeakReference<Image>>> iterator = imageCache.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, WeakReference<Image>> entry = iterator.next();
                if (entry.getValue().get() == null) {
                    iterator.remove();
                }
            }

            // Jika masih terlalu besar, hapus sebagian
            if (imageCache.size() > MAX_CACHE_SIZE) {
                Iterator<String> keyIterator = imageCache.keySet().iterator();
                int toRemove = imageCache.size() - MAX_CACHE_SIZE + 5; // Hapus lebih banyak untuk buffer
                while (keyIterator.hasNext() && toRemove > 0) {
                    keyIterator.next();
                    keyIterator.remove();
                    toRemove--;
                }
            }
        }
    }

    /**
     * Cleanup cache media yang sudah tidak terpakai
     */
    private static void cleanUpMediaCache() {
        if (mediaCache.size() > MAX_CACHE_SIZE) {
            // Hapus entry yang sudah null (garbage collected)
            Iterator<Map.Entry<String, WeakReference<Media>>> iterator = mediaCache.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, WeakReference<Media>> entry = iterator.next();
                if (entry.getValue().get() == null) {
                    iterator.remove();
                }
            }
        }
    }

    /**
     * Clear semua cache untuk hemat memory
     */
    public static void clearAllCaches() {
        imageCache.clear();
        mediaCache.clear();
        System.gc(); // Suggest garbage collection
    }

    /**
     * Dispose MediaPlayer dengan aman
     */
    public static void disposeMediaPlayer(MediaPlayer mediaPlayer) {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
                mediaPlayer.dispose();
            } catch (Exception e) {
                System.err.println("Error disposing MediaPlayer: " + e.getMessage());
            }
        }
    }

    /**
     * Get status cache untuk debugging
     */
    public static void printCacheStatus() {
        System.out.println("=== Memory Cache Status ===");
        System.out.println("Image cache size: " + imageCache.size());
        System.out.println("Media cache size: " + mediaCache.size());

        // Count active references
        int activeImages = 0;
        for (WeakReference<Image> ref : imageCache.values()) {
            if (ref.get() != null) activeImages++;
        }

        int activeMedia = 0;
        for (WeakReference<Media> ref : mediaCache.values()) {
            if (ref.get() != null) activeMedia++;
        }

        System.out.println("Active image references: " + activeImages);
        System.out.println("Active media references: " + activeMedia);
        System.out.println("========================");
    }
}
