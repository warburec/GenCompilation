package storage.fileEditors;

import java.io.File;
import java.nio.file.Path;

public abstract class FileEditor <T> implements FileReader<T>, FileWriter<T> {

    public T readFrom(File file) {
        return readFrom(file.getAbsolutePath());
    }

    public T store(Path path) {
        return readFrom(path.toString());
    }

    public void store(File file, T contents) {
        store(file.getAbsolutePath(), contents);
    }

    public void store(Path path, T contents) {
        store(path.toString(), contents);
    }

}
