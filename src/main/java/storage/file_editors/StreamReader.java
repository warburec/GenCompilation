package storage.file_editors;

import java.io.*;

public interface StreamReader<T> {
    public abstract T readFrom(InputStream stream) throws IOException;
}
