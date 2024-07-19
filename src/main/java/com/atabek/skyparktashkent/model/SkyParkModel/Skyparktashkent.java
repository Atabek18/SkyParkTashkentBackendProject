package com.atabek.skyparktashkent.model.SkyParkModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

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

    @DocumentReference
    private List<PrincipleContent> principleContentList;

    public Skyparktashkent(Counter counter, AboutUs aboutUs) {
        this.counter = counter;
        this.aboutUs = aboutUs;
    }
}
