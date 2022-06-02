package com.bonsai.accountservice.services.impl;

import com.bonsai.accountservice.services.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Path;


@Service
@Slf4j
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    @Value("${file.upload.location}")
    private String fileUploadLocation;

    @Override
    public void store(MultipartFile file,String storageName){
        try {
            file.transferTo(Path.of(fileUploadLocation+"/"+storageName));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
