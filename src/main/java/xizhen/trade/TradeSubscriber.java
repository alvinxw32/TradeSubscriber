package xizhen.trade;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.function.Consumer;

/**
 * @author Xizhen Wang
 * 
 * This class hides the Subscriber's implementation class as a private inner class to enforce to only work with
 * TradeSubscriptionBroker instead of a generic publisher publishing trades.
 *
 */
public class TradeSubscriber  {
	private InnerSubscriber subscriber = new InnerSubscriber();
	private Consumer<Trade> consumer;
	private int bufferSize;
	
	public TradeSubscriber(int bufferSize, Consumer<Trade> consumer) {
		this.bufferSize = bufferSize;
		this.consumer = consumer;		
	}
	
	public void subscribe(TradeSubscriptionBroker queue) {				
		queue.subscribe(subscriber);
	}
	
	private class InnerSubscriber implements Subscriber<Trade> {
		private Subscription subscription;
		private int count;
		
		@Override
		public void onSubscribe(Subscription subscription) {
			System.out.println("Subscribed");
			if (this.subscription != null) {
				this.subscription.cancel();
			}
			
			this.subscription = subscription;
		    count = bufferSize / 2; 
		    this.subscription.request(count);
			System.out.println("onSubscribe requested " + bufferSize + " items");
		}
	
		@Override
		public void onNext(Trade trade) {
			System.out.println("Processing trade " + trade);	
			if (--count <= 0) {
				count = bufferSize / 2;
				subscription.request(count);
				System.out.println("re-requested " + count + " items");
			}
			
			consumer.accept(trade);
		}
	
		@Override
		public void onError(Throwable e) {
			System.out.println("Some error happened: " + e.getMessage());
			e.printStackTrace();
		}
	
		@Override
		public void onComplete() {
			System.out.println("All Processing Done");
		}
	}
}