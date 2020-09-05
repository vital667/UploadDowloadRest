package restservice.process;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ShowAllFiles {

    public static List showAllFiles() {
    List <String> list = new ArrayList<>();
        //Stream<String> stream = Files.lines(Paths.get("downloads/count");

//        if (!Files.exists(Paths.get("downloads/count")))
//        return "No File was uploaded";


    {
        try {
            Stream<String> stream = Files.lines(Paths.get("downloads/count"));
             list = stream.collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
return list;

//        return Files.lines(Paths.get("uploads/count")).collect(Collectors.toList());
    }

}
