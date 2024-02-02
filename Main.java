import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("File not found");
            return;
        }
        
        String filename = args[0]; 
        try {
        	
            byte[] fileBytes = Files.readAllBytes(Paths.get(filename));
            String fileContent = new String(fileBytes);


    		Lexer lexer = new Lexer(fileContent);
            LinkedList<Token> tokens = lexer.lex();

            for (Token token : tokens) {
                System.out.println(token);
            }
        } catch (IOException e) {
        	e.printStackTrace();
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
