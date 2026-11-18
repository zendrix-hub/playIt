# Builds the 18-slide PlayIT PitchPerfect 2026 deck with PowerPoint Desktop (COM), following
# docs/tekcon-2026/HANDOFF_PITCHPERFECT.md. Every recurring element has a "!!" name so PowerPoint's
# Morph transition carries it from slide to slide.
#
#   powershell -ExecutionPolicy Bypass -File build_pitchperfect_com.ps1            # deck + PNGs + PDF
#   powershell -ExecutionPolicy Bypass -File build_pitchperfect_com.ps1 -Video     # also a Morph check video
#
# Needs: PowerPoint 2019 / Microsoft 365 (Morph), and the fonts Sniglet and Lexend (with Lexend SemiBold
# and Lexend ExtraBold) installed for the current user (they are in app/src/main/res/font/).
# It never closes a PowerPoint window someone already has open.
param([switch]$Video)
$ErrorActionPreference = 'Stop'

$Root = $PSScriptRoot
$A = Join-Path $Root 'assets'
$Prev = Join-Path $Root 'previews'
$Pptx = Join-Path $Root 'G56_PlayIT_PitchPerfect_2026_Deck.pptx'
$Pdf = Join-Path $Root 'G56_PlayIT_PitchPerfect_2026_Deck.pdf'
New-Item -ItemType Directory -Force $Prev | Out-Null

# ---------- palette (app theme: presentation/theme/Color.kt) ----------
$K = @{
  Brown = '4A2E18'; Ink = '3B2416'; Muted = '6E5643'; White = 'FFFFFF'; Cream = 'FFF2DC'
  Teal = '0B7F7A'; TealLip = '075C58'; Gold = 'FFC53D'; GoldLip = 'DFA019'
  Blue = '3BA7D9'; BlueLip = '227AA3'; Amber = 'F7922F'; AmberLip = 'C86A12'
  Green = '2E9A3E'; GreenLip = '1F6E2B'; Coral = 'F2545B'; CoralLip = 'C2383E'
  Shell = 'FFFDF8'; ShellLip = 'EADCC4'; Sky = 'E1F3FA'; Mid = 'F3F5EC'; Sand = 'FFF2DC'
  Dusk = '0E1013'; Bezel = '4A2E18'; BezelLip = '2B1A0E'; Rule = 'EADCC4'
}
$SN = 'Lexend ExtraBold'; $LX = 'Lexend'; $LXS = 'Lexend SemiBold'; $LXE = 'Lexend ExtraBold'; $SYM = 'Segoe UI Symbol'
$TILES = @(
  @{ n = '!!Tile_Blue'; f = $K.Blue; l = $K.BlueLip }, @{ n = '!!Tile_Amber'; f = $K.Amber; l = $K.AmberLip },
  @{ n = '!!Tile_Green'; f = $K.Green; l = $K.GreenLip }, @{ n = '!!Tile_Coral'; f = $K.Coral; l = $K.CoralLip })

function C([string]$h) { $h = $h.TrimStart('#'); [int]("0x" + $h.Substring(0, 2)) + 256 * [int]("0x" + $h.Substring(2, 2)) + 65536 * [int]("0x" + $h.Substring(4, 2)) }

# A paragraph spec: text, font, size, colour, bold, italic, space after.
function P([string]$t, [string]$f = $LX, [double]$s = 12, [string]$c = $K.Ink, [switch]$b, [switch]$i, [double]$sa = 0) {
  @{ t = $t; f = $f; s = $s; c = $c; b = [bool]$b; i = [bool]$i; sa = $sa }
}

function Set-Text($shp, $paras, [int]$align = 1, [int]$anchor = 3) {
  $tr = $shp.TextFrame2.TextRange
  $tr.Text = ($paras | ForEach-Object { $_.t }) -join "`r"
  for ($k = 0; $k -lt $paras.Count; $k++) {
    $p = $paras[$k]; $r = $tr.Paragraphs($k + 1, 1)
    $r.Font.Name = $p.f; $r.Font.Size = $p.s; $r.Font.Bold = $(if ($p.b -and $p.f -notlike '*ExtraBold*' -and $p.f -notlike '*SemiBold*') { -1 } else { 0 }); $r.Font.Italic = $(if ($p.i) { -1 } else { 0 })
    $r.Font.Fill.ForeColor.RGB = (C $p.c)
    $r.ParagraphFormat.Alignment = $align; $r.ParagraphFormat.SpaceBefore = 0; $r.ParagraphFormat.SpaceAfter = $p.sa
  }
  $shp.TextFrame2.VerticalAnchor = $anchor
}

# A tactile PlayIT tile: flat face, Lily Brown outline, hard 3D bottom lip (an outer shadow with no blur).
function Box($s, [string]$name, [double]$x, [double]$y, [double]$w, [double]$h, [string]$face, [string]$lip = '',
             [double]$lw = 2.5, [double]$r = 0.16, [double]$lipOff = 6, [string]$line = $K.Brown, [double]$m = 12) {
  $b = $s.Shapes.AddShape(5, $x, $y, $w, $h)
  if ($name) { $b.Name = $name }
  $b.Fill.Solid(); $b.Fill.ForeColor.RGB = (C $face)
  if ($lw -gt 0) { $b.Line.Visible = -1; $b.Line.ForeColor.RGB = (C $line); $b.Line.Weight = $lw } else { $b.Line.Visible = 0 }
  $b.Adjustments.Item(1) = $r
  if ($lip) { $sh = $b.Shadow; $sh.Visible = -1; $sh.OffsetX = 0; $sh.OffsetY = $lipOff; $sh.Blur = 0; $sh.Transparency = 0; $sh.Size = 100; $sh.ForeColor.RGB = (C $lip) }
  else { $b.Shadow.Visible = 0 }
  $tf = $b.TextFrame2; $tf.WordWrap = -1; $tf.AutoSize = 0
  $tf.MarginLeft = $m; $tf.MarginRight = $m; $tf.MarginTop = 6; $tf.MarginBottom = 6; $tf.VerticalAnchor = 3
  return $b
}

