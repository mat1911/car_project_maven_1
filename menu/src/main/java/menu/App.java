package menu;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        String filename = "resources/cars.json";
        MenuService menuService = new MenuService(filename);
        menuService.mainMenu();
    }
}
