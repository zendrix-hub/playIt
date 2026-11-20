# Builds the 10-slide PitchPerfect submission deck (PlayIT_PitchDeck.pptx) starting with the
# adapted PitchPerfect Team Template as Slide 1, followed by Slides 2-10 with continuous PowerPoint Morph.
$ErrorActionPreference = 'Stop'

$Root = $PSScriptRoot
$A = Join-Path $Root 'assets'
$Tmpl = 'C:\Users\Zendrix\.gemini\antigravity\brain\32fa1581-0da3-46db-afd9-328aad4e150e\scratch\Team_Template.pptx'
$Work = 'C:\Users\Zendrix\.gemini\antigravity\brain\32fa1581-0da3-46db-afd9-328aad4e150e\scratch\Deck10_TeamBase_Work.pptx'
$Prev = Join-Path $Root 'previews_10slide'
$Pptx = Join-Path $Root 'PlayIT_PitchDeck.pptx'
$Pdf  = Join-Path $Root 'PlayIT_PitchDeck.pdf'
New-Item -ItemType Directory -Force $Prev | Out-Null
Copy-Item $Tmpl $Work -Force

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
function P([string]$t, [string]$f = $LX, [double]$s = 12, [string]$c = $K.Ink, [switch]$b, [switch]$i, [double]$sa = 0) {
  @{ t = $t; f = $f; s = $s; c = $c; b = [bool]$b; i = [bool]$i; sa = $sa }
}
function Set-Text($shp, $paras, [int]$align = 1, [int]$anchor = 3) {
  $tr = $shp.TextFrame2.TextRange
  $tr.Text = ($paras | ForEach-Object { $_.t }) -join "`r"
  for ($k = 0; $k -lt $paras.Count; $k++) {
    $p = $paras[$k]; $r = $tr.Paragraphs($k + 1, 1)
    $r.Font.Name = $p.f; $r.Font.Size = $p.s
    $r.Font.Bold = $(if ($p.b -and $p.f -notlike '*ExtraBold*' -and $p.f -notlike '*SemiBold*') { -1 } else { 0 })
    $r.Font.Italic = $(if ($p.i) { -1 } else { 0 })
    $r.Font.Fill.ForeColor.RGB = (C $p.c)
    $r.ParagraphFormat.Alignment = $align; $r.ParagraphFormat.SpaceBefore = 0; $r.ParagraphFormat.SpaceAfter = $p.sa
  }
  $shp.TextFrame2.VerticalAnchor = $anchor
}
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

