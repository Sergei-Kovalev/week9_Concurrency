package kovalev.jdev.config;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author Sergey Kovalev
 * Класс для чтения конфигурации приложения из .yml файла.
 */
public class Config {
    /**
     * Метод возвращающий ключ-значение параметров приложения.
     * @return ключ-значения.
     */
    public static Map<String, Map<String, String>> getConfig() {
        try (InputStream inputStream = new FileInputStream("src/main/resources/properties.yml")) {
            Yaml yaml = new Yaml();
            return yaml.load(inputStream);
        } catch (IOException e) {
            try (InputStream inputStream = Config.class.getClassLoader().getResourceAsStream("properties.yml")) {
                Yaml yaml = new Yaml();
                return yaml.load(inputStream);
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

}
