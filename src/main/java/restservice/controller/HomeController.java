package restservice.controller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import restservice.service.FileDownloadService;
import restservice.service.ReadAndCountService;
import restservice.model.Response;
import restservice.service.FileStorageService;
import restservice.service.ReplaceEverySecond;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@CrossOrigin
@RestController
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(FileDownloadController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private FileDownloadService fileDownloadService;


    @ApiOperation(value = "Check connection", notes = "Check connection")
    @GetMapping("/echo")
    public String echo() {
        return "Echo 1 .. 2 .. 3";
    }


    @ApiOperation(value = "Upload File for ReadAndCount Processing", notes = "Upload File for ReadAndCount Processing")
    @PostMapping("/readAndCount")
    public Response readAndCountUpload(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);
        ReadAndCountService.fileName = fileName;

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/readAndCount")
                .pathSegment(fileName)
                .toUriString();
        return new Response(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }


    @ApiOperation(value = "Show result in html", notes = "Show result in html")
    @GetMapping  ("/readAndCount")
    public Map  readAndCountDownload (){
        return ReadAndCountService.readAndCount();
    }


    @ApiOperation(value = "Download processed file ReadAndCountDownload", notes = "Download processed file ReadAndCountDownload")
    @GetMapping("/readAndCount/{fileName:.+}")
    public ResponseEntity<Resource> downloadFileReadAndCountUpload (@PathVariable String fileName, HttpServletRequest request){
        Resource resource = fileDownloadService.loadFileAsResource(fileName);

        String contentType=null;
        try{
            contentType=request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type");
        }
        if (contentType==null) contentType="application/octet-stream";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+resource.getFilename()+"\"")
                .body(resource);
    }


    @ApiOperation(value = "Upload File for ReplaceEverySecond Processing", notes = "Upload File for ReplaceEverySecond Processing")
    @PostMapping("/replaceEverySecond")
    public Response replaceEverySecond(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);
        ReplaceEverySecond.fileName = fileName;
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/replaceEverySecond")
                .pathSegment(fileName)
                .toUriString();
        return new Response(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }


    @ApiOperation(value = "Show result in html", notes = "Show result in html")
    @GetMapping  ("/replaceEverySecond")
    public String  replaceEverySecond (){
        return ReplaceEverySecond.replaceEverySecond();
    }


    @ApiOperation(value = "Download processed file ReplaceEverySecond", notes = "Download processed file ReplaceEverySecond")
    @GetMapping("/replaceEverySecond/{fileName:.+}")
    public ResponseEntity<Resource> downloadFileReplaceEverySecond (@PathVariable String fileName, HttpServletRequest request){
        Resource resource = fileDownloadService.loadFileAsResource(fileName);

        String contentType=null;
        try{
            contentType=request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type");
        }
        if (contentType==null) contentType="application/octet-stream";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+resource.getFilename()+"\"")
                .body(resource);
    }
}