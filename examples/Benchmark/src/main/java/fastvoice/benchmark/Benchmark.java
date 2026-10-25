package fastvoice.benchmark;

import fastvoice.FastVoice;
import fastvoice.VoiceProfile;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 2, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class Benchmark {

    private byte[] sampleWav;
    private VoiceProfile profile;
    private Path tempDir;

    @Setup(Level.Trial)
    public void setup() throws Exception {
        sampleWav = new byte[24000 * 2]; // 1 second of 24kHz 16-bit PCM
        float[] embedding = new float[512];
        for (int i = 0; i < embedding.length; i++) {
            embedding[i] = (float) Math.sin(i);
        }
        profile = new VoiceProfile("BenchmarkVoice", "f5tts", "de-DE", embedding, sampleWav, 24000);
        tempDir = Files.createTempDirectory("fastvoice_jmh_");
    }

    @TearDown(Level.Trial)
    public void tearDown() throws IOException {
        // cleanup temp directory
    }

    @org.openjdk.jmh.annotations.Benchmark
    public VoiceProfile benchmarkProfileCreation() throws Exception {
        return FastVoice.createProfile("AcousticVoice", "f5tts", sampleWav);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public Path benchmarkProfileExport() throws Exception {
        return profile.exportToDirectory(tempDir);
    }

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }
}
