package storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
import java.lang.annotation.*;
import java.nio.file.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.io.TempDir;
import storage.storage_values.*;
import storage.value_formatters.ValueFormatter;
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
    public void loadEmpty(Path testFile) throws IOException {
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
    void convertAndStore(Path testFile)  throws IOException {
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
    void convertAndLoadInto(Path testFile)  throws IOException {
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
    void convertThenFormatAndLoadInto(Path testFile)  throws IOException {
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
    public void storeToStream(Path testFile) throws IOException {
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
    public void loadFromInputStream(Path testFile) throws IOException {
        String testString = "testString";
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringValueFormatter());

        Files.writeString(testFile, testString);

        InputStream inputStream = new FileInputStream(testFile.toFile());


        StorageValue<?> actualStorageValue = storage.<String>load(inputStream);

        
        StorageValue<?> expectedStorageValue = new TestStorageValue(testString);
        assertEquals(expectedStorageValue, actualStorageValue);
    }

    @Test
    @UseTestFile
    public void storeStorable(Path testFile) throws IOException {
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
    public void storeStorableAtFilepath(Path testFile) throws IOException {
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
    public void storeStorageValue(Path testFile) throws IOException {
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
    void LoadInto(Path testFile)  throws IOException {
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
    
    @UseTestFile
    public void loadFromTargetPath(Path testFile) throws IOException {
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

    //TODO: Apply for non-string types
    //TODO: Consider edge cases for all tests
        //Include mismatched component types. e.g. Read file as integer and attempt formatting of string input.
        //Ensure descriptive exceptions for this case.
}
