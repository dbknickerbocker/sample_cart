package org.croteau.supermarket.servicesImpl;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.croteau.supermarket.domain.StoreItem;
import org.croteau.supermarket.domain.StoreSpecial;
import org.croteau.supermarket.services.ShoppingCart;
import org.croteau.supermarket.services.Store;

public class GroceryShoppingCart implements ShoppingCart{

	private Store store;
	private Map<String, Map<String, StoreItem>> shoppingCartItems;
	
	public GroceryShoppingCart(Store store){
		this.setStore(store);
	}
	
	public void setStore(Store store) {
		this.store = store;
	}

	public Store getStore() {
		return this.store;
	}
	
	public void setShoppingCartItems(Map<String, Map<String, StoreItem>> shoppingCartItems) {
		this.shoppingCartItems = shoppingCartItems;
	}

	public Map<String, Map<String, StoreItem>> getShoppingCartItems() {
		return this.shoppingCartItems;
	}
	
	public void addItemToCart(StoreItem storeItem) {
		if(this.getShoppingCartItems() == null){
			this.shoppingCartItems = new HashMap<String, Map<String, StoreItem>>();
		}
		
		if(!this.getShoppingCartItems().containsKey(storeItem.getUpc())){
			this.getShoppingCartItems().put(storeItem.getUpc(), new HashMap<String, StoreItem>());
//			checkApplyShoppingSpecials();
		}
		Map<String, StoreItem> storeItemsMapByUpc = this.getShoppingCartItems().get(storeItem.getUpc());
		storeItemsMapByUpc.put(storeItem.getUuid(), storeItem);	
		this.getShoppingCartItems().put(storeItem.getUpc(), storeItemsMapByUpc);
	}
	
	
	public void removeItemFromCart(StoreItem storeItem) {
		
		if(this.getShoppingCartItems().containsKey(storeItem.getUpc()) && 
				this.getShoppingCartItems().get(storeItem.getUpc()).containsKey(storeItem.getUuid())){
			
			this.getShoppingCartItems().get(storeItem.getUpc()).remove(storeItem.getUuid());

			if(this.getShoppingCartItems().get(storeItem.getUpc()).size() == 0){
				this.getShoppingCartItems().remove(storeItem.getUpc());
			}
		}
	}



	

	public double calculateShoppingCartTotalAfterSpecials() {
		
		Iterator<Entry<String, StoreSpecial>> storeSpecialsIterator = this.getStore().getStoreSpecials().entrySet().iterator();
		while( storeSpecialsIterator.hasNext() ){
			Map.Entry<String, StoreSpecial> pairs = (Map.Entry<String, StoreSpecial>)storeSpecialsIterator.next();
			StoreSpecial special = pairs.getValue();
			special.applySpecialGetUpdatedCart(this);
		}
		
		
		double total = 0;
		Iterator<Entry<String, Map<String, StoreItem>>> scanningCartIterator = this.getShoppingCartItems().entrySet().iterator();
		while (scanningCartIterator.hasNext()) {
		    Map.Entry<String, Map<String, StoreItem>> pairs = (Map.Entry<String, Map<String, StoreItem>>)scanningCartIterator.next();
		    Map<String, StoreItem> storeItemsMap = (Map<String, StoreItem>) pairs.getValue();
		    
		    Iterator<Entry<String, StoreItem>> storeItemsIterator = storeItemsMap.entrySet().iterator();
		    while(storeItemsIterator.hasNext()){

			    Map.Entry<String, StoreItem> storeItemPair = (Map.Entry<String, StoreItem>)storeItemsIterator.next();
			    StoreItem storeItem = (StoreItem) storeItemPair.getValue();
			    
			    total+=storeItem.getPurchasePrice();			    
		    }
		}
		
		DecimalFormat newFormat = new DecimalFormat("#.##");
		return Double.valueOf(newFormat.format(total));	
	
	}
	
	
	
	
	

	public double calculateShoppingCartTotalPreSpecials() {
		

		double totalPreSpecials = 0;
		
		Iterator<Entry<String, Map<String, StoreItem>>> scanningCartIterator = this.getShoppingCartItems().entrySet().iterator();
		while (scanningCartIterator.hasNext()) {
		    Map.Entry<String, Map<String, StoreItem>> pairs = (Map.Entry<String, Map<String, StoreItem>>)scanningCartIterator.next();
		    Map<String, StoreItem> storeItemsMap = (Map<String, StoreItem>) pairs.getValue();
		    
		    Iterator<Entry<String, StoreItem>> storeItemsIterator = storeItemsMap.entrySet().iterator();
		    while(storeItemsIterator.hasNext()){

			    Map.Entry<String, StoreItem> storeItemPair = (Map.Entry<String, StoreItem>)storeItemsIterator.next();
			    StoreItem storeItem = (StoreItem) storeItemPair.getValue();
			    
			    totalPreSpecials+=storeItem.getPrice();
			    
		    }
	
		}
		DecimalFormat newFormat = new DecimalFormat("#.##");
		return Double.valueOf(newFormat.format(totalPreSpecials));	
	}	
	
	
	
	
	
	public double calculateSavings(){
		double totalPreSpecials = calculateShoppingCartTotalPreSpecials();
		double totalAfterSpecials = calculateShoppingCartTotalAfterSpecials();
		double savings = totalPreSpecials - totalAfterSpecials;

		DecimalFormat newFormat = new DecimalFormat("#.##");
		return Double.valueOf(newFormat.format(savings));	
	}
	
	
	
	public String toString(){
		String returnString = "";
		Iterator<Entry<String, Map<String, StoreItem>>> it = this.shoppingCartItems.entrySet().iterator();
		while (it.hasNext()) {
		    Map.Entry<String, Map<String, StoreItem>> pairs = (Map.Entry<String, Map<String, StoreItem>>)it.next();
		    returnString+= pairs.getKey() + " : " + getTotalItemsCount((Map<String, StoreItem>) pairs.getValue()) + "\n";
		  	
		}
		return returnString;
	}
	
	

	public int getTotalItemsCount(Map<String, StoreItem> itemsMap){
		int count = 0;
		Iterator<Entry<String, StoreItem>> it = itemsMap.entrySet().iterator();
		while (it.hasNext()) {
		    it.next();
			count++;
		}
		return count;
	}
	
	

	
	
	public int getCurrentItemQuantity(String upc){
		int currentQuantity = 0;
		if(null != this.getShoppingCartItems()  && 
				this.getShoppingCartItems().containsKey(upc)){
			currentQuantity = this.getShoppingCartItems().get(upc).size();
		}
		return currentQuantity;
	}
	
}
