package fastvoice;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <h1>FastVoice</h1>
 * Ultra-Fast Zero-Shot AI Voice Cloning &amp; Voice Conversion Engine for Java.
 * <p>
 * Extracts lightweight speaker embedding fingerprints from 3–10s audio samples
 * and performs real-time voice synthesis and conversion without requiring heavy model training.
 * </p>
 */
public final class FastVoice {

    private static final Map<String, VoiceCloner> BACKENDS = new ConcurrentHashMap<>();

    private FastVoice() {}

    /**
     * Registers a cloning backend engine.
     */
    public static void registerBackend(VoiceCloner cloner) {
        if (cloner != null) {
            BACKENDS.put(cloner.getEngineId().toLowerCase(), cloner);
        }
    }

    /**
     * Creates a new VoiceProfile from a reference WAV file.
     *
     * @param name           Name of the cloned voice (e.g. "Andre", "Narrator")
     * @param engineId       Cloning engine ("f5tts", "cosyvoice", "xtts")
     * @param referenceAudio Raw reference WAV bytes (3-10 seconds of clear speech)
     * @return VoiceProfile ready for immediate synthesis and export
     */
    public static VoiceProfile createProfile(String name, String engineId, byte[] referenceAudio) throws Exception {
        VoiceCloner cloner = BACKENDS.get(engineId.toLowerCase());
        float[] embedding = null;
        if (cloner != null) {
            embedding = cloner.extractEmbedding(referenceAudio);
        }

        return new VoiceProfile(
                name,
                engineId,
                "auto",
                embedding,
                referenceAudio,
                24000
        );
    }

    /**
     * Synthesizes text using a cloned voice profile.
     */
    public static byte[] synthesize(String text, VoiceProfile profile) throws Exception {
        if (profile == null) throw new IllegalArgumentException("Profile cannot be null");
        VoiceCloner cloner = BACKENDS.get(profile.engine().toLowerCase());
        if (cloner == null) {
            throw new IllegalStateException("No backend registered for voice engine: " + profile.engine());
        }
        return cloner.cloneAndSynthesize(text, profile);
    }

    /**
     * Performs Speech-to-Speech voice conversion.
     */
    public static byte[] convert(byte[] sourceWav, VoiceProfile targetProfile) throws Exception {
        if (targetProfile == null) throw new IllegalArgumentException("Target profile cannot be null");
        VoiceCloner cloner = BACKENDS.get(targetProfile.engine().toLowerCase());
        if (cloner == null) {
            throw new IllegalStateException("No backend registered for voice engine: " + targetProfile.engine());
        }
        return cloner.convertVoice(sourceWav, targetProfile);
    }
}
