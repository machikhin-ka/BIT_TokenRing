package tokenring.impl.consumers;

import tokenring.Token;

import java.util.function.Consumer;

public class ThroughputConsumerDecorator implements Consumer<Token> {
	private final int size = 10;
	private final long[] throughputs = new long[size];
	private final Consumer<Token> consumer;
	private int throughputCount;
	private long timestamp;
	private long count;


	public ThroughputConsumerDecorator(Consumer<Token> consumer) {
		this.consumer = consumer;
	}

	@Override
	public void accept(Token token) {
		consumer.accept(token);
		if (timestamp == 0) {
			timestamp = System.nanoTime();
		}
		if (System.nanoTime() - timestamp >= 1_000_000_000) {
			throughputs[throughputCount] = count;
			throughputCount = (throughputCount + 1) % size;
			count = 0;
			timestamp = System.nanoTime();
		} else {
			count++;
		}
	}

	public long[] getThroughputs() {
		return throughputs;
	}
}
