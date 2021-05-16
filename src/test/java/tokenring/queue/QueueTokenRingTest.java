package tokenring.queue;

import org.junit.jupiter.api.Test;
import tokenring.TokenRing;
import tokenring.single.SingleTokenRing;

import java.util.Arrays;

class QueueTokenRingTest {
	@Test
	public void queueTest() throws InterruptedException {
		QueueTokenRing tokenRing = new QueueTokenRing(4, 1, x -> { });
		tokenRing.sendTokens(2);
		tokenRing.start();
		Thread.sleep(15000);
		tokenRing.stop();
		System.out.println("queue: " + Arrays.toString(tokenRing.getLatencies()));
		System.out.println("queue: " + Arrays.toString(tokenRing.getThroughput()));
	}

	@Test
	public void singleTest() throws InterruptedException {
		TokenRing tokenRing = new SingleTokenRing(4, x -> { });
		tokenRing.sendTokens(2);
		tokenRing.start();
		Thread.sleep(15000);
		tokenRing.stop();
		System.out.println("single: " + Arrays.toString(tokenRing.getLatencies()));
		System.out.println("single: " + Arrays.toString(tokenRing.getThroughput()));
	}
}