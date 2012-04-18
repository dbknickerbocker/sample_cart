package org.croteau.supermarket.domain;

import java.util.Map;

import org.croteau.supermarket.services.ShoppingCart;

public interface StoreSpecial {
	
	public void setUuid(String uuid);
	
	public String getUuid();
	
	public void setDescription(String description);
	
	public String getDescription();
	
	public ShoppingCart applySpecialGetUpdatedCart(ShoppingCart shoppingCart);

//	public void resetSpecialPricing(Map<String, StoreItem> specialItemsMap, ShoppingCart shoppingCart);
	
}
