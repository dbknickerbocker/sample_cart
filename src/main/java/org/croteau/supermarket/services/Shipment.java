package org.croteau.supermarket.services;

import java.util.Map;

import org.croteau.supermarket.domain.Item;


public interface Shipment {
	
	public void setUuid(String uuid);
	
	public String getUuid();

	public boolean isProcessed();
	
	public void setProcessed(boolean processed);
	
	public void setItemQuantity(Item item, int itemQuantity);
	
	public int getItemQuantity(Item item);
	
	public void setShipmentDetails(Map<Item, Integer> shipmentDetails);
	
	public Map<Item, Integer> getShipmentDetails();
	
}
