package org.croteau.supermarket.services;

import java.util.Map;

import org.croteau.supermarket.domain.StoreItem;
import org.croteau.supermarket.domain.StoreSpecial;

public interface Store {
	
	public void setStoreName(String storeName);
	
	public String getStoreName();
	
	public void addStoreSpecial(StoreSpecial storeSpecial);
	
	public void removeStoreSpecial(StoreSpecial storeSpecial);	

	public void setStoreSpecials(Map<String, StoreSpecial> storeSpecials);
	
	public Map<String, StoreSpecial> getStoreSpecials();
	
	public void setStoreItemsInStock(Map<String, Map<String, StoreItem>> storeItemsInStock);
	
	public Map<String, Map<String, StoreItem>> getStoreItemsInStock();
	
	public StoreItem getStoreItem(String upc, String uuid);
	
	public void addStoreItemInStock(StoreItem storeItem);
	
	public void removeStoreItemInStock(StoreItem storeItem);
	
	public void setItemsMap(Map<String, StoreItem> itemsMap);
	
	public Map<String, StoreItem> getItemsMap();	
	
	public Map<String, String> getReverseLookupMap();
	
	public void setReverseLookupMap(Map<String, String> reverseLookupMap);
		
	public Map<String, Map<String, Shipment>> getStoreShipments() ;
	
	public void setStoreShipments(Map<String, Map<String, Shipment>> storeShipments);
	
	public void processShipments();
	
	public int getCurrentInventoryLevel(String upc);
	
}
