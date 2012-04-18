package org.croteau.supermarket.servicesImpl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.croteau.supermarket.domain.Item;
import org.croteau.supermarket.domain.StoreItem;
import org.croteau.supermarket.domain.StoreSpecial;
import org.croteau.supermarket.domainImpl.GroceryItem;
import org.croteau.supermarket.services.Shipment;
import org.croteau.supermarket.services.Store;

public class GroceryStore implements Store{

	private String storeName;
	private Map<String, StoreSpecial> storeSpecials;
	private Map<String, Map<String, StoreItem>> storeItemsInStock;
	private Map<String, Map<String, Shipment>> storeShipments;
	private Map<String, StoreItem> itemsMap;
	private Map<String, String> reverseLookupMap;
	
	


	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getStoreName() {
		return this.storeName;
	}


	
	
	public void setStoreSpecials(Map<String, StoreSpecial> storeSpecials) {
		this.storeSpecials = storeSpecials;
	}
	public Map<String, StoreSpecial> getStoreSpecials() {
		return this.storeSpecials;
	}

	
	
	
	public Map<String, Map<String, Shipment>> getStoreShipments() {
		return storeShipments;
	}
	public void setStoreShipments(Map<String, Map<String, Shipment>> storeShipments) {
		this.storeShipments = storeShipments;
	}
	
	
	
	
	public void setStoreItemsInStock(Map<String, Map<String, StoreItem>> storeItemsInStock) {
		this.storeItemsInStock = storeItemsInStock;
	}
	public Map<String, Map<String, StoreItem>> getStoreItemsInStock() {
		return this.storeItemsInStock;
	}
	
	
	
	
	public Map<String, String> getReverseLookupMap() {
		return reverseLookupMap;
	}
	public void setReverseLookupMap(Map<String, String> reverseLookupMap) {
		this.reverseLookupMap = reverseLookupMap;
	}

	
	
	


	public void setItemsMap(Map<String, StoreItem> itemsMap) {
		this.itemsMap = itemsMap;
	}
	public Map<String, StoreItem> getItemsMap() {
		return itemsMap;
	}
	
	
	
	
	
	
	public StoreItem getStoreItem(String upc, String uuid) {
		if(this.storeItemsInStock.containsKey(upc)){
			Map<String, StoreItem> storeItemsMapByUpc = this.storeItemsInStock.get(upc);
			if(storeItemsMapByUpc.containsKey(uuid)) return storeItemsMapByUpc.get(uuid);
		}
		return null;
	}
	
	
	
	
	
	public void addStoreItemInStock(StoreItem storeItem) {
		if(this.getStoreItemsInStock() == null){
			this.storeItemsInStock = new HashMap<String, Map<String, StoreItem>>();
		}
		
		if(!this.getStoreItemsInStock().containsKey(storeItem.getUpc())){
			this.getStoreItemsInStock().put(storeItem.getUpc(), new HashMap<String, StoreItem>());
		}

		Map<String, StoreItem> storeItemsMapByUpc = this.getStoreItemsInStock().get(storeItem.getUpc());
		storeItemsMapByUpc.put(storeItem.getUuid(), storeItem);	
	}
	
	
	public void removeStoreItemInStock(StoreItem storeItem) {
		System.out.println("here...");
		System.out.println("remove store item in stock : " + storeItem.getUuid());
		
		if(this.getStoreItemsInStock().containsKey(storeItem.getUpc()) && 
				!this.getStoreItemsInStock().get(storeItem.getUpc()).isEmpty() && 
				this.getStoreItemsInStock().get(storeItem.getUpc()).containsKey(storeItem.getUuid())){
			this.getStoreItemsInStock().get(storeItem.getUpc()).remove(storeItem.getUuid());
		}
	}
	
	

	
	
	public void addStoreSpecial(StoreSpecial storeSpecial) {
		if(this.getStoreSpecials() == null){
			this.storeSpecials = new HashMap<String, StoreSpecial>();
		}
		if(!this.getStoreSpecials().containsKey(storeSpecial.getUuid())){
			this.getStoreSpecials().put(storeSpecial.getUuid(), storeSpecial);
		}
	}
	public void removeStoreSpecial(StoreSpecial storeSpecial) {
		if(this.getStoreSpecials().containsKey(storeSpecial.getUuid())){
			this.getStoreSpecials().remove(storeSpecial.getUuid());
		}
	}

	
	
	

	public void processShipments(){
		
		while(shipmentsNeedProcessing()){
			
			Iterator<Entry<String, Shipment>> shipmentsIterator = this.getStoreShipments().get("unprocessed").entrySet().iterator();
			while(shipmentsIterator.hasNext()){
				
				Map.Entry<String, Shipment> pairs = (Entry<String, Shipment>) shipmentsIterator.next();
				Shipment shipment = pairs.getValue();
				
				if(!shipment.isProcessed()){
					
			    	Iterator<Entry<Item, Integer>> it = shipment.getShipmentDetails().entrySet().iterator();
					while (it.hasNext()) {
						
					    Map.Entry<Item, Integer> shipmentDetail = (Map.Entry<Item, Integer>)it.next();
					    
					    int shipmentQuantity = (Integer) shipmentDetail.getValue();	    
					    Item shipmentItem = shipmentDetail.getKey();
					    
					    stockShipmentItem(shipmentItem, shipmentQuantity);
					    
					    shipment.setProcessed(true);
					    Map<String, Shipment> processedShipment = new HashMap<String, Shipment>();
					    processedShipment.put(shipment.getUuid(), shipment);
					    this.getStoreShipments().put("processed", processedShipment);
					    this.getStoreShipments().get("unprocessed").remove(shipment.getUuid());
	
					}
				}
			}
		}
	}
	
	
	
	
	
	private void stockShipmentItem(Item shipmentItem, int shipmentQuantity){		
	    for(int j = 0; j < shipmentQuantity; j++){
	        GroceryItem groceryItem = new GroceryItem();
		    groceryItem.setUpc(shipmentItem.getUpc());
		    groceryItem.setPrice(shipmentItem.getPrice());
		    groceryItem.setDescription(shipmentItem.getDescription());

	    	System.out.println("stocking item : " + groceryItem.getUpc() + ":" + groceryItem.getUuid() + " " + groceryItem.getDescription());
		    this.addStoreItemInStock(groceryItem);    	
	    }
	}
	
	
	
	
	
	private boolean shipmentsNeedProcessing(){
		
		boolean needsProcessing = false;
		
		Iterator<Entry<String, Shipment>> shipmentsIterator = this.getStoreShipments().get("unprocessed").entrySet().iterator();
		while(shipmentsIterator.hasNext()){
			Map.Entry<String, Shipment> pairs = (Entry<String, Shipment>) shipmentsIterator.next();
			Shipment shipment = pairs.getValue();
			if(!shipment.isProcessed())needsProcessing = true;
		}
			
		return needsProcessing;	
	}
	
	
	public int getCurrentInventoryLevel(String upc){
		int currentQuantity = 0;
		if(this.getStoreItemsInStock().containsKey(upc)){
			currentQuantity = this.getStoreItemsInStock().get(upc).size();
		}
		return currentQuantity;
	}
	
}
