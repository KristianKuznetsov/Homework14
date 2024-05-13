import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaskStar {
    private static String getDataPats() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Введите путь к файлу: ");
//        String filePath = scanner.nextLine();
//        scanner.close();
//        return filePath;

        return "src\\filePackage\\inDataTaskStar.txt";
    }

    private static ArrayList<String> readFileData(String path) {
        ArrayList<String> list = new ArrayList<>();
        try (FileReader reader = new FileReader(path)) {
            Scanner scan = new Scanner(reader);

            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                line.trim();
                if (!line.equals("")) {
                    list.add(line);
                }
            }

            return list;
        } catch (FileNotFoundException e) {
            System.out.println(e);
            return list;
        } catch (IOException e) {
            System.out.println(e);
            return list;
        }
    }

    private static Pair<ArrayList<String>, ArrayList<Pair<String, String>>> distributeDocNum(ArrayList<String> list) {
        ArrayList<String> validList = new ArrayList<>();
        ArrayList<Pair<String, String>> invalidList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).matches("^(docnum|contract)[a-zA-Z0-9]{15}$")) {
                validList.add(list.get(i));
            } else {
                String el = list.get(i);
                Pair<String, String> pair = new Pair<>(el, "[");

                String pHeading = "^(docnum|contract)";
                Matcher matcherH = Pattern.compile(pHeading).matcher(el);
                if (matcherH.find()) {
                    el = (el.charAt(0) == 'd') ? el.substring(6) : el.substring(8);
                } else {
                    pair.setSecond(updateInf(pair.getSecond(), "Отсутствуют модификаторы {docnum|contract}"));
                }

                if (el.length() != 15) {
                    pair.setSecond(updateInf(pair.getSecond(), "Несоответствующая длина"));
                }

                String pInvalidCharacter = "[^a-zA-Z0-9]";
                Matcher matcherC = Pattern.compile(pInvalidCharacter).matcher(el);
                if (matcherC.find()) {
                    pair.setSecond(updateInf(pair.getSecond(), "Недопустимые символы"));
                }

                pair.setSecond(pair.getSecond() + "]");
                invalidList.add(pair);
            }
        }

        return new Pair<>(validList, invalidList);
    }

    private static String updateInf(String data, String newData) {
        return (data.equals("[")) ? (data + newData) : (data + ", " + newData);
    }

    private static void writeData(Pair<ArrayList<String>, ArrayList<Pair<String, String>>> pair) {
        try (FileWriter fileWriter = new FileWriter("src\\filePackage\\taskStarValidDocumentNumbers.txt")) {
            fileWriter.write("Валидные номера документов:\n");
            for (String el : pair.getFirst()) {
                fileWriter.write(el + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (FileWriter fileWriter = new FileWriter("src\\filePackage\\taskStarInvalidDocumentNumbers.txt")) {
            fileWriter.write("Невалидные номера документов : [причины невалидность]\n");
            for (Pair<String, String> el : pair.getSecond()) {
                fileWriter.write(el.getFirst() + " : " + el.getSecond() + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void build() {
        writeData(distributeDocNum(readFileData(getDataPats())));
    }


}
