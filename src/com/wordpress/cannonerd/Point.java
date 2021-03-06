package com.wordpress.cannonerd;


/**
 * Calculates userlocation- geohash location distances and bearing
 * 
 *
 * @param   latitude, longitude   GPS positioning data
 * 
 * @return Bearing and distance
 */
public class Point implements Location {
    private double lat;
    private double lon;

    public Point(double latitude, double longitude) {
        //super((int) (latitude * 1E6), (int) (longitude * 1E6));
        this.lat = latitude;
        this.lon = longitude;
    }

    public Point(int latitudeE6, int longitudeE6) {
        this((double) (latitudeE6 / 1E6), (double) (longitudeE6 / 1E6));
    }
    
    public double getLatitude() {
    	return lat;
    }
    public double getLongitude() {
    	return lon;
    }

/**
 * Metodi korvaamaan Math.luokan samaa, katoaa tod nak myohemmin.
 * 
 *
 * @param   toRadians  degrees
 * 
 * @return degrees in radians 
 */

    private double toRadians(double degrees)
    {
        return (degrees * Math.PI / 180.0);
    }

/**
 * Mettodi joka kertoo etaisyyden kahden pisteen valilla
 * 
 *
 * @param   distanceTo   location, latitude and longitude
 * 
 * @return Distance in 
 */

	public double distanceTo(Location to)
    {
        double startLon = toRadians(lon); 
        double startLat = toRadians(lat);
        double endLon  = toRadians(to.getLongitude());
        double endLat = toRadians(to.getLatitude());

        double dLat = endLat - startLat;
        double dLon = endLon - startLon;
        double a = Math.pow(Math.sin(dLat/2.0), 2) + Math.cos(startLat) * Math.cos(endLat) * Math.pow(Math.sin(dLon/2.0),2);
        double c = 2.0 * Math.atan2(Math.sqrt(a),  Math.sqrt(1.0-a));     
       return 6371.0 * c;

    }

/**
 * Metodi kertoo kompassisuuntiman lahtopisteesta loppupisteeseen. Sijainnin siirtyessa suuntima 
 * lasketaan uudelleen.
 *
 * @param   bearingTo location and end point latitudes and longitudes
 * 
 * @return bearing in degrees 
 */
    public double bearingTo(Point to) {
        double distance = distanceTo(to);    
        if (distance == 0.0)
        {
            return 0.0;
        }

        double deltaLong = Math.toRadians(to.lon - lon);

        double lat1 = Math.toRadians(lat);
        double lat2 = Math.toRadians(to.lat);

        double y = Math.sin(deltaLong) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(deltaLong);
        double result = Math.toDegrees(Math.atan2(y, x));
        return (result + 360.0) % 360.0;


    }
	
    public static String prettyPrintCoordinate(double coordinate) {
        double deg = Math.abs(coordinate);
        int degrees = (int) Math.floor(deg);
        double min = 60.0 * (deg - degrees);
        int minutes = (int) min;
        double sec = 60 *  (min - minutes);
        int seconds = (int) sec;
        if(degrees < 0) degrees++;
        //String degr  = Integer.toString(degrees);
        //String minu  = Integer.toString(minutes);
        //String seco  = Integer.toString(seconds);
        String coordinates = degrees +"\u00b0"+ minutes +"\u2032"+seconds+"\u2033";  
        return coordinates;
    }

    @Override
    public String toString() {
        String lati = prettyPrintCoordinate(lat);
        String longi = prettyPrintCoordinate(lon);
        
        if (lat > 0.0)
        {
            lati = lati + " N";
        }
        else
        {
            lati = lati + " S";
        }
        if (lon > 0.0)
        {
            longi = longi + " E";
        }
        else
        {
            longi = longi + " W";
        }
        String total = new String(lati + ", " + longi); 
        return total;
   }
}
