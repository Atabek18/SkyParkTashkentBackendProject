package com.atabek.skyparktashkent.model.SkyParkModel;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Images {
    private String fileName;
    private String fileType;
    private String filePath;
    private byte[] imageData;
    private String uploadType;

}
