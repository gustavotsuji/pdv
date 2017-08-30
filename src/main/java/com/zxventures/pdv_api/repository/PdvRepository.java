package com.zxventures.pdv_api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.zxventures.pdv_api.domain.Pdv;

public interface PdvRepository extends MongoRepository<Pdv, String> {
	Pdv findById(String id);
}
