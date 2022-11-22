package com.webapp.webapp.Service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileServiceImpl {
    String saveFile(MultipartFile file);



    String deleteFile(String filename);


    List<String> listAllFiles();
}