function Bg($s, [string]$mode) {
  $b = $s.Shapes.AddShape(1, 0, 0, 960, 540); $b.Name = '!!Bg_Canvas'; $b.Line.Visible = 0
  switch ($mode) {
    'dusk' { $b.Fill.Solid(); $b.Fill.ForeColor.RGB = (C $K.Dusk) }
    'teal' { $b.Fill.Solid(); $b.Fill.ForeColor.RGB = (C $K.Teal) }
    'cream' { $b.Fill.Solid(); $b.Fill.ForeColor.RGB = (C $K.Sand) }
    default { $b.Fill.TwoColorGradient(1, 1); $b.Fill.ForeColor.RGB = (C $K.Sky); $b.Fill.BackColor.RGB = (C $K.Sand); [void]$b.Fill.GradientStops.Insert((C $K.Mid), 0.5) }
  }
}
function Pixar($s, [string]$file, [switch]$Parked) { [void](Pic $s '!!Bg_Pixar' $file $(if ($Parked) { -1000 } else { 0 }) 0 960 540) }
function Logo($s, [double]$x, [double]$y, [double]$w) { [void](Pic $s '!!Logo_Badge' 'product_logo_v4.png' $x $y $w $w) }
function WordmarkPic($s, [double]$x, [double]$y, [double]$w, [double]$h) { [void](Pic $s '!!Wordmark' 'wordmark_v4_teal.png' $x $y $w $h) }
function Lily($s, [string]$pose, [double]$x, [double]$y, [double]$w, [switch]$Mirror) {
  $p = Pic $s '!!Lily_Guide' "lily_$pose.png" $x $y $w $w
  if ($Mirror) { $p.Flip(0) }
}
function Headline($s, [string]$text, [string]$color = $K.Ink, [switch]$Parked, [double]$w = 720, [double]$size = 24) {
  [void](TB $s '!!Headline' 74 $(if ($Parked) { -80 } else { 16 }) $w 40 @(P $text $SN $size $color -b) 1 3)
}
function Phone($s, [double]$x, [double]$y, [double]$h, [string]$screen) {
  $sh = $h - 28; $sw = [math]::Round($sh * 720 / 1480, 1); $w = $sw + 18
  $b = Box $s '!!Phone_Card' $x $y $w $h $K.Bezel $K.BezelLip 2.5 0.12 6 $K.BezelLip
  $iw = $(if ($screen -eq 'screen_parent.png') { [math]::Round($sh * 576 / 1200, 1) } else { $sw })
  [void](Pic $s '!!Phone_Screen' $screen ($x + 9 + ($sw - $iw) / 2) ($y + 14) $iw $sh)
}
function ParkTiles($s, [double]$y = 600) { $i = 0; foreach ($t in $TILES) { [void](Box $s $t.n (120 + 190 * $i) $y 170 60 $t.f $t.l); $i++ } }
function ParkLily($s, [string]$side = 'left') { if ($side -eq 'left') { Lily $s 'thinking' -200 560 150 } else { Lily $s 'idle' 1000 300 120 } }

# ---------- 10 slides ----------
# Slide 1: Adapted PitchPerfect Team Template (uses template SlideLayout 1, exposes !! Morph actors)
function P01($s) {
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

  # Park Pixar background & Lily off-stage so Slide 1 -> Slide 2 sweeps them smoothly on via Morph
  Pixar $s '01_Slide01_Pitch_Intro_NB.png' -Parked
  ParkLily $s

  # Top-right PlayIT Brand Pill (shares !!Logo_Badge & !!Wordmark with Slide 2)
  [void](Box $s 'PlayIT_Brand_Pill' 667 30 220 72 $K.Shell $K.GoldLip 2.5 0.18 5 $K.Brown 6)
  Logo $s 679 42 48
  WordmarkPic $s 733 40 136 50.4

  # 5 Member Cards aligned to banner grid (x = 222..887); colored frames carry !!Gold_Hero & !!Tile_* actors
  $members = @(
    @{ name = 'Zendrix B. Riva';       sub = 'BSIT-4 · Team Lead'; lip = $K.TealLip;  badge = $K.Gold;  actor = '!!Gold_Hero';  photo = 'team_member_1.png' },
    @{ name = 'Erik Samuel L. Bien';   sub = 'BSIT-4 · Member';    lip = $K.BlueLip;  badge = $K.Blue;  actor = '!!Tile_Blue';  photo = 'team_member_2.png' },
    @{ name = 'Ashlee Skye A. Durano'; sub = 'BSIT-4 · Member';    lip = $K.AmberLip; badge = $K.Amber; actor = '!!Tile_Amber'; photo = 'team_member_3.png' },
    @{ name = 'Kaysean Miel';          sub = 'BSIT-4 · Member';    lip = $K.GreenLip; badge = $K.Green; actor = '!!Tile_Green'; photo = 'team_member_4.png' },
    @{ name = 'John James Palis';      sub = 'BSIT-4 · Member';    lip = $K.CoralLip; badge = $K.Coral; actor = '!!Tile_Coral'; photo = 'team_member_5.png' }
  )
  for ($i = 0; $i -lt 5; $i++) {
    $m = $members[$i]; $x = 222 + 136 * $i
    [void](Box $s ("Member_Card_" + ($i+1)) $x 232 121 180 $K.Shell $m.lip 2.5 0.14 6 $K.Brown 4)
    [void](Box $s $m.actor ($x + 8) 240 105 105 $m.badge '' 1.5 0.12 0 $K.Brown 0)
    [void](Box $s $null ($x + 12) 244 97 97 $K.White '' 0 0.08 0 $K.Brown 0)
    [void](Pic $s ("Member_Photo_" + ($i+1)) $m.photo ($x + 14) 246 93 93)
    $lbl = Box $s ("Member_Label_" + ($i+1)) ($x + 4) 348 113 58 $K.Shell '' 0 0 0 $K.Brown 4
    Set-Text $lbl @(
      (P $m.name $LXE 10 $K.Ink -sa 2),
      (P $m.sub $(if ($i -eq 0) { $LXE } else { $LXS }) 9.5 $(if ($i -eq 0) { $K.Teal } else { $K.Muted }))
    ) 2 3
  }

  # Footer card named !!Phone_Card so it Morphs smoothly into Slide 2's bottom story card
  [void](Box $s '!!Phone_Card' 222 442 665 80 $K.Shell $K.GoldLip 2.5 0.14 5 $K.Brown 12)
  [void](Pic $s 'Team_2D_Group' 'team_2d_clear.png' 236 446 112 72.9)
  [void](TB $s $null 362 446 514 72 @(
    (P 'PlayIT — Offline-First Gamified Phonics Companion (DepEd Marungko)' $LXE 12.5 $K.Teal -sa 3),
    (P 'Mentor / Coach: Mr. Joemarie C. Amparo · College of Computer Studies, CIT-University' $LXS 10.5 $K.Ink -sa 2),
    (P 'IT411 Group 56 · Team Leader Contact: zendrix.riva@cit.edu · 0909 188 9602' $LX 10 $K.Muted)
  ) 1 3)
}

