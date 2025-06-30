package newhellow;

import java.util.Arrays;

public class HelloWorld {
    public static void main(String[] args) {

        if(args.length > 0) {

            // Get the first parameter supplied to this application.
            String parameter1 = args[0];

            if(parameter1.equals("1")){
                System.out.println("Executing part 1!");

                if(args.length > 0) {

                    String[] newArray = Arrays.copyOfRange(args, 1, args.length);

                    for(int i = 1; i <= newArray.length; i++) {
                        newhellow.JsonWriter jsonWriter;
                        jsonWriter = new newhellow.JsonWriter();
                        jsonWriter.generateAndWriteJson("target/jsons/output" + i + ".json", String.valueOf(i));
                    }


                }

            } else if (parameter1.equals("2")) {
                System.out.println("Executing part 2!");
            } else if (parameter1.equals("3")) {
                System.out.println("Executing part 3!");
            } else {
                System.out.println("Invalid parameter supplied for the first parameter. " +
                        "Expected either 1, 2, or 3. But received " + parameter1 + ".");
            }


        } else {
            System.out.println("Error: No input parameters supplied.");
        }
    }
}