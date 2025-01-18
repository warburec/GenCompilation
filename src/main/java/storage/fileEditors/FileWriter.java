package storage.fileEditors;

public interface FileWriter<T> {
    public abstract void store(String path, T contents);
}