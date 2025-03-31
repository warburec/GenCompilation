package storage.file_editors;

public interface FileReader<T> {
    public abstract T readFrom(String path);
}
