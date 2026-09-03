package component_construction.storage;

import java.io.*;
import java.io.UncheckedIOException;
import java.nio.file.Path;

import component_construction.storage.factories.StorableCustomCompilerFactory;
import storage.Storage;
import storage.exceptions.*;
import storage.external_interfaces.Loadable;
import storage.file_editors.*;
import storage.storage_value_adapters.UnsupportedValueException;
import storage.storage_values.StorageValue;
import storage.value_formatters.ValueFormatter;

public class CompilerStorage {
    protected Storage storage = new Storage();
    protected StorableCustomCompilerFactory storableCustomCompilerFactory = new StorableCustomCompilerFactory();

    //#region Getters

    /**
     * Gets the currently tagetted filepath being used for storage
     * @return The targeted filepath
     */
    public Path getTargetFilepath() {
        return storage.getTargetFilepath();
    }

    //#endregion

    //#region Configuration

    /**
     * Sets the target filepath the be use for storage
     * @param filepath The filepath to target
     * @return This object for method chaining
     */
    public CompilerStorage setTargetPath(Path filepath) {
        storage.setTargetPath(filepath);
        return this;
    }

    /**
     * Sets the target filepath the be use for storage
     * @param filepath The absolute filepath to be targeted
     * @return This object for method chaining
     */
    public CompilerStorage setAbsoluteTargetPath(String filepath) {
        storage.setAbsoluteTargetPath(filepath);
        return this;
    }

    /**
     * Sets the target filepath the be use for storage
     * @param filepath The relative filepath to be targeted
     * @return This object for method chaining
     */
    public CompilerStorage setRelativeTargetPath(String filepath) {
        storage.setRelativeTargetPath(filepath);
        return this;
    }

    /**
     * Sets the formatter to be used for converting StorageValues into storage representations
     * @param <F> The target format to convert values to/from
     * @param formatter The formatter to be used
     * @return This object for method chaining
     */
    public <F> CompilerStorage setFormatter(ValueFormatter<F> formatter) {
        storage.setFormatter(formatter);
        return this;
    }

    /**
     * Sets the stream writer to be used for storing storage values
     * @param <W> The data format expected to be writen
     * @param streamWriter The stream writer to be selected
     * @return This object for method chaining
     */
    public <W> CompilerStorage setStreamWriter(StreamWriter<W> streamWriter) {
        storage.setStreamWriter(streamWriter);
        return this;
    }

    /**
     * Sets the stream reader to be used for reading values from storage
     * @param <R> The data format expected to be output when read
     * @param streamReader The stream reader to be selected
     * @return This object for method chaining
     */
    public <R> CompilerStorage setStreamReader(StreamReader<R> streamReader) {
        storage.setStreamReader(streamReader);
        return this;
    }

    /**
     * Sets the stream reader to be used for reading and storing values in storage
     * @param <E> The data format expected to be read or stored
     * @param streamEditor The stream editor to be selected
     * @return This object for method chaining
     */
    public <E> CompilerStorage setStreamEditor(StreamEditor<E> streamEditor) {
        storage.setStreamEditor(streamEditor);
        return this;
    }

    //#endregion

    //#region ConvenienceMethods

    /**
     * Sets the file reader to be used for reading and storing values in storage.
     * An equvalent method to setStreamWriter(StreamWriter<W> streamWriter)
     * @param <W> The data format expected to be writen
     * @param fileWriter The file writer to be selected
     * @return This object for method chaining
     */
    public <W> CompilerStorage setFileWriter(StreamWriter<W> fileWriter) {
        storage.setStreamWriter(fileWriter);
        return this;
    }

    /**
     * Sets the file reader to be used for reading values from storage.
     * An equvalent method to setStreamReader(StreamReader<R> streamReader)
     * @param <R> The data format expected to be writen
     * @param fileReader The file reader to be selected
     * @return This object for method chaining
     */
    public <R> CompilerStorage setFileReader(StreamReader<R> fileReader) {
        storage.setStreamReader(fileReader);
        return this;
    }

