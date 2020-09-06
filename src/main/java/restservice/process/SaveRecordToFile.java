package restservice.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

public class SaveRecordToFile {

    private static final Logger logger = LoggerFactory.getLogger(SaveRecordToFile.class);

    public static boolean save(String fileName) {
        try {
            if (!Files.exists(Paths.get("downloads/count"))) {
                Files.write(Paths.get("downloads/count"), (fileName + "\n").getBytes(), StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND);
                logger.warn("Record " + fileName + " was successfully added to File");
                return true;
            } else {
                List<String> list;
                try {
                    list = Files.lines(Paths.get("downloads/count")).collect(Collectors.toList());
                    boolean isRecordExists = list.stream().anyMatch(f -> f.equals(fileName));
                    if (!isRecordExists) {
                        Files.write(Paths.get("downloads/count"), (fileName + "\n").getBytes(), StandardOpenOption.APPEND);
                        logger.warn("Record " + fileName + " was successfully added to File");
                        return true;
                    } else {
                        logger.warn("Record " + fileName + " already exists");
                        return false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
