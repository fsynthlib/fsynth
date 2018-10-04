[![Build Status](https://travis-ci.com/krzema12/fsynth.svg?branch=master)](https://travis-ci.com/krzema12/fsynth) [![codecov](https://codecov.io/gh/krzema12/fsynth/branch/master/graph/badge.svg)](https://codecov.io/gh/krzema12/fsynth)

# Online Web demo

https://krzema12.github.io/fsynth/

# CLI

1. To generate the raw sound data to standard output:

    ```
    java -jar cli/build/libs/cli.jar "Van Halen - Jump (intro)"
    ```

2. To play it:

* Linux: `<raw data> | aplay -r 44100`
* Mac OS (`sox` is needed): `<raw data> | play -c 1 -b 8 -e unsigned -t raw -r 44100 -`

# Rendering sound to file

```
sox -r 44100 -e unsigned -b 8 -c 1 sound.raw sound.wav
play sound.wav
```
