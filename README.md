# FastVoice 0.1.0 — Ultra-Fast Zero-Shot AI Voice Cloning & Voice Conversion Engine for Java

[![Status](https://img.shields.io/badge/status-0.1.0-brightgreen.svg)](https://github.com/andrestubbe/FastVoice)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-21+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Windows%2010+%20%7C%20Linux%20%7C%20macOS-lightgrey.svg)]()
[![JitPack](https://img.shields.io/badge/JitPack-0.1.0-green.svg)](https://jitpack.io/#andrestubbe/FastVoice)

---

**⚡ Hardware-accelerated, zero-shot AI voice cloning and speech-to-speech conversion for the FastJava ecosystem.**

**FastVoice** enables high-fidelity voice cloning in seconds without training heavy neural models. It extracts compact mathematical speaker embeddings (`~2 KB`) from short reference audio samples (3–10s) and performs instant multi-engine voice cloning and conversion for Java applications.

---

## Table of Contents

- [Why FastVoice?](#why-fastvoice)
- [Key Features](#key-features)
- [Architecture Overview](#architecture-overview)
- [Quick Start](#quick-start)
- [API Quick Reference](#api-quick-reference)
- [Installation](#installation)
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

## Quick Start

```java
import fastvoice.FastVoice;
import fastvoice.VoiceProfile;
import java.nio.file.Files;
import java.nio.file.Path;

public class Demo {
    public static void main(String[] args) throws Exception {
        // 1. Create a cloned voice profile from a short audio sample
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

## Technical Demos & Examples

| Case | Java Example | Description |
|---|---|---|
| **Showcase Demo** | [Demo.java](examples/Demo/src/main/java/fastvoice/Demo.java) | Creates a zero-shot voice profile from reference audio, verifies speaker metadata, and exports it to disk. |

---

## API Quick Reference

| Class / Record | Responsibility | Key Methods |
|:---|:---|:---|
| [`FastVoice`](src/main/java/fastvoice/FastVoice.java) | Central facade for backend registration and synthesis | `createProfile()`, `synthesize()`, `convert()`, `registerBackend()` |
| [`VoiceProfile`](src/main/java/fastvoice/VoiceProfile.java) | Immutable container for embeddings and reference audio | `exportToDirectory()`, `embedding()`, `referenceWav()` |
| [`VoiceCloner`](src/main/java/fastvoice/VoiceCloner.java) | Interface for cloning engine implementations | `extractEmbedding()`, `cloneAndSynthesize()`, `convertVoice()` |

*For complete method signatures and details, refer to [REFERENCE.md](docs/REFERENCE.md).*

---

## Installation

### Option 1: Maven (Recommended)

Add JitPack to your `<repositories>` and include the dependency in your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastVoice</artifactId>
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
}
```

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
| **Linux** | `x64` / `AArch64` | ✅ Fully Supported | Standard JVM 21+ runtime |
| **macOS (Sonoma+)** | `Apple Silicon` / `x64` | ✅ Fully Supported | Standard JVM 21+ runtime |

---

## Related Projects

- **[FastTTS](https://github.com/andrestubbe/FastTTS)** — Unified, zero-bloat TTS backend orchestration for Java.
- **[FastAudioProcess](https://github.com/andrestubbe/FastAudioProcess)** — Multi-track mixing, denoise, and real-time DSP.
- **[FastAudioCapture](https://github.com/andrestubbe/FastAudioCapture)** — Low-latency native WASAPI / ALSA microphone audio capture.
- **[FastCore](https://github.com/andrestubbe/FastCore)** — Native platform loader and low-level runtime infrastructure.
- **[FastSIMD](https://github.com/andrestubbe/FastSIMD)** — Vector hardware acceleration for high-throughput math and DSP.

---

## License

This project is licensed under the [MIT License](LICENSE).  
Part of the **FastJava Ecosystem**.
