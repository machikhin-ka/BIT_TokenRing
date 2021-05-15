package tokenring.queue;

import tokenring.*;
import tokenring.consumers.LatencyConsumerDecorator;
import tokenring.consumers.ThroughputConsumerDecorator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QueueTokenRing implements TokenRing {
	private final List<Medium> mediums;
	private final List<Node> nodes;
	private final List<Thread> nodeThreads;
	private final Consumer<Token> consumer;
	private int tokenCount;

	public QueueTokenRing(int nodeCount, int capacity, Consumer<Token> consumer) {
		this.consumer = consumer;
		mediums = createMedium(nodeCount, capacity);
		nodes = createNodes();
		nodeThreads = createNodeThreads();
	}

	private List<Medium> createMedium(int nodeCount, int capacity) {
		return Stream.generate(() -> new QueueMedium(capacity))
				.limit(nodeCount)
				.collect(Collectors.toList());
	}

	private List<Node> createNodes() {
		List<Node> nodes = new ArrayList<>(mediums.size());
		for (int i = 0; i < mediums.size(); i++) {
			int nextId = (i + 1) % mediums.size();
			nodes.add(new NodeImpl(i, consumer, mediums.get(nextId)));
		}
		Node node = nodes.get(0);
		Consumer<Token> consumer = node.getConsumer();
		node.setConsumer(new LatencyConsumerDecorator(consumer));
		node = nodes.get(nodes.size() - 1);
		consumer = node.getConsumer();
		node.setConsumer(new ThroughputConsumerDecorator(consumer));
		return nodes;
	}

	private List<Thread> createNodeThreads() {
		return nodes.stream()
				.map(n -> new NodeRunnable(n, mediums.get(n.getId())))
				.map(Thread::new)
				.collect(Collectors.toList());
	}

	@Override
	public void start() {
		nodeThreads.forEach(Thread::start);
	}

	@Override
	public void stop() {
		nodeThreads.stream()
				.peek(Thread::interrupt)
				.forEach(thread -> {
					try {
						thread.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				});
	}

	@Override
	public void sendTokens(int n) {
		for (int i = 0; i < n; i++) {
			sendToken();
		}
	}

	@Override
	public void sendToken() {
		try {
			int from = tokenCount;
			mediums.get(from).push(new Token());
			tokenCount = (tokenCount + 1) % mediums.size();
		} catch (InterruptedException ignore) {
		}
	}


	@Override
	public long[] getLatencies() {
		LatencyConsumerDecorator consumer = (LatencyConsumerDecorator) nodes.get(0).getConsumer();
		return consumer.getLatencies();
	}

	@Override
	public long[] getThroughput() {
		ThroughputConsumerDecorator consumer = (ThroughputConsumerDecorator) nodes.get(nodes.size() - 1).getConsumer();
		return consumer.getThroughputs();
	}
}
