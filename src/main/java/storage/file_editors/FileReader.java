package storage.file_editors;

import java.nio.file.Path;

public interface FileReader<T> {
    public abstract T readFrom(Path path);
}
