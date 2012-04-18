package org.croteau.supermarket.servicesImpl;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.croteau.supermarket.domain.StoreItem;
import org.croteau.supermarket.domainImpl.GroceryItem;
import org.croteau.supermarket.services.Shopping;
import org.croteau.supermarket.services.ShoppingCart;
import org.croteau.supermarket.services.Store;

public class GroceryShopping implements Shopping {

	private Store store;
	private ShoppingCart shoppingCart;
	
	public GroceryShopping(Store store, ShoppingCart shoppingCart){
		this.store = store;
		this.shoppingCart = shoppingCart;
	}

	public void setStore(Store store) {
		this.store = store;
	}
	public Store getStore() {
		return this.store;
	}


	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}
	public ShoppingCart getShoppingCart() {
		return this.shoppingCart;
	}
	
	
	
	public void checkout() {		
		
		double totalAfterSpecials = this.getShoppingCart().calculateShoppingCartTotalAfterSpecials();
		double savings = this.getShoppingCart().calculateSavings();
		
		System.out.println("Total : $" + totalAfterSpecials);
		
		emptyCart();		
		
		System.out.println("Received : $" + totalAfterSpecials);
		System.out.println("Congratulations, you saved $" + savings + " today");
		System.out.println("Thank you for shopping here at " + this.getStore().getStoreName() + "\n\nCome again soon!\n\n\n");
	}
	
	
	
	public void removeItemFromShoppingCart(String itemDescription){
		itemDescription = itemDescription.toLowerCase();
		
		String upc = this.getStore().getReverseLookupMap().get(itemDescription);
		
		if(this.getShoppingCart().getShoppingCartItems() == null || 
				this.getShoppingCart().getShoppingCartItems().isEmpty()){
			System.out.println("Your cart is currently empty.. nothing to remove.  Either buy something or get the f*$! out");
			return;
		}
		
		
		if(this.getShoppingCart().getShoppingCartItems().containsKey(upc)){
			GroceryItem groceryCartItem = (GroceryItem) getItemFromCart(upc);
			if(null != groceryCartItem){
			    this.getStore().addStoreItemInStock(groceryCartItem); 	
			}else{
				System.out.println("You don't have " + itemDescription + " in your cart.");
			}
			
				
		}	
	}
	
	private StoreItem getItemFromCart(String upc){
		StoreItem storeItemInShoppingCart = null;
		
		if(this.getShoppingCart().getShoppingCartItems().containsKey(upc) &&
				this.getShoppingCart().getShoppingCartItems().size() > 0){
			
			int count = 0;
			String uuid = "";
			Iterator<Entry<String, StoreItem>> upcIterator = this.getShoppingCart().getShoppingCartItems().get(upc).entrySet().iterator();
			while(upcIterator.hasNext()){

			    Map.Entry<String, StoreItem> cartItemPair = (Map.Entry<String, StoreItem>)upcIterator.next();
			    uuid = cartItemPair.getKey();
			    count++;
			    break;
			}
			
			storeItemInShoppingCart = this.getShoppingCart().getShoppingCartItems().get(upc).get(uuid);
			
		}


		this.getShoppingCart().removeItemFromCart(storeItemInShoppingCart);
		
		return storeItemInShoppingCart;
	}
	
	
	

	
	
	
	
	
	public void addItemToShoppingCart(String itemDescription) {	
		
		itemDescription = itemDescription.toLowerCase();
		
		if(this.getStore().getStoreItemsInStock() == null || 
				this.getStore().getStoreItemsInStock().isEmpty()){
			System.out.println("We are sorry, we sold out of everything... we just fired our logistics guy and are now going with UPS to handle our business logistics");
			return;
		}
		
		
		String upc = this.store.getReverseLookupMap().get(itemDescription);

		GroceryItem groceryItem = (GroceryItem) getFromStoreInventory(upc);
		if(null != groceryItem){
		    this.shoppingCart.addItemToCart(groceryItem); 	
		}else{
			System.out.println("We are sorry, " + itemDescription + " is no longer in stock.");
		}
		
	}
	
	
	private StoreItem getFromStoreInventory(String upc){
		StoreItem storeItemInStock = null;
		
		if(this.getStore().getStoreItemsInStock().containsKey(upc) &&
				this.getStore().getStoreItemsInStock().get(upc).size() > 0){
			
			Object[] uuids = this.getStore().getStoreItemsInStock().get(upc).keySet().toArray();
			String uuid = (String) uuids[0];
			
			storeItemInStock = this.store.getStoreItem(upc, uuid);
		}

		this.getStore().removeStoreItemInStock(storeItemInStock);
		
		return storeItemInStock;
		
	}
	
	

	
	
	public void emptyCart(){
		this.shoppingCart.getShoppingCartItems().clear();	
	};
	
	
	public double calculateSavings(){
		return this.getShoppingCart().calculateSavings();
	}

	
	public double calculateCartTotalPreSpecials(){
		return this.getShoppingCart().calculateShoppingCartTotalPreSpecials();
	}	
	
	
	public double calculateCartTotal(){
		return this.getShoppingCart().calculateShoppingCartTotalAfterSpecials();
	}
	

	public int getCurrentInventoryLevel(String itemDescription){
		itemDescription = itemDescription.toLowerCase();
		String upc = this.getStore().getReverseLookupMap().get(itemDescription);
		
		return this.getStore().getCurrentInventoryLevel(upc);
	}
	
	public int getCurrentCartQuantity(String itemDescription){
		itemDescription = itemDescription.toLowerCase();
		String upc = this.getStore().getReverseLookupMap().get(itemDescription);
		return this.getShoppingCart().getCurrentItemQuantity(upc);
	}
}
