package newhellow;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonWriter {

    public JsonWriter() {
        int i = 0;
    }

    public void generateAndWriteJson(String outputFilePath, String jsonIterationValue) {

        RandomStringGenerator randomStringGenerator = new RandomStringGenerator();
        String randomString = randomStringGenerator.generateRandomString(3);

        // Sample data to write as JSON
        Map<String, Object> data = new HashMap<>();
        data.put("randomString", randomString);
        data.put("jsonIterationValue", jsonIterationValue);

        // Create ObjectMapper instance
        ObjectMapper mapper = new ObjectMapper();

        try {

            // Ensure the parent directory exists
            File outputFile = new File(outputFilePath);
            File parentDir = outputFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                boolean dirsCreated = parentDir.mkdirs();
                if (!dirsCreated) {
                    throw new IOException("Failed to create directory: " + parentDir.getAbsolutePath());
                }
            }

            // Convert map to JSON string
            String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
            System.out.println("Generated JSON:\n" + jsonString);

            // Write JSON string to file
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(outputFilePath), data);
            System.out.println("JSON written to file: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("Error writing JSON to file: " + e.getMessage());
        }
    }
}