# Slide 2: Compelling Story Hook (Meet Mia in Bohol Dusk)
function P02($s) {
  Bg $s 'dusk'; Pixar $s '01_Slide01_Pitch_Intro_NB.png'; Headline $s 'PlayIT' -Parked
  [void](Box $s $null 24 18 194 58 $K.Shell $K.ShellLip 2 0.25 4 $K.Brown 4)
  Logo $s 34 25 44; WordmarkPic $s 86 24 120 44.5
  $mia = Box $s $null 458 190 88 34 $K.Coral '' 2.5 0.48 0 $K.White 0
  Set-Text $mia @(P 'Mia' $LXE 14 $K.White -b) 2 3
  $c = Box $s '!!Phone_Card' 130 464 700 64 $K.Ink '' 2 0.22 0 $K.Gold 16
  $c.Fill.Transparency = 0.12
  Set-Text $c @(
    (P 'Meet Mia. Age 6. Grade 1 in Bohol.' $SN 17 $K.White -b -sa 2),
    (P 'Full classroom · Nanay at work · No signal — stuck on one sound, she closes the book.' $LX 11 $K.Cream)) 2 3
  $g = Box $s '!!Gold_Hero' 638 112 138 92 $K.Shell $K.TealLip 4 0.22 6 $K.Teal 0
  Set-Text $g @(P 'Mm ?' $SN 38 $K.Teal -b) 2 3; $g.TextFrame2.WordWrap = 0
  $g.TextFrame2.TextRange.Characters(2, 1).Font.Fill.ForeColor.RGB = (C $K.Amber)
  $g.TextFrame2.TextRange.Characters(4, 1).Font.Fill.ForeColor.RGB = (C $K.Coral)
  ParkTiles $s 600
  ParkLily $s
}

