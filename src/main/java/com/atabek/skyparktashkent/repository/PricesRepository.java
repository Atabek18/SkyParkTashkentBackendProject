package com.atabek.skyparktashkent.repository;

import com.atabek.skyparktashkent.model.PricesModel.CoreInfo;
import com.atabek.skyparktashkent.model.PricesModel.Prices;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PricesRepository extends MongoRepository<Prices, ObjectId> {
}
