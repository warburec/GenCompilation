package storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
import java.lang.annotation.*;
import java.nio.file.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.io.TempDir;
import storage.storage_values.*;
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
    public void store(Path testFile) throws IOException {
        String expectedString = "testString";
        StorageValue<?> storageValue = new StringStorageValue(expectedString);
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStreamEditor())
            .setFormatter(new TestValueFormatter());


        storage.store(storageValue);
        String actualString = Files.readString(testFile);


        assertEquals(expectedString, actualString);
    }

    @Test
    @UseTestFile
    public void loadString(Path testFile) throws IOException {
        String testString = "testString";
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStreamEditor())
            .setFormatter(new TestValueFormatter());

        Files.writeString(testFile, testString);


        StorageValue<?> actualStorageValue = storage.load();

        
        StorageValue<?> expectedStorageValue = new TestStorageValue(testString);
        assertEquals(expectedStorageValue, actualStorageValue);
    }

    @Test
    @UseTestFile
    public void loadEmpty(Path testFile) throws IOException {
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStreamEditor())
            .setFormatter(new TestValueFormatter());

        StorageValue<?> actualStorageValue = storage.load();

        
        StorageValue<?> expectedStorageValue = new TestStorageValue("");
        assertEquals(expectedStorageValue, actualStorageValue);
    }

    // void convertAndStore(Storable storageObject, ValueFormatter<F> formatter, OutputStream outputStream) throws UncheckedIOException
    // void convertAndLoadInto(Loadable objectToLoad, InputStream inputStream) throws UncheckedIOException
    // void convertAndLoadInto(Loadable objectToLoad, ValueFormatter<F> formatter, InputStream inputStream) throws UncheckedIOException, RuntimeException
    // void store(Storable storageObject, OutputStream outputStream) throws UncheckedIOException, RuntimeException

    @Test
    @UseTestFile
    public void load(Path testFile) throws IOException {
        String testString = "testString";
        Storage storage = new Storage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStreamEditor())
            .setFormatter(new TestValueFormatter());

        Files.writeString(testFile, testString);

        InputStream inputStream = new FileInputStream(testFile.toFile());


        StorageValue<?> actualStorageValue = storage.<String>load(inputStream);

        
        StorageValue<?> expectedStorageValue = new TestStorageValue(testString);
        assertEquals(expectedStorageValue, actualStorageValue);
    }

    // void store(Storable storageObject)
    // void store(Storable storageObject, String filePath) throws UncheckedIOException, RuntimeException
    // void store(StorageValue<T> storageObject)
    // void loadInto(Loadable targetObject)
    // StorageValue<?> load()
}
