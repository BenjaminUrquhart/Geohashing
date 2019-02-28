package com.wordpress.cannonerd;

import java.net.*;
import java.io.*;
import java.text.*;
import java.util.Date;
import net.exclaimindustries.tools.HexFraction;
import net.exclaimindustries.tools.MD5Tools;


public class Geohash{
	
    public static String fetchURL(String url) throws Exception {
	    URL address = new URL(url);
	    BufferedReader in = new BufferedReader(
				    new InputStreamReader(
				    address.openStream()));

	    String inputLine;
        String result = "";

	    while ((inputLine = in.readLine()) != null) 
        {
	        result +=inputLine;
        }

	    in.close();
        return result;
    }
/**
 * Calculates the Geohash point using djia opening and given location. 
 * Takes to account the TD30 problem
 *
 * @param  Location location, can be a Point or Graticule object
 * 
 * @return Point Geohash
 */
    
    public static Point getGeohash(Location location) throws Exception {
    	
    	double[] djia = getDJIADecimals(location.getLongitude() > -30.0);
    	double hashLat = djia[0];
    	double hashLon = djia[1];

        return new Point
        (
            Double.parseDouble((int)Math.floor(location.getLatitude()) + ("" + hashLat).substring(1)),
            Double.parseDouble((int)Math.floor(location.getLongitude()) + ("" + hashLon).substring(1))
        );
    }
    
    /**
     * Calculates the Globalhash for today.
     * 
     * @return Point Globalhash
     */
    
    public static Point getGlobalhash() throws Exception {
    	return getGlobalhash(new Date());
    }
    
    /**
     * Calculates the Globalhash on the given date.
     * 
     * @param Date date
     * 
     * @return Point Globalhash
     */
    public static Point getGlobalhash(Date date) throws Exception {
    	double[] djia = getDJIADecimals(true, date);
    	double lat = djia[0]*180.0-90.0;
    	double lon = djia[1]*360.0-180.0;
    	return new Point(lat, lon);
    }
    private static double[] getDJIADecimals(boolean useTD30) throws Exception {
    	return getDJIADecimals(useTD30, new Date());
    }
    private static double[] getDJIADecimals(boolean useTD30, Date date) throws Exception {
        DateFormat urlDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat hashDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        int td30 = 0;
        if (useTD30) {
            td30 = 1000 * 60 * 60 * 24;
        }
       
        String djia = fetchURL("http://irc.peeron.com/xkcd/map/data/" + urlDateFormat.format(date.getTime() - td30));

        String hash = MD5Tools.MD5hash(hashDateFormat.format(date) + "-" + djia);
        double hashLat = HexFraction.calculate(hash.substring(0, 16));
        double hashLon = HexFraction.calculate(hash.substring(16, 32));
        return new double[] {hashLat, hashLon};
    }
} 
