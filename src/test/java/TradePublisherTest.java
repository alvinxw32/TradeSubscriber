
import java.util.Random;
import java.util.concurrent.SubmissionPublisher;
import java.util.function.Consumer;

import xizhen.trade.Trade.Status;
import xizhen.trade.TradeSubscriptionBroker;
import xizhen.trade.Trade;
import xizhen.trade.TradeSubscriber;
import xizhen.trade.impl.MyTradeSnapshotConsumer;

import org.junit.jupiter.api.Test;

public class TradePublisherTest {

	@Test
	public void testPublish() throws InterruptedException {

		// Create Publisher. This can be any Publisher implementation for all
		// sorts of data streaming. Here I just use SubmissionPublisher to emulate. 
		SubmissionPublisher<Trade> publisher = new SubmissionPublisher<>();

		TradeSubscriptionBroker queue = new TradeSubscriptionBroker();
		publisher.subscribe(queue);
		
		// Register Subscriber
		int bufferSize = 128;
		Consumer<Trade> processer = new MyTradeSnapshotConsumer();
		TradeSubscriber subs = new TradeSubscriber(bufferSize, processer);
		subs.subscribe(queue);
		
		// Publish 
		Random random = new Random();
		System.out.println("Publishing to Subscriber");
		String s = "ABCD";
		for (int i = 0; i < 1000; i++) {
			Thread.sleep(random.nextInt(10));
			Trade trade = new Trade(System.currentTimeMillis(), 
				s.substring(random.nextInt(s.length())),
				random.nextDouble() * 100,
				random.nextInt(100000),
				Status.values()[random.nextInt(3)]);
			publisher.submit(trade);
		}
		
	    // close the Publisher
	    publisher.close();
	    System.out.println("Exiting the app");
	}	
	
	public static void main(String args[]) throws InterruptedException {
		new TradePublisherTest().testPublish();
	}
}
