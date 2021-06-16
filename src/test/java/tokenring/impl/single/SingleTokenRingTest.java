package tokenring.impl.single;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tokenring.Token;
import tokenring.TokenRing;
import tokenring.impl.shared.Load;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

import static tokenring.impl.shared.Utils.createFile;
import static tokenring.impl.shared.Utils.getRelativeLoads;

class SingleTokenRingTest {
    private static final Path pathLatency =
            Path.of("src", "test", "resources", "single", "LatencyForNodesSizeWithLoad.csv");
    private static final Path pathThroughput =
            Path.of("src", "test", "resources", "single", "ThroughputForNodesSizeWithLoad.csv");
    private final static Consumer<Token> empty = x -> {
    };

    @BeforeAll
    static void warpUp() throws InterruptedException {
        System.out.println("Warm up is started");
        SingleTokenRing tokenRing = new SingleTokenRing(2, empty);
        tokenRing.sendTokens(2);
        tokenRing.start();
        Thread.sleep(2000);
        tokenRing.stop();
    }

    @Test
    void testLatencyAndThroughputForDifferentNumberOfNodes() throws InterruptedException {
        StringBuilder sbLatency = new StringBuilder("node_size,load,latency\n");
        StringBuilder sbThroughput = new StringBuilder("node_size,load,throughput\n");

        for (int size = 2; size < 5; size++) { //todo 8, 16, 32(24)
            Load[] relativeLoads = getRelativeLoads(size);
            for (Load load : relativeLoads) {
                TokenRing tokenRing = new SingleTokenRing(size, empty);
                tokenRing.sendTokens(load.getLength());
                tokenRing.start();
                Thread.sleep(13000);
                tokenRing.stop();
                long[] latencies = tokenRing.getLatencies();
                long[] throughput = tokenRing.getThroughput();
                addData(sbLatency, latencies, size, load.getRelativeLoad());
                addData(sbThroughput, throughput, size, load.getRelativeLoad());
            }
            createFile(sbLatency, pathLatency);
            createFile(sbThroughput, pathThroughput);
        }
    }

    @Test
    void additionTestLatencyAndThroughputForDifferentNumberOfNodes() throws InterruptedException {
        StringBuilder sbLatency = new StringBuilder("node_size,load,latency\n");
        StringBuilder sbThroughput = new StringBuilder("node_size,load,throughput\n");

        for (int size : List.of(8, 16, 24)) { //todo 8, 16, 32(24)
            Load[] relativeLoads = getRelativeLoads(size);
            for (Load load : relativeLoads) {
                TokenRing tokenRing = new SingleTokenRing(size, empty);
                tokenRing.sendTokens(load.getLength());
                tokenRing.start();
                Thread.sleep(13000);
                tokenRing.stop();
                long[] latencies = tokenRing.getLatencies();
                long[] throughput = tokenRing.getThroughput();
                addData(sbLatency, latencies, size, load.getRelativeLoad());
                addData(sbThroughput, throughput, size, load.getRelativeLoad());
            }
            createFile(sbLatency, pathLatency);
            createFile(sbThroughput, pathThroughput);
        }
    }

    private void addData(StringBuilder sb, long[] data, int size, Load.LoadLevel relativeLoad) {
        for (long d : data) {
            sb.append(size).append(relativeLoad).append(",").append(d).append("\n");
        }
    }
}