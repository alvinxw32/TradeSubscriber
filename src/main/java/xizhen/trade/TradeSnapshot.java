package xizhen.trade;
/**
 * @author Xizhen Wang
 * 
 * Different subscriber can manage different TradeSnapshot or one can manage multiple. 
 * For each "question", we can implement an individual TradeSnapshot subclass, 
 * or we can implement a TradeSnapshot subclass to encapsulate multiple "questions".
 * Or a TradeSnapshot subclass can encapsulate a collection of TradeSnapshot subclass objects, as a composite.
 *
 */
public interface TradeSnapshot {

	/**
	 * Update TradeSnapshot with a trade.
	 * @param trade
	 * @throws Exception
	 */
	public void update(Trade trade) throws Exception;

}