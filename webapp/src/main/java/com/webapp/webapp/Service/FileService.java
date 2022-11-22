package com.webapp.webapp.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.webapp.webapp.Repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
//import com.webapp.webapp.Model.File;
import com.webapp.webapp.Model.Docu;

import javax.print.Doc;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class FileService implements FileServiceImpl {

    @Value("${bucketname}")
    private String bucketname;
    private  final AmazonS3 s3;
    private FileRepository fileRepository;
    public FileService(AmazonS3 s3, FileRepository fileRepository) {

        this.s3 = s3;
        this.fileRepository = fileRepository;
    }


    @Override
    public String saveFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        try {
            System.out.println("hi!!!");
            s3.putObject(new PutObjectRequest(bucketname, originalFilename, file.getInputStream(), null));
            Docu docu = new Docu();
            docu.setName(originalFilename);
            docu.setDate_created(new Date(System.currentTimeMillis()));
            //docu.setDate_created(originalFilename);
            fileRepository.save(docu);
            return "Done";
        } catch (IOException e) {
            throw  new RuntimeException(e);
        }
    }


    @Override
    public String deleteFile(String filename) {

        s3.deleteObject(bucketname,filename);
        return "File deleted";
        
    }

    @Override
    public List<String> listAllFiles() {
        ListObjectsV2Result listObjectsV2Result = s3.listObjectsV2(bucketname);
        return  listObjectsV2Result.getObjectSummaries().stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
    }
   
}