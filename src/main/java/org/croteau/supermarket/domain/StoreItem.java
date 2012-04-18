package org.croteau.supermarket.domain;

public interface StoreItem extends Item {

	public void setUuid(String uuid);
	
	public String getUuid();
	
	public void setSpecialPrice(double specialPrice);
	
	public double getSpecialPrice();
	
	public double getPurchasePrice();
	
	public void setQualifiesForSpecial(boolean qualifiesForSpecial);
	
	public boolean getQualifiesForSpecial();
	
	public boolean qualifiesForSpecial();
	
	public void setSpecialApplied(boolean specialApplied);
	
	public boolean getSpecialApplied();
	
	public boolean specialApplied();
	
}
