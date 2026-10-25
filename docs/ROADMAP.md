# FastVoice Roadmap

## Milestone 0.1.x (Current - Core Facade & Profile Standard)
- [x] Immutable `VoiceProfile` record standard with IEEE 754 float32 vector serialization.
- [x] Directory-based voice profile export (`voice.json`, `embedding.bin`, `reference.wav`).
- [x] Unified facade (`FastVoice`) and backend interface (`VoiceCloner`).
- [x] Full integration into the FastJava ecosystem (`FastCore`, `FastAudioProcess`, `FastSIMD`).

---

## Milestone 0.2.x (Native ONNX Embedders & Backends)
- [ ] Bundled ONNX acoustic embedder (ECAPA-TDNN / CAMPPlus) via ONNX Runtime / `FastCore`.
- [ ] Native F5-TTS and CosyVoice client orchestration.
- [ ] Cosine similarity and timbre distance calculator between voice profiles.

---

## Milestone 0.3.x (Real-Time Streaming Voice Conversion)
- [ ] Chunked low-latency Speech-to-Speech conversion pipeline.
- [ ] Direct integration with `FastAudioCapture` and `FastAudioPlayer`.
