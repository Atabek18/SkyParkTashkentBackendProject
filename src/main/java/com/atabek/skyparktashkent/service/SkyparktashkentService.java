package com.atabek.skyparktashkent.service;

import com.atabek.skyparktashkent.Dto.skyParkTashkentDto;
import com.atabek.skyparktashkent.model.SkyParkModel.*;
import com.atabek.skyparktashkent.repository.PrincipleRepository;
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

    @Autowired
    private PricesService pricesService;

    @Autowired
    private PrincipleRepository principleRepository;

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

    public void deleteAll(){
        skyparktashkentRepository.deleteAll();
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


    public Images uploadImage(PrincipleContent payload) throws IOException {

        String uploadType = payload.getImageStorage().getUploadType();
        String imageFile = payload.getImageFile();

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

            return imageStorage;
        }
        return null;
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
    public PrincipleContent createPrincipleContent(PrincipleContent payload) throws IOException {
        Images image = uploadImage(payload);
        payload.setImageStorage(image);
        return principleRepository.save(payload);
    }

    public PrincipleContent updatePrincipleContent(ObjectId id, PrincipleContent payload) throws IOException {
        Optional<PrincipleContent> principleContentOptional = principleRepository.findById(id);
        if (principleContentOptional.isPresent()){
            PrincipleContent principleContent = principleContentOptional.get();
            principleContent.setTitle(payload.getTitle()!=null ? payload.getTitle() : principleContent.getTitle());
            principleContent.setDescription(payload.getDescription()!=null ? payload.getDescription() : principleContent.getDescription());
            if (payload.getImageStorage() != null){
                Images images = mapImages(payload.getImageFile()==null ? principleContent.getImageFile() : payload.getImageFile(), payload.getImageStorage());
                if (images!=null) principleContent.setImageStorage(images);
            }
            principleContent.setImageFile(payload.getImageFile()!=null ? payload.getImageFile() : principleContent.getImageFile());
            return principleContent;
        }
        return null;
    }

    public Images mapImages(String imageFileName, Images payload) throws IOException {
        Images imagesNew = new Images();
        String uploadType = payload.getUploadType();
        if (uploadType == null) return null;
        imagesNew.setUploadType(uploadType);
        ImageToMultipartFileConverter imageConverterToByte = new ImageToMultipartFileConverter();
        MultipartFile imageData = imageConverterToByte.convertFileToMultipartFile(imageFileName);
        imagesNew.setFileName(imageData.getOriginalFilename());
        imagesNew.setFileType(imageData.getContentType());
        if (Objects.equals(uploadType, "storage")){
            imagesNew.setImageData(ImageToMultipartFileConverter.compressImage(imageData.getBytes()));
        }
        else if (Objects.equals(uploadType, "folder")){
            String filePath = FOLDER_PATH + imageData.getOriginalFilename();
            imagesNew.setFilePath(filePath);
            imageData.transferTo(new File(filePath));
        }
        return imagesNew;
    }
}
