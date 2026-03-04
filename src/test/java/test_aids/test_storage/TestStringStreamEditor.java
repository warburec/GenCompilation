package test_aids.test_storage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import storage.file_editors.StreamEditor;

public class TestStringStreamEditor implements StreamEditor<String> {
    
    @Override
    public String readFrom(InputStream stream) throws IOException {
        try (stream) {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    @Override
    public void addTo(OutputStream stream, String contents) throws IOException {
        try (stream) {
            stream.write(contents.getBytes(StandardCharsets.UTF_8));
        }
    }

}