function TB($s, [string]$name, [double]$x, [double]$y, [double]$w, [double]$h, $paras, [int]$align = 1, [int]$anchor = 1) {
  $t = $s.Shapes.AddTextbox(1, $x, $y, $w, $h)
  if ($name) { $t.Name = $name }
  $tf = $t.TextFrame2; $tf.WordWrap = -1; $tf.AutoSize = 0
  $tf.MarginLeft = 0; $tf.MarginRight = 0; $tf.MarginTop = 0; $tf.MarginBottom = 0
  Set-Text $t $paras $align $anchor
  $t.Left = $x; $t.Top = $y; $t.Width = $w; $t.Height = $h
  return $t
}

function Pic($s, [string]$name, [string]$file, [double]$x, [double]$y, [double]$w, [double]$h) {
  $p = $s.Shapes.AddPicture((Join-Path $A $file), 0, -1, $x, $y, $w, $h)
  if ($name) { $p.Name = $name }
  return $p
}

# ---------- recurring actors ----------
function Bg($s, [string]$mode) {
  $b = $s.Shapes.AddShape(1, 0, 0, 960, 540); $b.Name = '!!Bg_Canvas'; $b.Line.Visible = 0
  switch ($mode) {
    'dusk' { $b.Fill.Solid(); $b.Fill.ForeColor.RGB = (C $K.Dusk) }
    'teal' { $b.Fill.Solid(); $b.Fill.ForeColor.RGB = (C $K.Teal) }
    'cream' { $b.Fill.Solid(); $b.Fill.ForeColor.RGB = (C $K.Sand) }
    default { $b.Fill.TwoColorGradient(1, 1); $b.Fill.ForeColor.RGB = (C $K.Sky); $b.Fill.BackColor.RGB = (C $K.Sand); [void]$b.Fill.GradientStops.Insert((C $K.Mid), 0.5) }
  }
}
# Full-bleed story picture (1376 x 768 -> 967.5 x 540); parked off-stage left on the app-world slides.
function Pixar($s, [string]$file, [switch]$Parked) { [void](Pic $s '!!Bg_Pixar' $file $(if ($Parked) { -1000 } else { -3.75 }) 0 967.5 540) }
function Logo($s, [double]$x, [double]$y, [double]$w) { [void](Pic $s '!!Logo_Badge' 'product_logo_v4.png' $x $y $w $w) }
function Lily($s, [string]$pose, [double]$x, [double]$y, [double]$w, [switch]$Mirror) {
  $p = Pic $s '!!Lily_Guide' "lily_$pose.png" $x $y $w $w
  if ($Mirror) { $p.Flip(0) }
}
function Headline($s, [string]$text, [string]$color = $K.Ink, [switch]$Parked) {
  [void](TB $s '!!Headline' 74 $(if ($Parked) { -80 } else { 16 }) 640 40 @(P $text $SN 24 $color -b) 1 3)
}
function Wordmark($s, [double]$x, [double]$y, [double]$w, [double]$h, [double]$size, [string]$play = $K.Gold) {
  $t = TB $s $null $x $y $w $h @(P 'PlayIT' $SN $size $K.Teal -b) 1 3
  $tr = $t.TextFrame2.TextRange
  $tr.Characters(1, 4).Font.Fill.ForeColor.RGB = (C $play)
  $tr.Font.Line.Visible = -1; $tr.Font.Line.ForeColor.RGB = (C $K.Brown); $tr.Font.Line.Weight = 1.5
}
# The phone: a chunky brown bezel (!!Phone_Card) with the real screenshot inside (!!Phone_Screen).
function Phone($s, [double]$x, [double]$y, [double]$h, [string]$screen) {
  $sh = $h - 28; $sw = [math]::Round($sh * 720 / 1480, 1); $w = $sw + 18
  $b = Box $s '!!Phone_Card' $x $y $w $h $K.Bezel $K.BezelLip 2.5 0.12 6 $K.BezelLip
  $iw = $(if ($screen -eq 'screen_parent.png') { [math]::Round($sh * 576 / 1200, 1) } else { $sw })
  [void](Pic $s '!!Phone_Screen' $screen ($x + 9 + ($sw - $iw) / 2) ($y + 14) $iw $sh)
}
# Parks the four tiles off-stage (below the slide) so Morph slides them out and back in.
function ParkTiles($s, [double]$y = 600) { $i = 0; foreach ($t in $TILES) { [void](Box $s $t.n (120 + 190 * $i) $y 170 60 $t.f $t.l); $i++ } }
function ParkLily($s, [string]$side = 'left') { if ($side -eq 'left') { Lily $s 'thinking' -200 560 150 } else { Lily $s 'idle' 1000 300 120 } }

