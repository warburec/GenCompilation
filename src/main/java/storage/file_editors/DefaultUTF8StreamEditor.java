package storage.file_editors;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class DefaultUTF8StreamEditor extends StreamEditor<String> {

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
