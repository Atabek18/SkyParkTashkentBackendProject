package com.atabek.skyparktashkent.Dto;

import lombok.Getter;
import org.bson.types.ObjectId;

@Getter
public class CoreInfoDto {
    private ObjectId id;
    private String client;
    private String colorLine;
    private Float value;
    private String valueType;
    private String discountType;
    private String discountName;
}
