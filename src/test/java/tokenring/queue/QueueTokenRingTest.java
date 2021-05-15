package tokenring.queue;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

class QueueTokenRingTest {
	@Test
	public void test() throws InterruptedException {
		QueueTokenRing tokenRing = new QueueTokenRing(4, 10, x -> { });
		tokenRing.sendTokens(38);
		tokenRing.start();
		Thread.sleep(15000);
		tokenRing.stop();
		System.out.println(Arrays.toString(tokenRing.getLatencies()));
		System.out.println(Arrays.toString(tokenRing.getThroughput()));
	}
}