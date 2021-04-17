package xizhen.trade;
import java.util.function.Consumer;

public interface TradeSnapshotConsumer<T, K extends TradeSnapshot> extends Consumer<Trade> {

	/** 
	 * Allows implementation class to implement different query condition (i.e. not just symbol) 
	 * and retrieve different kind of trade snapshot
	 * @param condition
	 * @return
	 */
	public K getTradeSnapshot(T condition);

}