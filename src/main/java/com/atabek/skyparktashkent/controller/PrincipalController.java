package com.atabek.skyparktashkent.controller;


import com.atabek.skyparktashkent.model.SkyParkModel.PrincipleContent;
import com.atabek.skyparktashkent.service.PrincipalService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v2/skyparktashkent/principal")
public class PrincipalController {

    @Autowired
    private PrincipalService principalService;


    @PostMapping
    public ResponseEntity<?> createPrincipal(@RequestBody PrincipleContent payload) throws IOException {
        PrincipleContent principleContent = principalService.createPrincipleContent(payload);
        return ResponseEntity.ok().body(principleContent);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePrincipal(@PathVariable ObjectId id, @RequestBody PrincipleContent payload) throws IOException {
        PrincipleContent updatedPrincipleContent = principalService.updatePrincipleContent(id, payload);
        return ResponseEntity.ok().body(updatedPrincipleContent);
    }
}
