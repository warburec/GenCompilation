package storage.fileWriters;

import java.io.*;
import java.nio.file.*;

public class StringFileWriter extends FileWriter<String> {

    @Override
    public void store(String path, String contents) {
        try {
            Files.writeString(Paths.get(path), contents);
        } catch (IOException e) {
            new UncheckedIOException("Failed to store content to \"" + path + "\"", e);
        }
    }
    
}
