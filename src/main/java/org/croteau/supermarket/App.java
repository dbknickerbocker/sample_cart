package org.croteau.supermarket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.croteau.supermarket.domain.Item;
import org.croteau.supermarket.domain.StoreItem;
import org.croteau.supermarket.domainImpl.GroceryItem;
import org.croteau.supermarket.services.ShoppingCart;
import org.croteau.supermarket.services.Store;
import org.croteau.supermarket.servicesImpl.GroceryShopping;
import org.croteau.supermarket.servicesImpl.GroceryShoppingCart;
import org.croteau.supermarket.common.SupermarketConstants;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;


@SuppressWarnings("deprecation")
public class App implements SupermarketConstants{
	
	
	static BeanFactory beanFactory; 
	static ShoppingCart shoppingCart;
	static Store store;
	
    public static void main( String[] args ) {

    	beanFactory = new XmlBeanFactory(new ClassPathResource("application-context.xml"));    	
    	
    	store = (Store)beanFactory.getBean("store");
    	store.processShipments();
    	
    	shoppingCart = new GroceryShoppingCart(store);
    	
    	GroceryShopping shopping = new GroceryShopping(store, shoppingCart);

    	System.out.println("\n\n Welcome to Joe's Supermarket... \n");
    	
    	System.out.println("type \"help\" to see the list of commands : \n\n");
    	
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		try {

			String line = null;
			while ((line = br.readLine()) != null){
				
				if(line.startsWith("add") || line.startsWith("remove") 
						|| line.startsWith("instock") || line.startsWith("incart")){
					
					int firstSpace = line.indexOf(" ");
					
					if(firstSpace > 0){
						String command = line.substring(0, firstSpace);
						String product = line.substring(firstSpace+1);
						
						if(command.equals("instock")){
							
							System.out.println(shopping.getCurrentInventoryLevel(product));
			
						}else if(command.equals("incart")){
							
							System.out.println(shopping.getCurrentCartQuantity(product));
							
							
						}else{
							
							if (product.equals(MILK) || 
									product.equals(TOOTHBRUSH) ||
									product.equals(WINE) ||
									product.equals(CHIPS) ||
									product.equals(SALSA)) {
								
								
								if(command.equals("remove")){
									System.out.println("removing " + product + " from cart");
									shopping.removeItemFromShoppingCart(product);
								}
								if(command.equals("add")){
									System.out.println("adding " + product + " to cart");
									shopping.addItemToShoppingCart(product);
								}
								
						
							}else{
								System.out.println("please enter a valid product.  type \"help\" for a list of available products");
							}						}

						
					}else{
						
						System.out.println("missing product.  make sure to enter your command like so : \"add milk\"");
					
					}
					
				}


				
				if(line.equals("checkout"))shopping.checkout();
				
				if(line.equals("savings"))System.out.println(shopping.calculateSavings());
				
				if(line.equals("total"))System.out.println(shopping.calculateCartTotal());
				
				if(line.equals("pre"))System.out.println(shopping.calculateCartTotalPreSpecials());
				
				if(line.equals("list"))System.out.println("milk, toothbrush, wine, chips, salsa");
								
				if(line.equals("help"))showHelp();
				
				if(line.equals("exit"))System.exit(1);
				
			}
			
			
		} catch (IOException e) {
			System.out.println("Error!");
//		   	System.exit(1);
		}

	}
    


    private static void showHelp(){
    	System.out.println("\n\n ********************* SUPERMARKET HELP ******************** \n\n");
    	System.out.println("List of Commands : \n");
    	System.out.println("add <product description> : adds item to cart");
    	System.out.println("remove <product description> : removes item from cart");
    	System.out.println("instock <product description> : checks current inventory level for a given product");
    	System.out.println("incart <product description> : checks current quantity of product in cart");
    	System.out.println("total    : displays cart total");
    	System.out.println("pre      : displays cart total pre special pricing");
    	System.out.println("savings  : displays current savings");
    	System.out.println("checkout : checks you out of the store & empties cart");
    	System.out.println("list     : lists current items available");
    	System.out.println("exit     : exits supermarket");
    }
    

	
    
    
}