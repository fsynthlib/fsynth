[![Build Status](https://travis-ci.com/krzema12/fsynth.svg?branch=master)](https://travis-ci.com/krzema12/fsynth)

# Online Web demo

https://krzema12.github.io/fsynth/

# CLI

Pipe the output from the command to `aplay -r 44100` and enjoy!

# Rendering sound to file

```
sox -r 44100 -e unsigned -b 8 -c 1 sound.raw sound.wav
play sound.wav
```
