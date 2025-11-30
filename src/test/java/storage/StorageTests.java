package storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
import java.lang.annotation.*;
import java.nio.file.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.io.TempDir;
import test_aids.test_storage.*;
import storage.storage_values.*;

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
            .setFileEditor(new TestFileEditor())
            .setFormatter(new TestValueFormatter());


        storage.store(storageValue);
        String actualString = Files.readString(testFile);


        assertEquals(expectedString, actualString);
    }

    // Storage setFormatter(ValueFormatter<F> formatter)
    // Storage setFileWriter(FileWriter<W> fileWriter)
    // Storage setFileReader(FileReader<R> fileReader)
    // Storage setFileEditor(FileEditor<E> fileEditor)
    // void convertAndStore(Storable storageObject, ValueFormatter<F> formatter, OutputStream outputStream) throws UncheckedIOException
    // void convertAndLoadInto(Loadable objectToLoad, InputStream inputStream) throws UncheckedIOException
    // void convertAndLoadInto(Loadable objectToLoad, ValueFormatter<F> formatter, InputStream inputStream) throws UncheckedIOException, RuntimeException
    // void store(Storable storageObject, OutputStream outputStream) throws UncheckedIOException, RuntimeException
    // StorageValue<?> load(InputStream inputStream)
    // void store(Storable storageObject)
    // void store(Storable storageObject, String filePath) throws UncheckedIOException, RuntimeException
    // void store(StorageValue<T> storageObject)
    // void loadInto(Loadable targetObject)
    // StorageValue<?> load()
}
