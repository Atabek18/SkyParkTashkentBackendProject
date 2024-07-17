package com.atabek.skyparktashkent.service;

import com.atabek.skyparktashkent.model.Counter;
import com.atabek.skyparktashkent.model.Images;
import com.atabek.skyparktashkent.model.PrincipleContent;
import com.atabek.skyparktashkent.model.Skyparktashkent;
import com.atabek.skyparktashkent.repository.SkyparktashkentRepository;
import com.atabek.skyparktashkent.utils.ImageToMultipartFileConverter;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Stream;


@Service
public class SkyparktashkentService {

    @Autowired
    private SkyparktashkentRepository skyparktashkentRepository;

    private final List<String> uploadTypes = Stream.of("folder", "storage").toList();

    private final String FOLDER_PATH = "D:/Victus/Projects/Newproject/Newproject/skyparktashkent/src/main/resources/images/";


    public Skyparktashkent saveSkyparktashkent(Skyparktashkent skyparktashkent) {
        return skyparktashkentRepository.save(skyparktashkent);
    }


    public Optional<Skyparktashkent> getSkyparktashkentById(ObjectId id) {
        return skyparktashkentRepository.findById(id);
    }


    public List<Skyparktashkent> findAllSkyparktashkent(){
        return skyparktashkentRepository.findAll();
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


    public void uploadImage(Skyparktashkent payload, int idx) throws IOException {
        PrincipleContent principalContent = payload.getPrincipleContentList().get(idx);

        String uploadType = principalContent.getImageStorage().getUploadType();
        String imageFile = principalContent.getImageFile();

        ImageToMultipartFileConverter imageConverterToByte = new ImageToMultipartFileConverter();
        MultipartFile imageData = imageConverterToByte.convertFileToMultipartFile(imageFile);

        Images imageStorage = new Images();

        imageStorage.setFileName(imageData.getOriginalFilename());
        imageStorage.setFileType(imageData.getContentType());
        imageStorage.setUploadType(uploadType);

        if (uploadTypes.contains(uploadType)){

            if (Objects.equals(uploadType, "storage")){
                imageStorage.setImageData(ImageToMultipartFileConverter.compressImage(imageData.getBytes()));
            }

            else if (Objects.equals(uploadType, "folder")){

                String filePath = FOLDER_PATH + imageData.getOriginalFilename();
                imageStorage.setFilePath(filePath);
                imageData.transferTo(new File(filePath));
            }

            principalContent.setImageStorage(imageStorage);
        }
    }


//    public Skyparktashkent updateSkyPark(ObjectId id, Skyparktashkent payload) throws IOException{
//        Optional<Skyparktashkent> skyparktashkentOptional = skyparktashkentRepository.findById(id);
//        if (skyparktashkentOptional.isPresent()){
//            Skyparktashkent skyparktashkent = skyparktashkentOptional.get();
//            skyparktashkent.setCounter(mapCounter(payload.getCounter(), skyparktashkent));
//            skyparktashkentRepository.save(skyparktashkent);
//            return skyparktashkent;
//        }
//        return null;
//    }
//
//    public Counter mapCounter(Counter payload, Skyparktashkent skyparktashkent){
//        if (payload != null){
//            Counter counter = new Counter();
//            Counter.Quadrature quadrature = payload.getQuadrature();
//            Counter.Attractions attractions = payload.getAttractions();
//            String cafe = payload.getCafe();
//            if (quadrature != null) {
//                counter.setQuadrature(quadrature);
//            }
//            if (attractions == null){
//                counter.setAttractions(skyparktashkent.getCounter().getAttractions());
//            }
//            if (cafe != null){
//                counter.setCafe(cafe);
//            } else {
//                counter.setCafe(skyparktashkent.getCounter().getCafe());
//            }
//            return counter;
//        }
//        return null;
//    }
}
