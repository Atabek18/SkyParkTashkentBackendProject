package com.atabek.skyparktashkent.controller;

import com.atabek.skyparktashkent.Dto.skyParkTashkentDto;
import com.atabek.skyparktashkent.model.SkyParkModel.Images;
import com.atabek.skyparktashkent.model.SkyParkModel.PrincipleContent;
import com.atabek.skyparktashkent.model.SkyParkModel.Skyparktashkent;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(skyparktashkentService.saveSkyparktashkent(payload));

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
    public ResponseEntity<?> getSkyparktashkent(@PathVariable ObjectId id) {

        return ResponseEntity.status(HttpStatus.OK).
                body(skyparktashkentService.getSkyparktashkentById(id));

    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllSkyparktashkent() {

        return ResponseEntity.status(HttpStatus.OK).
                body(skyparktashkentService.findAllSkyparktashkent());

    }

    @GetMapping("/deleteAll")
    public ResponseEntity<?> deleteAllSkyParkTashkent(){
        skyparktashkentService.deleteAll();
        return ResponseEntity.ok().body("Data cleaned!!!");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAll(@PathVariable ObjectId id, @RequestBody skyParkTashkentDto payload) throws IOException {
        Skyparktashkent skyparktashkent = skyparktashkentService.updateSkyPark(id, payload);
        return ResponseEntity.status(HttpStatus.OK).body(skyparktashkent);
    }

    @PostMapping("/principal")
    public ResponseEntity<?> createPrincipal(@RequestBody PrincipleContent payload) throws IOException {
        PrincipleContent principleContent = skyparktashkentService.createPrincipleContent(payload);
        return ResponseEntity.ok().body(principleContent);
    }

    @PutMapping("/principal/update/{id}")
    public ResponseEntity<?> updatePrincipal(@PathVariable ObjectId id, @RequestBody PrincipleContent payload) throws IOException {
        PrincipleContent updatedPrincipleContent = skyparktashkentService.updatePrincipleContent(id, payload);
        return ResponseEntity.ok().body(updatedPrincipleContent);
    }


}
