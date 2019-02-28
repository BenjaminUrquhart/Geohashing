package com.wordpress.cannonerd;

import java.io.FileNotFoundException;

public class Graticule implements Location {

	private String lat;
	private String lon;
	
	private String name;
	
	public Graticule(String lat, String lon) {
		Integer.parseInt(lat);
		Integer.parseInt(lon);
		this.lat = lat;
		this.lon = lon;
		try {
			//System.out.println(this.toString());
			this.name = Geohash.fetchURL(String.format("http://irc.peeron.com/xkcd/map/data/loc/%s,%s", lat, lon)).split("!/")[0];
		}
		catch(FileNotFoundException e) {
			throw new IllegalArgumentException("Invalid graticule");
		}
		catch(Exception e) {}
	}
	public Graticule(int lat, int lon) {
		this(lat+"", lon+"");
	}
	public static boolean isValidGraticule(int lat, int lon) {
		return isValidGraticule(lat+"", lon+"");
	}
	public static boolean isValidGraticule(String lat, String lon) {
		try {
			new Graticule(lat, lon);
			return true;
		}
		catch(Exception e) {}
		return false;
	}
	public String getName() {
		return name;
	}
	public String getAsSave() {
		return lat+","+lon;
	}
	@Override
	public double getLatitude() {
		return Double.parseDouble(lat);
	}

	@Override
	public double getLongitude() {
		return Double.parseDouble(lon);
	}
	@Override
	public String toString() {
		return String.format("%s (%s, %s)", name, lat, lon);
	}
	@Override
	public boolean equals(Object object) {
		return object instanceof Graticule && object.toString().equals(this.toString());
	}

}
