package com.atabek.skyparktashkent.controller;

import com.atabek.skyparktashkent.Dto.CoreInfoDto;
import com.atabek.skyparktashkent.Dto.PricesDto;
import com.atabek.skyparktashkent.model.PricesModel.CoreInfo;
import com.atabek.skyparktashkent.model.PricesModel.Prices;
import com.atabek.skyparktashkent.service.PricesService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v2/skyparktashkent/price")
public class PriceController {
    @Autowired
    private PricesService pricesService;

    @PostMapping("/info")
    public ResponseEntity<?> createInfo(@RequestBody CoreInfoDto payload){
        return ResponseEntity.status(HttpStatus.OK).body(pricesService.createPriceInfo(payload));
    }

    @PostMapping
    public ResponseEntity<?> createPrice(@RequestBody Prices payload){
        return ResponseEntity.status(HttpStatus.OK).body(pricesService.savePrice(payload));
    }

    @PutMapping("/updatePrice/{id}")
    public ResponseEntity<?> updatePrice(@PathVariable ObjectId id, @RequestBody PricesDto payload){
        return ResponseEntity.ok().body(pricesService.updatePrice(id, payload));
    }

    @PutMapping("/updateInfo/{infoId}")
    public ResponseEntity<?> updateInfo(@PathVariable ObjectId infoId, @RequestBody Map<String, Object> payload){
        return ResponseEntity.ok().body(pricesService.updatedPriceDynamic("infoId", infoId, payload, CoreInfo.class));
    }

}
