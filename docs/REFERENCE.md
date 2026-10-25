# FastVoice API Reference Manual

`FastVoice` provides high-performance zero-shot AI voice cloning and speech-to-speech voice conversion for Java 21+.

---

## Table of Contents

- [1. Core Facade API (`FastVoice`)](#1-core-facade-api-fastvoice)
- [2. Voice Profile Record (`VoiceProfile`)](#2-voice-profile-record-voiceprofile)
- [3. Backend Provider Interface (`VoiceCloner`)](#3-backend-provider-interface-voicecloner)

---

## 1. Core Facade API (`FastVoice`)

```java
package fastvoice;
```

The central entry point for voice registration, profile creation, and synthesis.

### `registerBackend`
```java
public static void registerBackend(VoiceCloner cloner)
```
Registers a custom or bundled voice cloning engine.
- **Parameters:** `cloner` — An implementation of the `VoiceCloner` interface.

### `createProfile`
```java
public static VoiceProfile createProfile(String name, String engineId, byte[] referenceAudio) throws Exception
```
Creates a new `VoiceProfile` from reference speech audio. If a registered backend matching `engineId` is present, it extracts acoustic speaker embeddings immediately.
- **Parameters:**
  - `name`: Display and directory identifier for the voice (e.g. `"Andre"`, `"Narrator"`).
  - `engineId`: Cloning engine identifier (e.g. `"f5tts"`, `"cosyvoice"`).
  - `referenceAudio`: Clean reference WAV audio bytes (3–10 seconds recommended).
- **Returns:** An immutable `VoiceProfile`.

### `synthesize`
```java
public static byte[] synthesize(String text, VoiceProfile profile) throws Exception
```
Synthesizes speech from text using the acoustic timbre of the target profile.
- **Parameters:**
  - `text`: Text string to synthesize.
  - `profile`: Cloned voice profile.
- **Returns:** Synthesized audio WAV bytes.

### `convert`
```java
public static byte[] convert(byte[] sourceWav, VoiceProfile targetProfile) throws Exception
```
Performs Speech-to-Speech (STS) conversion, preserving prosody and phonetics while transforming the voice timbre.
- **Parameters:**
  - `sourceWav`: Spoken source WAV audio.
  - `targetProfile`: Target speaker profile.
- **Returns:** Converted audio WAV bytes.

---

## 2. Voice Profile Record (`VoiceProfile`)

```java
public record VoiceProfile(
    String name,
    String engine,
    String language,
    float[] embedding,
    byte[] referenceWav,
    int sampleRate
)
```

Immutable data container storing the speaker's acoustic identity.

### Methods

#### `exportToDirectory`
```java
public Path exportToDirectory(Path targetDirectory) throws Exception
```
Exports the voice profile into a structured directory containing:
1. `voice.json` — Profile metadata (name, engine, sample rate, language).
2. `embedding.bin` — Raw IEEE 754 float32 speaker embedding vector.
3. `reference.wav` — Reference audio sample.

---

## 3. Backend Provider Interface (`VoiceCloner`)

```java
public interface VoiceCloner
```

Contract for voice synthesis and voice conversion backends.

| Method | Description |
|:---|:---|
| `String getEngineId()` | Returns unique engine identifier (e.g. `"f5tts"`). |
| `float[] extractEmbedding(byte[] referenceWavAudio)` | Extracts acoustic vector representation from audio. |
| `byte[] cloneAndSynthesize(String text, VoiceProfile profile)` | Renders text into speech using profile timbre. |
| `byte[] convertVoice(byte[] sourceAudioWav, VoiceProfile targetProfile)` | Transforms source voice into target speaker timbre. |
