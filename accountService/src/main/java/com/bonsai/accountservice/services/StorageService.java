package com.bonsai.accountservice.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {

    void store(MultipartFile file,String newName);

    Resource retrieve(String fileName) throws IOException;

}