package storage.storageMethods;


import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Stream;

import storage.FileOperations;
import storage.helper_objects.DuplicateKeyException;
import storage.storageValues.*;

public class StringStorage extends StorageMethod<String> {

    protected static final String KEY_VALUE_SEPERATOR = " : ";

    boolean storageUsed = false;
    List<String> existingKeys = new ArrayList<String>();

    @Override
    protected String defineFileExtension() {
        return ".storage.txt";
    }

    @Override
    public void store(String key, String storageType, String value) {
        if (existingKeys.contains(key)) throw new DuplicateKeyException(key);

        if (!storageUsed) {
            if (!FileOperations.fileExists(storageFile))
                FileOperations.createFile(storageFile);
            else
                FileOperations.eraseFileContents(storageFile);
        }

        if (key.contains("KEY_VALUE_SEPERATOR")) throw new IllegalArgumentException("The character sequence \'" + KEY_VALUE_SEPERATOR + "\' may not appear within a key");
        if (key.contains("\n")) throw new IllegalArgumentException("Newline characters may not appear within a key");

        FileOperations.appendLine(storageFile, key + KEY_VALUE_SEPERATOR + value);

        storageUsed = true;
        existingKeys.add(key);
    }

    @Override
    public StorageValue<String> load(String key) {
        try (Stream<String> stream = Files.lines(storageFile)) {
            List<String> keyLines = stream.filter(line -> line.startsWith(key)).toList();
            String line = keyLines.get(0);

            String value = line.split(KEY_VALUE_SEPERATOR, 2)[1];
            return new StringStorageValue(value);
        }
        catch (IOException e) {
            throw new UncheckedIOException("The file " + storageFile.toString() + "could not be appended to.", e);
        }
    }
    
}
