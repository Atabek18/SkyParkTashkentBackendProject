package com.atabek.skyparktashkent.repository;

import com.atabek.skyparktashkent.model.Images;
import com.atabek.skyparktashkent.model.Skyparktashkent;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SkyparktashkentRepository extends MongoRepository<Skyparktashkent, ObjectId> { }
