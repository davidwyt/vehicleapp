package com.vehicle.app.utils;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

public class LocationUtil {
	public static Location getCurLocation(Context context) {
		try {
			LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			criteria.setAltitudeRequired(false);
			criteria.setBearingRequired(false);
			criteria.setCostAllowed(true);
			criteria.setPowerRequirement(Criteria.POWER_LOW);
			String provider = locationManager.getBestProvider(criteria, true);
			Location location = locationManager.getLastKnownLocation(provider);

			if (null == location) {
				location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			}

			return location;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