    /**
     * Sets the stream reader to be used for reading and storing values in storage.
     * An equvalent method to setStreamEditor(StreamEditor<E> streamEditor)
     * @param <E> The data format expected to be read or stored
     * @param fileEditor The stream editor to be selected
     * @return This object for method chaining
     */
    public <E> CompilerStorage setFileEditor(StreamEditor<E> fileEditor) {
        storage.setStreamEditor(fileEditor);
        return this;
    }

    //#endregion

    //#region StreamStorage

    /**
     * Stores a specified compiler in the given output stream
     * @param compiler The object to be stored
     * @param outputStream The stream to be used
     * @throws StoreFailureException
     * @throws FormattingException
     * @throws StorageFormatMismatchException
     * @throws NullStorageObjectException
     */
    public void store(StorableCustomCompiler compiler, OutputStream outputStream) throws StoreFailureException, FormattingException, StorageFormatMismatchException, NullStorageObjectException {
        storage.store(compiler, outputStream);
    }

    /**
     * Reads a value from the given input stream, converts the value using the chosen formatter and loads it into the given compiler
     * @param compiler An existing to load the value into
     * @param inputStream The input stream to be read
     * @throws LoadFailureException
     * @throws FormatParseException
     * @throws StorageFormatMismatchException
     */
    public void convertAndLoadInto(Loadable compiler, InputStream inputStream) throws LoadFailureException, FormatParseException, StorageFormatMismatchException {
        storage.convertAndLoadInto(compiler, inputStream);
    }

    /**
     * Loads a compiler from the given input stream
     * @param inputStream The input stream to read from
     * @return The compiler built for the stream, once formatted by the chosen formatter
     * @throws LoadFailureException
     * @throws FormatParseException
     * @throws StorageFormatMismatchException
     */
    public StorableCustomCompiler load(InputStream inputStream) throws LoadFailureException, FormatParseException, StorageFormatMismatchException {
        StorageValue<?> storageValue = storage.load(inputStream);
        return storableCustomCompilerFactory.produce(storageValue);
    }

    //#endregion

    //#region FileStorage
    
    /**
     * Stores the compiler in the targetted file
     * @param compiler The compiler to be stored
     * @throws UnsupportedValueException
     * @throws UncheckedIOException
     * @throws RuntimeException
     * @throws UncheckedIOException
     * @throws NullStorageObjectException
     */
    public void store(StorableCustomCompiler compiler) throws UnsupportedValueException, UncheckedIOException, RuntimeException, UncheckedIOException, NullStorageObjectException {
        storage.store(compiler);
    }

    /**
     * Stores the compiler in the given file
     * @param compiler The compiler to be stored
     * @param filePath The target file to use for storage
     * @throws UnsupportedValueException
     * @throws UncheckedIOException
     * @throws RuntimeException
     * @throws UncheckedIOException
     * @throws NullStorageObjectException
     */
    public void store(StorableCustomCompiler compiler, String filePath) throws UnsupportedValueException, UncheckedIOException, RuntimeException, UncheckedIOException, NullStorageObjectException {
        storage.store(compiler, filePath);
    }


    /**
     * Loads a compiler from the chosen file
     * @return The loaded compiler
     * @throws LoadFailureException
     * @throws FormatParseException
     * @throws StorageFormatMismatchException
     * @throws UncheckedIOException
     */
    public StorableCustomCompiler load() throws LoadFailureException, FormatParseException, StorageFormatMismatchException, UncheckedIOException {
        StorageValue<?> storageValue = storage.load();
        return storableCustomCompilerFactory.produce(storageValue);
    }

    /**
     * Loads a value from the chosen file into the given compiler
     * @param compiler An existing compiler to load stored values into
     * @throws LoadFailureException
     * @throws FormatParseException
     * @throws StorageFormatMismatchException
     * @throws UncheckedIOException
     */
    public void loadInto(Loadable compiler) throws LoadFailureException, FormatParseException, StorageFormatMismatchException, UncheckedIOException {
        storage.loadInto(compiler);
    }

    //#endregion
    
}
