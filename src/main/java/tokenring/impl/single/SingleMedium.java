package tokenring.impl.single;

import tokenring.Medium;
import tokenring.Token;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SingleMedium implements Medium {
	private final Lock lock = new ReentrantLock();
	private final Condition condition = lock.newCondition();
	private Token token;

	@Override
	public void push(Token token) throws InterruptedException {
		lock.lock();
		try {
			while (this.token != null) {
				condition.await();
			}
			this.token = token;
			condition.signal();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public Token pull() throws InterruptedException {
		lock.lock();
		try {
			while (this.token == null) {
				condition.await();
			}
			Token tmp = token;
			token = null;
			condition.signal();
			return tmp;
		} finally {
			lock.unlock();
		}
	}
}
