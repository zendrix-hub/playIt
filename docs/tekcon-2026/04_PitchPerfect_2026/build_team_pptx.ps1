# Adapts and adopts the official PitchPerfect Team Template into PlayIT_Team.pptx
# preserving the PitchPerfect Finals + Wildcat Innovation Labs layout while applying PlayIT's visual theme.
$ErrorActionPreference = 'Stop'

$Root = $PSScriptRoot
$A = Join-Path $Root 'assets'
$Tmpl = 'C:\Users\Zendrix\.gemini\antigravity\brain\32fa1581-0da3-46db-afd9-328aad4e150e\scratch\Team_Template.pptx'
$Work = 'C:\Users\Zendrix\.gemini\antigravity\brain\32fa1581-0da3-46db-afd9-328aad4e150e\scratch\Team_Template_Work.pptx'
$OutPptx = Join-Path $Root 'PlayIT_Team.pptx'
$OutPdf  = Join-Path $Root 'PlayIT_Team.pdf'
$OutPng  = Join-Path $Root 'previews_10slide\team_slide.png'
Copy-Item $Tmpl $Work -Force

$K = @{
  Brown = '4A2E18'; Ink = '3B2416'; Muted = '6E5643'; White = 'FFFFFF'; Cream = 'FFF2DC'
  Teal = '0B7F7A'; TealLip = '075C58'; Gold = 'FFC53D'; GoldLip = 'DFA019'
  Blue = '3BA7D9'; BlueLip = '227AA3'; Amber = 'F7922F'; AmberLip = 'C86A12'
  Green = '2E9A3E'; GreenLip = '1F6E2B'; Coral = 'F2545B'; CoralLip = 'C2383E'
  Shell = 'FFFDF8'; ShellLip = 'EADCC4'; Sky = 'E1F3FA'
}
$LX = 'Lexend'; $LXS = 'Lexend SemiBold'; $LXE = 'Lexend ExtraBold'

function C([string]$h) { $h = $h.TrimStart('#'); [int]("0x" + $h.Substring(0, 2)) + 256 * [int]("0x" + $h.Substring(2, 2)) + 65536 * [int]("0x" + $h.Substring(4, 2)) }
function P([string]$t, [string]$f = $LX, [double]$s = 11, [string]$c = $K.Ink, [switch]$b, [double]$sa = 0) {
  @{ t = $t; f = $f; s = $s; c = $c; b = [bool]$b; sa = $sa }
}
function Set-Text($shp, $paras, [int]$align = 2, [int]$anchor = 3) {
  $tr = $shp.TextFrame2.TextRange
  $tr.Text = ($paras | ForEach-Object { $_.t }) -join "`r"
  for ($k = 0; $k -lt $paras.Count; $k++) {
    $p = $paras[$k]; $r = $tr.Paragraphs($k + 1, 1)
    $r.Font.Name = $p.f; $r.Font.Size = $p.s
    $r.Font.Bold = $(if ($p.b -and $p.f -notlike '*ExtraBold*' -and $p.f -notlike '*SemiBold*') { -1 } else { 0 })
    $r.Font.Fill.ForeColor.RGB = (C $p.c)
    $r.ParagraphFormat.Alignment = $align; $r.ParagraphFormat.SpaceBefore = 0; $r.ParagraphFormat.SpaceAfter = $p.sa
  }
  $shp.TextFrame2.VerticalAnchor = $anchor
}
function Box($s, [string]$name, [double]$x, [double]$y, [double]$w, [double]$h, [string]$face, [string]$lip = '',
             [double]$lw = 2.5, [double]$r = 0.14, [double]$lipOff = 5, [string]$line = $K.Brown, [double]$m = 6) {
  $b = $s.Shapes.AddShape(5, $x, $y, $w, $h)
  if ($name) { $b.Name = $name }
  $b.Fill.Solid(); $b.Fill.ForeColor.RGB = (C $face)
  if ($lw -gt 0) { $b.Line.Visible = -1; $b.Line.ForeColor.RGB = (C $line); $b.Line.Weight = $lw } else { $b.Line.Visible = 0 }
  $b.Adjustments.Item(1) = $r
  if ($lip) { $sh = $b.Shadow; $sh.Visible = -1; $sh.OffsetX = 0; $sh.OffsetY = $lipOff; $sh.Blur = 0; $sh.Transparency = 0; $sh.Size = 100; $sh.ForeColor.RGB = (C $lip) }
  else { $b.Shadow.Visible = 0 }
  $tf = $b.TextFrame2; $tf.WordWrap = -1; $tf.AutoSize = 0
  $tf.MarginLeft = $m; $tf.MarginRight = $m; $tf.MarginTop = 4; $tf.MarginBottom = 4; $tf.VerticalAnchor = 3
  return $b
}
function Pic($s, [string]$name, [string]$file, [double]$x, [double]$y, [double]$w, [double]$h) {
  $p = $s.Shapes.AddPicture((Join-Path $A $file), 0, -1, $x, $y, $w, $h)
  if ($name) { $p.Name = $name }
  return $p
}

