# Windows + PowerPoint only. Renders tools\_preview.pptx (or the real poster) to 02_Research_Poster\poster_preview.png
param([string]$Source = "$PSScriptRoot\_preview.pptx")
$png = Join-Path (Split-Path $PSScriptRoot -Parent) "02_Research_Poster\poster_preview.png"
$app = New-Object -ComObject PowerPoint.Application
try {
  $pres = $app.Presentations.Open((Resolve-Path $Source).Path, -1, 0, 0)
  $pres.Slides.Item(1).Export($png, 'PNG', 1500, 1900)
  $pres.Close()
  "preview written: $png"
} finally {
  # Never close a PowerPoint window the user has open: quit only if nothing else is loaded.
  if ($app.Presentations.Count -eq 0) { $app.Quit() }
}
