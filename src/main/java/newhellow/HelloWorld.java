package newhellow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

            // This switch determines which Part of the application runs.
            // 1 = Stage 1
            // 2 = Stage 2 (Parallel GitHub Action Matrix)
            // 3 = Stage 3
            switch (parameter1) {
                case "1":
                    // If the application runs in here this means Part 1 of the application is running.
                    System.out.println("Executing part 1!");

                    // Ensure that at least two arguments are supplied to this application.
                    // Array follows this format:
                    // Index 0: Specifies which part of the application should run. Example: 1 = Part 1, 2 = Part 2, 3 = Part 3)
                    // Index 1 and onwards: The parameters supplied to the application. Each parameter should trigger a
                    //                      unique Part 2 instance as part of a Parallel GitHub Action Matrix)
                    if (args.length > 1) {

                        // Make a new array which includes everything from the args array except for the first "application part" value.
                        String[] newArray = Arrays.copyOfRange(args, 1, args.length);

                        // For each parameter supplied to this application except for the first parameter, generate a
                        // unique JSON file containing the parameter and some other random parameters. (The GitHub
                        // Actions will later upload each JSON file into Blob storage so that it is available for Part 2
                        // and Part 3 application processing).
                        for (int i = 1; i <= newArray.length; i++) {
                            JsonWriter jsonWriter;
                            jsonWriter = new JsonWriter();
                            jsonWriter.generateAndWriteJson("target/jsons/output" + i + ".json",
                                    String.valueOf(i), newArray[i - 1], false, null);
                        }
                    }

                    break;
                case "2":
                    // If the application runs in here this means Part 2 of the application is running.
                    System.out.println("Executing part 2!");

                    if (args.length > 1) {

                        // Make a new array which includes everything from the args array except for the first "application part" value.
                        String[] newArray = Arrays.copyOfRange(args, 1, args.length);

                        String content;
                        try{
                            // Read the contents of the JSON file assigned to this instance of the Part 2 application.
                            content = Files.readString(Path.of(newArray[0]));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        // Parse the JSON into a JSON Node so that Part 2 can read the values of the JSON.
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode root;
                        try{
                            root = objectMapper.readTree(content);
                        } catch (JsonMappingException e) {
                            throw new RuntimeException(e);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }

                        // Extract the values from the JSON into variables.
                        String jsonIterationValue = root.path("jsonIterationValue").asText();
                        String randomString = root.path("randomString").asText();
                        String arrayValue = root.path("arrayValue").asText();

                        // Output the JSON variables to the console.
                        System.out.println("jsonIterationValue: " + jsonIterationValue);
                        System.out.println("randomString: " + randomString);
                        System.out.println("arrayValue: " + arrayValue);

                        // Generate a unique JSON file for this specific  Part 2 instance. (The GitHub Actions will
                        // later upload this JSON file into Blob storage so that it is available for Part 3 application
                        // processing).
                        JsonWriter jsonWriter;
                        jsonWriter = new JsonWriter();
                        jsonWriter.generateAndWriteJson("target/jsons2/output2-" + jsonIterationValue + ".json",
                                jsonIterationValue, arrayValue, true, randomString);
                    }

                    break;
                case "3":
                    // If the application runs in here this means Part 2 of the application is running.
                    System.out.println("Executing part 3!");

                    // Define the folder path to where all the jsons were uploaded from Part 2 instances of this application.
                    Path baseDir = Paths.get("target/all-jsons2");

                    try {
                        // Recursively find files matching pattern: jsons2-output2*/output2-*.json
                        List<Path> jsonFiles = Files.walk(baseDir)
                                .filter(Files::isRegularFile)
                                .filter(path -> path.getFileName().toString().startsWith("output2-"))
                                .filter(path -> path.toString().contains("jsons2-output"))
                                .filter(path -> path.toString().endsWith(".json"))
                                .collect(Collectors.toList());

                        // Output the amount of JSON files found to the console.
                        System.out.println("Found " + jsonFiles.size() + " JSON file(s).");

                        // Read the contents of each JSON file and output those contents to the console.
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