# Slide 3: Problem (9 out of 10 + 2 Walls)
function P03($s) {
  Bg $s 'teal'; Pixar $s '03_Slide03_Pitch_9of10_NB.png'
  [void](Box $s $null 899 13 48 48 $K.Shell '' 0 0.24 0)
  Logo $s 902 16 42; Headline $s 'PlayIT' -Parked
  $g = Box $s '!!Gold_Hero' 32 26 440 100 $K.Gold $K.GoldLip 3 0.18 7
  Set-Text $g @(P '9 out of 10' $SN 52 $K.Ink -b) 2 3
  $c = Box $s '!!Phone_Card' 32 140 440 66 $K.Shell $K.ShellLip 2 0.2 5 $K.Brown 14
  Set-Text $c @((P 'Filipino 10-year-olds struggle with a simple text.' $LXS 13.5 $K.Ink -sa 2), (P 'World Bank & UNESCO Learning Poverty Brief, June 2022 (90.9%)' $LX 9.5 $K.Muted))
  $co = Box $s '!!Tile_Coral' 500 26 390 84 $K.Coral $K.CoralLip 2.5 0.18 6 $K.Brown 16
  Set-Text $co @((P 'THE CONNECTIVITY WALL' $LXE 15 $K.Ink -sa 3), (P 'Cloud apps break without steady Wi-Fi.' $LX 12.5 $K.Ink))
  $am = Box $s '!!Tile_Amber' 500 122 390 84 $K.Amber $K.AmberLip 2.5 0.18 6 $K.Brown 16
  Set-Text $am @((P 'THE AFFORDABILITY WALL' $LXE 15 $K.Ink -sa 3), (P ([char]0x20B1 + '400' + [char]0x2013 + [char]0x20B1 + '600/mo paywalls · foreign accents.') $LX 12.5 $K.Ink))
  [void](Box $s '!!Tile_Blue' -200 600 76 60 $K.Blue $K.BlueLip 2 0.3 4)
  [void](Box $s '!!Tile_Green' 1000 600 76 60 $K.Green $K.GreenLip 2 0.3 4)
  ParkLily $s
}

# Slide 4: Solution + 4-Step Phonics Loop + Parent Zone (Built Today)
function P04($s) {
  Bg $s 'day'; Pixar $s '03_Slide03_Pitch_9of10_NB.png' -Parked; Logo $s 22 16 40
  Headline $s 'PlayIT: Offline Phonics in 4 Steps' $K.Ink -w 540
  $g = Box $s '!!Gold_Hero' 584 18 336 34 $K.Gold $K.GoldLip 2 0.5 4
  Set-Text $g @(P 'MARUNGKO · ANDROID 8.0+ · 691 TESTS' $LXE 11 $K.Ink) 2 3
  Phone $s 34 72 384 'screen_sayit_cue.png'
  Lily $s 'celebrating' 158 394 94

  $steps = @(
    @{ t = $TILES[0]; x = 264; y = 74;  title = '1. HEAR IT';              body = ("DepEd Marungko (M, S, A, I first)." + [char]11 + "Pure /m/ sound & Mouse replay."); icon = 'letter_m.png'; iw = 52; ix = 520; iy = 148 },
    @{ t = $TILES[1]; x = 604; y = 74;  title = '2. SAY IT · OFFLINE AI';  body = ("Vosk checks speech (" + [char]0x20B1 + "0 cloud)." + [char]11 + "Miss 2x " + [char]0x2192 + " mouth cue, 0 hearts lost."); icon = 'mouth_lips_together.png'; iw = 54; ix = 858; iy = 146 },
    @{ t = $TILES[2]; x = 264; y = 224; title = '3. FIND IT · PICTURE MATCH'; body = ("Tap 3 words starting with /m/:" + [char]11 + "Mouse · Mom · Map (5 hearts)."); icon = 'reward_heart.png'; iw = 44; ix = 524; iy = 306 },
    @{ t = $TILES[3]; x = 604; y = 224; title = '4. BLEND IT · REAL WORDS';  body = ("Tiles snap into SAM · SIS · AM." + [char]11 + "3 stars saved in Room SQLite."); icon = 'reward_star.png'; iw = 46; ix = 864; iy = 304 }
  )
  foreach ($st in $steps) {
    $b = Box $s $st.t.n $st.x $st.y 316 134 $st.t.f $st.t.l 2.5 0.14 6 $K.Brown 16
    $b.TextFrame2.MarginTop = 12; $b.TextFrame2.MarginRight = 64
    Set-Text $b @((P $st.title $SN 14.5 $K.Ink -b -sa 6), (P $st.body $LX 12 $K.Ink)) 1 1
    [void](Pic $s $null $st.icon $st.ix $st.iy $st.iw $st.iw)
  }

  $pz = Box $s $null 264 374 656 118 $K.Shell $K.ShellLip 2.5 0.12 6 $K.Brown 16
  $pz.TextFrame2.MarginLeft = 78; $pz.TextFrame2.MarginTop = 12
  Set-Text $pz @(
    (P 'PARENT ZONE + BUILT TODAY' $SN 14.5 $K.Teal -b -sa 5),
    (P '•  26-letter mastery in plain words + one-tap offline A4 PDF report' $LX 12 $K.Ink -sa 3),
    (P '•  Kotlin + Jetpack Compose · 697 JUnit tests (691 passed, 6 skipped) · 0 INTERNET permission' $LX 11.5 $K.Ink)) 1 1
  [void](Pic $s $null 'screen_parent.png' 280 385 46 96)
}

