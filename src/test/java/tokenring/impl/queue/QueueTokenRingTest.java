package tokenring.impl.queue;

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

class QueueTokenRingTest {
    private static final Path pathCompareLatency =
            Path.of("src", "test", "resources", "queue", "LatencyCompareWithSingle.csv");
    private static final Path pathCompareThroughput =
            Path.of("src", "test", "resources", "queue", "ThroughputCompareWithSingle.csv");
    private static final Path pathCapacityRelativeLatency =
            Path.of("src", "test", "resources", "queue", "LatencyRelativeCapacity.csv");
    private static final Path pathCapacityRelativeThroughput =
            Path.of("src", "test", "resources", "queue", "ThroughputRelativeCapacity.csv");
    private static final Path pathCapacityAbsoluteLatency =
            Path.of("src", "test", "resources", "queue", "maxthroughput", "LatencyAbsoluteCapacity30_.csv");
    private static final Path pathCapacityAbsoluteThroughput =
            Path.of("src", "test", "resources", "queue", "maxthroughput", "ThroughputAbsoluteCapacity30_.csv");
    private final static Consumer<Token> empty = x -> {
    };

    @BeforeAll
    static void warpUp() throws InterruptedException {
        System.out.println("Warm up is started");
        TokenRing tokenRing = new QueueTokenRing(2, 1, empty);
        tokenRing.sendTokens(2);
        tokenRing.start();
        Thread.sleep(5000);
        tokenRing.stop();
    }

    @Test
    public void testForCompareWithSingle() throws InterruptedException {
        StringBuilder sbLatency = new StringBuilder("nodes,latency\n");
        StringBuilder sbThroughput = new StringBuilder("nodes,throughput\n");

        for (int size = 2; size < 5; size++) { //todo 8 and 16, 24 (32)
            Load[] relativeLoads = getRelativeLoads(size);
            for (Load load : relativeLoads) {
                TokenRing tokenRing = new QueueTokenRing(size, 2, empty);
                tokenRing.sendTokens(load.getLength());
                tokenRing.start();
                Thread.sleep(11000);
                tokenRing.stop();
                long[] latencies = tokenRing.getLatencies();
                long[] throughput = tokenRing.getThroughput();
                addData(sbLatency, size, load.getRelativeLoad(), latencies);
                addData(sbThroughput, size, load.getRelativeLoad(), throughput);
            }
            createFile(sbLatency, pathCompareLatency);
            createFile(sbThroughput, pathCompareThroughput);
        }
    }

    @Test
    public void additionTestForCompareWithSingle() throws InterruptedException {
        StringBuilder sbLatency = new StringBuilder("nodes,latency\n");
        StringBuilder sbThroughput = new StringBuilder("nodes,throughput\n");

        for (int size: List.of(8, 16, 24)) { //todo 8 and 16, 24 (32)
            Load[] relativeLoads = getRelativeLoads(size);
            for (Load load : relativeLoads) {
                TokenRing tokenRing = new QueueTokenRing(size, 2, empty);
                tokenRing.sendTokens(load.getLength());
                tokenRing.start();
                Thread.sleep(11000);
                tokenRing.stop();
                long[] latencies = tokenRing.getLatencies();
                long[] throughput = tokenRing.getThroughput();
                addData(sbLatency, size, load.getRelativeLoad(), latencies);
                addData(sbThroughput, size, load.getRelativeLoad(), throughput);
            }
            createFile(sbLatency, pathCompareLatency);
            createFile(sbThroughput, pathCompareThroughput);
        }
    }

    @Test
    public void testRelativeLoadWithDifferentCapacity() throws InterruptedException {
        StringBuilder sbLatency = new StringBuilder("node_size,capacity,load,latency\n");
        StringBuilder sbThroughput = new StringBuilder("node_size,capacity,load,throughput\n");

        for (int capacity : List.of(5, 10, 15)) {
            for (int size = 2; size < 5; size++) {
                Load[] relativeLoads = getRelativeLoads(size * capacity);
                for (Load load : relativeLoads) {
                    TokenRing tokenRing = new QueueTokenRing(size, capacity, empty);
                    tokenRing.sendTokens(load.getLength());
                    tokenRing.start();
                    Thread.sleep(11000);
                    tokenRing.stop();
                    long[] latencies = tokenRing.getLatencies();
                    long[] throughput = tokenRing.getThroughput();
                    addData(sbLatency, size, capacity, load.getRelativeLoad(), latencies);
                    addData(sbThroughput, size, capacity, load.getRelativeLoad(), throughput);
                }
            }
            createFile(sbLatency, pathCapacityRelativeLatency);
            createFile(sbThroughput, pathCapacityRelativeThroughput);
        }
    }

    @Test
    public void testAbsoluteLoadWithDifferentCapacity() throws InterruptedException {
        StringBuilder sbLatency = new StringBuilder("capacity|tokenNum,latency\n");
        StringBuilder sbThroughput = new StringBuilder("capacity|tokenNum,throughput\n");
        int size = 4;

        for (int capacity : List.of(150)) {
            for (int tokenNum : List.of(5, 15, 25, 35, 45, 60)) {
                if (capacity * size < tokenNum) break;
                TokenRing tokenRing = new QueueTokenRing(size, capacity, empty);
                tokenRing.sendTokens(tokenNum);
                tokenRing.start();
                Thread.sleep(11000);
                tokenRing.stop();
                long[] latencies = tokenRing.getLatencies();
                long[] throughput = tokenRing.getThroughput();
                addData(sbLatency, capacity, tokenNum, latencies);
                addData(sbThroughput, capacity, tokenNum, throughput);
            }
            createFile(sbLatency, pathCapacityAbsoluteLatency);
            createFile(sbThroughput, pathCapacityAbsoluteThroughput);
        }
    }

    private void addData(StringBuilder sb, int arg1, Load.LoadLevel arg2, long[] data) {
        for (long d : data) {
            sb.append(arg1)
                    .append(arg2).append(",")
                    .append(d).append("\n");
        }
    }

    private void addData(StringBuilder sb, int arg1, int arg2, long[] data) {
        String prefix = arg1 + "|" + arg2 + ",";
        for (long d : data) {
            sb.append(prefix)
                    .append(d).append("\n");
        }
    }

    private void addData(StringBuilder sb, int arg1, int arg2, Load.LoadLevel relativeLoad, long[] data) {
        String prefix = arg1 + "|" + arg2 + "|" + relativeLoad + ",";
        for (long d : data) {
            sb.append(prefix)
                    .append(d).append("\n");
        }
    }
}