# PowerShell script to convert GIFs to MP4 videos using FFmpeg
# This will preserve the animation and make them playable in JavaFX MediaView

Write-Host "GIF to MP4 Conversion Script for SportEdu" -ForegroundColor Green
Write-Host "=====================================" -ForegroundColor Green

# Check if FFmpeg is available
$ffmpegPath = $null
$ffmpegLocations = @(
    "ffmpeg",
    "C:\ffmpeg\bin\ffmpeg.exe",
    "C:\Program Files\ffmpeg\bin\ffmpeg.exe",
    "$env:USERPROFILE\Downloads\ffmpeg\bin\ffmpeg.exe"
)

foreach ($location in $ffmpegLocations) {
    try {
        $result = & $location -version 2>$null
        if ($LASTEXITCODE -eq 0) {
            $ffmpegPath = $location
            Write-Host "Found FFmpeg at: $ffmpegPath" -ForegroundColor Green
            break
        }
    } catch {
        # Continue checking other locations
    }
}

if (-not $ffmpegPath) {
    Write-Host ""
    Write-Host "ERROR: FFmpeg not found!" -ForegroundColor Red
    Write-Host "Please download FFmpeg from: https://ffmpeg.org/download.html" -ForegroundColor Yellow
    Write-Host "Extract it and either:" -ForegroundColor Yellow
    Write-Host "1. Add FFmpeg to your PATH, or" -ForegroundColor Yellow
    Write-Host "2. Place ffmpeg.exe in C:\ffmpeg\bin\" -ForegroundColor Yellow
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit 1
}

# Define paths
$imagesPath = "src\main\resources\images"
$videosPath = "src\main\resources\videos"

# Create videos directory if it doesn't exist
if (-not (Test-Path $videosPath)) {
    New-Item -ItemType Directory -Path $videosPath -Force
    Write-Host "Created videos directory: $videosPath" -ForegroundColor Green
}

# Create backup directory for original GIFs
$backupPath = "$imagesPath\gif_backup"
if (-not (Test-Path $backupPath)) {
    New-Item -ItemType Directory -Path $backupPath -Force
    Write-Host "Created backup directory: $backupPath" -ForegroundColor Green
}

# List of GIFs to convert
$gifFiles = @("passing.gif", "heading.gif", "shooting.gif", "dribbling.gif")

Write-Host ""
Write-Host "Converting GIFs to MP4 videos..." -ForegroundColor Cyan
Write-Host ""

foreach ($gifFile in $gifFiles) {
    $gifPath = Join-Path $imagesPath $gifFile
    $mp4File = $gifFile.Replace(".gif", ".mp4")
    $mp4Path = Join-Path $videosPath $mp4File
    $backupGifPath = Join-Path $backupPath $gifFile
    
    if (Test-Path $gifPath) {
        Write-Host "PROCESSING: $gifFile" -ForegroundColor Yellow
        
        # Backup original GIF
        Copy-Item $gifPath $backupGifPath -Force
        Write-Host "  Backed up to: $backupGifPath" -ForegroundColor Gray
        
        # Convert GIF to MP4 with optimized settings for JavaFX
        $ffmpegArgs = @(
            "-i", $gifPath,
            "-movflags", "faststart",        # Optimize for streaming
            "-pix_fmt", "yuv420p",           # Ensure compatibility
            "-vf", "scale=trunc(iw/2)*2:trunc(ih/2)*2",  # Ensure even dimensions
            "-r", "30",                      # Set frame rate to 30fps
            "-crf", "23",                    # Good quality
            "-preset", "medium",             # Balance speed vs compression
            "-y",                            # Overwrite output file
            $mp4Path
        )
        
        try {
            & $ffmpegPath @ffmpegArgs 2>$null
            
            if ($LASTEXITCODE -eq 0 -and (Test-Path $mp4Path)) {
                $mp4Size = (Get-Item $mp4Path).Length
                Write-Host "  SUCCESS: Created $mp4File ($mp4Size bytes)" -ForegroundColor Green
            } else {
                Write-Host "  ERROR: Failed to convert $gifFile" -ForegroundColor Red
            }
        } catch {
            Write-Host "  ERROR: Exception converting $gifFile - $($_.Exception.Message)" -ForegroundColor Red
        }
    } else {
        Write-Host "ERROR: $gifFile not found at $gifPath" -ForegroundColor Red
    }
    
    Write-Host ""
}

Write-Host "CONVERSION COMPLETE!" -ForegroundColor Green
Write-Host ""
Write-Host "Next steps:" -ForegroundColor Cyan
Write-Host "1. The Java code needs to be updated to use MediaView instead of ImageView" -ForegroundColor White
Write-Host "2. Video files are in: $videosPath" -ForegroundColor White
Write-Host "3. Original GIFs are backed up in: $backupPath" -ForegroundColor White
Write-Host ""
Write-Host "Note: JavaFX MediaView properly supports video animation playback" -ForegroundColor Yellow
