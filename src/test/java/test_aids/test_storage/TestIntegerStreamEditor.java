package test_aids.test_storage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import storage.file_editors.StreamEditor;

public class TestIntegerStreamEditor implements StreamEditor<Integer> {
    
    @Override
    public Integer readFrom(InputStream stream) throws IOException {
        try (stream) {
            return Integer.parseInt(new String(stream.readAllBytes(), StandardCharsets.UTF_8));
        }
    }

    @Override
    public void addTo(OutputStream stream, Integer contents) throws IOException {
        try (stream) {
            stream.write(contents.toString().getBytes(StandardCharsets.UTF_8));
        }
    }

}