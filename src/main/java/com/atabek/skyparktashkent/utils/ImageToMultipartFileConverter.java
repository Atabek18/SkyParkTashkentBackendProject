package com.atabek.skyparktashkent.utils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ImageToMultipartFileConverter {

    public MultipartFile convertFileToMultipartFile(String filePath) throws IOException {

        File file = new File(filePath);
        Path path = Paths.get(filePath);
        String contentType = Files.probeContentType(path);
        byte[] input = Files.readAllBytes(path);

        return new MockMultipartFile(file.getName(), file.getName(), contentType, input);
    }

    public static byte[] compressImage(byte[] data) {

        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];

        while (!deflater.finished()) {
            int size = deflater.deflate(tmp);
            outputStream.write(tmp, 0, size);
        }

        try {
            outputStream.close();
        } catch (Exception ignored) {}

        return outputStream.toByteArray();
    }



    public static byte[] decompressImage(byte[] data) {

        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];

        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0, count);
            }
            outputStream.close();
        } catch (Exception ignored) {}

        return outputStream.toByteArray();
    }

}
