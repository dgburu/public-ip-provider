package com.dgbsoft.core.services;

public class MovieInfo {

	private static final String SEPARATOR = "@@";
	private String title;
	private String description;
	private String imageAddress;
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getImageAddress() {
		return imageAddress;
	}
	
	public void setImageAddress(String imageAddress) {
		this.imageAddress = imageAddress;
	}
	
	public String toString() {
		String result = title;
		if (description != null) {
			result += SEPARATOR + description;
			if (imageAddress!= null) {
				result += SEPARATOR + imageAddress;
			}
		}
		return result;
	}
	
	public void fromString(String info) {
		if (info != null) {
			String [] values = info.split(SEPARATOR);
			if (values.length > 0) {
				title = values[0];
			}
			if (values.length > 1) {
				description = values[1];
			}
			if (values.length > 2) {
				imageAddress = values[2];
			}
		}
	}
	
}
