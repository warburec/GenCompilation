package storage.file_editors;

import java.nio.file.Path;

public interface FileWriter<T> {
    public abstract void store(Path path, T contents);
}