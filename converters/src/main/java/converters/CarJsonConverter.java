package converters;


import model.Car;

import java.util.List;

public class CarJsonConverter extends JsonConverter<List<Car>>{
    public CarJsonConverter(String jsonFileName) {
        super(jsonFileName);
    }
}
