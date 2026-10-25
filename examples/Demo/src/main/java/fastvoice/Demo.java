package fastvoice;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * FastVoice Showcase Demo:
 * Demonstrates creating a zero-shot voice profile from reference audio
 * and exporting it to disk.
 */
public class Demo {
    public static void main(String[] args) throws Exception {
        System.out.println("=== FastVoice Showcase Demo ===");

        // 1. Mock 5-second 24 kHz mono reference audio sample
        byte[] sampleWav = new byte[] {
            'R', 'I', 'F', 'F', 36, 0, 0, 0,
            'W', 'A', 'V', 'E', 'f', 'm', 't', ' ',
            16, 0, 0, 0, 1, 0, 1, 0,
            (byte) 0xC0, 0x5D, 0, 0, (byte) 0x80, (byte) 0xBB, 0, 0,
            2, 0, 16, 0, 'd', 'a', 't', 'a',
            0, 0, 0, 0
        };

        // 2. Create voice profile
        VoiceProfile profile = FastVoice.createProfile("AcousticSpeaker", "f5tts", sampleWav);
        System.out.println("Created VoiceProfile: " + profile.name() + " (Engine: " + profile.engine() + ")");

        // 3. Export to demo directory
        Path exportDir = Path.of("target", "clones");
        Path resultDir = profile.exportToDirectory(exportDir);
        System.out.println("Exported profile directory: " + resultDir.toAbsolutePath());
        System.out.println(" - Metadata:  " + Files.exists(resultDir.resolve("voice.json")));
        System.out.println(" - Reference: " + Files.exists(resultDir.resolve("reference.wav")));

        System.out.println("FastVoice Demo finished successfully!");
    }
}
