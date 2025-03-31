package storage.file_editors;

public interface FileWriter<T> {
    public abstract void store(String path, T contents);
}