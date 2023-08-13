package com.bonsai.accountservice.services.impl;

import com.bonsai.accountservice.services.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;


@Service
@Slf4j
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    @Value("${file.upload.location}")
    private String fileUploadLocation;

    private final ResourceLoader resourceLoader;

    @Override
    public void store(MultipartFile file,String storageName){
        try {
            file.transferTo(Path.of(fileUploadLocation+"/"+storageName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Resource retrieve(String fileName) {
        return resourceLoader.getResource("file:"+fileUploadLocation+"/"+fileName);
    }
}
