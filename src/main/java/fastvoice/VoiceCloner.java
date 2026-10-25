package fastvoice;

/**
 * Interface for AI voice cloning and speech-to-speech conversion backends.
 */
public interface VoiceCloner {

    /**
     * Unique identifier for this cloning engine (e.g. "f5tts", "cosyvoice", "xtts").
     */
    String getEngineId();

    /**
     * Extracts an acoustic speaker embedding vector from a short audio sample.
     */
    float[] extractEmbedding(byte[] referenceWavAudio) throws Exception;

    /**
     * Synthesizes new speech with the cloned voice timbre.
     */
    byte[] cloneAndSynthesize(String text, VoiceProfile profile) throws Exception;

    /**
     * Converts source spoken audio into the target speaker's voice (Speech-to-Speech).
     */
    byte[] convertVoice(byte[] sourceAudioWav, VoiceProfile targetProfile) throws Exception;
}
