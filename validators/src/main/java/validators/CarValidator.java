package validators;

import model.Car;
import model.enums.Color;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CarValidator {

    private Map<String, String> errors = new HashMap<>();

    public Map<String, String> validate(Car car) {

        errors.clear();

        if (!isModelValid(car)) {
            errors.put("model", "not valid: " + car.getModel());
        }

        if(!isPriceValid(car)){
            errors.put("price", "not valid: " + car.getPrice());
        }

        if(!isColorValid(car)){
            errors.put("color", "not valid: " + car.getColor());
        }

        if(!isMileageValid(car)){
            errors.put("mileage", "not valid: " + car.getMileage());
        }

        if (!areComponentsValid(car)) {
            errors.put("components", "not valid: " + car.getComponents().stream().collect(Collectors.joining(", ")));
        }

        return errors;

    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    private boolean isModelValid(Car car){
        return car.getModel().matches("[A-Z]+");
    }

    private boolean isPriceValid(Car car){
        if(car.getPrice().compareTo(BigDecimal.ZERO) < 0)
            return false;
        return true;
    }

    private boolean isColorValid(Car car){
        Color[] colors = Color.values();

        Long result = Arrays
                        .stream(colors)
                        .filter(color -> car.getColor() == color)
                        .count();

        return result > 0;
    }

    private boolean isMileageValid(Car car){
        if(car.getMileage() < 0)
            return false;
        return true;
    }

    private boolean areComponentsValid(Car car) {
        return car.getComponents() != null && car.getComponents().stream().allMatch(comp -> comp.matches("[A-Z ]+"));
    }
}
