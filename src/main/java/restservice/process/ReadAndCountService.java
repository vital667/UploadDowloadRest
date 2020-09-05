package restservice.process;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReadAndCountService {

    public static String fileName;

    public static Map<String, Integer> readAndCount() {
        Map<String, Integer> map = new TreeMap<>();
        List<String> list;
        try (Stream<String> stream = Files.lines(Paths.get("uploads/" + fileName))) {
            list = stream.collect(Collectors.toList());
            for (String i : list)
                if (map.containsKey(i)) map.put(i, map.get(i) + 1);
                else map.put(i, 1);
            BufferedWriter writer = Files.newBufferedWriter(Paths.get("downloads/" + fileName));
            map.forEach((k, v) -> {
                try {
                    writer.write(k + " : " + v + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
}