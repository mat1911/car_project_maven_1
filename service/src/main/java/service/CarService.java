package service;

import converters.CarJsonConverter;
import excpetions.AppException;
import model.Car;
import model.enums.Color;
import org.eclipse.collections.impl.collector.BigDecimalSummaryStatistics;
import org.eclipse.collections.impl.collector.Collectors2;
import service.enums.SortingOptions;
import validators.CarValidator;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CarService {

    private final List<Car> cars;

    public CarService(String jsonFileName) {
        cars = getCarsFromFile(jsonFileName);
    }

    private List<Car> getCarsFromFile(String jsonFilename) {

        CarJsonConverter carConverter = new CarJsonConverter(jsonFilename);
        CarValidator carValidator = new CarValidator();
        AtomicInteger counter = new AtomicInteger(1);

        return carConverter
                .fromJsonFile()
                .orElseThrow(() -> new AppException("CAR SERVICE - GET CARS FROM JSON FILE"))
                .stream()
                .filter(car -> {
                    Map<String, String> errors = carValidator.validate(car);
                    if (carValidator.hasErrors()) {
                        System.out.println("-------------------- CAR NO. " + counter.getAndIncrement() + " ---------------");
                        errors.forEach((k, v) -> System.out.println(k + " " + v));
                    }

                    return !carValidator.hasErrors();
                })
                .collect(Collectors.toList());
    }

    public List<Car> sortCars(SortingOptions sortingOption, boolean descending) {

        List<Car> sortedCars = switch (sortingOption) {

            case COLOR:
                break cars.stream().sorted(Comparator.comparing(Car::getColor)).collect(Collectors.toList());

            case MODEL:
                break cars.stream().sorted(Comparator.comparing(Car::getModel)).collect(Collectors.toList());

            case PRICE:
                break cars.stream().sorted(Comparator.comparing(Car::getPrice)).collect(Collectors.toList());

            case MILEAGE:
                break cars.stream().sorted(Comparator.comparing(Car::getMileage)).collect(Collectors.toList());
        };

        /*List<Car> sortedCars2 = switch( sortingOption ) {

            case COLOR ->  cars.stream().sorted(Comparator.comparing(Car::getColor)).collect(Collectors.toList());

            case MODEL -> cars.stream().sorted(Comparator.comparing(Car::getModel)).collect(Collectors.toList());

            case PRICE -> cars.stream().sorted(Comparator.comparing(Car::getPrice)).collect(Collectors.toList());

            case MILEAGE -> cars.stream().sorted(Comparator.comparing(Car::getMileage)).collect(Collectors.toList());
        };*/

        if (descending) {
            Collections.reverse(sortedCars);
        }

        return sortedCars;
    }

    public List<Car> getCarsWithMileage(int mileage) {

        if (mileage <= 0) {
            throw new AppException("Mileage is not correct: " + mileage);
        }

        return cars.stream()
                .filter(x -> x.getMileage() >= mileage)
                .collect(Collectors.toList());
    }

    public Map<Color, Long> countedByColor() {
        return cars.stream()
                .collect(Collectors.groupingBy(Car::getColor, Collectors.counting()))
                .entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::max, LinkedHashMap::new));
    }

    public Map<String, Car> theMostExpensiveCarsByModel() {
        return cars.stream()
                .collect(Collectors.groupingBy(
                        car -> car.getModel(),
                        Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparing(Car::getPrice)), carOp -> carOp.orElseThrow())
                ))
                .entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (c1, c2) -> c1, LinkedHashMap::new));
    }

    public void showCarsStatistics() {

        BigDecimalSummaryStatistics priceStats = takePriceStatistics();
        IntSummaryStatistics mileageStats = takeMileageStatistics();

        System.out.println("PRICE");
        System.out.println("AVERAGE PRICE: " + priceStats.getAverage());
        System.out.println("LOWEST PRICE: " + priceStats.getMin());
        System.out.println("HIGHEST PRICE: " + priceStats.getMax());

        System.out.println("\nMILEAGE");
        System.out.println("AVERAGE MILEAGE: " + mileageStats.getAverage());
        System.out.println("LOWEST MILEAGE: " + mileageStats.getMin());
        System.out.println("HIGHEST MILEAGE: " + mileageStats.getMax());

    }

    private BigDecimalSummaryStatistics takePriceStatistics() {
        return cars.stream()
                .collect(Collectors2.summarizingBigDecimal(car -> car.getPrice()));
    }

    private IntSummaryStatistics takeMileageStatistics() {
        return cars.stream()
                .collect(Collectors.summarizingInt(car -> car.getMileage()));
    }

    public List<Car> carWithHighestPrice() {

        return cars.stream()
                .collect(Collectors.groupingBy(Car::getPrice))
                .entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getKey))
                .stream()
                .map(Map.Entry::getValue)
                .findFirst().orElseThrow(() -> new AppException("No most expensive cars"));
    }

    public List<Car> getCarsWithSortedComponents() {

        return cars
                .stream()
                .peek(car -> car.setComponents(car.getComponents().stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new))))
                .collect(Collectors.toList());
    }

    public Map<String, List<Car>> carsWithComponents() {

        return cars
                .stream()
                .flatMap(car -> car.getComponents().stream())
                .distinct()
                .collect(Collectors.toMap(
                        Function.identity(),
                        component -> cars.stream().filter(car -> car.getComponents().contains(component)).collect(Collectors.toList())
                ));
    }

    public List<Car> getCarsWithPriceBetween(BigDecimal from, BigDecimal to) {

        if (from.compareTo(to) >= 0) {
            throw new AppException("Price range is not correct");
        }

        return cars
                .stream()
                .filter(car -> car.getPrice().compareTo(from) >= 0 && car.getPrice().compareTo(to) <= 0)
                .sorted(Comparator.comparing(car -> car.getModel()))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return cars.stream().map(this::carStr).collect(Collectors.joining("\n"));
    }

    private String carStr(Car car) {
        return  "MODEL: " + car.getModel() + "\n" +
                "COLOR: " + car.getColor() + "\n" +
                "COMPONENTS: " + car.getComponents().stream().collect(Collectors.joining(", ")) + "\n" +
                "\nMILEAGE: " + car.getMileage() + "\n" +
                "PRICE: " + car.getPrice();
    }
}
