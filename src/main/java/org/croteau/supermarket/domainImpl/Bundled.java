package org.croteau.supermarket.domainImpl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.croteau.supermarket.domain.Item;
import org.croteau.supermarket.domain.StoreItem;
import org.croteau.supermarket.domain.StoreSpecial;
import org.croteau.supermarket.services.ShoppingCart;

public class Bundled implements StoreSpecial{

	private String uuid;
	private Item item;
	private double comboPrice;
	private double originalComboPrice;
	private List<StoreItem> bundledItems;
	private String description;
	
	private Map<String, String> cartBundledItems;
	

	public Bundled(){
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
	
	
	
	public void setComboPrice(double comboPrice){
		this.comboPrice = comboPrice;
	}
	public double getComboPrice(){
		return this.comboPrice;
	}
	
	
	
	public void setOriginalComboPrice(){
		for(int m = 0; m < this.getBundledItems().size(); m++){
			this.originalComboPrice+=this.getBundledItems().get(m).getPrice();
		}
	}
	public double getOriginalComboPrice(){
		return this.originalComboPrice;
	}
	
	
	
	public void setBundledItems(List<StoreItem> bundledItems){
		this.bundledItems = bundledItems;
		setOriginalComboPrice();
	}
	public List<StoreItem> getBundledItems(){
		return this.bundledItems;
	}
	
	
	

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	

	public Map<String, String> getCartBundledItems() {
		return cartBundledItems;
	}

	public void setCartBundledItems(Map<String, String> cartBundledItems) {
		this.cartBundledItems = cartBundledItems;
	}
	
	
	
	public ShoppingCart applySpecialGetUpdatedCart(ShoppingCart shoppingCart) {
		

		List<Map<String, String>> cartBundlesList = getCartBundlesList(shoppingCart);
		
		for(int m = 0; m < cartBundlesList.size(); m++){
			
			Map<String, String>  itemUpcMap = cartBundlesList.get(m);
			Iterator<Entry<String, String>>  itemUpcMapIterator = itemUpcMap.entrySet().iterator();
			
			while (itemUpcMapIterator.hasNext()) {	
				
				Map.Entry<String, String>  pairs = itemUpcMapIterator.next();
				String upc = pairs.getKey();
				String uuid = pairs.getValue();
				
				double specialPrice = calculateSpecialPriceDiscount(shoppingCart.getShoppingCartItems().get(upc).get(uuid).getPrice());
						
				shoppingCart.getShoppingCartItems().get(upc).get(uuid).setSpecialPrice(specialPrice);
				shoppingCart.getShoppingCartItems().get(upc).get(uuid).setSpecialApplied(true);
			
			}
			
		}
		
		return shoppingCart;
	
	}

	
	private List<Map<String, String>> getCartBundlesList(ShoppingCart shoppingCart){
		
		List<Map<String, String>> allbundles = new ArrayList<Map<String, String>>();

		//start fresh, reset special pricing
		if(cartContainsBundledItems(shoppingCart))resetCartSpecialPricing(shoppingCart);
		
		
		int count = 0;
		while(cartContainsBundledItems(shoppingCart)){
			
			count++;
			Map<String, String> cartBundle = getBundle(shoppingCart);
			if(cartBundleMatchesSpecial(cartBundle))allbundles.add(cartBundle);
			
		}	
		
		return allbundles;
		
	}

	
	private Map<String, String> getBundle(ShoppingCart shoppingCart){

		Map<String, String> bundle = new HashMap<String, String>();
		
		for(int m = 0; m < this.getBundledItems().size(); m++){
			
			StoreItem bundledItem = this.getBundledItems().get(m);
			Iterator<Entry<String, StoreItem>>  bundledItemIterator = shoppingCart.getShoppingCartItems().get(bundledItem.getUpc()).entrySet().iterator();
			
			while (bundledItemIterator.hasNext()) {	
				
				Map.Entry<String, StoreItem>  pairs = bundledItemIterator.next();
				StoreItem storeItem = pairs.getValue();

				if(!storeItem.specialApplied() &&
						!bundle.containsKey(storeItem.getUpc())){
					
					bundle.put(storeItem.getUpc(), storeItem.getUuid());
					shoppingCart.getShoppingCartItems().get(storeItem.getUpc()).get(storeItem.getUuid()).setSpecialApplied(true);
					
				}
			}
		}
		
		return bundle;
		
	}
	
	

	
	
	private void resetSpecialPricing(String upc, Map<String, StoreItem> specialItemsMap, ShoppingCart shoppingCart){
		Iterator<Entry<String, StoreItem>>  storeItems = specialItemsMap.entrySet().iterator();
		while (storeItems.hasNext()) {		
			Map.Entry<String, StoreItem> pairs = storeItems.next();
			String uuidKey = pairs.getKey().toString();
			shoppingCart.getShoppingCartItems().get(upc).get(uuidKey).setSpecialPrice(0);			
			shoppingCart.getShoppingCartItems().get(upc).get(uuidKey).setSpecialApplied(false);	
		}
	}
	
	
	private double calculateSpecialPriceDiscount(double price){
		double percent = price/this.getOriginalComboPrice();
		double adjustedPrice = percent * this.getComboPrice();
	
		DecimalFormat newFormat = new DecimalFormat("#.##");
		double specialPriceRounded =  Double.valueOf(newFormat.format(adjustedPrice));
		
		return specialPriceRounded;
	}
	
	
	private boolean cartBundleMatchesSpecial(Map<String, String> cartBundle){
		int matches = 0;
		for(int m = 0; m < this.getBundledItems().size(); m++){
			StoreItem item = this.getBundledItems().get(m);
			if(cartBundle.containsKey(item.getUpc()))matches++;
		}
		return (matches == this.getBundledItems().size());
	}
	
	
	private boolean cartContainsBundledItems(ShoppingCart shoppingCart){
		int matches = 0;
		for(int m = 0; m < this.getBundledItems().size(); m++){
			StoreItem item = this.getBundledItems().get(m);
			
			if(shoppingCart.getShoppingCartItems().containsKey(item.getUpc()) &&
					itemsNotAlreadyBundled(shoppingCart.getShoppingCartItems().get(item.getUpc())))matches++;
		}

		return (this.getBundledItems().size() == matches);
	}
	
	
	private boolean itemsNotAlreadyBundled(Map<String, StoreItem> storeItemsMap){
		
		boolean qualifies = false;
		Iterator<Entry<String, StoreItem>> storeItemsIterator = storeItemsMap.entrySet().iterator();
		while(storeItemsIterator.hasNext()){
			
			Map.Entry<String, StoreItem> pairs = storeItemsIterator.next();
			StoreItem storeItem = pairs.getValue();
			if(!storeItem.specialApplied()){
				qualifies = true;
			}
			
		}
		
		return qualifies;
	}
	
	
	
	private void resetCartSpecialPricing(ShoppingCart shoppingCart){
		for(int m = 0; m < this.getBundledItems().size(); m++){
			StoreItem bundledItem = this.getBundledItems().get(m);
			Map<String, StoreItem> specialItemsMap = shoppingCart.getShoppingCartItems().get(bundledItem.getUpc());
			resetSpecialPricing(bundledItem.getUpc(), specialItemsMap, shoppingCart);			
		}
		
	}
	
}
