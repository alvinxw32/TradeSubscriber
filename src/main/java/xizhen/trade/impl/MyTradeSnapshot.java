package xizhen.trade.impl;
import java.util.concurrent.ConcurrentHashMap;

import xizhen.trade.Trade.Status;
import xizhen.trade.Trade;
import xizhen.trade.TradeSnapshot;

public class MyTradeSnapshot implements TradeSnapshot {
	private String symbol;
	private int maxTradeSize = Integer.MIN_VALUE;
	private double avgPrice = 0;
	private double count = 0;
	private ConcurrentHashMap<Status, Integer> statusMap = new ConcurrentHashMap<>();
	
	public MyTradeSnapshot(String symbol) {
		this.symbol = symbol;
	}
	
	@Override
	public synchronized void update(Trade trade) throws Exception {
		if (!symbol.equals(trade.getSymbol())) {
			throw new Exception("Invalid symbol for trade " + trade);
		}
		
		if (maxTradeSize < trade.getSize()) {
			maxTradeSize = trade.getSize();
		}
		
		avgPrice = (avgPrice * count + trade.getPrice()) / (count + 1);
		count++;
		
		Integer num = getStatusCount(trade.getStatus());
		if (num == null) {
			num = 0;
		}
		statusMap.put(trade.getStatus(), num + 1);
	}

	public String getSymbol() {
		return symbol;
	}

	public int getMaxTradeSize() {
		return maxTradeSize;
	}

	public double getAvgPrice() {
		return avgPrice;
	}

	public double getCount() {
		return count;
	}
	
	public Integer getStatusCount(Status status) {
		return statusMap.get(status);
	}
	
	@Override
	public String toString() {
		return symbol + " " + maxTradeSize + " " + avgPrice + " " + count;
	}
}
