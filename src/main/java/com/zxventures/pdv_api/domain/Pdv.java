package com.zxventures.pdv_api.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="pdvs")
public class Pdv {

    @Id
    public String id;

    public String tradingName;
    public String ownerName;
    public String document;
    public GeoJsonMultiPolygon coverageArea;
    public GeoJsonPoint address;

    public GeoJsonPoint getAddress() {
		return address;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTradingName() {
		return tradingName;
	}

	public void setTradingName(String tradingName) {
		this.tradingName = tradingName;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public GeoJsonMultiPolygon getCoverageArea() {
		return coverageArea;
	}

	public void setCoverageArea(GeoJsonMultiPolygon coverageArea) {
		this.coverageArea = coverageArea;
	}

	public void setAddress(GeoJsonPoint address) {
		this.address = address;
	}

	public Pdv() {}
    
    public Pdv(String tradingName, String ownerName, String document, GeoJsonMultiPolygon coverageArea,
			GeoJsonPoint address) {
		super();
		this.tradingName = tradingName;
		this.ownerName = ownerName;
		this.document = document;
		this.coverageArea = coverageArea;
		this.address = address;
	}


	@Override
    public String toString() {
        return String.format(
                "Customer[id=%s, tradingName='%s', address='%s']",
                id, tradingName, address);
    }

}

