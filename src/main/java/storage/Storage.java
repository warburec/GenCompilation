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

    /**
     * Gets the currently tagetted filepath being used for storage
     * @return The targeted filepath
     */
    public Path getTargetFilepath() {
        return targetFilepath;
    }

    //#endregion

    //#region Configuration

    /**
     * Sets the target filepath the be use for storage
     * @param filepath The filepath to target
     * @return This Storage object for method chaining
     */
    public Storage setTargetPath(Path filepath) {
        targetFilepath = filepath;
        return this;
    }

    /**
     * Sets the target filepath the be use for storage
     * @param filepath The absolute filepath to be targeted
     * @return This Storage object for method chaining
     */
    public Storage setAbsoluteTargetPath(String filepath) {
        targetFilepath = Path.of(filepath);
        return this;
    }

    /**
     * Sets the target filepath the be use for storage
     * @param filepath The relative filepath to be targeted
     * @return This Storage object for method chaining
     */
    public Storage setRelativeTargetPath(String filepath) {
        targetFilepath = Path.of("." + File.separator + filepath);
        return this;
    }

    /**
     * Sets the formatter to be used for converting StorageValues into storage representations
     * @param <F> The target format to convert values to/from
     * @param formatter The formatter to be used
     * @return This Storage object for method chaining
     */
    public <F> Storage setFormatter(ValueFormatter<F> formatter) {
        this.formatter = new ChosenFormatter<F>(formatter);
        return this;
    }

    /**
     * Sets the stream writer to be used for storing storage values
     * @param <W> The data format expected to be writen
     * @param streamWriter The stream writer to be selected
     * @return This Storage object for method chaining
     */
    public <W> Storage setStreamWriter(StreamWriter<W> streamWriter) {
        this.streamWriter = new ChosenStreamWriter<>(streamWriter);
        return this;
    }

    /**
     * Sets the stream reader to be used for reading values from storage
     * @param <R> The data format expected to be output when read
     * @param streamReader The stream reader to be selected
     * @return This Storage object for method chaining
     */
    public <R> Storage setStreamReader(StreamReader<R> streamReader) {
        this.streamReader = new ChosenStreamReader<>(streamReader);
        return this;
    }

    /**
     * Sets the stream reader to be used for reading and storing values in storage
     * @param <E> The data format expected to be read or stored
     * @param streamEditor The stream editor to be selected
     * @return This Storage object for method chaining
     */
    public <E> Storage setStreamEditor(StreamEditor<E> streamEditor) {
        this.streamWriter = new ChosenStreamWriter<>(streamEditor);
        this.streamReader = new ChosenStreamReader<>(streamEditor);
        return this;
    }

    //#endregion

    //#region ConvenienceMethods

    /**
     * Sets the file reader to be used for reading and storing values in storage.
     * An equvalent method to setStreamWriter(StreamWriter<W> streamWriter)
     * @param <W> The data format expected to be writen
     * @param fileWriter The file writer to be selected
     * @return This Storage object for method chaining
     */
    public <W> Storage setFileWriter(StreamWriter<W> fileWriter) {
        return setStreamWriter(fileWriter);
    }

    /**
     * Sets the file reader to be used for reading values from storage.
     * An equvalent method to setStreamReader(StreamReader<R> streamReader)
     * @param <R> The data format expected to be writen
     * @param fileReader The file reader to be selected
     * @return This Storage object for method chaining
     */
    public <R> Storage setFileReader(StreamReader<R> fileReader) {
        return setStreamReader(fileReader);
    }

    /**
     * Sets the stream reader to be used for reading and storing values in storage.
     * An equvalent method to setStreamEditor(StreamEditor<E> streamEditor)
     * @param <E> The data format expected to be read or stored
     * @param fileEditor The stream editor to be selected
     * @return This Storage object for method chaining
     */
    public <E> Storage setFileEditor(StreamEditor<E> fileEditor) {
        return setStreamEditor(fileEditor);
    }

    //#endregion
    
    //#region StreamStorage

    /**
     * Stores the given object to the given output stream, converting the object using the specified formatter to produce a storable data format
     * @param <F> The data format to be used for storage
     * @param storageObject The object to be stored
     * @param formatter The formatter to be used to convert values for storage. Only set for execution of this function. Use setFormatter to use this for multiple executions.
     * @param outputStream The stream to be used
     * @throws StoreFailureException
     * @throws FormattingException
     * @throws StorageFormatMismatchException
     */
    public <F> void convertAndStore(Storable storageObject, ValueFormatter<F> formatter, OutputStream outputStream) throws StoreFailureException, FormattingException, StorageFormatMismatchException {
        convertAndStore_Inner(storageObject.getStorageRepresentation(), new ChosenFormatter<F>(formatter), streamWriter, outputStream);
    }

    /**
     * Stores the given object in the given output stream
     * @param storageObject The object to be stored
     * @param outputStream The stream to be used
     * @throws StoreFailureException
     * @throws FormattingException
     * @throws StorageFormatMismatchException
     */
    public void store(Storable storageObject, OutputStream outputStream) throws StoreFailureException, FormattingException, StorageFormatMismatchException {
        convertAndStore_Inner(storageObject.getStorageRepresentation(), formatter, streamWriter, outputStream);
    }

    /**
     * Reads a value from the given input stream, converts the value using the chosen formatter and loads it into the given object
     * @param objectToLoad The object to load the value into
     * @param inputStream The input stream to be read
     * @throws LoadFailureException
     * @throws FormatParseException
     * @throws StorageFormatMismatchException
     */
    public void convertAndLoadInto(Loadable objectToLoad, InputStream inputStream) throws LoadFailureException, FormatParseException, StorageFormatMismatchException {
        objectToLoad.load(load_Inner(inputStream, formatter, streamReader));
    }

    /**
     * Reads a value from the given input stream, converts the value using the provided formatter and loads it into the given object
     * @param <F> The expected value format to be read and provided to the formatter
     * @param objectToLoad The object to load the value into 
     * @param formatter The formatter to be used to convert data from the stream
     * @param inputStream The input stream to be read
     * @throws LoadFailureException
     * @throws FormatParseException
     * @throws StorageFormatMismatchException
     */
    public <F> void convertAndLoadInto(Loadable objectToLoad, ValueFormatter<F> formatter, InputStream inputStream) throws LoadFailureException, FormatParseException, StorageFormatMismatchException {
        objectToLoad.load(load_Inner(inputStream, new ChosenFormatter<F>(formatter), streamReader));
    }

    /**
     * Loads a value from the given input stream
     * @param inputStream The input stream to read from
     * @return The value read from the stream, once formatted by the chosen formatter
     * @throws LoadFailureException
     * @throws FormatParseException
     * @throws StorageFormatMismatchException
     */
    public StorageValue<?> load(InputStream inputStream) throws LoadFailureException, FormatParseException, StorageFormatMismatchException {
        return load_Inner(inputStream, formatter, streamReader);
    }

    /**
     * Loads a value from the given input stream and specifies the expected type
     * @param <F> The expected inner type of the returned storage value
     * @param inputStream The input stream to be read
     * @return The value read from the stream, once formatted by the chosen formatter
     * @throws LoadFailureException
     * @throws FormatParseException
     * @throws StorageFormatMismatchException
     */
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
    
    /**
     * Stores the given object in the targetted file
     * @param storageObject The object to be stored
     * @throws UnsupportedValueException
     * @throws UncheckedIOException
     * @throws RuntimeException
     * @throws UncheckedIOException
     */
    public void store(Storable storageObject) throws UnsupportedValueException, UncheckedIOException, RuntimeException, UncheckedIOException {
        usingTargetFileOutputStream(
            (outputStream) -> convertAndStore_Inner(storageObject.getStorageRepresentation(), formatter, streamWriter, outputStream)
        );
    }

    /**
     * Stores the given object in the given file
     * @param storageObject The object to be stored
     * @param filePath The target file to use for storage
     * @throws UnsupportedValueException
     * @throws UncheckedIOException
     * @throws RuntimeException
     * @throws UncheckedIOException
     */
    public void store(Storable storageObject, String filePath) throws UnsupportedValueException, UncheckedIOException, RuntimeException, UncheckedIOException {
        usingFileOutputStream(
            new File(filePath),
            (outputStream) -> convertAndStore_Inner(storageObject.getStorageRepresentation(), formatter, streamWriter, outputStream)
        );
    }

    /**
     * Stores the given object in the targetted file
     * @param <T> The type of storage value to be stored
     * @param storageObject The object to be stored
     * @throws StoreFailureException
     * @throws FormattingException
     * @throws StorageFormatMismatchException
     * @throws UncheckedIOException
     */
    public <T> void store(StorageValue<T> storageObject) throws StoreFailureException, FormattingException, StorageFormatMismatchException, UncheckedIOException {
        usingTargetFileOutputStream(
            (outputStream) -> convertAndStore_Inner(storageObject, formatter, streamWriter, outputStream)
        );
    }

    /**
     * Loads a value from the chosen file
     * @return The loaded value
     * @throws LoadFailureException
     * @throws FormatParseException
     * @throws StorageFormatMismatchException
     * @throws UncheckedIOException
     */
    public StorageValue<?> load() throws LoadFailureException, FormatParseException, StorageFormatMismatchException, UncheckedIOException {
        return usingTargetFileInputStream(
            (inputStream) -> load(inputStream)
        );
    }

    /**
     * Loads a value from the chosen file into the given object
     * @param targetObject The object to load stored values into
     * @throws LoadFailureException
     * @throws FormatParseException
     * @throws StorageFormatMismatchException
     * @throws UncheckedIOException
     */
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
