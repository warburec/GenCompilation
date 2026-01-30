package storage;

import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.nio.file.Path;
import storage.file_editors.*;
import storage.storage_value_adapters.UnsupportedValueException;
import storage.storage_values.StorageValue;
import storage.value_formatters.*;

/**
 * A utility class for storage of compiler components.
 */
public class Storage {

    private Path targetFilepath = Path.of("." + File.separator + "compilerFile.txt");
    private ChosenFormatter<?> formatter = new ChosenFormatter<>(new ValueToStringFormatter());
    private ChosenStreamWriter streamWriter = new ChosenStreamWriter(new DefaultUTF8StreamEditor());
    private StreamReader<?> streamReader = new DefaultUTF8StreamEditor();

    //#region Constructors

    public Storage() {}

    //#endregion

    //#region Getters

    public Path getTargetFilepath() {
        return targetFilepath;
    }

    //#endregion

    //#region Configuration

    public Storage setTargetPath(Path filepath) {
        targetFilepath = filepath;
        return this;
    }

    public Storage setAbsoluteTargetPath(String filepath) {
        targetFilepath = Path.of(filepath);
        return this;
    }

    public Storage setRelativeTargetPath(String filepath) {
        targetFilepath = Path.of("." + File.separator + filepath);
        return this;
    }

    public <F> Storage setFormatter(ValueFormatter<F> formatter) {
        this.formatter = new ChosenFormatter<F>(formatter);
        return this;
    }

    public <W> Storage setStreamWriter(StreamWriter<W> streamWriter) {
        this.streamWriter = new ChosenStreamWriter(streamWriter);
        return this;
    }

    public <R> Storage setStreamReader(StreamReader<R> streamReader) {
        this.streamReader = streamReader;
        return this;
    }

    public <E> Storage setStreamEditor(StreamEditor<E> streamEditor) {
        this.streamWriter = new ChosenStreamWriter(streamEditor);
        this.streamReader = streamEditor;
        return this;
    }

    //#endregion

    //#region ConvenienceMethods

    /**
     * An equvalent method to setStreamWriter(StreamWriter<W> streamWriter)
     * @param <W>
     * @param fileWriter
     * @return
     */
    public <W> Storage setFileWriter(StreamWriter<W> fileWriter) {
        return setStreamWriter(fileWriter);
    }

    /**
     * An equvalent method to setStreamReader(StreamReader<R> streamReader)
     * @param <W>
     * @param fileWriter
     * @return
     */
    public <R> Storage setFileReader(StreamReader<R> fileReader) {
        return setStreamReader(fileReader);
    }

    /**
     * An equvalent method to setStreamEditor(StreamEditor<E> streamEditor)
     * @param <W>
     * @param fileWriter
     * @return
     */
    public <E> Storage setFileEditor(StreamEditor<E> fileEditor) {
        return setStreamEditor(fileEditor);
    }

    //#endregion
    
    //#region StreamStorage

    /**
     * 
     * @param <F>
     * @param storageObject
     * @param formatter Only set for execution of this function. Use setFormatter to use this for multiple executions.
     * @param outputStream
     * @throws UnsupportedValueException
     * @throws UncheckedIOException
     * @throws RuntimeException
     */
    public <F> void convertAndStore(Storable storageObject, ValueFormatter<F> formatter, OutputStream outputStream) throws UnsupportedValueException, UncheckedIOException, RuntimeException {
        try {
            streamWriter.addTo(
                outputStream, 
                formatter.format(storageObject.getStorageRepresentation())
            );
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void convertAndLoadInto(Loadable objectToLoad, InputStream inputStream) throws UncheckedIOException {
        convertAndLoadInto(objectToLoad, formatter, inputStream);
    }

    public <F> void convertAndLoadInto(Loadable objectToLoad, ValueFormatter<F> formatter, InputStream inputStream) throws UncheckedIOException, RuntimeException {
        Class<F> expectedValueType = TypeReference.<F>instantiate().getContainedClass();

        try (ObjectInputStream reader = new ObjectInputStream(inputStream)) {
            objectToLoad.load(
                formatter.parse(expectedValueType.cast(reader.readObject()))
            );
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        } 
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void store(Storable storageObject, OutputStream outputStream) throws UncheckedIOException, RuntimeException {
        convertAndStore(storageObject, formatter, outputStream);
    }

    @SuppressWarnings("unchecked")
    public <F> StorageValue<?> load(InputStream inputStream) throws UncheckedIOException, RuntimeException {
        try {
            F value = (F)streamReader.readFrom(inputStream);
            return formatter.parse(value);
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        } 
        catch (UnsupportedValueException e) {
            throw new RuntimeException(e);
        }
    }

    //#endregion

    //#region FileStorage
    
    public void store(Storable storageObject) throws UnsupportedValueException, UncheckedIOException, RuntimeException {
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

    public <T> void store(StorageValue<T> storageObject) throws UnsupportedValueException, UncheckedIOException, RuntimeException {
        try (FileOutputStream outputStream = new FileOutputStream(targetFilepath.toFile())) {
            streamWriter.addTo(
                outputStream, 
                formatter.format(storageObject)
            );
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void loadInto(Loadable targetObject) throws UnsupportedValueException, UncheckedIOException, RuntimeException {
       targetObject.load(load());
    }

    public StorageValue<?> load() throws UnsupportedValueException, UncheckedIOException {
        try (FileInputStream inputStream = new FileInputStream(targetFilepath.toFile())) {
            Object data = streamReader.readFrom(inputStream);
            return formatter.parse(data);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    //#endregion

    //#region ComponentWrappers
    
    private class ChosenFormatter <F> implements ValueFormatter<Object> {
        ValueFormatter<F> innerFormatter;
        Class<F> format;

        @SuppressWarnings("unchecked")
        public ChosenFormatter(ValueFormatter<F> formatter) {
            innerFormatter = formatter;
            ParameterizedType formatterType = (ParameterizedType)(formatter.getClass().getGenericInterfaces()[0]);
            format = (Class<F>)formatterType.getActualTypeArguments()[0];
        }

        @Override
        public F format(StorageValue<?> value) throws UnsupportedValueException {
            return innerFormatter.format(value);
        }

        @Override
        public StorageValue<?> parse(Object formattedData) throws UnsupportedValueException {
            F data = format.cast(formattedData);
            return innerFormatter.parse(data);
        }
    }

    private class ChosenStreamWriter {
        StreamWriter<?> innerStreamWriter;

        public ChosenStreamWriter(StreamWriter<?> streamWriter) {
            innerStreamWriter = streamWriter;
        }

        @SuppressWarnings("unchecked")
        public <F> void addTo(OutputStream stream, F contents) throws IOException, RuntimeException {
            Class<F> typeReference = TypeReference.<F>instantiate().getContainedClass();
            StreamWriter<F> streamWriter;

            try {
                streamWriter = (StreamWriter<F>)innerStreamWriter;
            } catch (ClassCastException e) {
                throw new RuntimeException("The type " + typeReference.getName() + " could not be used by the file writer or formatter. Please check you are using the correct storage component and expecting the correct storage value type.");
            }

            streamWriter.addTo(stream, contents);
        }
    }

    //#endregion
}
