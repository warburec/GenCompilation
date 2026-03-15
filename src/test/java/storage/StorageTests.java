package storage;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.lang.annotation.*;
import java.nio.file.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.io.TempDir;

import storage.exceptions.*;
import storage.storage_values.*;
import storage.value_formatters.*;
import test_aids.exceptions.ExampleException;
import test_aids.test_storage.*;

@ExtendWith(StorageTests.UseTestFileExtension.class)
public class StorageTests {

    @TempDir
    Path storageTestPath;

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    protected @interface UseTestFile {}

    public static class UseTestFileExtension implements ParameterResolver {
        @Override
        public boolean supportsParameter(ParameterContext pc, ExtensionContext ec) {
            return ec
                .getTestMethod()
                .get()
                .isAnnotationPresent(UseTestFile.class);
        }

        @Override
        public Path resolveParameter(ParameterContext pc, ExtensionContext ec) {
            try {
                StorageTests testInstance = (StorageTests) ec.getRequiredTestInstance();
                Path tempDir = testInstance.storageTestPath;
                return createTestFile(tempDir);
            } 
            catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        protected Path createTestFile(Path tempDir) throws IOException {
            String fileName = "testFile.txt";
            return Files.createFile(tempDir.resolve(fileName));
        }
    }

    @Test
    public void setTargetPath() {
        Path expectedPath = Path.of("./testPath");
        Storage storage = new Storage();


        storage.setTargetPath(expectedPath);
        Path actualPath = storage.getTargetFilepath();


        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void setAbsoluteTargetPath() {
        Path expectedPath = Path.of("testPath");
        Storage storage = new Storage();


        storage.setAbsoluteTargetPath(expectedPath.toString());
        Path actualPath = storage.getTargetFilepath();


        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void setRelativeTargetPath() {
        String fileName = "testPath.txt";
        Storage storage = new Storage();
        

        storage.setRelativeTargetPath(fileName);
        Path actualPath = storage.getTargetFilepath();
        

        Path expectedPath = Path.of("./" + fileName);
        assertEquals(expectedPath, actualPath);
    }

    @Test
    @UseTestFile
    public void storeString(Path testFile) throws IOException {
        String expectedString = "testString";
        StorageValue<?> storageValue = new TestStorageValue(expectedString);
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringValueFormatter());


        storage.store(storageValue);
        String actualString = Files.readString(testFile);


        assertEquals(expectedString, actualString);
    }

    @Test
    @UseTestFile
    public void storeInteger(Path testFile) throws IOException {
        int expectedInt = 10;
        StorageValue<?> storageValue = new TestStorageValue(expectedInt);
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringValueFormatter());


        storage.store(storageValue);
        int actualString = Integer.parseInt(Files.readString(testFile));


        assertEquals(expectedInt, actualString);
    }

    @Test
    @UseTestFile
    public void storeString_IncorrectFormatter(Path testFile) throws IOException {
        String expectedString = "testString";
        StorageValue<?> storageValue = new TestStorageValue(expectedString);
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestIntegerValueFormatter());


        assertThrows(StorageFormatMismatchException.class, () -> storage.store(storageValue));
    }

