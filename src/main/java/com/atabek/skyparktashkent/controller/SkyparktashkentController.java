package com.atabek.skyparktashkent.controller;
import com.atabek.skyparktashkent.model.*;
import com.atabek.skyparktashkent.service.SkyparktashkentService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/skyparktashkent")
public class SkyparktashkentController {

    @Autowired
    private SkyparktashkentService skyparktashkentService;

    @PostMapping
    public ResponseEntity<?> createSkyparktashkent(
            @RequestBody Skyparktashkent payload
    ) throws IOException {

        List<PrincipleContent> principalContentList = payload.getPrincipleContentList();

        for (int i = 0;i < principalContentList.size();i++){
            skyparktashkentService.uploadImage(payload, i);
        }

        Skyparktashkent createdSkyparktashkent = skyparktashkentService.saveSkyparktashkent(payload);
        return ResponseEntity.status(HttpStatus.CREATED).
                body(createdSkyparktashkent.getAboutUs());

    }

    @PostMapping("/getImage")
    public ResponseEntity<?> getImages(
            @RequestParam("id") ObjectId id,
            @RequestParam("fileName") String fileName
    ) throws IOException {

        Object[] imageStorage = skyparktashkentService.downloadImage(id, fileName);

        byte[] imageDecomposed = (byte[]) imageStorage[0];
        Images imageInfo = (Images) imageStorage[1];

        return ResponseEntity.status(HttpStatus.OK).
                contentType(MediaType.valueOf(imageInfo.getFileType())).
                body(imageDecomposed);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSkyparktashkent(@PathVariable ObjectId id){

        return ResponseEntity.status(HttpStatus.OK).
                body(skyparktashkentService.getSkyparktashkentById(id));

    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllSkyparktashkent(){

        return ResponseEntity.status(HttpStatus.OK).
                body(skyparktashkentService.findAllSkyparktashkent());

    }

//    @PutMapping("/update/{id}")
//    public ResponseEntity<?> updateAll(@PathVariable ObjectId id, @RequestBody Skyparktashkent payload) throws IOException {
//        Skyparktashkent skyparktashkent = skyparktashkentService.updateSkyPark(id, payload);
//        return ResponseEntity.status(HttpStatus.OK).body(skyparktashkent);
//    }


}