# Slide 5: Field Validation (Bridge to Mia)
function P05($s) {
  Bg $s 'day'; Pixar $s '03_Slide03_Pitch_9of10_NB.png' -Parked; Logo $s 22 16 40
  Headline $s 'Tested with 30 people: the bridge to Mia'
  $g = Box $s '!!Gold_Hero' 36 80 600 50 $K.Gold $K.GoldLip 3 0.3 6
  Set-Text $g @(P 'THE BRIDGE TO THE CHILD' $SN 21 $K.Ink -b) 2 3
  $cols = @(
    @(@('PARENTS · B2C', $LXE, 11.5), @('4.6 / 5', $SN, 28), @('Grade 1 fit', $LX, 11.5), @('75.5', $SN, 24), @('SUS usability', $LX, 11.5), @('n = 5 parents', $LXS, 10.5)),
    @(@('TEACHERS · B2B', $LXE, 11.5), @('4.73 / 5', $SN, 28), @('teaching-design rubric', $LX, 11), @('n = 5 final rubric', $LXS, 10.5), @('9 teachers total', $LX, 10.5)),
    @(@('LEARNERS · B2G & CSR', $LXE, 11.5), @('15 of 16', $SN, 28), @('happiest face', $LX, 11.5), @('14 of 16', $SN, 24), @('would play again', $LX, 11.5), @('n = 16 learners', $LXS, 10.5)))
  for ($i = 0; $i -lt 3; $i++) {
    $t = $TILES[$i]; $b = Box $s $t.n (40 + 196 * $i) 144 184 250 $t.f $t.l 3 0.12 7 $K.Brown 10
    $b.TextFrame2.MarginTop = 26
    Set-Text $b @($cols[$i] | ForEach-Object { P $_[0] $_[1] $_[2] $K.Ink -b:($_[1] -eq $SN) -sa 3 }) 2 1
  }
  $f = Box $s '!!Tile_Coral' 36 408 600 50 $K.Coral $K.CoralLip 3 0.3 6
  Set-Text $f @(P '30 participants · 16 learners · 5 parents · 9 teachers · Rounds 1–2' $LXS 13 $K.Ink) 2 3
  [void](TB $s $null 40 470 600 24 @(P 'STD v1.0 & survey sheets · Reactions and rubric ratings; reading gains not yet measured.' $LX 9.5 $K.Muted) 2 1)
  $q = Box $s '!!Phone_Card' 664 80 268 150 $K.Shell $K.ShellLip 2.5 0.14 6 $K.Brown 16
  Set-Text $q @((P ([char]0x201C + 'The app is very useful and engaging for Grade 1 learners.' + [char]0x201D) $LXS 15 $K.Ink -sa 6), (P ([char]0x2014 + ' Grade 1 reading teacher, Round 1') $LX 10.5 $K.Muted)) 1 3
  [void](Pic $s $null 'mapprop_bridge_storybook.png' 664 294 264 264)
  Lily $s 'waving' 728 286 136
}

