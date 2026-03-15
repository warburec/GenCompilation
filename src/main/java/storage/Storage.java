package storage;

import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.nio.file.Path;

import storage.exceptions.*;
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
    private ChosenStreamWriter<?> streamWriter = new ChosenStreamWriter<>(new DefaultUTF8StreamEditor());
    private ChosenStreamReader<?> streamReader = new ChosenStreamReader<>(new DefaultUTF8StreamEditor());

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
        this.streamWriter = new ChosenStreamWriter<>(streamWriter);
        return this;
    }

    public <R> Storage setStreamReader(StreamReader<R> streamReader) {
        this.streamReader = new ChosenStreamReader<>(streamReader);
        return this;
    }

    public <E> Storage setStreamEditor(StreamEditor<E> streamEditor) {
        this.streamWriter = new ChosenStreamWriter<>(streamEditor);
        this.streamReader = new ChosenStreamReader<>(streamEditor);
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
    public <F> void convertAndStore(Storable storageObject, ValueFormatter<F> formatter, OutputStream outputStream) throws StoreFailureException, FormattingException, StorageFormatMismatchException {
        convertAndStore_Inner(storageObject.getStorageRepresentation(), new ChosenFormatter<F>(formatter), streamWriter, outputStream);
    }

    public void store(Storable storageObject, OutputStream outputStream) throws StoreFailureException, FormattingException, StorageFormatMismatchException {
        convertAndStore_Inner(storageObject.getStorageRepresentation(), formatter, streamWriter, outputStream);
    }

    public void convertAndLoadInto(Loadable objectToLoad, InputStream inputStream) throws LoadFailureException, FormatParseException, StorageFormatMismatchException {
        objectToLoad.load(load_Inner(inputStream, formatter, streamReader));
    }

    public <F> void convertAndLoadInto(Loadable objectToLoad, ValueFormatter<F> formatter, InputStream inputStream) throws LoadFailureException, FormatParseException, StorageFormatMismatchException {
        objectToLoad.load(load_Inner(inputStream, new ChosenFormatter<F>(formatter), streamReader));
    }

    public StorageValue<?> load(InputStream inputStream) throws LoadFailureException, FormatParseException, StorageFormatMismatchException {
        return load_Inner(inputStream, formatter, streamReader);
    }

    //TODO: Test
    @SuppressWarnings("unchecked")
    public <F> StorageValue<F> loadWithType(InputStream inputStream) throws LoadFailureException, FormatParseException, StorageFormatMismatchException {
        try {
            return (StorageValue<F>)load(inputStream);
        }
        catch (ClassCastException e) {  
            Class<F> chosenType = TypeReference.<F>instantiate().getContainedClass();

            throw new ClassCastException(
                "The format " 
                + chosenType.getSimpleName() 
                + " is inconsistant with the type " 
                + formatter.format.getSimpleName()
                + " produced by the chosen formatter."
            );
        }
    }

    //#endregion

    //#region FileStorage
    
    public void store(Storable storageObject) throws UnsupportedValueException, UncheckedIOException, RuntimeException, UncheckedIOException {
        usingTargetFileOutputStream(
            (outputStream) -> convertAndStore_Inner(storageObject.getStorageRepresentation(), formatter, streamWriter, outputStream)
        );
    }

    public void store(Storable storageObject, String filePath) throws UnsupportedValueException, UncheckedIOException, RuntimeException, UncheckedIOException {
        usingFileOutputStream(
            new File(filePath),
            (outputStream) -> convertAndStore_Inner(storageObject.getStorageRepresentation(), formatter, streamWriter, outputStream)
        );
    }

    public <T> void store(StorageValue<T> storageObject) throws StoreFailureException, FormattingException, StorageFormatMismatchException, UncheckedIOException {
        usingTargetFileOutputStream(
            (outputStream) -> convertAndStore_Inner(storageObject, formatter, streamWriter, outputStream)
        );
    }

    public StorageValue<?> load() throws LoadFailureException, FormatParseException, StorageFormatMismatchException, UncheckedIOException {
        return usingTargetFileInputStream(
            (inputStream) -> load(inputStream)
        );
    }

    public void loadInto(Loadable targetObject) throws LoadFailureException, FormatParseException, StorageFormatMismatchException, UncheckedIOException {
        targetObject.load(load());
    }

    //#endregion

    //#region StorageOperations

    private StorageValue<?> load_Inner(InputStream inputStream, ChosenFormatter<?> formatter, ChosenStreamReader<?> streamReader) throws LoadFailureException, FormatParseException, StorageFormatMismatchException {
        checkForFormatMismatch(formatter, streamReader);

        Object format;

        try {
            format = streamReader.readFrom(inputStream);
        }
        catch (Exception e) {
            throw new LoadFailureException(e);
        }

        try {
            return formatter.parse(format);
        }
        catch (Exception e) {
            throw new FormatParseException(e);
        }
    }

    private void convertAndStore_Inner(StorageValue<?> storageObject, ChosenFormatter<?> formatter, ChosenStreamWriter<?> streamWriter, OutputStream outputStream) throws StoreFailureException, FormattingException, StorageFormatMismatchException {
        checkForFormatMismatch(formatter, streamWriter);
        
        Object format;

        try {
            format = formatter.format(storageObject);
        }
        catch (Exception e) {
            throw new FormattingException(e);
        }

        try {
            streamWriter.addTo(outputStream, format);
        } catch (Exception e) {
            throw new StoreFailureException(e);
        }
    }

    //#endregion
    
    //#region ComponentWrappers

    private abstract class FormatHolder <F> {
        public Class<F> format;

        @SuppressWarnings("unchecked")
        public FormatHolder(Object genericTarget) {
            ParameterizedType targetType = (ParameterizedType)(genericTarget.getClass().getGenericInterfaces()[0]);
            format = (Class<F>)targetType.getActualTypeArguments()[0];
        }
    }
    
    private class ChosenFormatter <F> extends FormatHolder<F> implements ValueFormatter<Object> {
        ValueFormatter<F> innerFormatter;

        public ChosenFormatter(ValueFormatter<F> formatter) {
            super(formatter);
            innerFormatter = formatter;
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

    private class ChosenStreamWriter <F> extends FormatHolder<F> {
        StreamWriter<F> innerStreamWriter;

        public ChosenStreamWriter(StreamWriter<F> streamWriter) {
            super(streamWriter);
            innerStreamWriter = streamWriter;
        }

        public void addTo(OutputStream stream, Object contents) throws IOException, RuntimeException {
            innerStreamWriter.addTo(stream, format.cast(contents));
        }
    }

    private class ChosenStreamReader <F> extends FormatHolder<F> {
        StreamReader<F> innerStreamReader;

        public ChosenStreamReader(StreamReader<F> streamReader) {
            super(streamReader);
            innerStreamReader = streamReader;
        }

        public F readFrom(InputStream stream) throws IOException {
            return innerStreamReader.readFrom(stream);
        }
    }

    //#endregion

    //#region Helpers

    private void checkForFormatMismatch(FormatHolder<?> holder1, FormatHolder<?> holder2) throws StorageFormatMismatchException {
        if (holder1.format.equals(holder2.format)) return;
        
        throw new StorageFormatMismatchException(
            holder1.format.getSimpleName(), 
            holder2.format.getSimpleName()
        );
    }

    private interface OutputStreamReceiver {
        void run(OutputStream outputStream);
    }

    private interface InputStreamReceiver <T> {
        T run(InputStream inputStream);
    }

    private void usingFileOutputStream(File file, OutputStreamReceiver function) throws UncheckedIOException {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            function.run(outputStream);
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void usingTargetFileOutputStream(OutputStreamReceiver function) throws UncheckedIOException {
        usingFileOutputStream(targetFilepath.toFile(), function);
    }

    private <T> T usingFileInputStream(File file, InputStreamReceiver<T> function) throws UncheckedIOException {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            return function.run(inputStream);
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private <T> T usingTargetFileInputStream(InputStreamReceiver<T> function) throws UncheckedIOException {
        return usingFileInputStream(targetFilepath.toFile(), function);
    }
    
    //#endregion
}
