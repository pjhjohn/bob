package com.appspot.wecookbob.custom;

public class ContactUserData {
	private String userName;
	private boolean isUser;
	private final int imageId;

	public ContactUserData(String name, boolean isUser, int imgId) {
		this.userName = name;
		this.isUser = isUser;
		this.imageId = imgId;
	}
	
	public String getUserName() {
		return this.userName;
	}
	public boolean isUser() {
		return this.isUser;
	}
	public int getImageId() {
		return this.imageId;
	}
	
	public void changeUserStatus(boolean isUser) {
		this.isUser = isUser;
	}
}
