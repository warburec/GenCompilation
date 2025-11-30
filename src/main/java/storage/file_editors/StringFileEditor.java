package storage.file_editors;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class StringFileEditor extends FileEditor<String> {

    @Override
    public void store(Path path, String contents) throws IOException {
        Files.writeString(path, contents);
    }

    @Override
    public String readFrom(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
    
}
