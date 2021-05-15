package tokenring;

import java.util.function.Consumer;

public interface Node {
	void receive(Token token) throws InterruptedException;

	int getId();

	Consumer<Token> getConsumer();

	void setConsumer(Consumer<Token> consumer);
}
