package org.croteau.supermarket.servicesImpl;

import java.util.Map;
import java.util.UUID;

import org.croteau.supermarket.domain.Item;
import org.croteau.supermarket.services.Shipment;

public class GroceryShipment implements Shipment {

	private String uuid;
	private boolean processed;
	private Map<Item, Integer> shipmentDetails;
	
	public GroceryShipment(){
		this.setUuid(UUID.randomUUID().toString());
	}
	
	public void setUuid(String uuid){
		this.uuid = uuid;
	}
	public String getUuid(){
		return this.uuid;
	}

	
	public boolean isProcessed() {
		return processed;
	}
	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

	
	public void setShipmentDetails(Map<Item, Integer> shipmentDetails) {
		this.shipmentDetails = shipmentDetails;	
	}
	public Map<Item, Integer> getShipmentDetails() {
		return this.shipmentDetails;
	}
	
	
	public void setItemQuantity(Item item, int itemQuantity) {
		this.shipmentDetails.put(item, itemQuantity);
	}
	public int getItemQuantity(Item item) {
		if(this.shipmentDetails.containsKey(item))return this.shipmentDetails.get(item);
		return 0;
	}


}
