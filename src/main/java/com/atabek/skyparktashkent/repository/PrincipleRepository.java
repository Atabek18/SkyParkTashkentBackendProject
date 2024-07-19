package com.atabek.skyparktashkent.repository;

import com.atabek.skyparktashkent.model.SkyParkModel.PrincipleContent;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrincipleRepository extends MongoRepository<PrincipleContent, ObjectId> {
}
