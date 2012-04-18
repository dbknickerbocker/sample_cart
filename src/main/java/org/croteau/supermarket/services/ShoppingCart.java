package org.croteau.supermarket.services;

import java.util.Map;

import org.croteau.supermarket.domain.StoreItem;

public interface ShoppingCart {
	
	public void setStore(Store store);
	
	public Store getStore();

	public void setShoppingCartItems(Map<String, Map<String, StoreItem>> shoppingCartItems);
	
	public Map<String, Map<String, StoreItem>> getShoppingCartItems();
	
	public void addItemToCart(StoreItem storeItem);
	
	public void removeItemFromCart(StoreItem storeItem);
	
	public double calculateShoppingCartTotalAfterSpecials();
	
	public double calculateShoppingCartTotalPreSpecials();
	
	public double calculateSavings();
	
	public int getCurrentItemQuantity(String upc);
	
}
