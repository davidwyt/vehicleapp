package com.vehicle.service.bean;

public class Locate {

	private int x;
	private int y;
	
	@Override
	public boolean equals(Object obj) { 
		if((obj instanceof Locate)){
			if(this.getX()==((Locate)obj).getX()&&this.getY()==((Locate)obj).getY()){
				return true;
			}
		}
		return false;
	} 
	
	@Override
	public int hashCode(){
		return this.getX()*this.getY();
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
}
