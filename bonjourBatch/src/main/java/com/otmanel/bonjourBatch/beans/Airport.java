package com.otmanel.bonjourBatch.beans;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="airport") // chqaue airport sera ds une baslise aerport
public class Airport {

	private String iata;
	private String airport;
	private String city;
	private String state;
	private String country;
	private double latitude;
	private double longitude;
	
	public Airport() {}

	public Airport(String iata, String airport, String city, String state, String country, double latitude, double longitude) {
		this.iata = iata;
		this.airport = airport;
		this.city = city;
		this.state = state;
		this.country = country;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	@XmlAttribute(name="iata")
	public String getIata() {
		return iata;
	}
	public void setIata(String iata) {
		this.iata = iata;
	}
	
	@XmlAttribute(name="nom")
	public String getAirport() {
		return airport;
	}
	public void setAirport(String airport) {
		this.airport = airport;
	}
	@XmlAttribute(name="ville")
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	@XmlAttribute(name="etat")
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	@XmlAttribute(name="pays")
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	@XmlAttribute(name="latitude")
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	@XmlAttribute(name="longitude")
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
