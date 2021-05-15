package tokenring;

public class NodeRunnable implements Runnable {
	private final Node node;
	private final Medium previousMedium;

	public NodeRunnable(Node node, Medium previousMedium) {
		this.node = node;
		this.previousMedium = previousMedium;
	}

	@Override
	public void run() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				Token token = previousMedium.pull();
				node.receive(token);
			}
		} catch (InterruptedException ignore) {
		}
	}
}
