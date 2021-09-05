package com.bookit.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Environment {

        static {

            Properties properties = null;
            String environment = ConfigurationReader.get("environment");

            try {

                String path = System.getProperty("user.dir") + "/src/test/resources/Environments/" + environment + ".properties";

                FileInputStream input = new FileInputStream(path);
                properties = new Properties();
                properties.load(input);
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }





        }

}
