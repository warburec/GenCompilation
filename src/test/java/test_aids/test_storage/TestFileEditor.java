package test_aids.test_storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import storage.file_editors.FileEditor;

public class TestFileEditor extends FileEditor<Object> {

    @Override
    public Object readFrom(Path path) throws IOException {
        return Files.readAllBytes(path);
    }

    @Override
    public void store(Path path, Object contents) throws IOException {
        Files.write(path, (byte[])contents);
    }
    
}
