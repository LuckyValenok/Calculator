package net.luckyvalenok.calculator;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) throws IOException {
        File input = new File("input.txt");
        if (!input.exists() && input.createNewFile()) {
            System.out.println("Файл был создан. Введите в него пример");
            return;
        }
        
        FileReader fileReader = new FileReader(input);
        Scanner scanner = new Scanner(fileReader);
        if (scanner.hasNext()) {
            String[] blocks = Arrays.stream(scanner.nextLine().trim().split(" "))
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);
            fileReader.close();
            
            String stringResult;
            try {
                double result = Double.parseDouble(blocks[0]);
                for (int i = 1; i < blocks.length - 1; i += 2) {
                    String operator = blocks[i];
                    double aDouble = Double.parseDouble(blocks[i + 1]);
                    result = calculate(result, aDouble, operator);
                }
                stringResult = String.valueOf(result);
            } catch (Exception exception) {
                stringResult = "В примере ошибка. " + exception.getMessage();
            }
    
            FileWriter output = new FileWriter("output.txt");
            output.write(stringResult);
            output.close();
        } else {
            System.out.println("Отсутствует пример");
        }
    }
    
    private static double calculate(double number1, double number2, String operation) throws Exception {
        switch (operation) {
            case "+":
                return number1 + number2;
            case "-":
                return number1 - number2;
            case "/":
                if (number2 == 0) {
                    throw new Exception("Деление на 0 невозможно");
                }
                return number1 / number2;
            case "*":
                return number1 * number2;
            default:
                throw new Exception("Недопустимая операция " + operation);
        }
    }
}
