[![Build Status](https://travis-ci.com/krzema12/fsynth.svg?branch=master)](https://travis-ci.com/krzema12/fsynth) [![codecov](https://codecov.io/gh/krzema12/fsynth/branch/master/graph/badge.svg)](https://codecov.io/gh/krzema12/fsynth)

# What is fsynth? 

It's a simple music synthesizer. It focuses on generating the waveforms from scratch, no samples are used. You can listen to it via the Web player, or using the cross-platform Java CLI. Read on to learn more.

Secondly, this project is my playground for learning Kotlin, functional programming (hence the "f" in "fsynth"), front-end development, Gradle, multiplatform projects, and other. It's also a place where I can focus more on quality than I normally could afford in a professional environment, because here I don't have a pressure to deliver on time. That's why my aim here is also to have as little technical debt as possible, and have as clean code as I can write.

# How to listen

## Web

https://krzema12.github.io/fsynth/

## Java CLI

The below command uses system sound output to play music:

```
java -jar cli/build/libs/cli.jar "Van Halen - Jump (intro)"
```

To see a list of available songs, call the CLI without arguments. 

# Build prerequisites

The below dependencies won't be installed by Gradle:

* JDK + path to it in `JAVA_HOME`
* npm + path to its binary in `PATH`
