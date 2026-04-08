# OOS-Praktikum (OOS-P5)

Maven-based Java project (Java 25) with JavaFX UI and JUnit tests.

## Project Root

Run all commands from:

```bash
cd /home/amier/dev/OOS-Praktikum
```

## Prerequisites

- Java 25
- Maven 3.9+

Check installed versions:

```bash
java -version
mvn -version
```

Project helper (recommended):

```bash
./scripts/mvn-latest.sh -v
```

If `mvn` is not available in your PATH, use the full binary path:

```bash
/home/amier/.maven/maven-3.9.14/bin/mvn -version
```

## Most Important Commands

### 1) Compile main + test sources

```bash
./scripts/mvn-latest.sh clean test-compile
```

### 2) Run all tests

```bash
./scripts/mvn-latest.sh clean test
```

### 3) Run one test class

```bash
./scripts/mvn-latest.sh -Dtest=PrivateBankTest test
```

### 4) Run one test method

```bash
./scripts/mvn-latest.sh -Dtest=PrivateBankTest#getAccountBalanceTest test
```

### 5) Run JavaFX app

```bash
./scripts/mvn-latest.sh clean javafx:run
```

### 6) Build artifact

```bash
./scripts/mvn-latest.sh clean package
```

### 7) Full validation lifecycle

```bash
./scripts/mvn-latest.sh clean verify
```

## Where To Find Things

- Build config: `pom.xml`
- Main source: `src/main/java`
- Tests: `src/test/java`
- JavaFX FXML files: `src/main/resources`
- Test reports: `target/surefire-reports`
- Build output: `target`

## Daily Workflow (Recommended)

Quick dev cycle:

```bash
./scripts/mvn-latest.sh -q test
```

Before pushing:

```bash
./scripts/mvn-latest.sh clean verify
```

Run app locally:

```bash
./scripts/mvn-latest.sh clean javafx:run
```

## Notes

- Current Java release target is set in `pom.xml` via `maven.compiler.release=25`.
- JavaFX entry point is configured in `pom.xml` as `FxApplication`.
- `scripts/mvn-latest.sh` forces Java 25 + Maven 3.9.14 for consistent local runs.
