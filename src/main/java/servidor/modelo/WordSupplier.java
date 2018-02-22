package servidor.modelo;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class WordSupplier implements Supplier<String> {
    private static final String WORDS_FILE = "/lista.txt";

    private final List<String> words;

    public WordSupplier() {
        URL resource = this.getClass().getResource(WORDS_FILE);
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(resource.toURI()))) {
            words = reader.lines().collect(Collectors.toList());
        } catch (IOException | URISyntaxException e) {
            throw new IllegalStateException("Couldn't load words");
        }
    }

    @Override
    public String get() {
        return words.get(new Random().nextInt(words.size()));
    }
}
