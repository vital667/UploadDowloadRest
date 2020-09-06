package restservice.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ReplaceEverySecond {

    public static boolean processAndSave(String fileName) {
        StringBuffer inputBuffer = new StringBuffer();

        try (BufferedReader br = Files.newBufferedReader(Paths.get("uploads/" + fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                inputBuffer.append(line);
                inputBuffer.append("\n");
            }
           Files.write(Paths.get("downloads/" + fileName), replace(inputBuffer.toString()).getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SaveRecordToFile.save(fileName);
    }


    public static String replace(String input) {
        String[] array = input.split("<br>");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < array.length; i++) {
            stringBuilder.append(array[i - 1]);
            if (i % 2 == 0) stringBuilder.append("</br></br>");
            else stringBuilder.append("<br>");
        }
        return stringBuilder.toString();
    }
}