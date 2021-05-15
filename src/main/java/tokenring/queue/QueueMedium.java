package tokenring.queue;

import tokenring.Medium;
import tokenring.Token;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class QueueMedium implements Medium {
	private final BlockingQueue<Token> queue;

	public QueueMedium(int capacity) {
		this.queue = new ArrayBlockingQueue<>(capacity);
	}

	@Override
	public void push(Token token) throws InterruptedException {
		queue.put(token);
	}

	@Override
	public Token pull() throws InterruptedException {
		return queue.take();
	}
}
