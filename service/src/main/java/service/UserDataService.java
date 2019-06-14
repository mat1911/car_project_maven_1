package service;

import excpetions.AppException;
import service.enums.SortingOptions;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class UserDataService {

    private Scanner scanner = new Scanner(System.in);

    public int getInt(String message) {

        System.out.println(message);
        String text = scanner.nextLine();

        if (!text.matches("\\d+")) {
            throw new AppException("INT VALUE IS NOT CORRECT: " + text);
        }

        return Integer.parseInt(text);

    }

    public double getDouble(String message) {

        System.out.println(message);
        String text = scanner.nextLine();

        if (!text.matches("([0-9]+)|([0-9]+.[0-9]+)")) {
            throw new AppException("DOUBLE VALUE IS NOT CORRECT: " + text);
        }

        return Double.parseDouble(text);

    }

    public SortingOptions getSortingOptions() {
        SortingOptions[] sortingOptions = SortingOptions.values();

        AtomicInteger counter = new AtomicInteger(1);
        Arrays
                .stream(sortingOptions)
                .forEach(carOption -> System.out.println(counter.getAndIncrement() + ". " + carOption));
        System.out.println("Enter sorting option number:");
        String text = scanner.nextLine();

        if (!text.matches("[1-" + sortingOptions.length + "]")) {
            throw new AppException("Sorting option value is not correct: " + text);
        }

        return sortingOptions[Integer.parseInt(text) - 1];
    }

    public boolean getBoolean(String message) {
        System.out.println(message + "[y / n]?");
        return Character.toLowerCase(scanner.nextLine().charAt(0)) == 'y';
    }

    public BigDecimal getBigDecimal(String message){
        System.out.println(message);
        String text = scanner.nextLine();

        if(!text.matches("([0-9]+)|([0-9]+.[0-9]+)")){
            throw new AppException("BIGDECIMAL VALUE IS NOT CORRECT: " + text);
        }

        return new BigDecimal(text);
    }

    public String getStringOfLetters(String message, boolean upperCase){
        System.out.println(message);

        String text = scanner.nextLine();

        if(upperCase && !text.matches("[A-Z]+")){
            throw new AppException("STRING IS NOT CORRECT: " + text);
        }
        else if(!upperCase && !text.matches("[a-z]+")){
            throw new AppException("STRING IS NOT CORRECT: " + text);
        }

        return text;
    }
    public void close() {

        if (scanner != null) {
            scanner.close();
            scanner = null;
        }
    }

}
