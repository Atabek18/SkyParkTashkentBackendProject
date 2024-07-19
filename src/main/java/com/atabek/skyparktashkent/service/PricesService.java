package com.atabek.skyparktashkent.service;

import com.atabek.skyparktashkent.Dto.CoreInfoDto;
import com.atabek.skyparktashkent.Dto.PricesDto;
import com.atabek.skyparktashkent.model.PricesModel.CoreInfo;
import com.atabek.skyparktashkent.model.PricesModel.Prices;
import com.atabek.skyparktashkent.repository.PriceInfoRespository;
import com.atabek.skyparktashkent.repository.PricesRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PricesService {

    @Autowired
    private PricesRepository pricesRepository;

    @Autowired
    private PriceInfoRespository priceInfoRespository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Prices savePrice(Prices payload){
        return pricesRepository.save(payload);
    }


    public Prices getPrice(ObjectId id){
        Optional<Prices> price = pricesRepository.findById(id);
        return price.orElse(new Prices());
    }

    public List<Prices> getAll(){
        return pricesRepository.findAll();
    }

    public Prices updatePrice(ObjectId id, PricesDto payload){
        Optional<Prices> priceOptional = pricesRepository.findById(id);
        if (priceOptional.isPresent()){
            Prices price = priceOptional.get();
            if (payload.getCore_title() != null){
                price.setCore_title(payload.getCore_title());
            }
            if (payload.getCurrentVal() !=null ){
                price.setCurrentVal(payload.getCurrentVal());
            }
            if (payload.getCoreInfos()!=null){
                List<ObjectId> objectIds = payload.getCoreInfos().stream()
                        .map(ObjectId::new)
                        .collect(Collectors.toList());
                List<CoreInfo> coreInfos = priceInfoRespository.findAllById(objectIds);
                price.setCoreInfos(coreInfos);
            }
            return pricesRepository.save(price);
        }
        return null;
    }


    public CoreInfo createPriceInfo(CoreInfoDto payload){

        CoreInfo priceInfo = priceInfoRespository.insert(new CoreInfo(
                payload.getClient(),
                payload.getColorLine(),
                payload.getValue(),
                payload.getValueType(),
                payload.getDiscountType(),
                payload.getDiscountName()));
        mongoTemplate.update(Prices.class)
                .matching(Criteria.where("id").is(payload.getId()))
                .apply(new Update().push("coreInfos").value(priceInfo.getInfoId()))
                .first();
        return priceInfo;
    }

    public Optional<CoreInfo> updatePriceInfoByEachElement(ObjectId infoId, Map<String, Object> payload){
        String fieldName = (String) payload.get("fieldName");
        Object value = payload.get("value");

        Optional<CoreInfo> coreInfoOptional = priceInfoRespository.findById(infoId);
        if (coreInfoOptional.isPresent()){
            Query query  = new Query(Criteria.where("infoId").is(infoId));
            Update update = new Update().set(fieldName, value);
            mongoTemplate.updateFirst(query, update, CoreInfo.class);
            return Optional.ofNullable(mongoTemplate.findOne(query, CoreInfo.class));
        }
        return Optional.empty();
    }

    public CoreInfo updatedPriceDynamic(String idName, ObjectId infoId, Map<String, Object> updatePayload, Class<?> className){
        Query query = new Query(Criteria.where(idName).is(infoId));
        Update update = new Update();

        updatePayload.forEach((key, val) -> {
            Field field = ReflectionUtils.findField(className, key);
            if (field != null) {
                update.set(key, val);
            } else {
                String[] keys = key.split("\\.");
                if (keys.length > 1){
                    System.out.print(key);
                }
            }
        });
        mongoTemplate.findAndModify(query, update, className);
        return priceInfoRespository.findById(infoId).orElse(null);
    }

}
