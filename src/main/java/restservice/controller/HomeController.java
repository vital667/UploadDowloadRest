package restservice.controller;

import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import restservice.model.Record;
import restservice.process.ShowAllFilesFromFile;
import restservice.repository.RecordRepository;
import restservice.service.FileDownloadService;
import restservice.process.ReadAndCountService;
import restservice.model.UploadFileResponse;
import restservice.service.FileStorageService;
import restservice.process.ReplaceEverySecond;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private FileDownloadService fileDownloadService;
    @Autowired
    private RecordRepository recordRepository;


    @ApiOperation(value = "Check connection", notes = "Check connection")
    @GetMapping("/echo")
    public String echo() {
        return "Echo 1 .. 2 .. 3";
    }


    @ApiOperation(value = "Upload File for ReadAndCount Processing", notes = "Upload File for ReadAndCount Processing")
    @PostMapping("/readAndCount")
    public ResponseEntity<UploadFileResponse> readAndCountUpload(@RequestParam("file") MultipartFile file) {
        if (!file.getContentType().equals("text/plain"))
            return new ResponseEntity<>(
                    new UploadFileResponse(StringUtils.cleanPath(file.getOriginalFilename()), "Forbidden type!", file.getContentType(), file.getSize()),
                    HttpStatus.FORBIDDEN);

        String fileName = fileStorageService.storeFile(file);
        if (ReadAndCountService.processAndSave(fileName)) {
            Record record = new Record();
            record.setName(fileName);
            recordRepository.save(record);
            logger.warn("Record "+fileName+" was successfully added to DataBase");
        }
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api")
                .pathSegment(fileName)
                .toUriString();

        return new ResponseEntity<>(
                new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize()),
                HttpStatus.OK);
    }


    @ApiOperation(value = "Upload File for 'ReplaceEverySecond' Processing", notes = "Upload File for 'ReplaceEverySecond' Processing")
    @PostMapping("/replaceEverySecond")
    public ResponseEntity<UploadFileResponse> replaceEverySecond(@RequestParam("file") MultipartFile file) {
        if (!file.getContentType().equals("text/plain"))
            return new ResponseEntity<>(
                    new UploadFileResponse(StringUtils.cleanPath(file.getOriginalFilename()), "Forbidden type!", file.getContentType(), file.getSize()),
                    HttpStatus.FORBIDDEN);
        String fileName = fileStorageService.storeFile(file);
        if (ReplaceEverySecond.processAndSave(fileName)) {
            Record record = new Record();
            record.setName(fileName);
            recordRepository.save(record);
            logger.warn("Record was successfully added to DataBase");
        }
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api")
                .pathSegment(fileName)
                .toUriString();
        return new ResponseEntity<>(
                new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize()),
                HttpStatus.OK);
    }


    @ApiOperation(value = "Download processed file 'ReadAndCountDownload' or 'ReplaceEverySecond'"
            , notes = "Download processed file 'ReadAndCountDownload' or 'ReplaceEverySecond'")
    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Resource> downloadFileReadAndCountUpload(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = fileDownloadService.loadFileAsResource(fileName);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type");
        }
        if (contentType == null) contentType = "application/octet-stream";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


    @ApiOperation(value = "Show All Uploaded Files From File", notes = "Show All Uploaded Files")
    @GetMapping("/show")
    public List showAll(){
        logger.warn("Showing records from File");
        return ShowAllFilesFromFile.showAllFiles();
    }


    @ApiOperation(value = "Show All Uploaded Files From DataBase", notes = "Show All Uploaded Files")
    @GetMapping("/showdb")
    public List showAllFromDataBase() {
        logger.warn("Showing records from Database");
        return ((List<Record>) recordRepository.findAll()).stream().map(r -> r.getName()).collect(Collectors.toList());
    }


    @ApiOperation(value = "Clean Database and All Files", notes = "Clean Database and all files")
    @DeleteMapping("/clean")
    public String clean() {
        recordRepository.deleteAll();
        File downloads = new File ("downloads");
        File uploads = new File ("uploads");
        try {
            FileUtils.cleanDirectory(downloads);
            FileUtils.cleanDirectory(uploads);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.warn("Database and All the Files Upload/Download where deleted");
        return "DataBase and All the Files Upload/Download where cleaned successfully";
    }
}