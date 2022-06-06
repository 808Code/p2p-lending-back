package com.bonsai.accountservice.services.impl;

import com.bonsai.accountservice.services.StorageNameService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class StorageNameImpl implements StorageNameService {
    @Override
    public String storageNameGenerator(String fileName,String fileCategory) {
        String userId="5";

        String currentDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String storageName = fileName.replace(fileName, FilenameUtils.getBaseName(fileName).concat(currentDate) + userId + fileCategory + "." + FilenameUtils.getExtension(fileName));
        return storageName;
    }
}
