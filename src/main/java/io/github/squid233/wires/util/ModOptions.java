package io.github.squid233.wires.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.squid233.wires.Wires;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author squid233
 * @since 0.3.0
 */
public final class ModOptions {
    private static final Logger logger = LogManager.getLogger(ModOptions.class);
    private static final File file = new File("config/wires.json");
    private static final Path path = Paths.get("config/wires.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public boolean fileExists() {
        return file.exists();
    }

    public void load() {
        try (Stream<String> stream = Files.lines(path)) {
            String content = stream.collect(Collectors.joining(System.lineSeparator()));
            Object opt = gson.fromJson(content, ModOptions.class);
        } catch (Exception e) {
            logger.error("Error loading " + Wires.NAMESPACE + " options", e);
        }
    }

    public void save() {
        try (AutoCloseable w = new BufferedWriter(new FileWriter(file))) {
            gson.toJson(this, (Appendable) w);
        } catch (Exception e) {
            logger.error("Error saving " + Wires.NAMESPACE + " options", e);
        }
    }
}
