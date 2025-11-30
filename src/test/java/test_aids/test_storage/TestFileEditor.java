package test_aids.test_storage;

import java.io.IOException;
import java.nio.file.*;

import storage.file_editors.FileEditor;

public class TestFileEditor extends FileEditor<String> {

    @Override
    public String readFrom(Path path) throws IOException {
        return Files.readString(path);
    }

    @Override
    public void store(Path path, String contents) throws IOException {
        Files.writeString(path, contents.toString());
    }
    
}
