package com.zxventures.pdv_api.controller;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zxventures.pdv_api.domain.Pdv;
import com.zxventures.pdv_api.helper.PdvHelper;
import com.zxventures.pdv_api.repository.PdvRepository;

import static org.springframework.data.mongodb.core.query.Criteria.*;
import static org.springframework.data.mongodb.core.query.Query.*;

@RestController
public class PdvController {

	@Autowired
	private PdvRepository repository;

	@Autowired
	MongoTemplate template;

	@Autowired
	PdvHelper builder;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<Pdv> create(@RequestBody Map<String, Object> payload) {

		try {
			Pdv pdv = repository.insert(builder.build(payload));
			return new ResponseEntity<Pdv>(pdv, HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<Pdv>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value="/search/coordinate", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Pdv> searchByCoordinates(@RequestParam(value = "latitude") double latitude,
			@RequestParam(value = "longitude") double longitude) {

		//TODO: I had tried to create a Repository to encapsule this logic, but I have got an error
		// It should be refactory
		GeoJsonPoint point = new GeoJsonPoint(latitude, longitude);

		Query query = query(where("coverageArea").nearSphere(point).maxDistance(0.003712240453784));
		List<Pdv> candidates = template.find(query, Pdv.class);
		try {
			Pdv pdv = candidates.stream()
					.sorted((c1, c2) -> Double.compare(
							builder.distance(latitude, c1.getAddress().getX(), longitude, c1.getAddress().getY()),
							builder.distance(latitude, c2.getAddress().getX(), longitude, c2.getAddress().getY())))
					.findFirst().get();
			return new ResponseEntity<Pdv>(pdv, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<Pdv>(HttpStatus.NOT_FOUND);
		} catch (Exception ex) {
			return new ResponseEntity<Pdv>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value="/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Pdv> searchById(@PathVariable(value = "id") String id) {
		try {
			Pdv pdv = repository.findById(id);
			return new ResponseEntity<Pdv>(pdv, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<Pdv>(HttpStatus.NOT_FOUND);
		} catch (Exception ex) {
			return new ResponseEntity<Pdv>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
