
import static org.junit.Assert.*;


import org.croteau.supermarket.services.ShoppingCart;
import org.croteau.supermarket.services.Store;
import org.croteau.supermarket.servicesImpl.GroceryShopping;
import org.croteau.supermarket.servicesImpl.GroceryShoppingCart;
import org.junit.Test; 
import org.junit.*;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;


public class TestShoppingTest{
	
	
	private static BeanFactory beanFactory; 
	private static ShoppingCart shoppingCart;
	private static Store store;
	private static GroceryShopping shopping;
	
	private static final String MILK       = "milk";
	private static final String TOOTHBRUSH = "toothbrush";
	private static final String WINE       = "wine";
	private static final String CHIPS      = "chips";
	private static final String SALSA      = "salsa";
	

	
	@BeforeClass
	public static void beforeClass() {
		beanFactory = new XmlBeanFactory(new ClassPathResource("application-context.xml"));    	
    	store = (Store)beanFactory.getBean("store");
    	store.processShipments();
    	shoppingCart = new GroceryShoppingCart(store);
    	shopping = new GroceryShopping(store, shoppingCart);
    }
	

	
	@Test
	public void testItemsFreshlyStocked() {
		System.out.println("**************  FRESHLY STOCKED ITEMS TEST *****************");
		assertEquals("Just stocked 20 Cartons of milk",  20, shopping.getStore().getStoreItemsInStock().get(store.getReverseLookupMap().get(MILK)).size());
		assertEquals("Just stocked 20 Toothbrushes",     20, shopping.getStore().getStoreItemsInStock().get(store.getReverseLookupMap().get(TOOTHBRUSH)).size());
		assertEquals("Just stocked 20 Bottles of Wine",  20, shopping.getStore().getStoreItemsInStock().get(store.getReverseLookupMap().get(WINE)).size());
		assertEquals("Just stocked 20 Bags of Chips",    20, shopping.getStore().getStoreItemsInStock().get(store.getReverseLookupMap().get(CHIPS)).size());
		assertEquals("Just stocked 20 Bottles of Salsa", 20, shopping.getStore().getStoreItemsInStock().get(store.getReverseLookupMap().get(SALSA)).size());
	}
	
	

	@Test
	public void testMilkSpecial() {
		
		System.out.println("******* REGULAR PRICED MILK TEST ********");	    
	    
		addProductsToCart(5, MILK);
	    assertEquals("5 Cartons of Milk in my Shopping Cart ", 5, shopping.getShoppingCart().getShoppingCartItems().get(store.getReverseLookupMap().get(MILK)).size());
		assertEquals("Now 15 Cartons of Milk in Stock",  15, shopping.getStore().getStoreItemsInStock().get(store.getReverseLookupMap().get(MILK)).size());
		
		shopping.checkout();
		
		assertEquals("Cart is empty ", 0, shopping.getShoppingCart().getShoppingCartItems().size());
		
	}
	
	
	
	@Test
	public void testToothbrushSpecial() {

		System.out.println("******* TOOTHBRUSH SPECIAL TEST ********");
	    addProductsToCart(8, TOOTHBRUSH);
	    assertEquals("8 Toothbrushes in my Shopping Cart ", 8, shopping.getShoppingCart().getShoppingCartItems().get(store.getReverseLookupMap().get(TOOTHBRUSH)).size());
		assertEquals("Now 12 Toothbrushes in Stock",  12, shopping.getStore().getStoreItemsInStock().get(store.getReverseLookupMap().get(TOOTHBRUSH)).size());
		
		assertEquals("Cart total pre savings $15.92", 15.92, shopping.getShoppingCart().calculateShoppingCartTotalPreSpecials(), 0.0);
		assertEquals("Cart total after savings $11.96", 11.96, shopping.getShoppingCart().calculateShoppingCartTotalAfterSpecials(), 0.0);
		assertEquals("savings 3.96", 3.96, shopping.getShoppingCart().calculateSavings(), 0.0);

		shopping.checkout();
	}
	
	
	
