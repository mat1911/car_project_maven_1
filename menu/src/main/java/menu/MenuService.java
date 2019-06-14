package menu;

import excpetions.AppException;
import service.CarService;
import service.UserDataService;
import service.enums.SortingOptions;

import java.math.BigDecimal;

public class MenuService {

    private final CarService carService;
    private final UserDataService userDataService;

    public MenuService(String jsonFilename) {
        carService = new CarService(jsonFilename);
        userDataService = new UserDataService();
    }

    public void mainMenu() {
        while (true) {
            try {

                showMenu();
                int option = userDataService.getInt("Enter option number:");

                switch (option) {
                    case 1 -> option1();

                    case 2 -> option2();

                    case 3 -> option3();

                    case 4 -> option4();

                    case 5 -> option5();

                    case 6 -> option6();

                    case 7 -> option7();

                    case 8 -> option8();

                    case 9 -> option9();

                    case 10 -> option10();

                    case 11 -> {
                        System.out.println("Have a nice day!");
                        return;
                    }
                }

            } catch (AppException e) {

                System.out.println("\n--------------------- EXCEPTION ----------------------");
                System.out.println(e.getMessage());
            }
        }
    }

    private void showMenu() {

        System.out.println("=================MENU=================");
        System.out.println("1 - all cars");
        System.out.println("2 - sort cars");
        System.out.println("3 - cars with mileage greater than");
        System.out.println("4 - cars counted by color");
        System.out.println("5 - the most expensive cars (by model)");
        System.out.println("6 - cars statistics");
        System.out.println("7 - the most expensive car");
        System.out.println("8 - cars with sorted components");
        System.out.println("9 - cars with the given component");
        System.out.println("10 - cars with price in a given range");
        System.out.println("11 - end of app");
    }

    private void option1() {
        System.out.println(carService);
    }

    private void option2() {

        SortingOptions sortingOptions = userDataService.getSortingOptions();
        boolean descending = userDataService.getBoolean("Descending ");
        carService.sortCars(sortingOptions, descending).forEach(System.out::println);
    }

    private void option3(){

        int mileage = userDataService.getInt("Enter min mileage: ");
        carService.getCarsWithMileage(mileage).forEach(System.out::println);
    }

    private void option4(){
        carService.countedByColor().forEach((color, amount) -> System.out.println(color + " " + amount));
    }

    private void option5(){
        carService.theMostExpensiveCarsByModel().forEach((model, car) -> System.out.println(model + " " + car));
    }

    private void option6(){
        carService.showCarsStatistics();
    }

    private void option7(){
        carService.carWithHighestPrice().forEach(System.out::println);
    }

    private void option8(){
        carService.getCarsWithSortedComponents().forEach(System.out::println);
    }

    private void option9(){
        carService.carsWithComponents().forEach((component, car) -> System.out.println(component + " " + car));
    }

    private void option10(){
        BigDecimal from = userDataService.getBigDecimal("Enter price from: ");
        BigDecimal to = userDataService.getBigDecimal("Enter price to: ");

        carService.getCarsWithPriceBetween(from, to).forEach(System.out::println);
    }
}
