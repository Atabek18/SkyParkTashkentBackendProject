package com.atabek.skyparktashkent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrincipleContent{
    private String title;
    private String imageFile;
    private Images imageStorage;
    private String description;
}
