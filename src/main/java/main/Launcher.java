package main;

import com.sun.org.apache.xpath.internal.operations.Mod;
import resource.XMLModelSettingsParser;
import resource.XMLProductsParser;
import resource.XMLSettingsParser;
import resource.XMLStorageParser;
import storageModel.Model;
import utils.XMLStatisticOutput;

/**
 * Created by neikila on 31.10.15.
 */
public class Launcher {
    public static final String DEFAULT_SETTING_LOCATION = "Settings.xml";

    private Settings settings;
    private Model model;

    public Model getModel() {
        return model;
    }

    public Settings getSettings() {
        return settings;
    }

    public static void main(String[] args)  throws Exception {
        new Launcher().readSettings(args).init().start();
    }

    public Launcher readSettings(String[] args) {
        String settingsXML = DEFAULT_SETTING_LOCATION;
        if (args.length == 1) {
            settingsXML = args[0];
        }
        settings = new Settings(settingsXML);
        System.out.println("Settings was read from " + settingsXML);
        return this;
    }

    public Launcher init() {
        model = new Model(settings);
        return this;
    }

    public void start() {
        model.run();
        Analyzer analyzer = model.getAnalyzer();
        XMLStatisticOutput output = new XMLStatisticOutput(settings.getStatisticOutputFilename(), analyzer, "out");
        output.print();
        settings.getOutput().close();
    }
}
