package restservice.process;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

public class Counter {

    public static void count (String fileName){

        try {
            // OutputStream os = new FileOutputStream(new File("downloads/count.txt"), true);
//            BufferedWriter writer = Files.newBufferedWriter(Paths.get("downloads/count.txt"));
//            writer.append(fileName+"\n");
//            writer.close();
            if (!Files.exists(Paths.get("downloads/count")))
                Files.write(Paths.get("downloads/count"), (fileName+"\n").getBytes(), StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND);
            else {

//                try (Stream<String> stream = Files.lines(Paths.get("uploads/" + fileName))) {
                List<String> list = null;
                try {
                    list = Files.lines(Paths.get("downloads/count")).collect(Collectors.toList());
                    for (String i : list)
                        if (!i.equals(fileName))
                            Files.write(Paths.get("downloads/count"), (fileName).getBytes(),StandardOpenOption.APPEND);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
//            Files.copy(" ",this.fileDownloadLocation.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
