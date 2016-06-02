package main;

import resource.XMLModelSettingsParser;
import resource.XMLProductsParser;
import resource.XMLSettingsParser;
import resource.XMLStorageParser;
import utils.Output;

/**
 * Created by neikila on 17.12.15.
 */
public class Settings {
    private final String statisticOutputFilename;
    private final Output output;
    private final XMLProductsParser productsSettings;
    private final XMLModelSettingsParser modelSettings;
    private final XMLStorageParser storageSettings;

    public Settings(String settingsFilename) {
        XMLSettingsParser parser = new XMLSettingsParser(settingsFilename);
        boolean isDebug = parser.getIsDebug();
        boolean toConsole = parser.getToConsole();
        productsSettings = new XMLProductsParser(parser.getProductsFilename());
        storageSettings = new XMLStorageParser(parser.getStorageFilename());
        modelSettings = new XMLModelSettingsParser(parser.getModelSettingsFilename());
        output = new Output(parser.getOutputFile(), toConsole);
        output.setDebug(isDebug);
        statisticOutputFilename = parser.getStatisticFilename();
    }

    public Output getOutput() {
        return output;
    }

    public String getStatisticOutputFilename() {
        return statisticOutputFilename;
    }

    public XMLProductsParser getProductsSettings() {
        return productsSettings;
    }

    public XMLModelSettingsParser getModelSettings() {
        return modelSettings;
    }

    public XMLStorageParser getStorageSettings() {
        return storageSettings;
    }
}
