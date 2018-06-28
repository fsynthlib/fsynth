# Online Web demo

https://krzema12.github.io/fsynth/

# CLI

Pipe the output from the command to `aplay` and enjoy!

# Rendering sound to file

```
sox -r 8000 -e unsigned -b 8 -c 1 sound.raw sound.wav
play sound.wav
```
