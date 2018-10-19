[![Build Status](https://travis-ci.com/krzema12/fsynth.svg?branch=master)](https://travis-ci.com/krzema12/fsynth) [![codecov](https://codecov.io/gh/krzema12/fsynth/branch/master/graph/badge.svg)](https://codecov.io/gh/krzema12/fsynth)

# How to listen

## Web

https://krzema12.github.io/fsynth/

## Java CLI

The below command uses system sound output to play music:

```
java -jar cli/build/libs/cli.jar "Van Halen - Jump (intro)"
```

To play other defined songs, see the below repository and use song's `name` property to pass it to the above command.

# Song repository

See https://github.com/krzema12/fsynth/tree/master/core/src/main/kotlin/it/krzeminski/fsynth/songs
