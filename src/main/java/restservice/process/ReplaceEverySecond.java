package restservice.process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReplaceEverySecond {

    public static String fileName;

    public static String replaceEverySecond() {
        StringBuffer inputBuffer = new StringBuffer();

        try (BufferedReader br = Files.newBufferedReader(Paths.get("uploads/" + fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                inputBuffer.append(line);
                inputBuffer.append("\n");
            }
            BufferedWriter writer = Files.newBufferedWriter(Paths.get("downloads/" + fileName));
            writer.write(replace(inputBuffer.toString()));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return replace(inputBuffer.toString());
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