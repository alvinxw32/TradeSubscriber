package xizhen.trade;

import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Processor;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.SubmissionPublisher;
import java.util.function.Function;

/**
 * @author Xizhen Wang
 * 
 * The class stands in between the realtime trade publisher and TradeSubscriber for several reasons:
 * 1. It leverages SubmissionPublisher's buffering feature to achieve asynchronousness and control back pressure. 
 * User can set maxBufferCapacity through the constructor based on her requirement. The default size is 256.
 * 2. It leverages SubmissionPublisher to achieve concurrency for multiple subscribers.
 * 3. It allows user to use her transforming function to sanitize and 'massage' the trade received before delivering 
 * to subscribers.
 *
 */
public class TradeSubscriptionBroker extends SubmissionPublisher<Trade> implements Processor<Trade, Trade> {

	    private Function<Trade, Trade> transformer;
	    private Subscription subscription;

	    public TradeSubscriptionBroker() {
	    	this(Flow.defaultBufferSize(), null);
	    }
	    
	    public TradeSubscriptionBroker(int maxBufferCapacity) {
	    	this(maxBufferCapacity, null);
	    }
	    
	    public TradeSubscriptionBroker(int maxBufferCapacity, Function<Trade, Trade> transformer) {
	    	super(ForkJoinPool.commonPool(), maxBufferCapacity);
	        this.transformer = transformer;
	    }

	    @Override
	    public void onSubscribe(Subscription subscription) {
	        this.subscription = subscription;
	        subscription.request(1);
	    }

	    @Override
	    public void onNext(Trade trade) {
	    	Trade newTrade = transformer == null ? trade : transformer.apply(trade);
	    	if (newTrade != null) { //otherwise the trade is filtered
	    		submit(newTrade);
	    	}
	    	
	        subscription.request(1);
	    }

	    @Override
	    public void onError(Throwable t) {
	    	closeExceptionally(t);
	    }

	    @Override
	    public void onComplete() {
	        close();
	    }
	}