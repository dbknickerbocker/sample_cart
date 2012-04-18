package org.croteau.supermarket.domainImpl;

import java.util.UUID;

import org.croteau.supermarket.domain.StoreItem;

public class GroceryItem implements StoreItem{
	
	private String uuid;
	private String upc;
	private double price;
	private double specialPrice;
	private boolean qualifiesForSpecial;
	private boolean specialApplied;
	private String description;
	
	public GroceryItem(){
		this.uuid = UUID.randomUUID().toString();
	}
	
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	
	
	public String getUpc() {
		return upc;
	}
	public void setUpc(String upc) {
		this.upc = upc;
	}
	
	
	
	public double getPrice() {
		return this.price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	
	
	
	public double getSpecialPrice() {
		return specialPrice;
	}
	public void setSpecialPrice(double specialPrice) {
		this.specialPrice = specialPrice;
	}
	

	
	public double getPurchasePrice(){
		if(this.specialApplied)return this.getSpecialPrice();
		return this.price;	
	}
	
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	

		
	public void setQualifiesForSpecial(boolean qualifiesForSpecial){
		this.qualifiesForSpecial = qualifiesForSpecial;
	}
	public boolean getQualifiesForSpecial(){
		return this.qualifiesForSpecial;
	}
	public boolean qualifiesForSpecial(){
		return this.qualifiesForSpecial;
	}
	
	
	
	
	public void setSpecialApplied(boolean specialApplied){
		this.specialApplied = specialApplied;
	}
	public boolean getSpecialApplied(){
		return this.specialApplied;
	}
	public boolean specialApplied(){
		return this.specialApplied;
	}
	
}
