package com.atabek.skyparktashkent.model.PricesModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;


//  core_title: str
//  core_info: List<object>
//      Category client: str
//      color_line: str
//      value: double
//      value_type: str
//      discount_type: str
//      discount_name: str
//  current_val: str

@Document(collection = "sky_park_price")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Prices{
    @Id
    private ObjectId id;
    private String core_title;

    @DocumentReference
    private List<CoreInfo> coreInfos;
    private String currentVal;

    public Prices(String core_title, String currentVal) {
        this.core_title = core_title;
        this.currentVal = currentVal;
    }


//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    public static class CoreInfo{
//        private String client;
//        private String colorLine;
//        private Double value;
//        private String valueType;
//        private String discountType;
//        private String discountName;
//    }
}




