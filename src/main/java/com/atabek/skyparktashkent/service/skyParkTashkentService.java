package com.atabek.skyparktashkent.service;

import com.atabek.skyparktashkent.Dto.skyParkTashkentDto;
import com.atabek.skyparktashkent.model.SkyParkModel.*;
import com.atabek.skyparktashkent.repository.PrincipleRepository;
import com.atabek.skyparktashkent.repository.SkyparktashkentRepository;
import com.atabek.skyparktashkent.utils.ImageToMultipartFileConverter;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Stream;


@Service
public class skyParkTashkentService {

    @Autowired
    private SkyparktashkentRepository skyparktashkentRepository;

    @Autowired
    private PrincipleRepository principleRepository;

    private final List<String> uploadTypes = Stream.of("folder", "storage").toList();


    public Skyparktashkent saveSkyParkTashkent(Skyparktashkent skyparktashkent) {
        return skyparktashkentRepository.save(skyparktashkent);
    }


    public Optional<Skyparktashkent> getSkyParkTashkentById(ObjectId id) {
        return skyparktashkentRepository.findById(id);
    }


    public List<Skyparktashkent> findAllSkyParkTashkent(){
        return skyparktashkentRepository.findAll();
    }

    public void deleteAll(){
        skyparktashkentRepository.deleteAll();
    }


    public Skyparktashkent updateSkyPark(ObjectId id, skyParkTashkentDto payload) throws IOException{
        Optional<Skyparktashkent> skyparktashkentOptional = skyparktashkentRepository.findById(id);
        if (skyparktashkentOptional.isPresent()){
            Skyparktashkent skyparktashkent = skyparktashkentOptional.get();
            if (payload.getCounter()!=null){
                skyparktashkent.setCounter(mapCounter(payload.getCounter(), skyparktashkent.getCounter()));
            }
            if (payload.getAboutUs()!=null){
                skyparktashkent.setAboutUs(mapAboutUs(payload.getAboutUs(), skyparktashkent.getAboutUs()));
            }
            if (payload.getPrincipleContentList() != null){
                List<PrincipleContent> principleContentUpdated = mapPrincipleContentList(payload.getPrincipleContentList());
                skyparktashkent.setPrincipleContentList(principleContentUpdated);
            }
            skyparktashkentRepository.save(skyparktashkent);
            return skyparktashkent;
        }
        return null;
    }

    public Counter mapCounter(Counter payload, Counter counter){
        Counter.Quadrature quadrature = payload.getQuadrature();
        Counter.Attractions attractions = payload.getAttractions();
        String cafe = payload.getCafe();
        if(quadrature != null){
            counter.setQuadrature(mapQuadrature(quadrature, counter.getQuadrature()));
        }
        if (attractions != null){
            counter.setAttractions(mapAttractions(attractions, counter.getAttractions()));
        }
        if (cafe!=null){
            counter.setCafe(cafe);
        }
        return counter;
    }

    public Counter.Quadrature mapQuadrature(Counter.Quadrature payload, Counter.Quadrature quadrature){
        Counter.Quadrature quadratureUpdate = new Counter.Quadrature();
        quadratureUpdate.setInfo(payload.getInfo() != null ? payload.getInfo() : quadrature.getInfo());
        quadratureUpdate.setUnit(payload.getUnit() != null ? payload.getUnit() : quadrature.getUnit());
        quadratureUpdate.setTotal(payload.getTotal() != null ? payload.getTotal() : quadrature.getTotal());
        return quadratureUpdate;
    }

    public Counter.Attractions mapAttractions(Counter.Attractions payload, Counter.Attractions attractions){
        Counter.Attractions attractionsUpdate = new Counter.Attractions();
        attractionsUpdate.setInfo(payload.getInfo() != null ? payload.getInfo() : attractions.getInfo());
        attractionsUpdate.setTotal(payload.getTotal() != null ? payload.getTotal() : attractions.getTotal());
        return attractionsUpdate;
    }

    public AboutUs mapAboutUs(AboutUs payload, AboutUs aboutUs){
        AboutUs aboutUsUpdate = new AboutUs();
        aboutUsUpdate.setDescription(payload.getDescription() != null ? payload.getDescription() : aboutUs.getDescription());
        aboutUsUpdate.setTitle(payload.getTitle() != null ? payload.getTitle() : aboutUs.getTitle());
        return aboutUsUpdate;
    }

    public List<PrincipleContent> mapPrincipleContentList(List<String> payloadList) throws IOException {
        List<ObjectId> objectIds = payloadList.stream()
                .map(ObjectId::new)
                .toList();
        return principleRepository.findAllById(objectIds);

    }

    public Object[] downloadImage(ObjectId id, String fileName) throws IOException {

        Optional<Skyparktashkent> skyparktashkentOptional = skyparktashkentRepository.findById(id);

        if (skyparktashkentOptional.isPresent()){
            Skyparktashkent skyparktashkent =  skyparktashkentOptional.get();
            List<PrincipleContent> principleContentList = skyparktashkent.getPrincipleContentList();

            for (PrincipleContent principleContent : principleContentList) {

                Images imageStorage = principleContent.getImageStorage();
                String uploadType = imageStorage.getUploadType();

                if (Objects.equals(imageStorage.getFileName(), fileName)) {
                    if (uploadTypes.contains(uploadType)){

                        if (Objects.equals(uploadType, "storage")){
                            return new Object[]{ImageToMultipartFileConverter.decompressImage(imageStorage.getImageData()), imageStorage};
                        }

                        else if (Objects.equals(uploadType, "folder")) {
                            return new Object[]{Files.readAllBytes(new File(imageStorage.getFilePath()).toPath()), imageStorage};
                        }
                    }
                }
            }
        }
        return null;
    }
}
