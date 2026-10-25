# FastVoice Changelog

All notable changes to **FastVoice** are documented in this file.
The project adheres to [Semantic Versioning](https://semver.org/).

---

## [0.1.0] - 2026-10-25

### Added
- **Core Architecture**:
  - `FastVoice`: Central entry point and facade managing cloning backends, profile creation, speech synthesis, and speech-to-speech conversion.
  - `VoiceCloner`: Extensible backend interface supporting modular cloning engines (e.g. F5-TTS, CosyVoice, XTTS).
  - `VoiceProfile`: Immutable record holding voice metadata, acoustic speaker embeddings, and reference audio with built-in export capabilities.
- **Profile Persistence**:
  - Direct export of voice profiles (`voice.json`, `embedding.bin`, `reference.wav`) for immediate reuse across FastJava speech pipelines.
- **Ecosystem Integration**:
  - Direct compatibility with `FastAudioProcess` (preprocessing and normalization) and `FastSIMD` (hardware vector acceleration).
