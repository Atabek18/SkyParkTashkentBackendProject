package com.atabek.skyparktashkent.model.SkyParkModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sky_park_principle")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrincipleContent{
    @Id
    private ObjectId principleId;
    private String title;
    private String imageFile;
    private Images imageStorage;
    private String description;
}
