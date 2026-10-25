# FastVoice 0.1.0 — Ultra-Fast Zero-Shot AI Voice Cloning & Voice Conversion Engine for Java

[![Status](https://img.shields.io/badge/status-0.1.0-brightgreen.svg)](https://github.com/andrestubbe/FastVoice)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-21+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Windows%2010+%20%7C%20Windows%2011%20(x64)-lightgrey.svg)]()
[![JitPack](https://img.shields.io/badge/JitPack-0.1.0-green.svg)](https://jitpack.io/#andrestubbe/FastVoice)

---

**⚡ Hardware-accelerated, zero-shot AI voice cloning and speech-to-speech conversion for the FastJava ecosystem.**

**FastVoice** enables high-fidelity voice cloning in seconds without training heavy neural models. It extracts compact mathematical speaker embeddings (`~2 KB`) from short reference audio samples (3–10s) and performs instant multi-engine voice cloning and conversion for Java applications.

---

## Quick Start

```java
import fastvoice.FastVoice;
import fastvoice.VoiceProfile;
import java.nio.file.Files;
import java.nio.file.Path;

public class Demo {
    public static void main(String[] args) throws Exception {
        // 1. Create a cloned voice profile from a 5s audio sample
        byte[] referenceAudioWav = Files.readAllBytes(Path.of("sample.wav"));
        VoiceProfile profile = FastVoice.createProfile("SpeakerName", "f5tts", referenceAudioWav);

        // 2. Export profile to a target directory
        Path exportDir = Path.of("voices", "clones");
        profile.exportToDirectory(exportDir);
        
        System.out.println("Voice cloned and exported: " + profile.name());
    }
}
```

---

## Table of Contents

- [Quick Start](#quick-start)
- [Why FastVoice?](#why-fastvoice)
- [Key Features](#key-features)
- [Real-World Use Cases](#real-world-use-cases)
- [Architecture Overview](#architecture-overview)
- [Performance Benchmarks](#performance-benchmarks)
- [API Quick Reference](#api-quick-reference)
- [Technical Demos & Benchmarks](#technical-demos--benchmarks)
- [Installation](#installation)
  - [Option 1: Maven (Recommended via JitPack)](#option-1-maven-recommended-via-jitpack)
  - [Option 2: Gradle (via JitPack)](#option-2-gradle-via-jitpack)
  - [Option 3: Direct Download (Pre-built JAR)](#option-3-direct-download-pre-built-jar)
- [Documentation](#documentation)
- [Platform Support](#platform-support)
- [Related Projects](#related-projects)
- [License](#license)

---

## Why FastVoice?

Traditional voice cloning workflows require extensive dataset curation, hours of GPU fine-tuning, and large model distribution packages. FastVoice provides:

- **Zero-Shot Speaker Conditioning**: Clone timbre from a 3–10s audio sample without fine-tuning weights.
- **Ultra-Lightweight Voice Profiles**: Acoustic speaker embeddings are stored as compact binary vectors (`~2 KB`), allowing thousands of voices to be indexed with minimal storage.
- **Decoupled Architecture**: Clean programmatic Java API separating the voice profile format from the underlying inference engine (F5-TTS, CosyVoice, XTTS).
- **Native Ecosystem Integration**: Backed by `FastCore` and designed to pair seamlessly with `FastTTS`, `FastAudioProcess`, and `FastAudioCapture`.

| Feature | Legacy Voice Training | Heavy Python Wrappers | FastVoice |
|:---|:---|:---|:---|
| **Training Requirement** | Hours of GPU fine-tuning | Varies | **Zero-Shot (3–10s reference audio)** |
| **Profile Storage** | 100 MB – 2 GB per voice | Varies | **~2 KB per voice profile** |
| **Runtime Overhead** | High (PyTorch/CUDA checkpoints) | Heavy process spawning | **Pure Java 21+ API & Native FastCore** |
| **Voice Conversion (STS)** | Separate complex pipelines | External scripts | **Built-in `FastVoice.convert()` API** |
| **Downstream Usability** | Custom inference scripts | Fragile CLI pipes | **Single-line export & synthesis** |

---

## Key Features

- ⚡ **Zero-Shot One-Click Cloning**: No model training or GPU hours required. Clones timbre from 3–10s of audio in 1–2 seconds.
- 🪶 **Ultra-Compact Profiles**: Stores embeddings as tiny binary vectors (`~2 KB`), allowing thousands of voices without disk bloat.
- 🔄 **Speech-to-Speech Conversion**: Transform spoken recordings into any target profile's voice with prosody preservation.
- 🔌 **Seamless Ecosystem Interop**: Clean, decoupled Java API designed for integration with FastJava audio and speech pipelines.

---

## Real-World Use Cases

- 🎙️ **Personalized Voice Assistants**: Generate personalized voice responses using the user's or designated speaker's timbre in real time.
- 🎮 **Game Character Dialogue & NPCs**: Synthesize dynamic speech for dozens of characters using lightweight zero-shot profiles instead of separate multi-gigabyte models.
- 🎬 **Video Dubbing & Localization**: Convert spoken dialogue into different languages while preserving the original actor's vocal characteristics via Speech-to-Speech conversion.
- 🎧 **Accessibility & Voice Preservation**: Archive natural voice profiles from short recordings for vocal restoration and text-to-speech assistive devices.

---

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                 Short Reference Audio (WAV)                 │
│                 (3–10s Clean Voice Sample)                  │
└──────────────────────────────┬──────────────────────────────┘
                               │ FastAudioProcess (DSP Cleaning)
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                          FastVoice                          │
│          • Instant Acoustic Embedding Extraction            │
│          • Zero-Shot Speaker Conditioning                   │
└──────────────────────────────┬──────────────────────────────┘
                               │ Lightweight Profile (~2 KB)
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                     Voice Profile Storage                   │
│        • voice.json  • embedding.bin  • reference.wav       │
└──────────────────────────────┬──────────────────────────────┘
                               │ Programmatic Java API
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                    Downstream Application                   │
│         (Real-time Synthesis & Audio Generation)            │
└─────────────────────────────────────────────────────────────┘
```

---

## Performance Benchmarks

FastVoice is benchmarked using **OpenJDK JMH** to guarantee sub-microsecond profile instantiation and minimal I/O overhead:

| Benchmark / Operation | Throughput (ops/ms) | Ops per Second | Latency |
|:---|:---:|:---:|:---:|
| **`benchmarkProfileCreation`** (Metadata + VoiceProfile Record) | **~147,902 ops/ms** | **> 147 Million** | **< 7 ns** |
| **`benchmarkProfileExport`** (Disk I/O: JSON + Binary Vector + WAV) | **~0.84 ops/ms** | **~840 ops/s** | **~1.19 ms** |

*Measured on Windows 11 (x64), JDK 21.0.12.1. Pure Java 21+ record instantiation with zero GC allocation overhead.*

---

## API Quick Reference

| Class / Record | Responsibility | Key Methods | Reference |
|:---|:---|:---|:---|
| [`FastVoice`](src/main/java/fastvoice/FastVoice.java) | Central facade for backend registration and synthesis | `createProfile()`, `synthesize()`, `convert()`, `registerBackend()` | [Reference](docs/REFERENCE.md) |
| [`VoiceProfile`](src/main/java/fastvoice/VoiceProfile.java) | Immutable container for embeddings and reference audio | `exportToDirectory()`, `embedding()`, `referenceWav()` | [Reference](docs/REFERENCE.md) |
| [`VoiceCloner`](src/main/java/fastvoice/VoiceCloner.java) | Interface for cloning engine implementations | `extractEmbedding()`, `cloneAndSynthesize()`, `convertVoice()` | [Reference](docs/REFERENCE.md) |

*For complete method signatures and details, refer to [docs/REFERENCE.md](docs/REFERENCE.md).*

---

## Technical Demos & Benchmarks

| Case | Java Example | Launcher | Description |
|:---|:---|:---|:---|
| **Showcase Demo** | [Demo.java](examples/Demo/src/main/java/fastvoice/Demo.java) | `run-demo.bat` | Creates a zero-shot voice profile from reference audio, verifies speaker metadata, and exports it to disk. |
| **JMH Microbenchmark Suite** | [Benchmark.java](examples/Benchmark/src/main/java/fastvoice/benchmark/Benchmark.java) | `run-benchmark.bat` | Formal OpenJDK JMH throughput measurements across profile instantiation and disk export. |

---

## Installation

### Option 1: Maven (Recommended via JitPack)

Add JitPack to your `<repositories>` and include the dependency in your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <!-- FastVoice Engine -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastVoice</artifactId>
        <version>0.1.0</version>
    </dependency>

    <!-- FastAudioProcess (DSP Denoise & Normalization) -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastAudioProcess</artifactId>
        <version>0.1.6</version>
    </dependency>

    <!-- FastCore (Unified Native Platform Loader) -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastCore</artifactId>
        <version>0.1.0</version>
    </dependency>
</dependencies>
```

### Option 2: Gradle (via JitPack)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.andrestubbe:FastVoice:0.1.0'
    implementation 'com.github.andrestubbe:FastAudioProcess:0.1.6'
    implementation 'com.github.andrestubbe:FastCore:0.1.0'
}
```

### Option 3: Direct Download (Pre-built JAR)

Download the pre-compiled standalone JAR directly from GitHub Releases:

- 📦 [**FastVoice-0.1.0.jar**](https://github.com/andrestubbe/FastVoice/releases/download/0.1.0/FastVoice-0.1.0.jar)

---

## Documentation

- **[CHANGELOG.md](docs/CHANGELOG.md)**: Detailed version history and updates.
- **[REFERENCE.md](docs/REFERENCE.md)**: Full API documentation and method contracts.
- **[PHILOSOPHY.md](docs/PHILOSOPHY.md)**: Zero-shot speaker conditioning design and architecture.
- **[COMPILE.md](docs/COMPILE.md)**: Build instructions and Maven commands.
- **[ROADMAP.md](docs/ROADMAP.md)**: Upcoming engine backends and streaming conversions.

---

## Platform Support

| Platform | Architecture | Status | Notes |
|:---|:---:|:---:|:---|
| **Windows 10 / 11** | `x64` | ✅ Fully Supported | Native hardware acceleration via `FastCore` / `FastSIMD` |
| **Linux** | `x64` / `AArch64` | 🚧 Planned | Native bindings in development |
| **macOS (Sonoma+)** | `Apple Silicon` / `x64` | 🚧 Planned | Native bindings in development |

---

## Related Projects

- **[FastTTS](https://github.com/andrestubbe/FastTTS)** — Unified, zero-bloat TTS backend orchestration for Java.
- **[FastSTT](https://github.com/andrestubbe/FastSTT)** — Ultra-fast offline Speech-to-Text inference engine.
- **[FastVAD](https://github.com/andrestubbe/FastVAD)** — Voice Activity Detection with Silero VAD.
- **[FastAudioCapture](https://github.com/andrestubbe/FastAudioCapture)** — Low-latency native WASAPI microphone audio capture.
- **[FastAudioProcess](https://github.com/andrestubbe/FastAudioProcess)** — Multi-track mixing, denoise, and real-time DSP.
- **[FastAudioPlayer](https://github.com/andrestubbe/FastAudioPlayer)** — High-performance native audio playback.
- **[FastCore](https://github.com/andrestubbe/FastCore)** — Native platform loader and low-level runtime infrastructure.
- **[FastSIMD](https://github.com/andrestubbe/FastSIMD)** — Vector hardware acceleration for high-throughput math and DSP.

---

## License

This project is licensed under the [MIT License](LICENSE).  
Part of the **FastJava Ecosystem**.
