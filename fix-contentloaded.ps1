$file = "src\main\java\com\sportedu\view\PenjelasanView.java"
$content = Get-Content $file -Encoding UTF8

# Replace all contentLoaded with videoLoaded
$content = $content -replace "contentLoaded", "videoLoaded"

# Save back to file
$content | Set-Content $file -Encoding UTF8

Write-Host "Fixed contentLoaded variable references"
