package com.bonsai.accountservice.services;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    void store(MultipartFile file,String newName);
}