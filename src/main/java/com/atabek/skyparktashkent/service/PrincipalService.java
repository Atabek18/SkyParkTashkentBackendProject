package com.atabek.skyparktashkent.service;

import com.atabek.skyparktashkent.model.SkyParkModel.Images;
import com.atabek.skyparktashkent.model.SkyParkModel.PrincipleContent;
import com.atabek.skyparktashkent.model.SkyParkModel.Skyparktashkent;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class PrincipalService {

    @Autowired
    private PrincipleRepository principleRepository;

    @Autowired
    private SkyparktashkentRepository skyparktashkentRepository;

    private final List<String> uploadTypes = Stream.of("folder", "storage").toList();
    private final String FOLDER_PATH = "D:/Victus/Projects/Newproject/Newproject/skyparktashkent/src/main/resources/images/";



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
        if (Objects.equals(uploadType, "storage")) {
            imagesNew.setImageData(ImageToMultipartFileConverter.compressImage(imageData.getBytes()));
        } else if (Objects.equals(uploadType, "folder")) {
            String filePath = FOLDER_PATH + imageData.getOriginalFilename();
            imagesNew.setFilePath(filePath);
            imageData.transferTo(new File(filePath));
        }
        return imagesNew;

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


}