$running = @(Get-Process POWERPNT -ErrorAction SilentlyContinue).Count -gt 0
$app = New-Object -ComObject PowerPoint.Application
$pres = $app.Presentations.Open($Work, 0, 0, 0)
try {
  $s = $pres.Slides.Item(1)

  # Keep Google Shape;98;p13 (Team Name in maroon banner), delete placeholder pics & textboxes (86..97)
  $toDelete = @()
  foreach ($shp in $s.Shapes) {
    if ($shp.Name -eq 'Google Shape;98;p13') {
      $shp.Left = 222; $shp.Width = 665; $shp.Top = 141; $shp.Height = 72
      Set-Text $shp @(P 'PlayIT · Group 56' $LXE 44 $K.White) 2 3
    } elseif ($shp.Name -match '^Google Shape;(8[6-9]|9[0-7]);') {
      $toDelete += $shp
    }
  }
  foreach ($d in $toDelete) { $d.Delete() }

  # Top-right PlayIT Brand Lockup Pill aligned cleanly to right edge x=887 (667 + 220 = 887)
  [void](Box $s 'PlayIT_Brand_Pill' 667 30 220 72 $K.Shell $K.GoldLip 2.5 0.18 5 $K.Brown 6)
  [void](Pic $s $null 'product_logo_v4.png' 679 42 48 48)
  [void](Pic $s $null 'wordmark_v4_teal.png' 733 40 136 50.4)

  # 5 Symmetric Tactile PlayIT Member Cards aligned to maroon banner grid (x = 222 .. 887, w = 665)
  $members = @(
    @{ name = 'Zendrix B. Riva';       sub = 'BSIT-4 · Team Lead'; lip = $K.TealLip;  badge = $K.Gold;  photo = 'team_member_1.png' },
    @{ name = 'Erik Samuel L. Bien';   sub = 'BSIT-4 · Member';    lip = $K.BlueLip;  badge = $K.Blue;  photo = 'team_member_2.png' },
    @{ name = 'Ashlee Skye A. Durano'; sub = 'BSIT-4 · Member';    lip = $K.AmberLip; badge = $K.Amber; photo = 'team_member_3.png' },
    @{ name = 'Kaysean Miel';          sub = 'BSIT-4 · Member';    lip = $K.GreenLip; badge = $K.Green; photo = 'team_member_4.png' },
    @{ name = 'John James Palis';      sub = 'BSIT-4 · Member';    lip = $K.CoralLip; badge = $K.Coral; photo = 'team_member_5.png' }
  )
  for ($i = 0; $i -lt 5; $i++) {
    $m = $members[$i]
    $x = 222 + 136 * $i   # 5*121 + 4*15 = 665 -> 222..887 exact!
    [void](Box $s ("Member_Card_" + ($i+1)) $x 232 121 180 $K.Shell $m.lip 2.5 0.14 6 $K.Brown 4)
    [void](Box $s $null ($x + 8) 240 105 105 $m.badge '' 1.5 0.12 0 $K.Brown 0)
    [void](Box $s $null ($x + 12) 244 97 97 $K.White '' 0 0.08 0 $K.Brown 0)
    [void](Pic $s ("Member_Photo_" + ($i+1)) $m.photo ($x + 14) 246 93 93)
    $lbl = Box $s ("Member_Label_" + ($i+1)) ($x + 4) 348 113 58 $K.Shell '' 0 0 0 $K.Brown 4
    Set-Text $lbl @(
      (P $m.name $LXE 10 $K.Ink -sa 2),
      (P $m.sub $(if ($i -eq 0) { $LXE } else { $LXS }) 9.5 $(if ($i -eq 0) { $K.Teal } else { $K.Muted }))
    ) 2 3
  }

  # Footer strip safely inside lower maroon band (y = 442 .. 522, x = 222 .. 887)
  [void](Box $s 'Team_Footer_Card' 222 442 665 80 $K.Shell $K.GoldLip 2.5 0.14 5 $K.Brown 12)
  [void](Pic $s 'Team_2D_Group' 'team_2d_clear.png' 236 446 112 72.9)
  $ft = $s.Shapes.AddTextbox(1, 362, 446, 514, 72)
  $ft.TextFrame2.WordWrap = -1; $ft.TextFrame2.AutoSize = 0
  $ft.TextFrame2.MarginLeft = 0; $ft.TextFrame2.MarginRight = 0; $ft.TextFrame2.MarginTop = 0; $ft.TextFrame2.MarginBottom = 0
  Set-Text $ft @(
    (P 'PlayIT — Offline-First Gamified Phonics Companion (DepEd Marungko)' $LXE 12.5 $K.Teal -sa 3),
    (P 'Mentor / Coach: Mr. Joemarie C. Amparo · College of Computer Studies, CIT-University' $LXS 10.5 $K.Ink -sa 2),
    (P 'IT411 Group 56 · Team Leader Contact: zendrix.riva@cit.edu · 0909 188 9602' $LX 10 $K.Muted)
  ) 1 3
  # Re-pin box bounds after Set-Text (PowerPoint textbox AutoSize expands upward otherwise)
  $ft.Left = 362; $ft.Top = 446; $ft.Width = 514; $ft.Height = 72

  $s.Export($OutPng, 'PNG', 1920, 1080)
  $pres.SaveAs($OutPptx, 24, -1)
  $pres.SaveAs($OutPdf, 32)
  "saved: $OutPptx"; "saved: $OutPng"
} finally {
  $pres.Close()
  if ($app.Presentations.Count -eq 0 -and -not $running) { $app.Quit() }
  [System.Runtime.InteropServices.Marshal]::ReleaseComObject($app) | Out-Null
}
