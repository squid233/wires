package io.github.squid233.wires.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.squid233.wires.Wires;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author squid233
 * @since 0.3.0
 */
public final class ModOptions {
    private static final Logger logger = LoggerFactory.getLogger(ModOptions.class);
    private static final File file = new File("config/wires.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public boolean fileExists() {
        return file.exists();
    }

    public void load() {
        try (var r = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            var opt = gson.fromJson(r, ModOptions.class);
        } catch (Exception e) {
            logger.error("Error loading " + Wires.NAMESPACE + " options", e);
        }
    }

    public void save() {
        try (var w = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            gson.toJson(this, w);
        } catch (Exception e) {
            logger.error("Error saving " + Wires.NAMESPACE + " options", e);
        }
    }
}
