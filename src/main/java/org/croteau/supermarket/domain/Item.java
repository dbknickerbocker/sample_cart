package org.croteau.supermarket.domain;

public interface Item {

	public void setUpc(String upc);
	
	public String getUpc();
	
	public void setPrice(double price);
	
	public double getPrice();
	
	public void setDescription(String description);
	
	public String getDescription();
	
}
