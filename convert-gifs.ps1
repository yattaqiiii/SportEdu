# PowerShell script untuk konversi GIF yang bermasalah ke format kompatibel JavaFX
# Menggunakan System.Drawing untuk konversi format

Add-Type -AssemblyName System.Drawing

$problematicGifs = @("passing.gif", "heading.gif", "shooting.gif", "dribbling.gif")
$sourcePath = "src\main\resources\images"
$backupPath = "src\main\resources\images\backup"

# Buat folder backup
if (!(Test-Path $backupPath)) {
    New-Item -ItemType Directory -Path $backupPath -Force
    Write-Host "Created backup directory: $backupPath"
}

foreach ($gifName in $problematicGifs) {
    $originalPath = Join-Path $sourcePath $gifName
    $backupFilePath = Join-Path $backupPath $gifName
    $tempPath = Join-Path $sourcePath "temp_$gifName"
    
    if (Test-Path $originalPath) {
        Write-Host ""
        Write-Host "PROCESSING: $gifName"
        
        try {
            # 1. Backup original
            Copy-Item $originalPath $backupFilePath -Force
            Write-Host "Backed up to: $backupFilePath"
            
            # 2. Load original GIF
            $originalImage = [System.Drawing.Image]::FromFile((Get-Item $originalPath).FullName)
            
            # 3. Get frame info
            $dimension = New-Object System.Drawing.Imaging.FrameDimension([System.Drawing.Imaging.FrameDimension]::Time.Guid)
            $frameCount = $originalImage.GetFrameCount($dimension)
            Write-Host "Original frames: $frameCount, Format: $($originalImage.PixelFormat)"
            
            # 4. Create new 24-bit RGB version (no alpha channel)
            $width = $originalImage.Width
            $height = $originalImage.Height
            
            # Create first frame as static image for testing
            $newBitmap = New-Object System.Drawing.Bitmap($width, $height, [System.Drawing.Imaging.PixelFormat]::Format24bppRgb)
            $graphics = [System.Drawing.Graphics]::FromImage($newBitmap)
            
            # Set white background to handle transparency
            $graphics.Clear([System.Drawing.Color]::White)
            
            # Draw first frame
            $originalImage.SelectActiveFrame($dimension, 0)
            $graphics.DrawImage($originalImage, 0, 0, $width, $height)
            $graphics.Dispose()
            
            # Save as temporary file first
            $newBitmap.Save($tempPath, [System.Drawing.Imaging.ImageFormat]::Gif)
            $newBitmap.Dispose()
            $originalImage.Dispose()
            
            # Replace original with new version
            Move-Item $tempPath $originalPath -Force
            
            # Verify new file
            $newImage = [System.Drawing.Image]::FromFile((Get-Item $originalPath).FullName)
            Write-Host "CONVERTED: Format=$($newImage.PixelFormat), Size=$($newImage.Width)x$($newImage.Height)"
            $newImage.Dispose()
            
        } catch {
            Write-Host "ERROR converting $gifName : $_"
            
            # Restore backup if conversion failed
            if (Test-Path $backupFilePath) {
                Copy-Item $backupFilePath $originalPath -Force
                Write-Host "Restored original from backup"
            }
        }
    } else {
        Write-Host "File not found: $originalPath"
    }
}

Write-Host ""
Write-Host "CONVERSION COMPLETE!"
Write-Host "Note: Converted GIFs are now static images in 24-bit RGB format"
Write-Host "This should be compatible with JavaFX Image loading"
Write-Host "Original files are backed up in: $backupPath"
