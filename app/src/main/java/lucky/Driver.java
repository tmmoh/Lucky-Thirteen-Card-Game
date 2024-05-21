package lucky;

import java.util.Properties;

public class Driver {
    public static final String DEFAULT_PROPERTIES_PATH = "properties/game1.properties";

    public static void main(String[] args) {
        final Properties properties = PropertiesLoader.loadPropertiesFile(DEFAULT_PROPERTIES_PATH);
        assert properties != null;
        String logResult = new LuckyThirdteen(properties).runApp();
        System.out.println("logResult = " + logResult);
    }

}
