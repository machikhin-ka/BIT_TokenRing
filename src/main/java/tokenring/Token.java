package tokenring;

public class Token {
	private long sendingStamp;
	private final int recipientId;
	private final int senderId;

	public Token(int recipientId, int senderId) {
		this.recipientId = recipientId;
		this.senderId = senderId;
	}

	public long getSendingStamp() {
		return sendingStamp;
	}

	public void setSendingStamp(long sendingStamp) {
		this.sendingStamp = sendingStamp;
	}

	public int getRecipientId() {
		return recipientId;
	}

	public int getSenderId() {
		return senderId;
	}
}
