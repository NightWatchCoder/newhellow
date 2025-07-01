package newhellow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HelloWorld {
    public static void main(String[] args) {

        if(args.length > 0) {

            // Get the first parameter supplied to this application.
            String parameter1 = args[0];

            switch (parameter1) {
                case "1":
                    System.out.println("Executing part 1!");

                    if (args.length > 1) {

                        String[] newArray = Arrays.copyOfRange(args, 1, args.length);

                        for (int i = 1; i <= newArray.length; i++) {
                            JsonWriter jsonWriter;
                            jsonWriter = new JsonWriter();
                            jsonWriter.generateAndWriteJson("target/jsons/output" + i + ".json",
                                    String.valueOf(i), newArray[i - 1], false, null);
                        }
                    }

                    break;
                case "2":
                    System.out.println("Executing part 2!");

                    if (args.length > 1) {
                        String[] newArray = Arrays.copyOfRange(args, 1, args.length);

                        String content;
                        try{
                            content = Files.readString(Path.of(newArray[0]));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        // Parse the JSON
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode root;
                        try{
                            root = objectMapper.readTree(content);
                        } catch (JsonMappingException e) {
                            throw new RuntimeException(e);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }

                        // Extract values
                        String jsonIterationValue = root.path("jsonIterationValue").asText();
                        String randomString = root.path("randomString").asText();
                        String arrayValue = root.path("arrayValue").asText();

                        // Output the variables
                        System.out.println("jsonIterationValue: " + jsonIterationValue);
                        System.out.println("randomString: " + randomString);
                        System.out.println("arrayValue: " + arrayValue);

                        JsonWriter jsonWriter;
                        jsonWriter = new JsonWriter();
                        jsonWriter.generateAndWriteJson("target/jsons2/output2-" + jsonIterationValue + ".json",
                                jsonIterationValue, arrayValue, true, randomString);
                    }

                    break;
                case "3":
                    System.out.println("Executing part 3!");

                    Path baseDir = Paths.get("target/all-jsons2");

                    List<Path> jsonFiles = new ArrayList<>();

                    try {
                        // Recursively find files matching pattern: jsons2-output2*/output2-*.json
                        jsonFiles = Files.walk(baseDir)
                                .filter(Files::isRegularFile)
                                .filter(path -> path.getFileName().toString().startsWith("output2-"))
                                .filter(path -> path.toString().contains("jsons2-output2"))
                                .filter(path -> path.toString().endsWith(".json"))
                                .collect(Collectors.toList());

                        System.out.println("Found " + jsonFiles.size() + " JSON file(s).");

                        ObjectMapper mapper = new ObjectMapper();

                        for (Path jsonFile : jsonFiles) {
                            System.out.println("Reading file: " + jsonFile);
                            try {
                                JsonNode rootNode = mapper.readTree(jsonFile.toFile());
                                // Process JSON as needed
                                System.out.println("Parsed content: " + rootNode.toPrettyString());
                            } catch (IOException e) {
                                System.err.println("Error parsing file: " + jsonFile + " - " + e.getMessage());
                            }
                        }

                    } catch (IOException e) {
                        System.err.println("Error walking directory: " + e.getMessage());
                    }

                    break;
                default:
                    System.out.println("Invalid parameter supplied for the first parameter. " +
                            "Expected either 1, 2, or 3. But received " + parameter1 + ".");
                    break;
            }


        } else {
            System.out.println("Error: No input parameters supplied.");
        }
    }
}