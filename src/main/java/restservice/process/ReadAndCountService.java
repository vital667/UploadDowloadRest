package restservice.process;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReadAndCountService {

    public static boolean processAndSave(String fileName) {
        Map<String, Integer> map = new TreeMap<>();
        List<String> list;
        try (Stream<String> stream = Files.lines(Paths.get("uploads/" + fileName))) {
            list = stream.collect(Collectors.toList());
            for (String i : list)
                if (map.containsKey(i)) map.put(i, map.get(i) + 1);
                else map.put(i, 1);
            map.forEach((k, v) -> {
                try {
                    Files.write(Paths.get("downloads/" + fileName), (k + " : " + v + "\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SaveRecordToFile.save(fileName);
    }
}