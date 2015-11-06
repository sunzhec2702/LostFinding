package com.example.homeApplication;

import android.app.Application;

public  class HomeApplication extends Application{
	
	private static HomeApplication application=new HomeApplication();
	
	private int[] itemNum = { 0, 0, 0 };
	
	
	

	public HomeApplication() {
		super();
		// TODO Auto-generated constructor stub
	}


	public static HomeApplication getHomeApplication() {
		return application;
	}


	public int[] getItemNum() {
		return itemNum;
	}

	public void setItemNum(int[] itemNum) {
		this.itemNum = itemNum;
	}
	
	
	
	
	
	

}
