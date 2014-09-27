package com.appspot.wecookbob.contact;

public class ContactUser {
	private final int imgResId;
	private final String userName;
	private String contactName;
	private	String customName;
	private boolean isBobUser;
	public ContactUser(String userName, int imgResId) {
		this.imgResId = imgResId;
		this.userName = userName;
		this.contactName = null;
		this.customName = null;
		this.isBobUser = false;
	}
	public ContactUser(String userName, int imgResId, boolean isBobUser) {
		this.imgResId = imgResId;
		this.userName = userName;
		this.contactName = null;
		this.customName = null;
		this.isBobUser = isBobUser;
	}
	public ContactUser(String userName, int imgResId, boolean isBobUser, String contactName) {
		this.imgResId = imgResId;
		this.userName = userName;
		this.contactName = contactName;
		this.customName = null;
		this.isBobUser = isBobUser;
	}
	
	// Setter
	public void setContactName(String name) {
		this.contactName = name;
	}
	public void setCustomName(String name) {
		this.customName = name;
	}
	public void setUserStatus(boolean isBobUser) {
		this.isBobUser = isBobUser;
	}
	
	// Getter
	public String getName() {
		if (this.customName != null) return this.customName;
		if (this.contactName != null) return this.contactName;
		return this.userName;
	}	
	public boolean isBobUser() {
		return this.isBobUser;
	}	
	public int getResourceId() {
		return this.imgResId;
	}
}
