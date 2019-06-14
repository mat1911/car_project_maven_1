package converters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import excpetions.AppException;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

public abstract class JsonConverter <T>{
    String jsonFileName;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Type type = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    public JsonConverter(String jsonFileName) {
        this.jsonFileName = jsonFileName;
    }

    public void toJsonFile(T dataTosafe){

        try (FileWriter fileWriter = new FileWriter(jsonFileName)){
            gson.toJson(dataTosafe, fileWriter);
        } catch (Exception e) {
            throw new AppException("JSON CONVERTER - TO JSON");
        }
    }

    public Optional<T> fromJsonFile(){

        try (FileReader fileReader = new FileReader(jsonFileName)){
            return Optional.of(gson.fromJson(fileReader, type));
        } catch (Exception e) {
            throw new AppException("JSON CONVERTER - FROM JSON");
        }
    }
}
