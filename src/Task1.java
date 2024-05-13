import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task1 {
    private static String readFileData() {
        try (FileReader reader = new FileReader("src\\filePackage\\romeo-and-juliet.txt")) {
            Scanner scan = new Scanner(reader);

            StringBuilder builder = new StringBuilder();

            while (scan.hasNextLine()) {
                builder.append(scan.nextLine());
                builder.append(" ");
            }

            return builder.toString();
        } catch (FileNotFoundException e) {
            System.out.println(e.toString());
            return null;
        } catch (IOException e) {
            System.out.println(e.toString());
            return null;
        }
    }

    private static Pair<Integer, String> wordMaxLength(String text) {
        if (text == null) {
            return new Pair(0, "");
        }

        String[] wordArray = text.split("(\\?|!|:|\"|\\[|\\]| |,|\\.|--|\\(|\\)|=)");

        int maxLength = 0;
        int tempLength = 0;
        String word = "";

        for (int i = 0; i < wordArray.length; i++) {
            tempLength = wordArray[i].length();
            if (tempLength > maxLength) {
                if ('\'' == wordArray[i].charAt(0)) {
                    tempLength = -1;
                }
                if ('\'' == wordArray[i].charAt(wordArray[i].length() - 1)) {
                    tempLength = -1;
                }

                if (tempLength > maxLength) {
                    word = wordArray[i];
                    maxLength = word.length();
                }
            }
        }

        return new Pair(maxLength, word);
    }

    private static void writeFileData(Pair data) {
        try (FileWriter fileWriter = new FileWriter("src\\filePackage\\resultTask1.txt")) {
            fileWriter.write("Самое длинное слово: " + data.getSecond() + "\nЕго длина: " + data.getFirst());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void build(){
        writeFileData(wordMaxLength(readFileData()));
    }

}
