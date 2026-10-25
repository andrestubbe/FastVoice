package fastvoice;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Immutable profile of a cloned or converted AI voice.
 * Stores lightweight speaker embedding vectors and reference audio.
 */
public record VoiceProfile(
        String name,
        String engine,
        String language,
        float[] embedding,
        byte[] referenceWav,
        int sampleRate
) {
    /**
     * Exports this voice profile directly to the specified directory structure.
     *
     * @param targetDirectory directory where clone subfolder will be created (e.g. voices/clones)
     * @return Path to created profile directory
     */
    public Path exportToDirectory(Path targetDirectory) throws Exception {
        Path voiceDir = targetDirectory.resolve(name);
        Files.createDirectories(voiceDir);

        // 1. Write metadata JSON
        String json = String.format(
                "{\n  \"name\": \"%s\",\n  \"engine\": \"%s\",\n  \"language\": \"%s\",\n  \"sampleRate\": %d\n}\n",
                name, engine, language, sampleRate
        );
        Files.writeString(voiceDir.resolve("voice.json"), json);

        // 2. Write speaker embedding vector binary if present
        if (embedding != null && embedding.length > 0) {
            byte[] embBytes = new byte[embedding.length * 4];
            for (int i = 0; i < embedding.length; i++) {
                int bits = Float.floatToIntBits(embedding[i]);
                embBytes[i * 4] = (byte) (bits & 0xFF);
                embBytes[i * 4 + 1] = (byte) ((bits >> 8) & 0xFF);
                embBytes[i * 4 + 2] = (byte) ((bits >> 16) & 0xFF);
                embBytes[i * 4 + 3] = (byte) ((bits >> 24) & 0xFF);
            }
            Files.write(voiceDir.resolve("embedding.bin"), embBytes);
        }

        // 3. Write reference WAV if present
        if (referenceWav != null && referenceWav.length > 0) {
            Files.write(voiceDir.resolve("reference.wav"), referenceWav);
        }

        return voiceDir;
    }
}
