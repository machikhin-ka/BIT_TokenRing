package tokenring.impl.single;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tokenring.Token;
import tokenring.TokenRing;
import tokenring.impl.shared.Load;

import java.nio.file.Path;
import java.util.function.Consumer;

import static tokenring.impl.shared.Utils.createFile;
import static tokenring.impl.shared.Utils.getRelativeLoads;

class NodesBoundTest {
    private static final Path pathLatency =
            Path.of("src", "test", "resources", "single", "LatencyForNodesSize.csv");
    private static final Path pathThroughput =
            Path.of("src", "test", "resources", "single", "ThroughputForNodesSize.csv");
    private final static Consumer<Token> emptyConsumer = x -> {
    };

    @BeforeAll
    static void warpUp() throws InterruptedException {
        System.out.println("Warm up is started");
        SingleTokenRing tokenRing = new SingleTokenRing(2, emptyConsumer);
        tokenRing.sendTokens(2);
        tokenRing.start();
        Thread.sleep(2000);
        tokenRing.stop();
    }

    @Test
    void latenciesAndThroughputForNodesSize() throws InterruptedException {
        StringBuilder sbLatency = new StringBuilder("nodes,latency\n");
        StringBuilder sbThroughput = new StringBuilder("nodes,throughput\n");

        for (int size = 2; size <= 8; size++) {
            Load[] relativeLoads = getRelativeLoads(size);
            for (Load load : relativeLoads) {
                TokenRing tokenRing = new SingleTokenRing(size, emptyConsumer);
                tokenRing.sendTokens(load.getLength());
                tokenRing.start();
                Thread.sleep(13000);
                tokenRing.stop();
                long[] latencies = tokenRing.getLatencies();
                long[] throughput = tokenRing.getThroughput();
                addData(sbLatency, latencies, size, load.getRelativeLoad());
                addData(sbThroughput, throughput, size, load.getRelativeLoad());
            }
        }
        createFile(sbLatency, pathLatency);
        createFile(sbThroughput, pathThroughput);
    }

    private void addData(StringBuilder sb, long[] data, int nodesSize, Load.LoadLevel level) {
        for (long d : data) {
            sb.append(nodesSize).append(level.toString()).append(",").append(d).append("\n");
        }
    }
}