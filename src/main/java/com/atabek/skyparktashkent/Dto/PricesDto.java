package com.atabek.skyparktashkent.Dto;

import com.atabek.skyparktashkent.model.PricesModel.CoreInfo;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;
@Getter
public class PricesDto {
    private String core_title;
    private List<String> coreInfos;
    private String currentVal;
}
