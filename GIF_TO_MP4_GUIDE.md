# SportEdu GIF to MP4 Conversion Guide

# ===================================

## Problem

JavaFX ImageView doesn't support animated GIFs - it only shows the first frame as a static image.
The solution is to convert GIFs to MP4 videos and use JavaFX MediaView for proper animation playback.

## Solutions (Choose One)

### Option 1: FFmpeg (Recommended - Best Quality)

1. Download FFmpeg from: https://ffmpeg.org/download.html
2. Extract to C:\ffmpeg\
3. Add C:\ffmpeg\bin to your Windows PATH
4. Run the conversion script: `convert-gifs-to-mp4.ps1`

### Option 2: Online Converters (Quick & Easy)

1. Go to https://cloudconvert.com/gif-to-mp4 or https://ezgif.com/gif-to-mp4
2. Upload each GIF file:
   - src/main/resources/images/passing.gif
   - src/main/resources/images/heading.gif
   - src/main/resources/images/shooting.gif
   - src/main/resources/images/dribbling.gif
3. Convert to MP4 format
4. Download and save to: src/main/resources/videos/
   - passing.mp4
   - heading.mp4
   - shooting.mp4
   - dribbling.mp4

### Option 3: VLC Media Player (Free Software)

1. Install VLC Media Player
2. Open VLC -> Media -> Convert/Save
3. Add your GIF file
4. Choose MP4 format
5. Save to videos folder

### Option 4: HandBrake (Advanced)

1. Download HandBrake (free video converter)
2. Import GIF files
3. Export as MP4
4. Save to videos folder

## File Structure After Conversion

```
SportEdu/
  src/
    main/
      resources/
        images/
          passing.gif (original - keep as backup)
          heading.gif (original - keep as backup)
          shooting.gif (original - keep as backup)
          dribbling.gif (original - keep as backup)
        videos/ (NEW FOLDER)
          passing.mp4 (converted video)
          heading.mp4 (converted video)
          shooting.mp4 (converted video)
          dribbling.mp4 (converted video)
```

## Java Code Changes (Already Done)

- Updated PenjelasanView.java to use MediaView instead of ImageView
- MediaView properly plays MP4 videos with animation
- Automatic fallback to GIF (static) if MP4 not found
- Proper cleanup of MediaPlayer resources

## Benefits of MP4 over GIF

- ✅ Actual animation playback in JavaFX
- ✅ Better compression (smaller file sizes)
- ✅ Better quality
- ✅ Proper loop control
- ✅ No format compatibility issues

## Current Status

- Java code: ✅ Ready for MP4 videos
- Video directory: ✅ Created
- Placeholder files: ✅ Created
- Real MP4 conversion: ❌ Pending (choose option above)

Run the application and test with any option above!
