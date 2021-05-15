package tokenring.consumers;

import tokenring.Token;

import java.util.function.Consumer;

public class LatencyConsumerDecorator implements Consumer<Token> {
	private final int size = 1000;
	private final long[] latencies = new long[size];
	private final Consumer<Token> consumer;
	private int count;

	public LatencyConsumerDecorator(Consumer<Token> consumer) {
		this.consumer = consumer;
	}

	@Override
	public void accept(Token token) {
		if (token.getSendingStamp() != 0) {
			latencies[count] = System.nanoTime() - token.getSendingStamp();
			count = (count + 1) % size;
		}
		token.setSendingStamp(System.nanoTime());
		consumer.accept(token);
	}

	public long[] getLatencies() {
		return latencies;
	}
}
