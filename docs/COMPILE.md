# Building FastVoice from Source

## Requirements

- **JDK 21+ (LTS)** — [Adoptium Temurin](https://adoptium.net/) or Oracle JDK
- **Maven 3.9+** — [Apache Maven](https://maven.apache.org/download.cgi)
- **Operating System**: Windows 10/11 (x64), Linux (x64/AArch64), macOS (Apple Silicon/Intel)

---

## Quick Build

```bash
mvn clean test
mvn clean package
```

The resulting artifact will be located in `target/FastVoice-0.1.0.jar`.

---

## Local Maven Installation

To make FastVoice available to downstream projects on your local machine:

```bash
mvn clean install
```
