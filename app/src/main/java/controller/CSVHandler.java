package controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class CSVHandler {

    private static Path resolvePath(String fileName) throws IOException {
        Path pathInModule = Paths.get("src", "main", "arquivos", fileName);
        Path pathInRoot = Paths.get("app", "src", "main", "arquivos", fileName);
        Path path;
        if (Files.exists(pathInModule.getParent()) || Files.exists(pathInModule)) {
            path = pathInModule;
        } else {
            path = pathInRoot;
        }
        if (Files.notExists(path)) {
            Files.createDirectories(path.getParent());
            Files.write(path, new ArrayList<>(), StandardCharsets.UTF_8);
        }
        return path;
    }

    public static void ensureFileExists(String fileName, String header) throws IOException {
        Path path = resolvePath(fileName);
        if (Files.size(path) == 0) {
            Files.write(path, List.of(header), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }
    }

    public static List<String> readAll(String fileName) throws IOException {
        Path path = resolvePath(fileName);
        return Files.readAllLines(path, StandardCharsets.UTF_8);
    }

    public static List<String> readAllWithoutHeader(String fileName) throws IOException {
        List<String> lines = readAll(fileName);
        if (lines.size() <= 1) {
            return List.of();
        }
        return lines.subList(1, lines.size());
    }

    public static void writeAll(String fileName, List<String> lines, String header) throws IOException {
        Path path = resolvePath(fileName);
        List<String> output = new ArrayList<>();
        output.add(header);
        output.addAll(lines);
        Files.write(path, output, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}
