package storage.file_editors;

import java.io.IOException;
import java.nio.file.Path;

public interface FileReader<T> {
    public abstract T readFrom(Path path) throws IOException;
}