# ---------- slides ----------
function S01($s) {
  Bg $s 'dusk'; Pixar $s '01_Slide01_Bohol_Dusk_2D.png'; Logo $s 902 16 42; Headline $s 'PlayIT' -Parked
  $c = Box $s '!!Phone_Card' 28 24 468 134 $K.Ink '' 1.5 0.14 0 $K.Shell
  $c.Fill.Transparency = 0.14; $c.TextFrame2.MarginLeft = 104; $c.TextFrame2.MarginRight = 18
  Set-Text $c @(
    (P 'Meet Mia. Age 6. Grade 1 in Bohol.' $SN 19.5 $K.White -b -sa 6),
    (P 'Every night after dinner, her reading book stays open on a single stuck sound—while her mother works late at the sewing machine.' $LX 12.5 $K.Cream))
  $g = Box $s '!!Gold_Hero' 46 57 68 68 $K.Gold $K.GoldLip 2.5 0.22 5 $K.Brown 0
  Set-Text $g @(P 'Mm' $SN 26 $K.Ink -b) 2 3; $g.TextFrame2.WordWrap = 0
  ParkTiles $s 600
  ParkLily $s
}
function S02($s) {
  Bg $s 'dusk'; Pixar $s '01_Slide01_Bohol_Dusk_2D.png'; Logo $s 902 16 42; Headline $s 'PlayIT' -Parked
  $c = Box $s '!!Phone_Card' 28 24 468 140 $K.Ink '' 1.5 0.14 0 $K.Gold
  $c.Fill.Transparency = 0.14; $c.TextFrame2.MarginLeft = 22; $c.TextFrame2.MarginRight = 18
  Set-Text $c @(
    (P 'What happens when one letter sound has no one at home to answer it?' $SN 17.5 $K.Gold -b -sa 6),
    (P 'No 1-on-1 phonics coaching. When Mia closes that book tonight, she falls another day behind.' $LX 13 $K.Cream))
  $g = Box $s '!!Gold_Hero' 326 392 60 60 $K.Gold $K.GoldLip 2.5 0.2 5 $K.Brown 0
  Set-Text $g @(P 'Mm' $SN 24 $K.Ink -b) 2 3; $g.TextFrame2.WordWrap = 0; $g.Rotation = -6
  ParkTiles $s 600
  ParkLily $s
}
function S03($s) {
  Bg $s 'teal'; Pixar $s '02_Slide02_Deep_Teal_Literacy_Gap_2D.png'
  [void](Box $s $null 899 13 48 48 $K.Shell '' 0 0.24 0)
  Logo $s 902 16 42; Headline $s 'PlayIT' -Parked
  $g = Box $s '!!Gold_Hero' 32 26 440 100 $K.Gold $K.GoldLip 3 0.18 7
  Set-Text $g @(P '9 out of 10' $SN 52 $K.Ink -b) 2 3
  $c = Box $s '!!Phone_Card' 32 140 440 66 $K.Shell $K.ShellLip 2 0.2 5 $K.Brown 14
  Set-Text $c @((P 'Filipino children at age 10 struggle to read a simple text.' $LXS 13.5 $K.Ink -sa 2), (P 'World Bank & UNESCO Learning Poverty Brief, June 2022 (90.9% rate)' $LX 9.5 $K.Muted))
  $co = Box $s '!!Tile_Coral' 500 26 390 84 $K.Coral $K.CoralLip 2.5 0.18 6 $K.Brown 16
  Set-Text $co @((P 'THE CONNECTIVITY WALL' $LXE 15 $K.Ink -sa 3), (P 'Cloud reading apps fail without steady Wi-Fi.' $LX 12.5 $K.Ink))
  $am = Box $s '!!Tile_Amber' 500 122 390 84 $K.Amber $K.AmberLip 2.5 0.18 6 $K.Brown 16
  Set-Text $am @((P 'THE AFFORDABILITY WALL' $LXE 15 $K.Ink -sa 3), (P ([char]0x20B1 + '400' + [char]0x2013 + [char]0x20B1 + '600 a month paywalls and foreign accents.') $LX 12.5 $K.Ink))
  [void](Box $s '!!Tile_Blue' -200 600 76 60 $K.Blue $K.BlueLip 2 0.3 4)
  [void](Box $s '!!Tile_Green' 1000 600 76 60 $K.Green $K.GreenLip 2 0.3 4)
  ParkLily $s
}
function S04($s) {
  Bg $s 'day'; Pixar $s '02_Slide02_Deep_Teal_Literacy_Gap_2D.png' -Parked; Headline $s 'PlayIT' -Parked
  $g = Box $s '!!Gold_Hero' 290 16 380 36 $K.Gold $K.GoldLip 2 0.5 4
  Set-Text $g @(P 'AN OFFLINE-FIRST PHONICS COMPANION' $LXE 12 $K.Ink) 2 3
  Logo $s 44 72 80; [void](Pic $s $null 'wordmark_v4_teal.png' 134 76 202 74.9)
  Phone $s 380.5 64 400 'screen_map.png'
  $specs = @(
    @{ t = $TILES[0]; x = 40; y = 172; a = '100% Offline'; b = 'No INTERNET permission in the app' },
    @{ t = $TILES[1]; x = 40; y = 274; a = 'Listens Out Loud'; b = 'Vosk speech check on the phone' },
    @{ t = $TILES[2]; x = 620; y = 172; a = 'DepEd Marungko'; b = 'M, S, A, I first, the classroom order' },
    @{ t = $TILES[3]; x = 620; y = 274; a = 'Budget Android'; b = 'Android 8.0+ · Kotlin · Room' })
  foreach ($q in $specs) { $b = Box $s $q.t.n $q.x $q.y 300 84 $q.t.f $q.t.l 2.5 0.2 6 $K.Brown 16; Set-Text $b @((P $q.a $LXE 17 $K.Ink -sa 2), (P $q.b $LX 12 $K.Ink)) }
  Lily $s 'waving' 196 368 154
}
# The 4-step accordion: one step expanded, the others as compact pills (x 380 .. 910).
function Accordion($s, [int]$open, [hashtable]$body) {
  $labels = @('1. Hear It', '2. Say It', '3. Find It', '4. Blend It')
  $y = 70
  for ($i = 0; $i -lt 4; $i++) {
    $t = $TILES[$i]
    if ($i -eq $open) {
      $b = Box $s $t.n 380 $y 530 216 $t.f $t.l 3 0.1 7 $K.Brown 20
      $b.TextFrame2.MarginTop = 16; $b.TextFrame2.MarginRight = $(if ($body.r) { $body.r } else { 20 })
      Set-Text $b @((P $body.title $SN 24 $K.Ink -b -sa 8), (P $body.text $LX 16 $K.Ink)) 1 1
      $y += 228
    } else {
      $b = Box $s $t.n 380 $y 530 50 $t.f $t.l 2.5 0.3 5 $K.Brown 20
      Set-Text $b @(P $labels[$i] $LXE 15 $K.Ink) 1 3
      $y += 62
    }
  }
}
function StepSlide($s, [int]$open, [string]$screen, [string]$pose, [hashtable]$body) {
  Bg $s 'day'; Pixar $s '02_Slide02_Deep_Teal_Literacy_Gap_2D.png' -Parked; Logo $s 22 16 40
  Headline $s 'How Lily teaches: 4 steps for every letter'
  $g = Box $s '!!Gold_Hero' 760 20 150 32 $K.Gold $K.GoldLip 2 0.5 4
  Set-Text $g @(P ("STEP {0} OF 4" -f ($open + 1)) $LXE 12 $K.Ink) 2 3
  Phone $s 61 78 420 $screen
  Accordion $s $open $body
  Lily $s $pose 268 395 110
}
function S05($s) {
  StepSlide $s 0 'screen_hearit.png' 'pointing' @{ title = '1. HEAR IT'; r = 118; text = 'Lily models the pure /m/ sound and the anchor word, Mouse. The child can replay it at their own pace.' }
  [void](Pic $s $null 'letter_m.png' 806 178 92 92)
}
function S06($s) {
  StepSlide $s 1 'screen_sayit_cue.png' 'listening' @{ title = '2. SAY IT · OFFLINE SPEECH CHECK'; r = 118; text = "Vosk checks the child's spoken word on the phone itself: no internet, no cloud fees. On a second miss Lily shows the mouth shape, and a miss never costs a heart." }
  [void](Pic $s $null 'mouth_lips_together.png' 806 240 92 92)
}
function S07($s) {
  StepSlide $s 2 'screen_findit.png' 'encouraging' @{ title = '3. FIND IT · SOUND TO PICTURE'; text = 'The child taps the 3 pictures that start with /m/: Mouse, Mom and Map. A wrong tap costs one of 5 hearts.' }
  for ($i = 0; $i -lt 5; $i++) { [void](Pic $s $null 'reward_heart.png' (402 + 34 * $i) 364 30 30) }
}
function S08($s) {
  StepSlide $s 3 'screen_blendit.png' 'celebrating' @{ title = '4. BLEND IT · BUILD REAL WORDS'; text = "When a chapter's letters are done, the child sounds out letter tiles and builds real words: SAM, SIS, AM. Progress and up to 3 stars per letter are saved on the phone." }
  for ($i = 0; $i -lt 3; $i++) { [void](Pic $s $null 'reward_star.png' (402 + 40 * $i) 422 36 36) }
}
function S09($s) {
  Bg $s 'day'; Pixar $s '02_Slide02_Deep_Teal_Literacy_Gap_2D.png' -Parked; Logo $s 22 16 40
  Headline $s 'Parent Zone, and what is built today'
  $labels = @('1. Hear It', '2. Say It', '3. Find It', '4. Blend It')
  for ($i = 0; $i -lt 4; $i++) { $t = $TILES[$i]; $b = Box $s $t.n 380 (70 + 46 * $i) 530 38 $t.f $t.l 2 0.3 4 $K.Brown 20; Set-Text $b @(P $labels[$i] $LXE 13.5 $K.Ink) 1 3 }
  $g = Box $s '!!Gold_Hero' 380 262 530 212 $K.Gold $K.GoldLip 3 0.1 7 $K.Brown 22
  $g.TextFrame2.MarginTop = 16
  Set-Text $g @((P 'PARENT ZONE + BUILT TODAY' $SN 22 $K.Ink -b -sa 8),
    (P '•  Progress for all 26 letters, in plain words' $LX 15 $K.Ink -sa 5),
    (P '•  One-tap A4 PDF report, made offline' $LX 15 $K.Ink -sa 5),
    (P '•  697 automated tests: 691 passed, 6 skipped, 0 failed' $LX 15 $K.Ink -sa 5),
    (P '•  No INTERNET permission in the APK' $LX 15 $K.Ink)) 1 1
  Phone $s 61 78 420 'screen_parent.png'
  Lily $s 'celebrating' 268 395 110
}
function S10($s) {
  Bg $s 'day'; Pixar $s '02_Slide02_Deep_Teal_Literacy_Gap_2D.png' -Parked; Logo $s 22 16 40
  Headline $s 'Tested with 30 people: the bridge to Mia'
  $g = Box $s '!!Gold_Hero' 36 80 600 50 $K.Gold $K.GoldLip 3 0.3 6
  Set-Text $g @(P 'THE BRIDGE TO THE CHILD' $SN 21 $K.Ink -b) 2 3
  $cols = @(
    @(@('PARENTS · B2C', $LXE, 11.5), @('4.6 / 5', $SN, 28), @('Grade 1 fit', $LX, 11.5), @('75.5', $SN, 24), @('SUS usability score', $LX, 11.5), @('n = 5 parents', $LXS, 10.5)),
    @(@('TEACHERS · B2B', $LXE, 11.5), @('4.73 / 5', $SN, 28), @('teaching-design rubric (4.725)', $LX, 11), @('n = 5 on the final rubric', $LXS, 10.5), @('9 teachers took part in all', $LX, 10.5)),
    @(@('LEARNERS · B2G & CSR', $LXE, 11.5), @('15 of 16', $SN, 28), @('chose the happiest face', $LX, 11.5), @('14 of 16', $SN, 24), @('would play again', $LX, 11.5), @('n = 16 Grade 1 learners', $LXS, 10.5)))
  for ($i = 0; $i -lt 3; $i++) {
    $t = $TILES[$i]; $b = Box $s $t.n (40 + 196 * $i) 144 184 250 $t.f $t.l 3 0.12 7 $K.Brown 10
    $b.TextFrame2.MarginTop = 26
    Set-Text $b @($cols[$i] | ForEach-Object { P $_[0] $_[1] $_[2] $K.Ink -b:($_[1] -eq $SN) -sa 3 }) 2 1
  }
  $f = Box $s '!!Tile_Coral' 36 408 600 50 $K.Coral $K.CoralLip 3 0.3 6
  Set-Text $f @(P '30 participants · 16 learners · 5 parents · 9 teachers · two rounds, 2026' $LXS 13 $K.Ink) 2 3
  [void](TB $s $null 40 470 600 24 @(P 'Source: STD v1.0 & Round 1–2 survey sheets. Reactions and expert ratings; reading gains were not measured.' $LX 9.5 $K.Muted) 2 1)
  $q = Box $s '!!Phone_Card' 664 80 268 150 $K.Shell $K.ShellLip 2.5 0.14 6 $K.Brown 16
  Set-Text $q @((P ([char]0x201C + 'The app is very useful and engaging for Grade 1 learners.' + [char]0x201D) $LXS 15 $K.Ink -sa 6), (P ([char]0x2014 + ' Grade 1 reading teacher, Round 1') $LX 10.5 $K.Muted)) 1 3
  [void](Pic $s $null 'mapprop_crayon_bridge.png' 676 296 230 230)
  Lily $s 'waving' 726 220 150
}
function S11($s) {
  Bg $s 'day'; Pixar $s '02_Slide02_Deep_Teal_Literacy_Gap_2D.png' -Parked; Logo $s 22 16 40
  Headline $s 'Market opportunity'
  $peso = [char]0x20B1
  $b = Box $s '!!Tile_Blue' 58 80 828 72 $K.Blue $K.BlueLip 3 0.25 6 $K.Brown 18
  Set-Text $b @((P 'TAM · 13.5M early learners' $LXE 16 $K.Ink -sa 1), (P 'Philippines and Southeast Asia (UNESCO UIS primary cohort)' $LX 11.5 $K.Ink)) 1 3
  [void](TB $s $null 690 80 180 72 @(P "$($peso)16.2B / yr" $LXE 24 $K.Ink) 3 3)
  $b = Box $s '!!Tile_Amber' 58 168 590 72 $K.Amber $K.AmberLip 3 0.25 6 $K.Brown 18
  Set-Text $b @((P 'SAM · 2.4M Philippine Grade 1 learners' $LXE 16 $K.Ink -sa 1), (P 'DepEd LIS & PSA cohort · prepaid & patchy-Wi-Fi homes' $LX 11.5 $K.Ink)) 1 3
  [void](TB $s $null 452 168 180 72 @(P "$($peso)2.88B / yr" $LXE 22 $K.Ink) 3 3)
  $b = Box $s '!!Tile_Green' 58 256 346 72 $K.Green $K.GreenLip 3 0.25 6 $K.Brown 18
  Set-Text $b @((P 'SOM · 185K learners' $LXE 16 $K.Ink -sa 1), (P "Region VII · $($peso)222M / yr market" $LX 11.5 $K.Ink)) 1 3
  $b = Box $s '!!Tile_Coral' 420 256 228 72 $K.Coral $K.CoralLip 3 0.25 6 $K.Brown 12
  Set-Text $b @((P ("$($peso)18M" + [char]0x2013 + "$($peso)36M") $SN 21 $K.Ink -b -sa 1), (P 'Cebu beachhead (8%–16%)' $LXS 11.5 $K.Ink)) 2 3
  $g = Box $s '!!Gold_Hero' 58 350 590 56 $K.Gold $K.GoldLip 2.5 0.3 5 $K.Brown 18
  Set-Text $g @(P "Tailwind: DepEd's National Learning Recovery Program and Catch-Up Fridays" $LXS 13 $K.Ink) 1 3
  $c = Box $s '!!Phone_Card' 58 424 590 48 $K.Shell $K.ShellLip 2 0.3 4 $K.Brown 18
  Set-Text $c @(P "Sources: UNESCO UIS, DepEd LIS & PSA cohorts · Valued at $($peso)1,200 / learner / yr." $LX 11 $K.Muted) 1 3
  Lily $s 'pointing' 690 210 170 -Mirror
}
function S12($s) {
  Bg $s 'day'; Pixar $s '02_Slide02_Deep_Teal_Literacy_Gap_2D.png' -Parked; Logo $s 22 16 40
  Headline $s 'How PlayIT earns: 4 revenue streams'
  $peso = [char]0x20B1
  $g = Box $s '!!Gold_Hero' 49 66 862 40 $K.Gold $K.GoldLip 2.5 0.5 5
  Set-Text $g @(P 'Chapter 1 (M, S, A, I) free forever · unlock all 7 chapters online or offline' $LXE 13 $K.Ink) 2 3
  $cards = @(
    @('B2C · FAMILY PASS', "$($peso)149 / mo", "or $($peso)999 one-time pass", 'Paid by GCash or ShopeePay voucher; less than three tutoring sessions.'),
    @('B2B · SCHOOLS', "$($peso)450", 'per student per year', 'Annual licence for private schools; Teacher Analytics Dashboard on the roadmap.'),
    @('B2G · LGU KITS', "$($peso)75,000", 'per barangay kit', "Funded through the Local School Board's Special Education Fund (SEF)."),
    @('CSR SPONSORS', "$($peso)150,000", 'per adopted school', 'Turnkey offline phone or tablet kit with a co-branded impact report.'))
  for ($i = 0; $i -lt 4; $i++) {
    $t = $TILES[$i]; $c = $cards[$i]
    $b = Box $s $t.n (49 + 220 * $i) 124 202 300 $t.f $t.l 3 0.1 7 $K.Brown 14
    $b.TextFrame2.MarginTop = 16
    Set-Text $b @((P $c[0] $LXE 12.5 $K.Ink -sa 10), (P $c[1] $SN 25 $K.Ink -b -sa 2), (P $c[2] $LXS 14 $K.Ink -sa 14), (P $c[3] $LX 13.5 $K.Ink)) 1 1
    $icon = @('reward_heart.png', 'mapprop_book_stack.png', 'mapprop_nipa_hut.png', 'reward_star.png')[$i]
    [void](Pic $s $null $icon (49 + 220 * $i + 114) 336 76 76)
  }
  [void](TB $s $null 49 446 862 22 @(P 'Planned pricing for launch; the capstone build has no payments yet.' $LX 10.5 $K.Muted) 2 1)
  [void](Box $s '!!Phone_Card' 680 600 240 60 $K.Shell $K.ShellLip 2 0.3 4)
  ParkLily $s 'right'
}
function S13($s) {
  Bg $s 'day'; Pixar $s '02_Slide02_Deep_Teal_Literacy_Gap_2D.png' -Parked; Logo $s 22 16 40
  Headline $s 'Why PlayIT wins'
  $board = Box $s '!!Phone_Card' 40 64 880 412 $K.Shell $K.ShellLip 2.5 0.05 6
  $heads = @('Cloud global apps', 'Flashcard apps', '', 'Private tutors')
  $colX = @(292, 446, 0, 600)
  foreach ($i in 0, 1, 3) { $t = $TILES[$i]; $b = Box $s $t.n $colX[$i] 78 146 42 $t.f $t.l 2 0.25 4 $K.Brown 6; Set-Text $b @(P $heads[$i] $LXE 11.5 $K.Ink) 2 3 }
  $g = Box $s '!!Gold_Hero' 754 74 150 392 $K.Gold $K.GoldLip 3 0.08 6 $K.Brown 6
  $g.TextFrame2.MarginTop = 8
  Set-Text $g @((P 'PlayIT' $SN 20 $K.Ink -b), (P ([char]0x20B1 + '149/mo · ' + [char]0x20B1 + '999') $LXS 10.5 $K.Ink)) 2 1
  $ok = [string][char]0x2713; $no = [string][char]0x2715
  $rows = @(
    @('Works fully offline', 'Limited', $ok, 'n/a', "$ok  no internet permission"),
    @("Checks the child's speech", "$ok  in the cloud", $no, $ok, "$ok  on the phone (Vosk)"),
    @('Marungko order + familiar words', $no, 'Partial', 'Varies', "$ok  4.73/5 (5 teachers)"),
    @('Parent or class-record PDF', $no, $no, 'Manual', "$ok  built in, offline"),
    @('Cost of checking speech', 'High (cloud GPU)', ([char]0x20B1 + '0, no speech check'), ([char]0x20B1 + '3,000+ / mo'), ([char]0x20B1 + '0 per learner')))
  $xs = @(58, 292, 446, 600, 754); $ws = @(226, 146, 146, 146, 150)
  for ($r = 0; $r -lt 5; $r++) {
    $y = 132 + 66 * $r
    $ln = $s.Shapes.AddLine(58, $y, 748, $y); $ln.Line.ForeColor.RGB = (C $K.Rule); $ln.Line.Weight = 1
    for ($c = 0; $c -lt 5; $c++) {
      $txt = $rows[$r][$c]
      if ($c -eq 0) { [void](TB $s $null 64 $y 220 64 @(P $txt $LXS 13 $K.Ink) 1 3); continue }
      $isMark = ($txt -eq $ok -or $txt -eq $no)
      $font = $(if ($isMark) { $SYM } else { $LXS }); $size = $(if ($isMark) { 20 } else { 11.5 })
      $col = $(if ($txt -eq $no) { $K.CoralLip } elseif ($txt -eq $ok) { $K.GreenLip } else { $K.Ink })
      $t = TB $s $null ($xs[$c] + 6) $y ($ws[$c] - 12) 64 @(P $txt $font $size $col) 2 3
      if (-not $isMark -and $txt.StartsWith($ok)) { $t.TextFrame2.TextRange.Characters(1, 1).Font.Name = $SYM; $t.TextFrame2.TextRange.Characters(1, 1).Font.Fill.ForeColor.RGB = (C $K.GreenLip) }
    }
  }
  $f = Box $s '!!Tile_Green' 40 494 470 30 $K.Green $K.GreenLip 2 0.5 4 $K.Brown 14
  Set-Text $f @(P "Based on the team's review of 7 early-reading apps" $LXS 11.5 $K.Ink) 1 3
  Lily $s 'celebrating' 304 6 54
}
function GtmSlide($s, [int]$phase, [string]$pose, [double[]]$lily, [hashtable]$card) {
  Bg $s 'day'; Pixar $s '02_Slide02_Deep_Teal_Literacy_Gap_2D.png' -Parked; Logo $s 22 16 40
  Headline $s 'Go-to-market: the classroom door'
  [void](Pic $s $null 'mapprop_book_stack.png' 846 424 96 96)
  $hs = @(100, 180, 260, 340); $labels = @(@('Phase 1', 'Cebu school pilot'), @('Phase 2', 'Zero-data sharing'), @('Phase 3', '20 schools & CSR'), @('Phase 4', 'National scale'))
  # Phases not yet reached are drawn in solid lighter tints (a transparent face would let the lip shadow
  # show through behind the text).
  $tint = @(@('99CEE3', '8DB7C8'), @('F7C38E', 'E0AF7F'), @('93C795', '8BB18C'), @('F5A4A4', 'DD9695'))
  for ($i = 0; $i -lt 4; $i++) {
    $t = $TILES[$i]
    $active = ($i -eq $phase) -or ($phase -eq 2 -and $i -eq 3)
    $face = $(if ($active) { $t.f } else { $tint[$i][0] }); $lip = $(if ($active) { $t.l } else { $tint[$i][1] })
    $b = Box $s $t.n (52 + 146 * $i) (500 - $hs[$i]) 132 $hs[$i] $face $lip 3 0.1 6 $K.Brown 8
    $b.TextFrame2.MarginTop = 10
    Set-Text $b @((P $labels[$i][0] $LXE 13 $K.Ink -sa 2), (P $labels[$i][1] $LX 11 $K.Ink)) 2 1
  }
  $gy = $(if ($phase -eq 2) { 112 } else { 92 })
  $g = Box $s '!!Gold_Hero' 512 $gy 88 48 $K.Gold $K.GoldLip 2.5 0.3 5 $K.Brown 4
  Set-Text $g @(P 'GOAL' $SN 18 $K.Ink -b) 2 3
  if ($phase -eq 2) { [void](Pic $s $null 'reward_confetti_burst.png' 588 20 82 82) }
  $c = Box $s '!!Phone_Card' 650 84 280 212 $K.Shell $K.ShellLip 2.5 0.1 6 $K.Brown 18
  $c.TextFrame2.MarginTop = 16
  Set-Text $c @((P $card.k $LXE 12.5 $K.Teal -sa 6), (P $card.h $SN 23 $K.Ink -b -sa 10), (P $card.t $LX 14.5 $K.Ink)) 1 1
  Lily $s $pose $lily[0] $lily[1] $lily[2]
}
function S14($s) { GtmSlide $s 0 'pointing' @(70, 304, 96) @{ k = 'PHASE 1 · CEBU SCHOOL PILOT'; h = '5 schools · 300 learners'; t = 'A 6-week longitudinal reading-fluency study in five Cebu elementary schools.' } }
function S15($s) { GtmSlide $s 1 'encouraging' @(216, 224, 96) @{ k = 'PHASE 2 · BRIGADA ESKWELA & PTA'; h = ([char]0x20B1 + '0 data · ' + [char]0x20B1 + '0 ads'); t = 'Teachers share playit.apk phone-to-phone with Nearby Share; parents install it without mobile data.' } }
function S16($s) { GtmSlide $s 2 'celebrating' @(376, 144, 96) @{ k = 'PHASES 3' + [char]0x2013 + '4 · REGIONAL ' + [char]0x2192 + ' NATIONAL'; h = '20 schools + 3 CSR partners'; t = 'Then LGU kits through the Special Education Fund (RA 7160 Sec. 272), and Bisaya & Tagalog modules nationwide.' } }
function S17($s) {
  Bg $s 'teal'; Pixar $s '02_Slide02_Deep_Teal_Literacy_Gap_2D.png' -Parked
  [void](Box $s $null 19 13 46 46 $K.Shell '' 0 0.24 0)
  Logo $s 22 16 40
  Headline $s 'Zero cloud burn, a fast path to profit' $K.Cream
  $peso = [char]0x20B1
  $g = Box $s '!!Gold_Hero' 48 76 270 138 $K.Gold $K.GoldLip 3 0.12 7 $K.Brown 14
  Set-Text $g @((P '88%' $SN 34 $K.Ink -b), (P 'gross margin (projected)' $LXS 13 $K.Ink -sa 3), (P "Speech is checked on the phone: $($peso)0 cloud cost per learner" $LX 10.5 $K.Ink)) 2 3
  $c = Box $s '!!Phone_Card' 345 76 270 138 $K.Shell $K.GoldLip 3 0.12 7 $K.Gold 14
  Set-Text $c @((P ('4.8' + [char]0x00D7) $SN 34 $K.Ink -b), (P 'LTV : CAC (projected)' $LXS 13 $K.Ink -sa 3), (P "School-led distribution: $($peso)245 CAC vs $($peso)1,200 LTV" $LX 10.5 $K.Ink)) 2 3
  $k3 = Box $s '!!KPI_3' 642 76 270 138 $K.Shell $K.GoldLip 3 0.12 7 $K.Gold 14
  Set-Text $k3 @((P '985 passes' $SN 28 $K.Ink -b), (P 'break-even volume (~82 / mo)' $LXS 12.5 $K.Ink -sa 3), (P 'Or 2 schools + 1 LGU kit covers annual fixed dev overhead' $LX 10 $K.Ink)) 2 3
  Lily $s 'waving' 878 6 62
  [void](TB $s $null 48 236 500 24 @(P 'Projected revenue' $LXE 14 $K.Cream) 1 3)
  [void](TB $s $null 48 270 88 40 @(P 'Year 1' $SN 16 $K.Cream -b) 1 3)
  [void](Box $s $null 140 270 172.5 40 $K.Gold $K.GoldLip 2.5 0.3 5)
  [void](TB $s $null 322 270 140 40 @(P "$($peso)1.45M" $SN 20 $K.Cream -b) 1 3)
  [void](TB $s $null 48 324 88 40 @(P 'Year 2' $SN 16 $K.Cream -b) 1 3)
  [void](Box $s $null 140 324 690 40 $K.Gold $K.GoldLip 2.5 0.3 5)
  [void](TB $s $null 836 324 118 40 @(P "$($peso)5.80M" $SN 20 $K.Cream -b) 1 3)
  [void](TB $s $null 48 384 88 36 @(P 'From' $LXS 12 $K.Cream) 1 3)
  $legend = @('B2C · families', 'B2B · schools', 'B2G · LGU kits', 'CSR · sponsors')
  for ($i = 0; $i -lt 4; $i++) { $t = $TILES[$i]; $b = Box $s $t.n (140 + 176 * $i) 384 162 36 $t.f $t.l 2.5 0.4 4 $K.Brown 6; Set-Text $b @(P $legend[$i] $LXS 12 $K.Ink) 2 3 }
  [void](TB $s $null 48 442 864 44 @(P "Bottom-up Year 1 target ($($peso)1.45M): 625 family passes ($($peso)624K) + 4 private schools ($($peso)300K) + 3 LGU kits ($($peso)225K) + 2 CSR schools ($($peso)300K). Verified $($peso)0.00 cloud speech cost per learner." $LX 10.5 $K.Cream) 1 1)
}
function S18($s) {
  Bg $s 'cream'; Pixar $s '06_Slide10_Bohol_Daylight_2D.png'; Headline $s 'PlayIT' -Parked
  [void](Box $s '!!Gold_Hero' 18 18 272 504 $K.Gold $K.GoldLip 3 0.06 7)
  [void](Box $s $null 32 26 244 62 $K.Shell $K.ShellLip 2 0.25 4 $K.Brown 4)
  Logo $s 48 33 48; [void](Pic $s $null 'wordmark_v4_teal.png' 106 32 148 54.9)
  [void](TB $s $null 34 94 244 28 @(P 'One sound at a time.' $SN 16.5 $K.Ink -b) 2 3)
  [void](Box $s '!!Phone_Card' 32 128 244 196 $K.Shell $K.ShellLip 2.5 0.08 5)
  [void](Pic $s $null 'qr_app.png' 44 140 104 104); [void](Pic $s $null 'qr_pitch.png' 160 140 104 104)
  [void](TB $s $null 40 250 112 68 @((P 'Try the app' $LXE 11 $K.Ink -sa 2), (P 'playit.apk · Android 8.0+ · works offline' $LX 9 $K.Ink)) 2 1)
  [void](TB $s $null 156 250 112 68 @((P 'Watch the demo' $LXE 11 $K.Ink -sa 2), (P '2-minute pitch video' $LX 9 $K.Ink)) 2 1)
  [void](TB $s $null 34 332 244 34 @(P 'Partner with us for our Cebu school pilot: 5 schools, 300 learners.' $LXS 10.5 $K.Ink) 2 3)
  [void](Pic $s $null 'team_2d_clear.png' 84 370 140 92.4)
  [void](TB $s $null 22 466 264 50 @((P 'Group 56 · CIT-U College of Computer Studies' $LXS 8.5 $K.Ink -sa 2), (P 'Zendrix Riva · Erik Samuel Bien · Ashlee Skye Durano' $LX 7.5 $K.Ink), (P 'Kaysean Miel · John James Palis · Adviser: Mr. Joemarie C. Amparo' $LX 7.5 $K.Ink)) 2 1)
  ParkTiles $s 600
  Lily $s 'waving' 360 338 140
}

