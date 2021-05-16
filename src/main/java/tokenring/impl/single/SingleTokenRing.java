package tokenring.impl.single;

import tokenring.Medium;
import tokenring.Token;
import tokenring.TokenRing;
import tokenring.impl.TokenRingImpl;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SingleTokenRing implements TokenRing {
	private final TokenRing tokenRing;

	public SingleTokenRing(int nodeCount, Consumer<Token> consumer) {
		List<Medium> mediums = createMedium(nodeCount);
		tokenRing = new TokenRingImpl(mediums, consumer);
	}

	private List<Medium> createMedium(int nodeCount) {
		return Stream.generate(SingleMedium::new)
				.limit(nodeCount)
				.collect(Collectors.toList());
	}

	@Override
	public void start() {
		tokenRing.start();
	}

	@Override
	public void stop() {
		tokenRing.stop();
	}

	@Override
	public void sendTokens(int n) {
		tokenRing.sendTokens(n);
	}

	@Override
	public void sendToken() {
		tokenRing.sendToken();
	}

	@Override
	public long[] getLatencies() {
		return tokenRing.getLatencies();
	}

	@Override
	public long[] getThroughput() {
		return tokenRing.getThroughput();
	}
}
