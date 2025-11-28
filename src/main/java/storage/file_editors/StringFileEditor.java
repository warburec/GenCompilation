package storage.file_editors;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class StringFileEditor extends FileEditor<String> {

    @Override
    public void store(Path path, String contents) throws UncheckedIOException {
        try {
            Files.writeString(path, contents);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to store content to \"" + path + "\"", e);
        }
    }

    @Override
    public String readFrom(Path path) throws UncheckedIOException {
        try {
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read the contents from \"" + path + "\"", e);
        }
    }
    
}