# ---------- speaker notes (timed script with click cues) ----------
$Notes = @{}
Get-Content (Join-Path $Root 'speaker_notes.txt') -Raw -Encoding UTF8 | ForEach-Object { $_ -split "(?m)^=== " } | Where-Object { $_.Trim() } | ForEach-Object {
  $lines = $_ -split "`r?`n", 2; $Notes[[int]$lines[0].Trim()] = $lines[1].Trim()
}

# ---------- build ----------
$running = @(Get-Process POWERPNT -ErrorAction SilentlyContinue).Count -gt 0
$app = New-Object -ComObject PowerPoint.Application
$pres = $app.Presentations.Add(0)
try {
  $pres.PageSetup.SlideWidth = 960; $pres.PageSetup.SlideHeight = 540
  $durations = @{ 2 = 0.70; 3 = 0.95; 4 = 0.95; 5 = 0.85; 6 = 0.70; 7 = 0.70; 8 = 0.70; 9 = 0.70; 10 = 0.95; 11 = 0.95; 12 = 0.95; 13 = 0.95; 14 = 0.95; 15 = 0.70; 16 = 0.70; 17 = 0.95; 18 = 0.95 }
  for ($n = 1; $n -le 18; $n++) {
    $s = $pres.Slides.Add($n, 12)
    & ("S{0:D2}" -f $n) $s
    $tr = $s.SlideShowTransition
    $tr.AdvanceOnClick = -1; $tr.AdvanceOnTime = 0
    if ($n -ge 2) { $tr.EntryEffect = 3954; $tr.Duration = $durations[$n] }
    if ($Notes.ContainsKey($n)) { $s.NotesPage.Shapes.Placeholders(2).TextFrame.TextRange.Text = $Notes[$n] }
    $s.Export((Join-Path $Prev ("slide_{0:D2}.png" -f $n)), 'PNG', 1920, 1080)
    "slide {0:D2}: {1} shapes" -f $n, $s.Shapes.Count
  }
  $pres.SaveAs($Pptx, 24, -1)
  $pres.SaveAs($Pdf, 32)
  "saved: $Pptx"; "saved: $Pdf"
} finally {
  $pres.Close()
  if ($app.Presentations.Count -eq 0 -and -not $running) { $app.Quit() }
  [System.Runtime.InteropServices.Marshal]::ReleaseComObject($app) | Out-Null
}

