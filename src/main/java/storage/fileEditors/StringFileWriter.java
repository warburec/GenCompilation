package storage.fileEditors;

import java.io.*;
import java.nio.file.*;

public class StringFileWriter extends FileEditor<String> {

    @Override
    public void store(String path, String contents) {
        try {
            Files.writeString(Paths.get(path), contents);
        } catch (IOException e) {
            new UncheckedIOException("Failed to store content to \"" + path + "\"", e);
        }
    }

    @Override
    public String readFrom(String path) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readFrom'");
    }
    
}
