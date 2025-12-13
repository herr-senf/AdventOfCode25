# Advent of Code 2025

Solutions for [Advent of Code 2025](https://adventofcode.com/2025) puzzles, written in Kotlin.

## Project Structure

Each day's solution is organized in its own directory:

```
AdventOfCode25/
├── Day01/
├── Day02/
├── Day03/
├── Day04/
│   .
│   .
│   .
└── lib/          # JUnit5 and other dependencies
```

Each day folder contains:

- Main solution file (e.g., `Day01.kt`)
- Part 2 solution if different (e.g., `Day01_2.kt`)
- Test files (e.g., `Day01Test.kt`)
- Input file (`input.txt`)

## Running Solutions

### Using IntelliJ IDEA (Recommended)

1. Open the project in IntelliJ IDEA
2. Navigate to the day you want to run (e.g., `Day04/src/Day04.kt`)
3. Click the green play button next to the `main()` function, or
4. Right-click the file and select "Run 'Day04Kt'"

**Running Tests:**

- Open the test file (e.g., `Day04Test.kt`)
- Click the green play button next to the test class or individual test methods
- Or right-click and select "Run 'Day04Test'"

### Using Command Line

#### Prerequisites

- Kotlin compiler installed (`kotlinc` Version
  2) ([check installation instructions](https://kotlinlang.org/docs/command-line.html#homebrew) if needed; SDKMAN should
     be the easiest one)
- Java Runtime Environment (JRE)

#### Compiling and Running

For a specific day (e.g., Day 04):

```bash
# Compile
kotlinc Day04/src/Day04.kt -include-runtime -d Day04.jar

# Run
java -jar Day04.jar
```

#### Running Tests

Tests require JUnit5. Compile with the JUnit libraries:

```bash
# Compile with JUnit
kotlinc Day04/src/*Test.kt -classpath "lib/*" -include-runtime -d Day04Test.jar

# Run tests
java -jar Day04Test.jar
```

## Solutions Progress

- [x] Day 01
- [x] Day 02
- [x] Day 03
- [x] Day 04
- [ ] Day 05
- [ ] Day 06
- [ ] Day 07
- [ ] Day 08
- [ ] Day 09
- [ ] Day 10
- [ ] Day 11
- [ ] Day 12
- [ ] Day 13
- [ ] Day 14
- [ ] Day 15
- [ ] Day 16
- [ ] Day 17
- [ ] Day 18
- [ ] Day 19
- [ ] Day 20
- [ ] Day 21
- [ ] Day 22
- [ ] Day 23
- [ ] Day 24

## About Advent of Code

[Advent of Code](https://adventofcode.com/2025) is an annual event featuring daily programming puzzles throughout
December, created by Eric Wastl.

## Disclaimer

All code is written by hand. AI has only been used to generate documentation because I'm lazy af. Everything else are my
origin ideas and craftsmanship.