# ---------- optional Morph check video (a timed copy; the deck itself stays click-to-advance) ----------
if ($Video) {
  $running = @(Get-Process POWERPNT -ErrorAction SilentlyContinue).Count -gt 0
  $app = New-Object -ComObject PowerPoint.Application
  $pres = $app.Presentations.Open($Pptx, -1, 0, 0)
  try {
    foreach ($s in $pres.Slides) { $s.SlideShowTransition.AdvanceOnTime = -1; $s.SlideShowTransition.AdvanceTime = 1.2 }
    $mp4 = Join-Path $Prev 'morph_test.mp4'
    $pres.CreateVideo($mp4, -1, 1.2, 720, 30, 85)
    $t0 = Get-Date
    while ($pres.CreateVideoStatus -eq 1 -or $pres.CreateVideoStatus -eq 2) { Start-Sleep -Milliseconds 500; if (((Get-Date) - $t0).TotalMinutes -gt 15) { throw 'video export timed out' } }
    "video status: $($pres.CreateVideoStatus) (3 = done) -> $mp4"
  } finally {
    $pres.Close()
    if ($app.Presentations.Count -eq 0 -and -not $running) { $app.Quit() }
    [System.Runtime.InteropServices.Marshal]::ReleaseComObject($app) | Out-Null
  }
}
