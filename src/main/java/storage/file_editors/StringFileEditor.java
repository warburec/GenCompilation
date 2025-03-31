package storage.file_editors;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class StringFileEditor extends FileEditor<String> {

    @Override
    public void store(String path, String contents) throws UncheckedIOException {
        try {
            Files.writeString(Paths.get(path), contents);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to store content to \"" + path + "\"", e);
        }
    }

    @Override
    public String readFrom(String path) throws UncheckedIOException {
        try {
            return Files.readString(Paths.get(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read the contents from \"" + path + "\"", e);
        }
    }
    
}
