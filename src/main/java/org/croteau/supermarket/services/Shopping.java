package org.croteau.supermarket.services;

public interface Shopping {

	public void setStore(Store store);
	
	public Store getStore();
	
	public void setShoppingCart(ShoppingCart shoppingCart);
	
	public ShoppingCart getShoppingCart();
	
	public void addItemToShoppingCart(String itemDescription);
	
	public void removeItemFromShoppingCart(String itemDescription);

	public void emptyCart();
	
	public void checkout();
	
}
