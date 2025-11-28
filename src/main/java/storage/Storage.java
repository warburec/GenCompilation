package storage;

import java.io.*;

import storage.file_editors.*;
import storage.file_editors.FileWriter;
import storage.file_editors.FileReader;
import storage.storage_value_adapters.UnsupportedValueException;
import storage.storage_values.StorageValue;
import storage.value_formatters.*;

/**
 * A utility class for storage of compiler components.
 */
public class Storage {

    private String targetFilepath = "." + File.separator + "compilerthis.txt";
    private ChosenFormatter<?> formatter = new ChosenFormatter<>(new ValueToStringFormatter());
    private FileWriter<?> fileWriter = new StringFileEditor();
    private FileReader<?> fileReader = new StringFileEditor();

    //#region Constructors

    public Storage() {}

    //#endregion

    //#region Configuration

    public Storage setAbsoluteTargetPath(String filepath) {
        targetFilepath = filepath;
        return this;
    }

    public Storage setRelativeTargetPath(String filepath) {
        targetFilepath = "./" + filepath;
        return this;
    }

    public <F> Storage setFormatter(ValueFormatter<F> formatter) {
        this.formatter = new ChosenFormatter<F>(formatter);
        return this;
    }

    public <W> Storage setFileWriter(FileWriter<W> fileWriter) {
        this.fileWriter = fileWriter;
        return this;
    }

    public <R> Storage setFileReader(FileReader<R> fileReader) {
        this.fileReader = fileReader;
        return this;
    }

    public <E> Storage setFileEditor(FileEditor<E> fileEditor) {
        this.fileWriter = fileEditor;
        this.fileReader = fileEditor;
        return this;
    }

    //#endregion
    
    //#region StreamStorage

    /**
     * 
     * @param storageObject
     * @param formatter Only set for execution of this function. Use setFormatter to use this for multiple executions.
     * @param outputStream
     * @throws UncheckedIOException
     */
    public <F> void convertAndStore(Storable storageObject, ValueFormatter<F> formatter, OutputStream outputStream) throws UncheckedIOException {
        try (ObjectOutputStream writer = new ObjectOutputStream(outputStream)) {
            writer.writeObject(
                formatter.format(storageObject.getStorageRepresentation())
            );
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void convertAndLoadInto(Loadable objectToLoad, InputStream inputStream) throws UncheckedIOException {
        convertAndLoadInto(objectToLoad, formatter, inputStream);
    }

    public <F> void convertAndLoadInto(Loadable objectToLoad, ValueFormatter<F> formatter, InputStream inputStream) throws UncheckedIOException, RuntimeException {
        Class<F> expectedValueType = TypeToken.<F>instantiate().getContainedClass();

        try (ObjectInputStream reader = new ObjectInputStream(inputStream)) {
            objectToLoad.load(
                formatter.parse(expectedValueType.cast(reader.readObject()))
            );
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        } 
        catch (UnsupportedValueException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void store(Storable storageObject, OutputStream outputStream) throws UncheckedIOException, RuntimeException {
        convertAndStore(storageObject, formatter, outputStream);
    }

    public <F> StorageValue<?> load(InputStream inputStream) {
        Class<F> expectedValueType = TypeToken.<F>instantiate().getContainedClass();

        try (ObjectInputStream reader = new ObjectInputStream(inputStream)) {
            return formatter.parse(expectedValueType.cast(reader.readObject()));
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        } 
        catch (UnsupportedValueException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //#endregion

    //#region FileStorage
    
    public void store(Storable storageObject) {
        store(storageObject.getStorageRepresentation());
    }

    public void store(Storable storageObject, String filePath) throws UncheckedIOException, RuntimeException {
        try (FileOutputStream fileStream = new FileOutputStream(filePath)) {
            convertAndStore(storageObject, formatter, fileStream);
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> void store(StorageValue<T> storageObject) {
        FileWriter<T> fileWriter;
        ValueFormatter<T> formatter;

        try {
            fileWriter = (FileWriter<T>)this.fileWriter;
            formatter = (ValueFormatter<T>)this.formatter;
        }
        catch (ClassCastException e) {
            throw e; 
        }

        fileWriter.store(
            targetFilepath, 
            formatter.format(storageObject)
        );
    }

    public void loadInto(Loadable targetObject) {
       targetObject.load(load());
    }

    public StorageValue<?> load() {
        return formatter.parse(fileReader.readFrom(targetFilepath));
    }

    //#endregion

    //#region ComponentWrappers
    
    private class ChosenFormatter <F> implements ValueFormatter<Object> {
        ValueFormatter<F> innerFormatter;
        Class<F> format;

        public ChosenFormatter(ValueFormatter<F> formatter) {
            innerFormatter = formatter;
        }

        @Override
        public F format(StorageValue<?> value) throws UnsupportedValueException {
            return innerFormatter.format(value);
        }

        @Override
        public StorageValue<?> parse(Object formattedData) throws UnsupportedValueException {
            return innerFormatter.parse(format.cast(formattedData));
        }
        
    }

    //#endregion
}
