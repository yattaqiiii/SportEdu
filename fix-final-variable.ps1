# PowerShell script to fix the final variable issue in animationPlayer
$filePath = "src\main\java\com\sportedu\view\PenjelasanView.java"
$content = Get-Content $filePath -Raw -Encoding UTF8

# Fix the error listener to use playerRef instead of animationPlayer
$content = $content -replace "animationPlayer\.getError\(\)", "playerRef.getError()"

# Write back to file
$content | Out-File -FilePath $filePath -Encoding UTF8 -NoNewline

Write-Host "✅ Fixed final variable issue in PenjelasanView.java"
