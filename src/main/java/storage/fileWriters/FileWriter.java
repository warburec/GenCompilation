package storage.fileWriters;

import java.io.File;
import java.nio.file.Path;

public abstract class FileWriter<T> {

    public void store(File file, T contents) {
        store(file.getAbsolutePath(), contents);
    }

    public void store(Path path, T contents) {
        store(path.toAbsolutePath(), contents);
    }
    
    public abstract void store(String path, T contents);
}