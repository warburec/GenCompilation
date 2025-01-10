package storage.storageMethods;

import java.nio.file.*;

import storage.storageValues.StorageValue;

public abstract class StorageMethod<T> {

    protected static final String DEFAULT_FILE_PATH = "./compilerStorage";

    protected String filePath;
    protected String fileExtension;

    protected Path storageFile;

    /**
     * Initiates this storage method with a default filepath of ./compilerStorage and a prechosen file extension.
     */
    public StorageMethod() {
        filePath = DEFAULT_FILE_PATH;
        fileExtension = defineFileExtension();

        createStoragePath(filePath, fileExtension);
    }

    /**
     * Initiates this storage method with a custom filepath of ./compilerStorage and a prechosen file extension.
     * @param filePath The path of the storage file
     */
    public StorageMethod(String filePath) {
        filePath = DEFAULT_FILE_PATH;

        createStoragePath(filePath, fileExtension);
    }


    private void createStoragePath(String path, String extension) {
        try {
            storageFile = Paths.get(path + extension);
        } catch (InvalidPathException e) {
            throw e;
        } catch (NullPointerException e) {
            throw e; // TODO: Test how to cause this, and make appropriate error message if not already provided.
        }
    }

    /**
     * Defines the file extension to use when creating the storage file. This must be in the form ".<extension name>".
     * @return The file extension for the storage file
     */
    protected abstract String defineFileExtension();

    public abstract void store(String key, String storageType, T value); // TODO: Consider wiping file on first storage attempt
    public abstract StorageValue<T> load(String key);
}
