package xizhen.trade.impl;

import java.util.concurrent.ConcurrentHashMap;

import xizhen.trade.Trade;
import xizhen.trade.TradeSnapshotConsumer;

public class MyTradeSnapshotConsumer implements TradeSnapshotConsumer<String, MyTradeSnapshot> {
	private ConcurrentHashMap<String, MyTradeSnapshot> snapshotMap = new ConcurrentHashMap<>(); 
	
	public MyTradeSnapshotConsumer() {
		//we may need to load existing trades into snapshotMap from database or data source using some query,
		//and run updateTradeSnapshot method to rebuild the snapshot
		//Or we may save/load snapshots from database or data source directly. It is business decision what need to be persisted.
	}
	
	@Override
	public void accept(Trade trade) {
		try {	
			String symbol = trade.getSymbol();
			MyTradeSnapshot snapshot = getTradeSnapshot(symbol);
			if (snapshot == null) {
				snapshot = new MyTradeSnapshot(symbol);
				snapshotMap.put(symbol, snapshot);
			}
			
			snapshot.update(trade);
			//we may need to save snapshot or trade to database or do some persistence, in case server is down.
			
			System.out.println("Snapshot: " + snapshot);	
		} catch (Throwable e) {
			System.out.println("Failed while processing trade " + trade);
			e.printStackTrace();			
		} 		
	}

	@Override
	public MyTradeSnapshot getTradeSnapshot(String symbol) {
		return snapshotMap.get(symbol);
	}	
}
