package ca.sfu.cmpt373.alpha.vrcladder.util;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.PropertyNotFoundException;

import java.io.IOException;
import java.util.Properties;

public class ConfigurationManager {
    private static final String RESOURCES_FILE_NAME = "configuration.properties";

    private static final String ERROR_PROPERTY_NOT_FOUND = "Property %s was not found in file";
    private static final String ERROR_LOADING_RESOURCES_FILE = "There was an error accessing the " +
            "configuration resources file. Please ensure that you have built the project using gradle, " +
            "and your $rootprj/build/resources/main directory contains a .properties file";

    private static final String PROPERTY_DATABASE_URL = "databaseUrl";
    private static final String PROPERTY_DATABASE_USERNAME = "databaseUsername";
    private static final String PROPERTY_DATABASE_PASSWORD = "databasePassword";
    private static final String PROPERTY_DATABASE_DRIVER = "databaseDriver";
    private static final String PROPERTY_DATABASE_DIALECT = "databaseDialect";
    private static final String PROPERTY_DATABASE_CREATE_MODE = "databaseCreateMode";

    private Properties properties;

    public ConfigurationManager() {
        ClassLoader classLoader = getClass().getClassLoader();
        properties = new Properties();
        try {
            properties.load(classLoader.getResourceAsStream(RESOURCES_FILE_NAME));
        } catch (IOException | NullPointerException e) {
            throw new IllegalStateException(ERROR_LOADING_RESOURCES_FILE);
        }
    }

    public String getDatabaseUrl() {
        return getProperty(PROPERTY_DATABASE_URL);
    }

    public String getDatabaseUsername() {
        return getProperty(PROPERTY_DATABASE_USERNAME);
    }

    public String getDatabasePassword() {
        return getProperty(PROPERTY_DATABASE_PASSWORD);
    }

    public String getDatabaseDriver() {
        return getProperty(PROPERTY_DATABASE_DRIVER);
    }

    public String getDatabaseDialect() {
        return getProperty(PROPERTY_DATABASE_DIALECT);
    }

    public String getDatabaseCreateMode() {
        return getProperty(PROPERTY_DATABASE_CREATE_MODE);
    }

    private String getProperty(String property) {
        String propertyValue = properties.getProperty(property);
        if (propertyValue == null) {
            throw new PropertyNotFoundException(String.format(ERROR_PROPERTY_NOT_FOUND, property));
        }
        return propertyValue;
    }
}
