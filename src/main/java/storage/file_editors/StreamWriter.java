package storage.file_editors;

import java.io.*;

public interface StreamWriter<T> {
    public abstract void addTo(OutputStream stream, T contents) throws IOException;
}