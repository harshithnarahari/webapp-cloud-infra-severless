package com.webapp.webapp.Controller;

import com.webapp.webapp.Service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.timgroup.statsd.NonBlockingStatsDClientBuilder;
import com.timgroup.statsd.StatsDClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;

@RestController
public class FileController {
    Logger logger = LoggerFactory.getLogger(HealthController.class);
    StatsDClient statsd = new NonBlockingStatsDClientBuilder()
            .prefix("statsd")
            .hostname("localhost")
            .port(8125)
            .build();

    @Autowired
    private FileService s3Service;;
    @PostMapping("/v1/documents")
    public String upload(@RequestParam("file") MultipartFile file){
        long startTime = System.currentTimeMillis();
        System.out.println("hi!!!");
        statsd.count("FileSave", 1, null);
        logger.info("FileSave endpoint hit");
        statsd.recordExecutionTime("FileSave", System.currentTimeMillis() - startTime, null);
        return s3Service.saveFile(file);
    }


    @DeleteMapping("/v1/documents/{filename}")
    public  String deleteFile(@PathVariable("filename") String filename){
        long startTime = System.currentTimeMillis();
        statsd.count("FileDel", 1, null);
        logger.info("FileDel endpoint hit");
        statsd.recordExecutionTime("FileDel", System.currentTimeMillis() - startTime, null);
        return s3Service.deleteFile(filename);
    }

    @GetMapping("/v1/documents")
    public List<String> getAllFiles(){

        long startTime = System.currentTimeMillis();
        statsd.count("FileList", 1, null);
        logger.info("FileList endpoint hit");
        statsd.recordExecutionTime("FileList", System.currentTimeMillis() - startTime, null);
        return s3Service.listAllFiles();
    }
}
