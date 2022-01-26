package com.util;

import java.util.List;

import org.apache.commons.lang3.SerializationUtils;

import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventDataBatch;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubProducerClient;

import domain.Settlement;

public class EventHubsUtil {

	private static final String eventHubsconnectionString = "<Enter EH Conn String>";
	private static final String eventHubName = "<Enter EH Topic>";

	public static void sendMsgsToEventHubs(List<Settlement> settlements) {
		
		String evthubConnStr = System.getProperty("eventHubsconnectionString", eventHubsconnectionString);
		String evthubName = System.getProperty(eventHubName, eventHubName);

		EventHubProducerClient producer = null;
		try {
			producer = new EventHubClientBuilder().connectionString(evthubConnStr, evthubName)
					.buildProducerClient();

			// create a batch
			EventDataBatch eventDataBatch = producer.createBatch();

			for (Settlement settlement : settlements) {
				
				byte[] data = SerializationUtils.serialize(settlement);
				EventData eventData = new EventData(data);

				// try to add the event from the array to the batch
				if (!eventDataBatch.tryAdd(eventData)) {
					// if the batch is full, send it and then create a new batch
					producer.send(eventDataBatch);
					eventDataBatch = producer.createBatch();

					// Try to add that event that couldn't fit before.
					if (!eventDataBatch.tryAdd(eventData)) {
						throw new IllegalArgumentException("Event is too large for an empty batch. Max size: "
								+ eventDataBatch.getMaxSizeInBytes());
					}
				}
			}
			// send the last batch of remaining events
			if (eventDataBatch.getCount() > 0) {
				producer.send(eventDataBatch);
				System.out.println("Sent " + eventDataBatch.getCount() + " records to Event Hub");
			}

		} catch (Exception e) {
			
			System.out.println("Exception: " + e);

		} finally {
			if (producer != null)
				producer.close();
		}

	}

}
