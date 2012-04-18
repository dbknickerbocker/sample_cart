package org.croteau.supermarket.domainImpl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.croteau.supermarket.domain.Item;
import org.croteau.supermarket.domain.StoreItem;
import org.croteau.supermarket.domain.StoreSpecial;
import org.croteau.supermarket.services.ShoppingCart;

public class BuyXGetYFree implements StoreSpecial{

	private String uuid;
	private Item item;
	private int buyQuantity;
	private int freeQuantity;
	private String description;
	
	public BuyXGetYFree(){
		this.uuid = UUID.randomUUID().toString();
	}
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	

	public Item getItem() {
		return this.item;
	}
	public void setItem(Item item) {
		this.item = item;
	}

	
	public int getBuyQuantity() {
		return buyQuantity;
	}
	public void setBuyQuantity(int buyQuantity) {
		this.buyQuantity = buyQuantity;
	}
	
	
	
	public int getFreeQuantity() {
		return freeQuantity;
	}
	public void setFreeQuantity(int freeQuantity) {
		this.freeQuantity = freeQuantity;
	}
	
	

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	public ShoppingCart applySpecialGetUpdatedCart(ShoppingCart shoppingCart) {

		String upc = this.getItem().getUpc();		
		if(shoppingCart.getShoppingCartItems().containsKey(upc)){
			
			int totalItemsPurchasedCount = getTotalItemsPurchasedCount(shoppingCart.getShoppingCartItems().get(upc));			
			int totalQualifyingItemCount = this.getBuyQuantity() + this.getFreeQuantity();
			
			
			double specialPrice = calculateSpecialPrice(totalQualifyingItemCount);
			
			Map<String, StoreItem> specialItemsMap = shoppingCart.getShoppingCartItems().get(this.getItem().getUpc());
			resetSpecialPricing(specialItemsMap, shoppingCart);
			
			if(totalItemsPurchasedCount >= totalQualifyingItemCount){
				List<String> qualifyingList = getItemsThatQualify(specialItemsMap, totalQualifyingItemCount);			
				for(int k = 0; k < qualifyingList.size(); k++){
					String uuidKey = qualifyingList.get(k);
					if(!shoppingCart.getShoppingCartItems().get(this.getItem().getUpc()).get(uuidKey).qualifiesForSpecial()){		
						setItemSpecialPricing(specialPrice, upc, uuidKey, shoppingCart);	
					}
				}
			}
		}
		return shoppingCart;
	}


	
	private double calculateSpecialPrice(int totalQualifyingItemCount){
		double specialPrice = (this.getItem().getPrice() * this.getBuyQuantity())/totalQualifyingItemCount;
		DecimalFormat newFormat = new DecimalFormat("#.##");
		return Double.valueOf(newFormat.format(specialPrice));	
	}
	
	
	
	private void setItemSpecialPricing(double specialPrice, String upc, String uuidKey, ShoppingCart shoppingCart){
		shoppingCart.getShoppingCartItems().get(upc).get(uuidKey).setQualifiesForSpecial(true);
		shoppingCart.getShoppingCartItems().get(upc).get(uuidKey).setSpecialApplied(true);
		shoppingCart.getShoppingCartItems().get(upc).get(uuidKey).setSpecialPrice(specialPrice);			
	}
	
	
	
	private void resetSpecialPricing(Map<String, StoreItem> specialItemsMap, ShoppingCart shoppingCart){
		Iterator<Entry<String, StoreItem>>  storeItems = specialItemsMap.entrySet().iterator();
		while (storeItems.hasNext()) {		
			Map.Entry<String, StoreItem>  pairs = storeItems.next();
			String uuidKey = pairs.getKey().toString();
			shoppingCart.getShoppingCartItems().get(this.getItem().getUpc()).get(uuidKey).setQualifiesForSpecial(false);
			shoppingCart.getShoppingCartItems().get(this.getItem().getUpc()).get(uuidKey).setSpecialPrice(0);			
			shoppingCart.getShoppingCartItems().get(this.getItem().getUpc()).get(uuidKey).setSpecialApplied(false);				
		}
	}
	
	
	
	
	private List<String> getItemsThatQualify(Map<String, StoreItem> specialItemsMap, int totalQualifyingItemCount){

		List<String> qualifyingList = new ArrayList<String>();
		List<String> temp = new ArrayList<String>();
		
		Iterator<Entry<String, StoreItem>>  storeItems = specialItemsMap.entrySet().iterator();
		while (storeItems.hasNext()) {		    
			Map.Entry<String, StoreItem>  pairs = storeItems.next();
			temp.add(pairs.getKey().toString());
			if(temp.size() == totalQualifyingItemCount){			
				for(int m = 0; m < temp.size(); m++){
					qualifyingList.add(temp.get(m).toString());
				}
				temp = new ArrayList<String>();	
			}			
		}		
		return qualifyingList;
	}
	
	
	
	private int getTotalItemsPurchasedCount(Map<String, StoreItem> storeItems){
		int count = 0;
		Iterator<Entry<String, StoreItem>> it = storeItems.entrySet().iterator();
		while (it.hasNext()) {
		    it.next();
			count++;
		}
		return count;
	}
	

}
