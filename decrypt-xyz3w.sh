#!/bin/bash

usage() {
  P=$(basename $0)
  cat <<EOF >&2
$P - Decrypt newly hardened XYZware *.3W format back into ZIPped *.gcode.
Usage: $P input.3w
NOTE:
  - This command still does not work. Needs update in openssl command use.
EOF
    exit 1
}

decode() {
  local input=$1
  local name=$(basename $input .3w)

  dd bs=1 if=$file skip=$((0x2000)) of=$name.bin
  openssl enc -d -aes256 -k @xyzprinting.com -iv 0 -nosalt -in $name.bin -out $name.zip
}

test $# -eq 1 || usage
decode "$@"