	@Test
	public void testWineTax() {
		System.out.println("********* WINE TAX TEST **********");
	    addProductsToCart(2, WINE);
	    assertEquals("2 Bottles of Wine in my Shopping Cart ", 2, shopping.getShoppingCart().getShoppingCartItems().get(store.getReverseLookupMap().get(WINE)).size());
		assertEquals("Now 18  Bottles of Wine in Stock",  18, shopping.getStore().getStoreItemsInStock().get(store.getReverseLookupMap().get(WINE)).size());
		
		assertEquals("Cart total pre savings $30.98", 30.98, shopping.getShoppingCart().calculateShoppingCartTotalPreSpecials(), 0.0);
		assertEquals("Cart total after savings $33.84", 33.84, shopping.getShoppingCart().calculateShoppingCartTotalAfterSpecials(), 0.0);
		assertEquals("savings -2.86.. no savings", -2.86, shopping.getShoppingCart().calculateSavings(), 0.0);

		shopping.checkout();
	}
	
	
	
	@Test
	public void testBundleSpecial(){
		System.out.println("**********  CHIPS & SALSA BUNDLE TEST **********");
		addProductsToCart(5, CHIPS);
		addProductsToCart(10, SALSA);
		
	    assertEquals("5 Bags of Chips in my Shopping Cart ", 5, shopping.getShoppingCart().getShoppingCartItems().get(store.getReverseLookupMap().get(CHIPS)).size());
		assertEquals("15 Bags of Chips left in Stock",  15, shopping.getStore().getStoreItemsInStock().get(store.getReverseLookupMap().get(CHIPS)).size());

	    assertEquals("10 Bottles of Salsa in my Shopping Cart ", 10, shopping.getShoppingCart().getShoppingCartItems().get(store.getReverseLookupMap().get(SALSA)).size());
		assertEquals("10 Bottles of Salsa left in Stock",  10, shopping.getStore().getStoreItemsInStock().get(store.getReverseLookupMap().get(SALSA)).size());

		assertEquals("Cart total pre savings $47.35", 47.35, shopping.getShoppingCart().calculateShoppingCartTotalPreSpecials(), 0.0);
		assertEquals("Cart total after savings $42.40", 42.40, shopping.getShoppingCart().calculateShoppingCartTotalAfterSpecials(), 0.0);
		assertEquals("savings of $4.95", 4.95, shopping.getShoppingCart().calculateSavings(), 0.0);
		
		shopping.checkout();
	}
	
	
	@Test
	public void testRemoveFromCart(){
		
		assertEquals("current inventory level for chips is 15", 15, shopping.getStore().getStoreItemsInStock().get(store.getReverseLookupMap().get(CHIPS)).size());
		addProductsToCart(3, CHIPS);
		assertEquals("new inventory level is 12", 12, shopping.getStore().getStoreItemsInStock().get(store.getReverseLookupMap().get(CHIPS)).size());
		assertEquals("cart currently has 3 bags of chips", 3, shopping.getShoppingCart().getShoppingCartItems().get(store.getReverseLookupMap().get(CHIPS)).size());
		shopping.removeItemFromShoppingCart(CHIPS);
		shopping.removeItemFromShoppingCart(CHIPS);
		assertEquals("new inventory level is 14", 14, shopping.getStore().getStoreItemsInStock().get(store.getReverseLookupMap().get(CHIPS)).size());
		assertEquals("cart currently has 1 bags of chips", 1, shopping.getShoppingCart().getShoppingCartItems().get(store.getReverseLookupMap().get(CHIPS)).size());
		
		shopping.checkout();
		
	}

	
	
	private void addProductsToCart(int quantity, String description){
		for(int m = 0; m < quantity; m++){
			shopping.addItemToShoppingCart(description);
		}
	}


}