package com.zxventures.pdv_api.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MissingServletRequestParameterException;

import com.zxventures.pdv_api.domain.Pdv;

@Component
public class PdvHelper {

	private final int EARTH_RADIUS = 6371; // Approx Earth radius in KM

	@SuppressWarnings("unchecked")
	/**
	 * From a dictionary payload, generate a Restaurant object
	 * 
	 * @param payload
	 * @return
	 */
	public Pdv build(Map<String, Object> payload) throws Exception{

		// TODO check a better way to parse it, graphql might be a better solution 
		try {
			
			if(payload.get("tradingName") == null) throw new MissingServletRequestParameterException("tradingName in json", "String");
			if(payload.get("ownerName") == null) throw new MissingServletRequestParameterException("ownerName in json", "String");
			if(payload.get("document") == null) throw new MissingServletRequestParameterException("document in json", "String");
			
			if(payload.get("coverageArea") == null) throw new MissingServletRequestParameterException("coverageArea in json", "GeoJson Multipolygon");
			Map<String, Map<String, Object>> coverageArea = (Map<String, Map<String, Object>>) payload
					.get("coverageArea");

			if(payload.get("address") == null) throw new MissingServletRequestParameterException("address in json", "String"); 
			Map<String, Map<String, Object>> address = (Map<String, Map<String, Object>>) payload.get("address");

			List<List<List<List<Double>>>> areas = (List<List<List<List<Double>>>>) coverageArea.get("coordinates");

			if(address.get("coordinates") == null) throw new MissingServletRequestParameterException("coordinates in address", "GeoJson Point");
			List<Double> position = (List<Double>) address.get("coordinates");

			List<GeoJsonPolygon> polygons = new ArrayList<GeoJsonPolygon>();
			for (List<List<List<Double>>> polygon : areas) {
				List<Point> points = new ArrayList<Point>();
				for (List<List<Double>> vertice : polygon) {
					for (List<Double> coordinate : vertice) {
						points.add(new Point(coordinate.get(0), coordinate.get(1)));
					}
				}
				polygons.add(new GeoJsonPolygon(points));
			}

			GeoJsonMultiPolygon g1 = new GeoJsonMultiPolygon(polygons);
			GeoJsonPoint g1address = new GeoJsonPoint(position.get(0), position.get(1));

			return new Pdv(payload.get("tradingName").toString(), payload.get("ownerName").toString(),
					payload.get("document").toString(), g1, g1address);

		} catch (Exception e) {
			throw new Exception("Parser error in provided json");
		}
	}

	/**
	 * Calculate a distance between two coordinate using a haversin formula This
	 * formula returns a distance in radius which means a transformation to km is
	 * necessary
	 * 
	 * @param startLat
	 *            latitude from point A
	 * @param startLong
	 *            longitude from point A
	 * @param endLat
	 *            latitude from point B
	 * @param endLong
	 *            longitude from point B
	 * @return distance in km
	 */
	public double distance(double startLat, double startLong, double endLat, double endLong) {

		double dLat = Math.toRadians((endLat - startLat));
		double dLong = Math.toRadians((endLong - startLong));

		startLat = Math.toRadians(startLat);
		endLat = Math.toRadians(endLat);

		double a = haversin(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversin(dLong);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return EARTH_RADIUS * c;
	}

	private double haversin(double val) {
		return Math.pow(Math.sin(val / 2), 2);
	}
}
