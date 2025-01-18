package storage.fileEditors;

public interface FileReader<T> {
    public abstract T readFrom(String path);
}
