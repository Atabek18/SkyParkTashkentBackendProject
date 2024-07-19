package com.atabek.skyparktashkent.model.PricesModel;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "price_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoreInfo {
    @Id
    private ObjectId infoId;
    private String client;
    private String colorLine;
    private Float value;
    private String valueType;
    private String discountType;
    private String discountName;


    public CoreInfo(String client, String colorLine, Float value, String valueType, String discountType, String discountName) {
        this.client = client;
        this.colorLine = colorLine;
        this.value = value;
        this.valueType = valueType;
        this.discountType = discountType;
        this.discountName = discountName;
    }

}
