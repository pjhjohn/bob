package com.appspot.wecookbob.view;

import java.util.Comparator;

public class BobLog {
	public long bobRequestTime;
	private String bobtnerId;
	private String bobtnerName;
	public NotificationType type;
	public static enum NotificationType{
		RECEIVED, SENT
	}
	
	public BobLog(String bobtnerId, String bobtnerName, NotificationType type, long bobRequestTime) {
		this.bobtnerId = bobtnerId;
		this.bobtnerName = bobtnerName;
		this.type = type;
		this.bobRequestTime = bobRequestTime;
	}
	
	public static Comparator<BobLog> COMPARE_BY_BOBREQUESTTIME = new Comparator<BobLog>() {
        public int compare(BobLog one, BobLog other) {
            return - new Long(one.bobRequestTime).compareTo(other.bobRequestTime);
        }
	};
	
	// Setter
	public void setBobtnerId(String bobtner_id) {
		this.bobtnerId = bobtner_id;
	}
	public void setBobtnerName(String bobtner_name) {
		this.bobtnerName = bobtner_name;
	}
	public void setNotificationType(NotificationType type) {
		this.type = type;
	}
	public void setBobRequestTime(long bob_request_time) {
		this.bobRequestTime = bob_request_time;
	}
	
	// Getter
	public String getBobtnerName() {
		if (this.bobtnerId != null) return this.bobtnerName;	
		return this.bobtnerName;
	}	
	
	public NotificationType getNotificationType() {
		return this.type;
	}	
	public String getBobtnerId() {
		return this.bobtnerId;
	}
	public long getBobRequestTime() {
		return this.bobRequestTime;
	}	
	
	// Converter
	public static NotificationType stringToNotificationType(String type) throws Exception{
		if(type.toLowerCase().equals("sent")) return NotificationType.SENT;
		if(type.toLowerCase().equals("received")) return NotificationType.RECEIVED;
		throw new Exception("Type Not Safe");
	}
}
