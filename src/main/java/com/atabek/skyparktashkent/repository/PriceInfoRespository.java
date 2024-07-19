package com.atabek.skyparktashkent.repository;

import com.atabek.skyparktashkent.model.PricesModel.CoreInfo;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceInfoRespository extends MongoRepository<CoreInfo, ObjectId> {}
