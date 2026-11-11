#!/usr/bin/env bash
# Downloads the Chatterbox voice model into tools/voice/.venv/models/chatterbox/ in parallel pieces.
# Use this when the normal download (inside make_voice.py) stalls, as it did on this network.
# Safe to run again: finished pieces are kept.   bash voice/get_model.sh   (from tools/)
set -u
DIR="$(cd "$(dirname "$0")" && pwd)/.venv/models/chatterbox"
BASE="https://huggingface.co/ResembleAI/chatterbox/resolve/main"
PARTS=10
mkdir -p "$DIR/parts"
cd "$DIR"

for f in ve.safetensors tokenizer.json conds.pt; do
  [ -s "$f" ] || curl -sSL --retry 5 -o "$f" "$BASE/$f"
done

big() {  # name size
  local f=$1 size=$2 step=$(( ($2 + PARTS - 1) / PARTS ))
  if [ -f "$f" ] && [ "$(stat -c %s "$f")" = "$size" ]; then return; fi
  for i in $(seq 0 $((PARTS - 1))); do
    local a=$((i * step)) b=$(( (i + 1) * step - 1 )); [ $b -ge $size ] && b=$((size - 1))
    local want=$((b - a + 1)) p="parts/$f.$i"
    (
      n=0
      while [ "$(stat -c %s "$p" 2>/dev/null || echo 0)" != "$want" ] && [ $n -lt 40 ]; do
        have=$(stat -c %s "$p" 2>/dev/null || echo 0)
        curl -sSL --connect-timeout 20 --speed-time 30 --speed-limit 20000 -r "$((a + have))-$b" "$BASE/$f" >> "$p" 2>/dev/null
        n=$((n + 1))
      done
    ) &
  done
}
big t3_cfg.safetensors 2129653744
big s3gen.safetensors 1056484620
wait
for pair in t3_cfg.safetensors:2129653744 s3gen.safetensors:1056484620; do
  f=${pair%%:*}; size=${pair##*:}
  if [ -f "$f" ] && [ "$(stat -c %s "$f")" = "$size" ]; then echo "$f already complete"; continue; fi
  cat $(for i in $(seq 0 $((PARTS - 1))); do echo "parts/$f.$i"; done) > "$f"
  if [ "$(stat -c %s "$f")" = "$size" ]; then echo "$f complete ($size bytes)"; rm -f parts/$f.*; else echo "$f INCOMPLETE: $(stat -c %s "$f") of $size bytes; run again"; fi
done
ls -la "$DIR"
