package org.croteau.supermarket.domainImpl;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.croteau.supermarket.domain.Item;
import org.croteau.supermarket.domain.StoreItem;
import org.croteau.supermarket.domain.StoreSpecial;
import org.croteau.supermarket.services.ShoppingCart;

public class AdditionalTaxes implements StoreSpecial{

	private String uuid;
	private Item item;
	private double tax;
	private String description;
	
	public AdditionalTaxes(){
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

	
	
	public void setTax(double tax){
		this.tax = tax;
	}
	public double getTax(){
		return this.tax;
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

			double specialPrice = calculateSpecialPrice();
			
			Map<String, StoreItem> specialItemsMap = shoppingCart.getShoppingCartItems().get(this.getItem().getUpc());
			resetSpecialPricing(specialItemsMap, shoppingCart);
			
			Iterator<Entry<String, StoreItem>>  storeItemsIterator = specialItemsMap.entrySet().iterator();
			while (storeItemsIterator.hasNext()) {		
				
				Map.Entry<String, StoreItem> pairs = storeItemsIterator.next();
				String uuidKey = pairs.getKey().toString();
				
				if(!shoppingCart.getShoppingCartItems().get(this.getItem().getUpc()).get(uuidKey).qualifiesForSpecial()){				
					setItemSpecialPricing(specialPrice, this.getItem().getUpc(), uuidKey, shoppingCart);
				}
				
			}
		}
		return shoppingCart;
	}

	
	private double calculateSpecialPrice(){
		double specialPrice = (this.getItem().getPrice() * this.getTax()) + this.getItem().getPrice();
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
			Map.Entry<String, StoreItem> pairs = storeItems.next();
			String uuidKey = pairs.getKey().toString();
			shoppingCart.getShoppingCartItems().get(this.getItem().getUpc()).get(uuidKey).setQualifiesForSpecial(false);
			shoppingCart.getShoppingCartItems().get(this.getItem().getUpc()).get(uuidKey).setSpecialPrice(0);			
			shoppingCart.getShoppingCartItems().get(this.getItem().getUpc()).get(uuidKey).setSpecialApplied(false);	
		}
	}
	
}
