package fastvoice;

import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class FastVoiceTest {

    @Test
    void testProfileCreationAndExport() throws Exception {
        float[] dummyEmbedding = new float[]{0.1f, -0.2f, 0.95f, 0.42f};
        byte[] dummyWav = new byte[]{ 'R', 'I', 'F', 'F', 0, 0, 0, 0, 'W', 'A', 'V', 'E' };

        VoiceProfile profile = new VoiceProfile(
                "TestSpeaker",
                "f5tts",
                "de-DE",
                dummyEmbedding,
                dummyWav,
                24000
        );

        Path tempDir = Files.createTempDirectory("fastvoice_test_");
        Path exportedDir = profile.exportToDirectory(tempDir);

        assertTrue(Files.exists(exportedDir));
        assertTrue(Files.exists(exportedDir.resolve("voice.json")));
        assertTrue(Files.exists(exportedDir.resolve("embedding.bin")));
        assertTrue(Files.exists(exportedDir.resolve("reference.wav")));

        String json = Files.readString(exportedDir.resolve("voice.json"));
        assertTrue(json.contains("TestSpeaker"));
        assertTrue(json.contains("f5tts"));
    }
}
