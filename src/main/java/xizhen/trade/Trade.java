package xizhen.trade;
/**
 * @author Xizhen Wang
 *
 */

public class Trade {
	public enum Status {X, Y, Z};
	
	private long timestamp;
	private String symbol;
	private double price;
	private int size;
	private Status status;

	public Trade(long timestamp, String symbol, double price, int size, Status status) {
		this.timestamp = timestamp;
		this.symbol = symbol;
		this.price = price;
		this.size = size;
		this.status = status;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public String getSymbol() {
		return symbol;
	}

	public double getPrice() {
		return price;
	}

	public int getSize() {
		return size;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