# Slide 6: Market Opportunity (TAM / SAM / SOM)
function P06($s) {
  Bg $s 'day'; Pixar $s '03_Slide03_Pitch_9of10_NB.png' -Parked; Logo $s 22 16 40
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

# Slide 7: Business Model (4 Revenue Streams)
function P07($s) {
  Bg $s 'day'; Pixar $s '03_Slide03_Pitch_9of10_NB.png' -Parked; Logo $s 22 16 40
  Headline $s 'How PlayIT earns: 4 revenue streams'
  $peso = [char]0x20B1
  $g = Box $s '!!Gold_Hero' 49 66 862 40 $K.Gold $K.GoldLip 2.5 0.5 5
  Set-Text $g @(P 'Chapter 1 (M, S, A, I) free forever · unlock all 7 chapters online or offline' $LXE 13 $K.Ink) 2 3
  $cards = @(
    @('B2C · FAMILY PASS', "$($peso)149 / mo", "or $($peso)999 one-time pass", 'GCash / ShopeePay offline voucher · less than 3 tutor sessions.'),
    @('B2B · SCHOOLS', "$($peso)450", 'per student per year', 'Annual school license · Teacher dashboard on roadmap.'),
    @('B2G · LGU KITS', "$($peso)75,000", 'per barangay kit', 'Funded via Special Education Fund (SEF).'),
    @('CSR SPONSORS', "$($peso)150,000", 'per adopted school', 'Turnkey offline kits + co-branded impact report.'))
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

# Slide 8: Competitive Advantage
function P08($s) {
  Bg $s 'day'; Pixar $s '03_Slide03_Pitch_9of10_NB.png' -Parked; Logo $s 22 16 40
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
  $f = Box $s '!!Tile_Green' 40 494 420 30 $K.Green $K.GreenLip 2 0.5 4 $K.Brown 14
  Set-Text $f @(P 'Team review of 7 early-reading apps' $LXS 11.5 $K.Ink) 1 3
  Lily $s 'celebrating' 304 6 54
}

# Slide 9: Go-To-Market Staircase + Projected Unit Economics
function P09($s) {
  Bg $s 'day'; Pixar $s '03_Slide03_Pitch_9of10_NB.png' -Parked; Logo $s 22 16 40
  Headline $s 'Go-to-market & projected unit economics' $K.Ink -w 760
  $peso = [char]0x20B1

  $hs = @(150, 206, 262, 318)
  $phases = @(
    @('PHASE 1', 'Q1 2027', '1–5 Cebu schools', '6-week fluency pilot'),
    @('PHASE 2', 'Q2 2027', 'Brigada Eskwela', 'Nearby Share · ₱0 data'),
    @('PHASE 3', 'Q3–Q4 2027', '20 schools + 3 CSR', 'Visayas expansion'),
    @('PHASE 4', '2028+', 'LGU SEF + National', 'Bisaya & Tagalog'))
  for ($i = 0; $i -lt 4; $i++) {
    $t = $TILES[$i]; $ph = $phases[$i]
    $b = Box $s $t.n (40 + 128 * $i) (458 - $hs[$i]) 116 $hs[$i] $t.f $t.l 2.5 0.1 6 $K.Brown 8
    $b.TextFrame2.MarginTop = 10
    Set-Text $b @((P $ph[0] $LXE 11 $K.Ink -sa 1), (P $ph[1] $SN 13.5 $K.Ink -b -sa 6), (P $ph[2] $LXS 11 $K.Ink -sa 3), (P $ph[3] $LX 10 $K.Ink)) 2 1
  }
  [void](Pic $s $null 'reward_confetti_burst.png' 350 72 68 68)
  Lily $s 'celebrating' 434 52 90
  [void](TB $s $null 40 472 500 28 @(P 'Classroom-door launch: teachers beam playit.apk phone-to-phone with zero mobile data.' $LX 10.5 $K.Muted) 1 3)

  [void](Box $s '!!Phone_Card' 562 74 362 420 $K.Teal $K.TealLip 3 0.08 7 $K.Brown 14)
  $g = Box $s '!!Gold_Hero' 580 90 326 34 $K.Gold $K.GoldLip 2 0.45 4
  Set-Text $g @(P 'PROJECTED UNIT ECONOMICS' $LXE 12 $K.Ink) 2 3

  $k1 = Box $s $null 580 138 156 92 $K.Shell $K.GoldLip 2.5 0.12 5 $K.Gold 8
  Set-Text $k1 @((P '88%' $SN 26 $K.Ink -b), (P 'Gross margin' $LXS 11.5 $K.Ink -sa 2), (P "$($peso)0 cloud ASR cost" $LX 9.5 $K.Muted)) 2 3
  $k2 = Box $s $null 750 138 156 92 $K.Shell $K.GoldLip 2.5 0.12 5 $K.Gold 8
  Set-Text $k2 @((P ('4.8' + [char]0x00D7) $SN 26 $K.Ink -b), (P 'LTV : CAC' $LXS 11.5 $K.Ink -sa 2), (P "$($peso)245 CAC · $($peso)1,200 LTV" $LX 9.5 $K.Muted)) 2 3

  $k3 = Box $s $null 580 242 326 62 $K.Shell $K.GoldLip 2.5 0.14 5 $K.Gold 10
  Set-Text $k3 @((P '985 passes break-even (~82 / mo)' $SN 13.5 $K.Ink -b -sa 2), (P '2 schools + 1 LGU kit covers operating overhead' $LX 10 $K.Muted)) 2 3

  [void](TB $s $null 580 318 326 22 @(P 'Projected revenue (Zero cloud burn)' $LXE 12 $K.Cream) 1 3)
  [void](TB $s $null 580 348 60 28 @(P 'Year 1' $SN 13 $K.Cream -b) 1 3)
  [void](Box $s $null 644 348 44 28 $K.Gold $K.GoldLip 2 0.3 4)
  [void](TB $s $null 698 348 110 28 @(P "$($peso)1.45M" $SN 16 $K.Cream -b) 1 3)

  [void](TB $s $null 580 386 60 28 @(P 'Year 2' $SN 13 $K.Cream -b) 1 3)
  [void](Box $s $null 644 386 176 28 $K.Gold $K.GoldLip 2 0.3 4)
  [void](TB $s $null 828 386 84 28 @(P "$($peso)5.80M" $SN 16 $K.Cream -b) 1 3)

  [void](TB $s $null 580 426 326 56 @(P "Bottom-up Year 1 ($($peso)1.45M): 625 passes ($($peso)624K) + 4 schools ($($peso)300K) + 3 LGU kits ($($peso)225K) + 2 CSR schools ($($peso)300K)." $LX 9.5 $K.Cream) 1 1)
}

# Slide 10: Bookend Closing (One sound at a time)
function P10($s) {
  Bg $s 'cream'; Pixar $s '18_Slide18_Pitch_Daylight_NB.png'; Headline $s 'PlayIT' -Parked
  [void](Box $s '!!Gold_Hero' 18 18 272 504 $K.Gold $K.GoldLip 3 0.06 7)
  [void](Box $s $null 32 26 244 62 $K.Shell $K.ShellLip 2 0.25 4 $K.Brown 4)
  Logo $s 48 33 48; WordmarkPic $s 106 32 148 54.9
  [void](TB $s $null 34 94 244 28 @(P 'One sound at a time.' $SN 16.5 $K.Ink -b) 2 3)
  [void](Box $s '!!Phone_Card' 32 128 244 196 $K.Shell $K.ShellLip 2.5 0.08 5)
  [void](Pic $s $null 'qr_app.png' 44 140 104 104); [void](Pic $s $null 'qr_pitch.png' 160 140 104 104)
  [void](TB $s $null 40 250 112 68 @((P 'Try the app' $LXE 11 $K.Ink -sa 2), (P 'playit.apk · Android 8.0+ · works offline' $LX 9 $K.Ink)) 2 1)
  [void](TB $s $null 156 250 112 68 @((P 'Watch the demo' $LXE 11 $K.Ink -sa 2), (P '2-minute pitch video' $LX 9 $K.Ink)) 2 1)
  [void](TB $s $null 30 330 252 38 @(P 'Next step: seeking Cebu school partners for a reading-fluency pilot.' $LXS 10.5 $K.Ink) 2 3)
  [void](Pic $s $null 'team_2d_clear.png' 84 370 140 92.4)
  [void](TB $s $null 22 466 264 50 @((P 'Group 56 · CIT-U College of Computer Studies' $LXS 8.5 $K.Ink -sa 2), (P 'Zendrix Riva · Erik Samuel Bien · Ashlee Skye Durano' $LX 7.5 $K.Ink), (P 'Kaysean Miel · John James Palis · Adviser: Mr. Joemarie C. Amparo' $LX 7.5 $K.Ink)) 2 1)
  ParkTiles $s 600
  Lily $s 'waving' 728 248 152
}

$Notes = @{}
Get-Content (Join-Path $Root 'speaker_notes_10slide.txt') -Raw -Encoding UTF8 | ForEach-Object { $_ -split "(?m)^=== " } | Where-Object { $_.Trim() } | ForEach-Object {
  $lines = $_ -split "`r?`n", 2; $Notes[[int]$lines[0].Trim()] = $lines[1].Trim()
}

function Set-Notes($slide, [string]$text) {
  foreach ($sh in $slide.NotesPage.Shapes) {
    if ($sh.HasTextFrame -eq -1 -and $sh.PlaceholderFormat.Type -eq 2) {
      $sh.TextFrame.TextRange.Text = $text; return
    }
  }
  foreach ($sh in $slide.NotesPage.Shapes) {
    if ($sh.HasTextFrame -eq -1) { $sh.TextFrame.TextRange.Text = $text; return }
  }
}

$running = @(Get-Process POWERPNT -ErrorAction SilentlyContinue).Count -gt 0
$app = New-Object -ComObject PowerPoint.Application
$pres = $app.Presentations.Open($Work, 0, 0, 0)
try {
  $blankLayout = $pres.SlideMaster.CustomLayouts.Item(7) # BLANK layout (without Slide 1 template background)
  # Build Slide 1 inside existing Slide 1 (Title Slide layout)
  $s1 = $pres.Slides.Item(1)
  P01 $s1
  $s1.SlideShowTransition.AdvanceOnClick = -1; $s1.SlideShowTransition.AdvanceOnTime = 0
  if ($Notes.ContainsKey(1)) { Set-Notes $s1 $Notes[1] }
  $s1.Export((Join-Path $Prev 'slide_01.png'), 'PNG', 1920, 1080)
  "slide 01: $($s1.Shapes.Count) shapes"

  # Build Slides 2-10 using Blank layout + Morph (3954)
  for ($n = 2; $n -le 10; $n++) {
    $s = $pres.Slides.AddSlide($n, $blankLayout)
    & ("P{0:D2}" -f $n) $s
    $tr = $s.SlideShowTransition
    $tr.AdvanceOnClick = -1; $tr.AdvanceOnTime = 0
    $tr.EntryEffect = 3954; $tr.Duration = 0.85
    if ($Notes.ContainsKey($n)) { Set-Notes $s $Notes[$n] }
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
