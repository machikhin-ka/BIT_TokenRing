package tokenring;

import java.util.function.Consumer;

public class NodeImpl implements Node {
	private final int id;

	private Consumer<Token> tokenHandler;

	private final Medium nextMedium;
	public NodeImpl(int id, Consumer<Token> tokenHandler, Medium nextMedium) {
		this.id = id;
		this.tokenHandler = tokenHandler;
		this.nextMedium = nextMedium;
	}

	@Override
	public void receive(Token token) throws InterruptedException {
		if (token.getRecipientId() == id) {
			token.setSendingStamp(System.nanoTime());
		}
		if (token.getSenderId() == id) {
			tokenHandler.accept(token); //latency
		}
		nextMedium.push(token);
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public Consumer<Token> getConsumer() {
		return tokenHandler;
	}

	public void setConsumer(Consumer<Token> tokenHandler) {
		this.tokenHandler = tokenHandler;
	}
}