    @Test
    @UseTestFile
    public void storeString_FileWriteError(Path testFile) throws IOException {
        String expectedString = "testString";
        StorageValue<?> storageValue = new TestStorageValue(expectedString);
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringErrorStreamEditor())
            .setFormatter(new TestStringValueFormatter());

        
        StoreFailureException exception = assertThrows(
            StoreFailureException.class,
            () -> storage.store(storageValue)
        );
        assertInstanceOf(ExampleException.class, exception.getCause());
    }

    @Test
    @UseTestFile
    public void storeString_FormatterError(Path testFile) throws IOException {
        String expectedString = "testString";
        StorageValue<?> storageValue = new TestStorageValue(expectedString);
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringErrorValueFormatter());


        FormattingException exception = assertThrows(
            FormattingException.class,
            () -> storage.store(storageValue)
        );
        assertInstanceOf(ExampleException.class, exception.getCause());
    }

    @Test
    @UseTestFile
    public void loadString(Path testFile) throws IOException {
        String testString = "testString";
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringValueFormatter());

        Files.writeString(testFile, testString);


        StorageValue<?> actualStorageValue = storage.load();

        
        StorageValue<?> expectedStorageValue = new TestStorageValue(testString);
        assertEquals(expectedStorageValue, actualStorageValue);
    }

    @Test
    @UseTestFile
    public void loadInteger(Path testFile) throws IOException {
        int testInteger = 10;
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestIntegerStreamEditor())
            .setFormatter(new TestIntegerValueFormatter());

        Files.writeString(testFile, Integer.toString(testInteger));


        StorageValue<?> actualStorageValue = storage.load();

        
        StorageValue<?> expectedStorageValue = new TestStorageValue(testInteger);
        assertEquals(expectedStorageValue, actualStorageValue);
    }

    @Test
    @UseTestFile
    public void loadEmptyString(Path testFile) throws IOException {
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringValueFormatter());

        StorageValue<?> actualStorageValue = storage.load();

        
        StorageValue<?> expectedStorageValue = new TestStorageValue("");
        assertEquals(expectedStorageValue, actualStorageValue);
    }

    @Test
    @UseTestFile
    public void loadEmptyInteger(Path testFile) throws IOException {
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestIntegerStreamEditor())
            .setFormatter(new TestIntegerValueFormatter());

        
        LoadFailureException exception = assertThrows(
            LoadFailureException.class,
            () -> storage.load()
        );
        assertInstanceOf(NumberFormatException.class, exception.getCause());
    }

    @Test
    @UseTestFile
    public void loadString_IncorrectFormatter(Path testFile) throws IOException {
        String testString = "testString";
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestIntegerValueFormatter());

        Files.writeString(testFile, testString);


        assertThrows(StorageFormatMismatchException.class, () -> storage.load());
    }

    @Test
    @UseTestFile
    public void loadString_FileWriteError(Path testFile) throws IOException {
        String testString = "testString";
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringErrorStreamEditor())
            .setFormatter(new TestStringValueFormatter());

        Files.writeString(testFile, testString);

        
        LoadFailureException exception = assertThrows(
            LoadFailureException.class,
            () -> storage.load()
        );
        assertInstanceOf(ExampleException.class, exception.getCause());
    }

    @Test
    @UseTestFile
    public void loadString_FormatterError(Path testFile) throws IOException {
        String testString = "testString";
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringErrorValueFormatter());

        Files.writeString(testFile, testString);


        FormatParseException exception = assertThrows(
            FormatParseException.class,
            () -> storage.load()
        );
        assertInstanceOf(ExampleException.class, exception.getCause());
    }

    @Test
    @UseTestFile
    void convertAndStoreString(Path testFile)  throws IOException {
        String expectedString = "testString";

        Storable storable = new TestStorable(expectedString);
        ValueFormatter<String> formatter = new TestStringValueFormatter();
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor());

        OutputStream outputStream = new FileOutputStream(testFile.toFile());


        try (outputStream) {
            storage.convertAndStore(storable, formatter, outputStream);
        }
        
        
        String actualString = Files.readString(testFile);
        assertEquals(expectedString, actualString);
    }

    @Test
    @UseTestFile
    void convertAndStoreInteger(Path testFile)  throws IOException {
        int expectedInteger = 10;

        Storable storable = new TestStorable(expectedInteger);
        ValueFormatter<Integer> formatter = new TestIntegerValueFormatter();
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestIntegerStreamEditor());

        OutputStream outputStream = new FileOutputStream(testFile.toFile());


        try (outputStream) {
            storage.convertAndStore(storable, formatter, outputStream);
        }
        
        
        int actualInteger = Integer.parseInt(Files.readString(testFile));
        assertEquals(expectedInteger, actualInteger);
    }

    @Test
    @UseTestFile
    public void convertAndStoreString_IncorrectFormatter(Path testFile) throws IOException {
        String expectedString = "testString";

        Storable storable = new TestStorable(expectedString);
        ValueFormatter<Integer> formatter = new TestIntegerValueFormatter();
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor());

        OutputStream outputStream = new FileOutputStream(testFile.toFile());

        try (outputStream) {
            assertThrows(StorageFormatMismatchException.class, () -> storage.convertAndStore(storable, formatter, outputStream));
        }
    }

    @Test
    @UseTestFile
    public void convertAndStoreString_FileWriteError(Path testFile) throws IOException {
        String expectedString = "testString";

        Storable storable = new TestStorable(expectedString);
        ValueFormatter<String> formatter = new TestStringValueFormatter();
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringErrorStreamEditor());

        OutputStream outputStream = new FileOutputStream(testFile.toFile());

        try (outputStream) {
            StoreFailureException exception = assertThrows(
                StoreFailureException.class,
                () -> storage.convertAndStore(storable, formatter, outputStream)
            );
            assertInstanceOf(ExampleException.class, exception.getCause());
        }
    }

    @Test
    @UseTestFile
    public void convertAndStoreString_FormatterError(Path testFile) throws IOException {
        String expectedString = "testString";

        Storable storable = new TestStorable(expectedString);
        ValueFormatter<String> formatter = new TestStringErrorValueFormatter();
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor());

        OutputStream outputStream = new FileOutputStream(testFile.toFile());

        try (outputStream) {
            FormattingException exception = assertThrows(
                FormattingException.class,
                () -> storage.convertAndStore(storable, formatter, outputStream)
            );
            assertInstanceOf(ExampleException.class, exception.getCause());
        }
    }

    @Test
    @UseTestFile
    void convertAndLoadStringInto(Path testFile)  throws IOException {
        String expectedString = "testString";

        TestLoadable loadable = new TestLoadable();
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFormatter(new TestStringValueFormatter())
            .setFileEditor(new TestStringStreamEditor());

        Files.writeString(testFile, expectedString);

        InputStream inputStream = new FileInputStream(testFile.toFile());


        try (inputStream) {
            storage.convertAndLoadInto(loadable, inputStream);
        }
    

        assertEquals(expectedString, loadable.getValue());
    }

    @Test
    @UseTestFile
    void convertAndLoadIntegerInto(Path testFile)  throws IOException {
        int expectedInteger= 10;

        TestLoadable loadable = new TestLoadable();
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFormatter(new TestIntegerValueFormatter())
            .setFileEditor(new TestIntegerStreamEditor());

        Files.writeString(testFile, Integer.toString(expectedInteger));

        InputStream inputStream = new FileInputStream(testFile.toFile());


        try (inputStream) {
            storage.convertAndLoadInto(loadable, inputStream);
        }
    

        assertEquals(expectedInteger, loadable.getValue());
    }

    @Test
    @UseTestFile
    public void convertAndLoadStringInto_IncorrectFormatter(Path testFile) throws IOException {
        String expectedString = "testString";

        TestLoadable loadable = new TestLoadable();
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFormatter(new TestIntegerValueFormatter())
            .setFileEditor(new TestStringStreamEditor());

        Files.writeString(testFile, expectedString);

        InputStream inputStream = new FileInputStream(testFile.toFile());


        try (inputStream) {
            assertThrows(StorageFormatMismatchException.class, () -> storage.convertAndLoadInto(loadable, inputStream));
        }
    }

    @Test
    @UseTestFile
    public void convertAndLoadStringInto_FileWriteError(Path testFile) throws IOException {
        String expectedString = "testString";

        TestLoadable loadable = new TestLoadable();
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFormatter(new TestStringValueFormatter())
            .setFileEditor(new TestStringErrorStreamEditor());

        Files.writeString(testFile, expectedString);

        InputStream inputStream = new FileInputStream(testFile.toFile());


        try (inputStream) {
            LoadFailureException exception = assertThrows(
                LoadFailureException.class,
                () -> storage.convertAndLoadInto(loadable, inputStream)
            );
            assertInstanceOf(ExampleException.class, exception.getCause());
        }
    }

    @Test
    @UseTestFile
    public void convertAndLoadStringInto_FormatterError(Path testFile) throws IOException {
        String expectedString = "testString";

        TestLoadable loadable = new TestLoadable();
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFormatter(new TestStringErrorValueFormatter())
            .setFileEditor(new TestStringStreamEditor());

        Files.writeString(testFile, expectedString);

        InputStream inputStream = new FileInputStream(testFile.toFile());


        try (inputStream) {
            FormatParseException exception = assertThrows(
                FormatParseException.class,
                () -> storage.convertAndLoadInto(loadable, inputStream)
            );
            assertInstanceOf(ExampleException.class, exception.getCause());
        }
    }

    @Test
    @UseTestFile
    void convertThenFormatAndLoadStringInto(Path testFile)  throws IOException {
        String expectedString = "testString";

        TestLoadable loadable = new TestLoadable();
        ValueFormatter<?> formatter = new TestStringValueFormatter();
        
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor());

        Files.writeString(testFile, expectedString);

        InputStream inputStream = new FileInputStream(testFile.toFile());


        try (inputStream) {
            storage.convertAndLoadInto(loadable, formatter, inputStream);
        }
    
        assertEquals(expectedString, loadable.getValue());
    }

    @Test
    @UseTestFile
    void convertThenFormatAndLoadIntegerInto(Path testFile)  throws IOException {
        int expectedInteger = 10;

        TestLoadable loadable = new TestLoadable();
        ValueFormatter<?> formatter = new TestIntegerValueFormatter();
        
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestIntegerStreamEditor());

        Files.writeString(testFile, Integer.toString(expectedInteger));

        InputStream inputStream = new FileInputStream(testFile.toFile());


        try (inputStream) {
            storage.convertAndLoadInto(loadable, formatter, inputStream);
        }
    
        assertEquals(expectedInteger, loadable.getValue());
    }

    @Test
    @UseTestFile
    public void convertThenFormatAndLoadStringInto_IncorrectFormatter(Path testFile) throws IOException {
        String expectedString = "testString";

        TestLoadable loadable = new TestLoadable();
        ValueFormatter<?> formatter = new TestIntegerValueFormatter();
        
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor());

        Files.writeString(testFile, expectedString);

        InputStream inputStream = new FileInputStream(testFile.toFile());


        try (inputStream) {
            assertThrows(StorageFormatMismatchException.class, () -> storage.convertAndLoadInto(loadable, formatter, inputStream));
        }
    }

    @Test
    @UseTestFile
    public void convertThenFormatAndLoadStringInto_FileWriteError(Path testFile) throws IOException {
        String expectedString = "testString";

        TestLoadable loadable = new TestLoadable();
        ValueFormatter<?> formatter = new TestStringValueFormatter();
        
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringErrorStreamEditor());

        Files.writeString(testFile, expectedString);

        InputStream inputStream = new FileInputStream(testFile.toFile());


        try (inputStream) {
            LoadFailureException exception = assertThrows(
                LoadFailureException.class,
                () -> storage.convertAndLoadInto(loadable, formatter, inputStream)
            );
            assertInstanceOf(ExampleException.class, exception.getCause());
        }
    }

    @Test
    @UseTestFile
    public void convertThenFormatAndLoadStringInto_FormatterError(Path testFile) throws IOException {
        String expectedString = "testString";

        TestLoadable loadable = new TestLoadable();
        ValueFormatter<?> formatter = new TestStringErrorValueFormatter();
        
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor());

        Files.writeString(testFile, expectedString);

        InputStream inputStream = new FileInputStream(testFile.toFile());


        try (inputStream) {
            FormatParseException exception = assertThrows(
                FormatParseException.class,
                () -> storage.convertAndLoadInto(loadable, formatter, inputStream)
            );
            assertInstanceOf(ExampleException.class, exception.getCause());
        }
    }

    @Test
    @UseTestFile
    public void storeStringToStream(Path testFile) throws IOException {
        String expectedString = "testString";
        Storable storable = new TestStorable(expectedString);
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringValueFormatter());

        OutputStream outputStream = new FileOutputStream(testFile.toFile());


        try (outputStream) {
            storage.store(storable, outputStream);
        }
        
        
        String actualString = Files.readString(testFile);
        assertEquals(expectedString, actualString);
    }

    @Test
    @UseTestFile
    public void storeIntegerToStream(Path testFile) throws IOException {
        int expectedInteger = 10;
        Storable storable = new TestStorable(expectedInteger);
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestIntegerStreamEditor())
            .setFormatter(new TestIntegerValueFormatter());

        OutputStream outputStream = new FileOutputStream(testFile.toFile());


        try (outputStream) {
            storage.store(storable, outputStream);
        }
        
        
        int actualInteger = Integer.parseInt(Files.readString(testFile));
        assertEquals(expectedInteger, actualInteger);
    }

    @Test
    @UseTestFile
    public void storeStringToStream_IncorrectFormatter(Path testFile) throws IOException {
        String expectedString = "testString";
        Storable storable = new TestStorable(expectedString);
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestIntegerValueFormatter());

        OutputStream outputStream = new FileOutputStream(testFile.toFile());


        try (outputStream) {
            assertThrows(StorageFormatMismatchException.class, () -> storage.store(storable, outputStream));
        }
    }

    @Test
    @UseTestFile
    public void storeStringToStream_FileWriteError(Path testFile) throws IOException {
        String expectedString = "testString";
        Storable storable = new TestStorable(expectedString);
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringErrorStreamEditor())
            .setFormatter(new TestStringValueFormatter());

        OutputStream outputStream = new FileOutputStream(testFile.toFile());


        try (outputStream) {
            StoreFailureException exception = assertThrows(
                StoreFailureException.class,
                () -> storage.store(storable, outputStream)
            );
            assertInstanceOf(ExampleException.class, exception.getCause());
        }
    }

    @Test
    @UseTestFile
    public void storeStringToStream_FormatterError(Path testFile) throws IOException {
        String expectedString = "testString";
        Storable storable = new TestStorable(expectedString);
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringErrorValueFormatter());

        OutputStream outputStream = new FileOutputStream(testFile.toFile());


        try (outputStream) {
            FormattingException exception = assertThrows(
                FormattingException.class,
                () -> storage.store(storable, outputStream)
            );
            assertInstanceOf(ExampleException.class, exception.getCause());
        }
    }

    @Test
    @UseTestFile
    public void loadStringFromInputStream(Path testFile) throws IOException {
        String testString = "testString";
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringValueFormatter());

        Files.writeString(testFile, testString);

        InputStream inputStream = new FileInputStream(testFile.toFile());


        StorageValue<?> actualStorageValue = storage.load(inputStream);

        
        StorageValue<?> expectedStorageValue = new TestStorageValue(testString);
        assertEquals(expectedStorageValue, actualStorageValue);
    }

    @Test
    @UseTestFile
    public void loadIntegerFromInputStream(Path testFile) throws IOException {
        int testInteger = 10;
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestIntegerStreamEditor())
            .setFormatter(new TestIntegerValueFormatter());

        Files.writeString(testFile, Integer.toString(testInteger));

        InputStream inputStream = new FileInputStream(testFile.toFile());


        StorageValue<?> actualStorageValue = storage.load(inputStream);

        
        StorageValue<?> expectedStorageValue = new TestStorageValue(testInteger);
        assertEquals(expectedStorageValue, actualStorageValue);
    }

    @Test
    @UseTestFile
    public void loadStringFromInputStream_IncorrectFormatter(Path testFile) throws IOException {
        String testString = "testString";
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestIntegerValueFormatter());

        Files.writeString(testFile, testString);

        InputStream inputStream = new FileInputStream(testFile.toFile());


        try (inputStream) {
            assertThrows(StorageFormatMismatchException.class, () -> storage.load(inputStream));
        }
    }

    @Test
    @UseTestFile
    public void loadStringFromInputStream_FileWriteError(Path testFile) throws IOException {
        String testString = "testString";
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringErrorStreamEditor())
            .setFormatter(new TestStringValueFormatter());

        Files.writeString(testFile, testString);

        InputStream inputStream = new FileInputStream(testFile.toFile());


        try (inputStream) {
            LoadFailureException exception = assertThrows(
                LoadFailureException.class,
                () -> storage.load(inputStream)
            );
            assertInstanceOf(ExampleException.class, exception.getCause());
        }
    }

    @Test
    @UseTestFile
    public void loadStringFromInputStream_FormatterError(Path testFile) throws IOException {
        String testString = "testString";
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringErrorValueFormatter());

        Files.writeString(testFile, testString);

        InputStream inputStream = new FileInputStream(testFile.toFile());


        try (inputStream) {
            FormatParseException exception = assertThrows(
                FormatParseException.class,
                () -> storage.load(inputStream)
            );
            assertInstanceOf(ExampleException.class, exception.getCause());
        }
    }

    @Test
    @UseTestFile
    public void storeStorableOfString(Path testFile) throws IOException {
        String expectedString = "testString";
        Storable storable = new TestStorable(expectedString);
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringValueFormatter());


        storage.store(storable);
        String actualString = Files.readString(testFile);


        assertEquals(expectedString, actualString);
    }

    @Test
    @UseTestFile
    public void storeStorableOfInteger(Path testFile) throws IOException {
        int expectedInteger = 10;
        Storable storable = new TestStorable(expectedInteger);
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestIntegerStreamEditor())
            .setFormatter(new TestIntegerValueFormatter());


        storage.store(storable);
        int actualInteger = Integer.parseInt(Files.readString(testFile));


        assertEquals(expectedInteger, actualInteger);
    }

    @Test
    @UseTestFile
    public void storeStorableOfString_IncorrectFormatter(Path testFile) throws IOException {
        String expectedString = "testString";
        Storable storable = new TestStorable(expectedString);
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestIntegerValueFormatter());


        assertThrows(StorageFormatMismatchException.class, () -> storage.store(storable));
    }

    @Test
    @UseTestFile
    public void storeStorableOfString_FileWriteError(Path testFile) throws IOException {
        String expectedString = "testString";
        Storable storable = new TestStorable(expectedString);
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringErrorStreamEditor())
            .setFormatter(new TestStringValueFormatter());


        StoreFailureException exception = assertThrows(
            StoreFailureException.class,
            () -> storage.store(storable)
        );
        assertInstanceOf(ExampleException.class, exception.getCause());
    }

    @Test
    @UseTestFile
    public void storeStorableOfString_FormatterError(Path testFile) throws IOException {
        String expectedString = "testString";
        Storable storable = new TestStorable(expectedString);
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringErrorValueFormatter());


        FormattingException exception = assertThrows(
            FormattingException.class,
            () -> storage.store(storable)
        );
        assertInstanceOf(ExampleException.class, exception.getCause());
    }

    @Test
    @UseTestFile
    public void storeStorableOfStringAtFilepath(Path testFile) throws IOException {
        String expectedString = "testString";
        Storable storable = new TestStorable(expectedString);
        Storage storage = new Storage()
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringValueFormatter());


        storage.store(storable, testFile.toString());
        String actualString = Files.readString(testFile);


        assertEquals(expectedString, actualString);
    }

    @Test
    @UseTestFile
    public void storeStorableOfIntegerAtFilepath(Path testFile) throws IOException {
        int expectedInteger = 10;
        Storable storable = new TestStorable(expectedInteger);
        Storage storage = new Storage()
            .setFileEditor(new TestIntegerStreamEditor())
            .setFormatter(new TestIntegerValueFormatter());


        storage.store(storable, testFile.toString());
        int actualInteger = Integer.parseInt(Files.readString(testFile));


        assertEquals(expectedInteger, actualInteger);
    }

    @Test
    @UseTestFile
    public void storeStorableOfStringAtFilepath_IncorrectFormatter(Path testFile) throws IOException {
        String expectedString = "testString";
        Storable storable = new TestStorable(expectedString);
        Storage storage = new Storage()
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestIntegerValueFormatter());


        assertThrows(StorageFormatMismatchException.class, () -> storage.store(storable, testFile.toString()));
    }

    @Test
    @UseTestFile
    public void storeStorableOfStringAtFilepath_FileWriteError(Path testFile) throws IOException {
        String expectedString = "testString";
        Storable storable = new TestStorable(expectedString);
        Storage storage = new Storage()
            .setFileEditor(new TestStringErrorStreamEditor())
            .setFormatter(new TestStringValueFormatter());


        StoreFailureException exception = assertThrows(
            StoreFailureException.class,
            () -> storage.store(storable, testFile.toString())
        );
        assertInstanceOf(ExampleException.class, exception.getCause());
    }

    @Test
    @UseTestFile
    public void storeStorableOfStringAtFilepath_FormatterError(Path testFile) throws IOException {
        String expectedString = "testString";
        Storable storable = new TestStorable(expectedString);
        Storage storage = new Storage()
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringErrorValueFormatter());


        FormattingException exception = assertThrows(
            FormattingException.class,
            () -> storage.store(storable, testFile.toString())
        );
        assertInstanceOf(ExampleException.class, exception.getCause());
    }

    @Test
    @UseTestFile
    public void storeStorageValueOfString(Path testFile) throws IOException {
        String expectedString = "testString";
        StorageValue<?> storageValue = new TestStorageValue(expectedString);
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringValueFormatter());


        storage.store(storageValue);
        String actualString = Files.readString(testFile);


        assertEquals(expectedString, actualString);
    }

    @Test
    @UseTestFile
    public void storeStorageValueOfInteger(Path testFile) throws IOException {
        int expectedInteger = 10;
        StorageValue<?> storageValue = new TestStorageValue(expectedInteger);
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestIntegerStreamEditor())
            .setFormatter(new TestIntegerValueFormatter());


        storage.store(storageValue);
        int actualInteger = Integer.parseInt(Files.readString(testFile));


        assertEquals(expectedInteger, actualInteger);
    }

    @Test
    @UseTestFile
    public void storeStorageValueOfString_IncorrectFormatter(Path testFile) throws IOException {
        String expectedString = "testString";
        StorageValue<?> storageValue = new TestStorageValue(expectedString);
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestIntegerValueFormatter());


        assertThrows(StorageFormatMismatchException.class, () -> storage.store(storageValue));
    }

    @Test
    @UseTestFile
    public void storeStorageValueOfString_FileWriteError(Path testFile) throws IOException {
        String expectedString = "testString";
        StorageValue<?> storageValue = new TestStorageValue(expectedString);
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringErrorStreamEditor())
            .setFormatter(new TestStringValueFormatter());


        StoreFailureException exception = assertThrows(
            StoreFailureException.class,
            () -> storage.store(storageValue)
        );
        assertInstanceOf(ExampleException.class, exception.getCause());
    }

    @Test
    @UseTestFile
    public void storeStorageValueOfString_FormatterError(Path testFile) throws IOException {
        String expectedString = "testString";
        StorageValue<?> storageValue = new TestStorageValue(expectedString);
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringErrorValueFormatter());


        FormattingException exception = assertThrows(
            FormattingException.class,
            () -> storage.store(storageValue)
        );
        assertInstanceOf(ExampleException.class, exception.getCause());
    }

    @Test
    @UseTestFile
    void LoadStringInto(Path testFile)  throws IOException {
        String expectedString = "testString";
        TestLoadable loadable = new TestLoadable();
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFormatter(new TestStringValueFormatter())
            .setFileEditor(new TestStringStreamEditor());

        Files.writeString(testFile, expectedString);
        

        storage.loadInto(loadable);

    
        assertEquals(expectedString, loadable.getValue());
    }

    @Test
    @UseTestFile
    void LoadIntegerInto(Path testFile)  throws IOException {
        int expectedInteger = 10;
        TestLoadable loadable = new TestLoadable();
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFormatter(new TestIntegerValueFormatter())
            .setFileEditor(new TestIntegerStreamEditor());

        Files.writeString(testFile, Integer.toString(expectedInteger));
        

        storage.loadInto(loadable);

    
        assertEquals(expectedInteger, loadable.getValue());
    }
    
    @Test
    @UseTestFile
    public void loadStringFromTargetPath(Path testFile) throws IOException {
        String testString = "testString";
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringValueFormatter());

        Files.writeString(testFile, testString);


        StorageValue<?> actualStorageValue = storage.load();

        
        StorageValue<?> expectedStorageValue = new TestStorageValue(testString);
        assertEquals(expectedStorageValue, actualStorageValue);
    }

    @Test
    @UseTestFile
    public void loadIntegerFromTargetPath(Path testFile) throws IOException {
        int testInteger = 10;
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestIntegerStreamEditor())
            .setFormatter(new TestIntegerValueFormatter());

        Files.writeString(testFile, Integer.toString(testInteger));


        StorageValue<?> actualStorageValue = storage.load();

        
        StorageValue<?> expectedStorageValue = new TestStorageValue(testInteger);
        assertEquals(expectedStorageValue, actualStorageValue);
    }

    //TODO: Consider edge cases for all tests
        //Include mismatched component types. e.g. Read file as integer and attempt formatting of string input.
        //Ensure descriptive exceptions for this case.
}
