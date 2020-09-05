package restservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import restservice.exceptions.FileNotFoundException;
import restservice.exceptions.FileStorageException;
import restservice.properties.FileStorageProperties;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileDownloadService {
    private final Path fileDownloadLocation;

    @Autowired
    public FileDownloadService(FileStorageProperties fileStorageProperties) {
        this.fileDownloadLocation = Paths.get(fileStorageProperties.getDownloadDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileDownloadLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the files for download will be stored.", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileDownloadLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName, ex);
        }
    }
}
