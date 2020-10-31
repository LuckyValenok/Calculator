package net.luckyvalenok.calculator;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class Main {
    
    private static final Map<String, Integer> priorities = new HashMap<>();
    private static final Stack<Double> numbers = new Stack<>();
    private static final Stack<String> operations = new Stack<>();
    
    static {
        priorities.put("+", 1);
        priorities.put("-", 1);
        priorities.put("*", 2);
        priorities.put("/", 2);
    }
    
    public static void main(String[] args) throws IOException {
        File input = new File("input.txt");
        if (!input.exists() && input.createNewFile()) {
            System.out.println("Файл был создан. Введите в него пример");
            return;
        }
        
        String[] blocks = getBlocks(input);
        if (blocks == null) {
            System.out.println("Отсутствует пример");
        } else {
            String result = getResult(blocks);
            
            FileWriter output = new FileWriter("output.txt");
            output.write(result);
            output.close();
        }
    }
    
    private static String[] getBlocks(File file) throws IOException {
        FileReader fileReader = new FileReader(file);
        Scanner scanner = new Scanner(fileReader);
        String[] blocks = null;
        if (scanner.hasNext()) {
            blocks = Arrays.stream(scanner.nextLine().trim().split(" "))
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);
        }
        fileReader.close();
        return blocks;
    }
    
    private static String getResult(String[] blocks) {
        String stringResult;
        try {
            for (String element : blocks) {
                try {
                    double number = Double.parseDouble(element);
                    numbers.push(number);
                } catch (NumberFormatException exception) {
                    if (priorities.get(element) == null) {
                        throw new Exception("Недопустимая операция " + element);
                    } else {
                        if (!operations.empty()) {
                            int priority = priorities.get(element);
                            
                            String operation;
                            while (!operations.empty() && (operation = operations.peek()) != null && priority <= priorities.get(operation)) {
                                calculate();
                            }
                        }
                        operations.push(element);
                    }
                }
            }
            
            while (!operations.empty()) {
                calculate();
            }
            
            double result = numbers.pop();
            if (numbers.empty()) {
                stringResult = result + ""; // Конкатенация происходит быстрее, чем String.valueOf(result), поэтому сделал так
            } else {
                stringResult = "В примере ошибка. Недостаточно операторов для всех чисел";
            }
        } catch (EmptyStackException exception) {
            stringResult = "В примере ошибка. Недостаточно чисел для всех операторов";
        } catch (Exception exception) {
            stringResult = exception.getMessage();
        }
        
        return stringResult;
    }
    
    private static void calculate() throws Exception {
        String operation = operations.pop();
        Double n2 = numbers.pop();
        Double n1 = numbers.pop();
        double result = 0;
        
        switch (operation) {
            case "+":
                result = n1 + n2;
                break;
            case "-":
                result = n1 - n2;
                break;
            case "*":
                result = n1 * n2;
                break;
            case "/":
                if (n2 == 0) {
                    throw new Exception("Деление на 0 невозможно");
                }
                result = n1 / n2;
                break;
        }
        
        numbers.push(result);
    }
}
