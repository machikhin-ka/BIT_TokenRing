package tokenring;

public interface Medium {
	void push(Token token) throws InterruptedException;

	Token pull() throws InterruptedException;
}
