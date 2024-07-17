package com.atabek.skyparktashkent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "sky_park_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Skyparktashkent {
    @Id
    private ObjectId id;
    private Counter counter;
    private AboutUs aboutUs;
    private List<PrincipleContent> principleContentList;

    public Skyparktashkent(Counter counter, AboutUs aboutUs, List<PrincipleContent> principleContentList) {
        this.counter = counter;
        this.aboutUs = aboutUs;
        this.principleContentList = principleContentList;
    }
}












