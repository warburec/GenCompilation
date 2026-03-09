package test_aids.test_storage;

import java.io.*;
import storage.file_editors.StreamEditor;
import test_aids.exceptions.ExampleException;

public class TestStringErrorStreamEditor implements StreamEditor<String> {
    
    @Override
    public String readFrom(InputStream stream) throws IOException {
        throw new ExampleException();
    }

    @Override
    public void addTo(OutputStream stream, String contents) throws IOException {
        throw new ExampleException();
    }

}