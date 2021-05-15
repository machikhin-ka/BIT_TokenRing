package tokenring;

public interface TokenRing {
	void start();

	void stop();

	void sendTokens(int n);

	void sendToken();

	long[] getLatencies();

	long[] getThroughput();
}
