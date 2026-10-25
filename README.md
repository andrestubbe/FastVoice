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
import java.nio.file.Path;

public class Demo {
    public static void main(String[] args) throws Exception {
        // 1. Create a cloned voice profile from a 5s audio sample
        byte[] referenceAudioWav = java.nio.file.Files.readAllBytes(Path.of("sample.wav"));
        VoiceProfile profile = FastVoice.createProfile("SpeakerName", "f5tts", referenceAudioWav);

        // 2. Export profile to a target directory
        Path exportDir = Path.of("voices", "clones");
        profile.exportToDirectory(exportDir);
        
        System.out.println("Voice cloned and exported: " + profile.name());
    }
}
```

---

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                 Short Reference Audio (WAV)                 │
│                 (3–10s Clean Voice Sample)                  │
└──────────────────────────────┬──────────────────────────────┘
                               │ FastAudioProcess (Cleanup & Trim)
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

## Features

- ⚡ **Zero-Shot One-Click Cloning**: No lengthy model fine-tuning or GPU hours required. Clones timbre from 3–10s of audio in 1–2 seconds.
- 🪶 **Ultra-Compact Profiles**: Stores embeddings as tiny binary vectors (`~2 KB`), allowing thousands of voices without disk bloat.
- 🔄 **Speech-to-Speech Conversion**: Transform spoken recordings into any target profile's voice.
- 🔌 **Seamless Ecosystem Interop**: Clean, decoupled Java API designed for integration with FastJava audio and speech pipelines.

---

## License

MIT License — Copyright (c) 2026 Andre Stubbe. Part of the **FastJava** Ecosystem.
