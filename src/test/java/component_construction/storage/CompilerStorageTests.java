package component_construction.storage;

import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.nio.file.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import storage.exceptions.*;
import storage.value_formatters.ValueFormatter;
import storage.value_formatters.ValueToStringFormatter;
import test_aids.exceptions.ExampleException;
import test_aids.storage_entities.custom_components.*;
import test_aids.storage_entities.custom_components.loadable.LoadableTestStorableCustomCompiler;
import test_aids.test_extensions.test_files.*;
import test_aids.test_storage.*;

@ExtendWith(UseTestFileExtension.class)
public class CompilerStorageTests {

    @Test
    public void setTargetPath() {
        Path expectedPath = Path.of("./testPath");
        CompilerStorage storage = new CompilerStorage();


        storage.setTargetPath(expectedPath);
        Path actualPath = storage.getTargetFilepath();


        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void setAbsoluteTargetPath() {
        Path expectedPath = Path.of("testPath");
        CompilerStorage storage = new CompilerStorage();


        storage.setAbsoluteTargetPath(expectedPath.toString());
        Path actualPath = storage.getTargetFilepath();


        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void setRelativeTargetPath() {
        String fileName = "testPath.txt";
        CompilerStorage storage = new CompilerStorage();
        

        storage.setRelativeTargetPath(fileName);
        Path actualPath = storage.getTargetFilepath();
        

        Path expectedPath = Path.of("./" + fileName);
        assertEquals(expectedPath, actualPath);
    }

    @Test
    @UseTestFile
    public void storeNull(Path testFile) throws IOException {
        StorableCustomCompiler compiler = null;
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringValueFormatter());


        assertThrows(NullStorageObjectException.class, () -> storage.store(compiler));
    }

    @Test
    @UseTestFile
    public void store(Path testFile) throws IOException {
        StorableCustomCompiler compiler = new TestStorableCustomCompiler();
        ValueFormatter<String> formatter = new TestStringValueFormatter();
        String expectedString = formatter.format(compiler.getStorageRepresentation());
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(formatter);


        storage.store(compiler);
        String actualString = Files.readString(testFile);


        assertEquals(expectedString, actualString);
    }

    @Test
    @UseTestFile
    public void storeString_IncorrectFormatter(Path testFile) throws IOException {
        StorableCustomCompiler compiler = new TestStorableCustomCompiler();
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestIntegerValueFormatter());


        assertThrows(StorageFormatMismatchException.class, () -> storage.store(compiler));
    }

    @Test
    @UseTestFile
    public void storeString_FileWriteError(Path testFile) throws IOException {
        StorableCustomCompiler compiler = new TestStorableCustomCompiler();
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringErrorStreamEditor())
            .setFormatter(new TestStringValueFormatter());

        
        StoreFailureException exception = assertThrows(
            StoreFailureException.class,
            () -> storage.store(compiler)
        );
        assertInstanceOf(ExampleException.class, exception.getCause());
    }

    @Test
    @UseTestFile
    public void storeString_FormatterError(Path testFile) throws IOException {
        StorableCustomCompiler compiler = new TestStorableCustomCompiler();
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringErrorValueFormatter());


        FormattingException exception = assertThrows(
            FormattingException.class,
            () -> storage.store(compiler)
        );
        assertInstanceOf(ExampleException.class, exception.getCause());
    }

    @Test
    @UseTestFile
    public void load(Path testFile) throws IOException {
        StorableCustomCompiler expectedCompiler = new TestStorableCustomCompiler();
        ValueFormatter<String> formatter = new ValueToStringFormatter();
        String testString = formatter.format(expectedCompiler.getStorageRepresentation());
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(formatter);

        Files.writeString(testFile, testString);


        StorableCustomCompiler actualCompiler = storage.load();

        
        assertEquals(expectedCompiler, actualCompiler);
    }

    @Test
    @UseTestFile
    public void loadString_IncorrectFormatter(Path testFile) throws IOException {
        StorableCustomCompiler expectedCompiler = new TestStorableCustomCompiler();
        String testString = expectedCompiler.getStorageRepresentation().toString();
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestIntegerValueFormatter());

        Files.writeString(testFile, testString);


        assertThrows(StorageFormatMismatchException.class, () -> storage.load());
    }

    @Test
    @UseTestFile
    public void loadString_FileWriteError(Path testFile) throws IOException {
        StorableCustomCompiler expectedCompiler = new TestStorableCustomCompiler();
        String testString = expectedCompiler.getStorageRepresentation().toString();
        CompilerStorage storage = new CompilerStorage()
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
        StorableCustomCompiler expectedCompiler = new TestStorableCustomCompiler();
        String testString = expectedCompiler.getStorageRepresentation().toString();
        CompilerStorage storage = new CompilerStorage()
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
    void convertAndStoreNull(Path testFile) throws IOException {
        StorableCustomCompiler compiler = null;
        ValueFormatter<String> formatter = new TestStringValueFormatter();
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor());

        OutputStream outputStream = new FileOutputStream(testFile.toFile());


        assertThrows(NullStorageObjectException.class, () -> {
            try (outputStream) {
                storage.convertAndStore(compiler, formatter, outputStream);
            }
        });
    }

    @Test
    @UseTestFile
    void convertAndStoreString(Path testFile) throws IOException {
        StorableCustomCompiler compiler = new TestStorableCustomCompiler();
        String expectedString = compiler.getStorageRepresentation().toString();
        ValueFormatter<String> formatter = new TestStringValueFormatter();
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor());

        OutputStream outputStream = new FileOutputStream(testFile.toFile());


        try (outputStream) {
            storage.convertAndStore(compiler, formatter, outputStream);
        }
        
        
        String actualString = Files.readString(testFile);
        assertEquals(expectedString, actualString);
    }

    @Test
    @UseTestFile
    public void convertAndStoreString_IncorrectFormatter(Path testFile) throws IOException {

        StorableCustomCompiler compiler = new TestStorableCustomCompiler();
        ValueFormatter<Integer> formatter = new TestIntegerValueFormatter();
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor());

        OutputStream outputStream = new FileOutputStream(testFile.toFile());

        try (outputStream) {
            assertThrows(StorageFormatMismatchException.class, () -> storage.convertAndStore(compiler, formatter, outputStream));
        }
    }

    @Test
    @UseTestFile
    public void convertAndStoreString_FileWriteError(Path testFile) throws IOException {

        StorableCustomCompiler compiler = new TestStorableCustomCompiler();
        ValueFormatter<String> formatter = new TestStringValueFormatter();
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringErrorStreamEditor());

        OutputStream outputStream = new FileOutputStream(testFile.toFile());

        try (outputStream) {
            StoreFailureException exception = assertThrows(
                StoreFailureException.class,
                () -> storage.convertAndStore(compiler, formatter, outputStream)
            );
            assertInstanceOf(ExampleException.class, exception.getCause());
        }
    }

    @Test
    @UseTestFile
    public void convertAndStoreString_FormatterError(Path testFile) throws IOException {

        StorableCustomCompiler compiler = new TestStorableCustomCompiler();
        ValueFormatter<String> formatter = new TestStringErrorValueFormatter();
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor());

        OutputStream outputStream = new FileOutputStream(testFile.toFile());

        try (outputStream) {
            FormattingException exception = assertThrows(
                FormattingException.class,
                () -> storage.convertAndStore(compiler, formatter, outputStream)
            );
            assertInstanceOf(ExampleException.class, exception.getCause());
        }
    }

    @Test
    @UseTestFile
    void convertAndLoadStringInto(Path testFile) throws IOException {
        LoadableTestStorableCustomCompiler expectedCompiler = new LoadableTestStorableCustomCompiler();
        LoadableTestStorableCustomCompiler actualCompiler = new LoadableTestStorableCustomCompiler();
        String expectedString = expectedCompiler.getStorageRepresentation().toString();
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFormatter(new TestStringValueFormatter())
            .setFileEditor(new TestStringStreamEditor());

        Files.writeString(testFile, expectedString);

        InputStream inputStream = new FileInputStream(testFile.toFile());


        try (inputStream) {
            storage.convertAndLoadInto(actualCompiler, inputStream);
        }
    

        assertEquals(expectedCompiler, actualCompiler);
    }

    @Test
    @UseTestFile
    public void convertAndLoadStringInto_IncorrectFormatter(Path testFile) throws IOException {
        LoadableTestStorableCustomCompiler expectedCompiler = new LoadableTestStorableCustomCompiler();
        LoadableTestStorableCustomCompiler actualCompiler = new LoadableTestStorableCustomCompiler();
        String expectedString = expectedCompiler.getStorageRepresentation().toString();
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFormatter(new TestIntegerValueFormatter())
            .setFileEditor(new TestStringStreamEditor());

        Files.writeString(testFile, expectedString);

        InputStream inputStream = new FileInputStream(testFile.toFile());


        try (inputStream) {
            assertThrows(StorageFormatMismatchException.class, () -> storage.convertAndLoadInto(actualCompiler, inputStream));
        }
    }

    @Test
    @UseTestFile
    public void convertAndLoadStringInto_FileWriteError(Path testFile) throws IOException {
        LoadableTestStorableCustomCompiler expectedCompiler = new LoadableTestStorableCustomCompiler();
        LoadableTestStorableCustomCompiler actualComipler = new LoadableTestStorableCustomCompiler();
        String expectedString = expectedCompiler.getStorageRepresentation().toString();
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFormatter(new TestStringValueFormatter())
            .setFileEditor(new TestStringErrorStreamEditor());

        Files.writeString(testFile, expectedString);

        InputStream inputStream = new FileInputStream(testFile.toFile());


        try (inputStream) {
            LoadFailureException exception = assertThrows(
                LoadFailureException.class,
                () -> storage.convertAndLoadInto(actualComipler, inputStream)
            );
            assertInstanceOf(ExampleException.class, exception.getCause());
        }
    }

    @Test
    @UseTestFile
    public void convertAndLoadStringInto_FormatterError(Path testFile) throws IOException {
        LoadableTestStorableCustomCompiler expectedCompiler = new LoadableTestStorableCustomCompiler();
        LoadableTestStorableCustomCompiler actualCompiler = new LoadableTestStorableCustomCompiler();
        String expectedString = expectedCompiler.getStorageRepresentation().toString();
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFormatter(new TestStringErrorValueFormatter())
            .setFileEditor(new TestStringStreamEditor());

        Files.writeString(testFile, expectedString);

        InputStream inputStream = new FileInputStream(testFile.toFile());


        try (inputStream) {
            FormatParseException exception = assertThrows(
                FormatParseException.class,
                () -> storage.convertAndLoadInto(actualCompiler, inputStream)
            );
            assertInstanceOf(ExampleException.class, exception.getCause());
        }
    }

    @Test
    @UseTestFile
    void convertThenFormatAndLoadStringInto(Path testFile) throws IOException {
        LoadableTestStorableCustomCompiler expectedCompiler = new LoadableTestStorableCustomCompiler();
        LoadableTestStorableCustomCompiler actualCompiler = new LoadableTestStorableCustomCompiler();
        String expectedString = expectedCompiler.getStorageRepresentation().toString();
        ValueFormatter<?> formatter = new TestStringValueFormatter();
        
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor());

        Files.writeString(testFile, expectedString);

        InputStream inputStream = new FileInputStream(testFile.toFile());


        try (inputStream) {
            storage.convertAndLoadInto(actualCompiler, formatter, inputStream);
        }
    
        assertEquals(expectedCompiler, actualCompiler);
    }

    @Test
    @UseTestFile
    public void convertThenFormatAndLoadStringInto_IncorrectFormatter(Path testFile) throws IOException {
        LoadableTestStorableCustomCompiler expectedCompiler = new LoadableTestStorableCustomCompiler();
        LoadableTestStorableCustomCompiler actualCompiler = new LoadableTestStorableCustomCompiler();
        String expectedString = expectedCompiler.getStorageRepresentation().toString();
        ValueFormatter<?> formatter = new TestIntegerValueFormatter();
        
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor());

        Files.writeString(testFile, expectedString);

        InputStream inputStream = new FileInputStream(testFile.toFile());


        try (inputStream) {
            assertThrows(StorageFormatMismatchException.class, () -> storage.convertAndLoadInto(actualCompiler, formatter, inputStream));
        }
    }

    @Test
    @UseTestFile
    public void convertThenFormatAndLoadStringInto_FileWriteError(Path testFile) throws IOException {
        LoadableTestStorableCustomCompiler expectedCompiler = new LoadableTestStorableCustomCompiler();
        LoadableTestStorableCustomCompiler actualCompiler = new LoadableTestStorableCustomCompiler();
        String expectedString = expectedCompiler.getStorageRepresentation().toString();
        ValueFormatter<?> formatter = new TestStringValueFormatter();
        
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringErrorStreamEditor());

        Files.writeString(testFile, expectedString);

        InputStream inputStream = new FileInputStream(testFile.toFile());


        try (inputStream) {
            LoadFailureException exception = assertThrows(
                LoadFailureException.class,
                () -> storage.convertAndLoadInto(actualCompiler, formatter, inputStream)
            );
            assertInstanceOf(ExampleException.class, exception.getCause());
        }
    }

    @Test
    @UseTestFile
    public void convertThenFormatAndLoadStringInto_FormatterError(Path testFile) throws IOException {
        LoadableTestStorableCustomCompiler expectedCompiler = new LoadableTestStorableCustomCompiler();
        LoadableTestStorableCustomCompiler actualCompiler = new LoadableTestStorableCustomCompiler();
        String expectedString = expectedCompiler.getStorageRepresentation().toString();
        ValueFormatter<?> formatter = new TestStringErrorValueFormatter();
        
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor());

        Files.writeString(testFile, expectedString);

        InputStream inputStream = new FileInputStream(testFile.toFile());


        try (inputStream) {
            FormatParseException exception = assertThrows(
                FormatParseException.class,
                () -> storage.convertAndLoadInto(actualCompiler, formatter, inputStream)
            );
            assertInstanceOf(ExampleException.class, exception.getCause());
        }
    }

    @Test
    @UseTestFile
    public void storeNullToStream(Path testFile) throws IOException {
        StorableCustomCompiler compiler = null;
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringValueFormatter());

        OutputStream outputStream = new FileOutputStream(testFile.toFile());


        assertThrows(NullStorageObjectException.class, () -> {
            try (outputStream) {
                storage.store(compiler, outputStream);
            }
        });
    }

    @Test
    @UseTestFile
    public void storeStringToStream(Path testFile) throws IOException {
        StorableCustomCompiler compiler = new TestStorableCustomCompiler();
        String expectedString = compiler.getStorageRepresentation().toString();
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringValueFormatter());

        OutputStream outputStream = new FileOutputStream(testFile.toFile());


        try (outputStream) {
            storage.store(compiler, outputStream);
        }
        
        
        String actualString = Files.readString(testFile);
        assertEquals(expectedString, actualString);
    }

    @Test
    @UseTestFile
    public void storeStringToStream_IncorrectFormatter(Path testFile) throws IOException {
        StorableCustomCompiler compiler = new TestStorableCustomCompiler();
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestIntegerValueFormatter());

        OutputStream outputStream = new FileOutputStream(testFile.toFile());


        try (outputStream) {
            assertThrows(StorageFormatMismatchException.class, () -> storage.store(compiler, outputStream));
        }
    }

    @Test
    @UseTestFile
    public void storeStringToStream_FileWriteError(Path testFile) throws IOException {
        StorableCustomCompiler compiler = new TestStorableCustomCompiler();
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringErrorStreamEditor())
            .setFormatter(new TestStringValueFormatter());

        OutputStream outputStream = new FileOutputStream(testFile.toFile());


        try (outputStream) {
            StoreFailureException exception = assertThrows(
                StoreFailureException.class,
                () -> storage.store(compiler, outputStream)
            );
            assertInstanceOf(ExampleException.class, exception.getCause());
        }
    }

    @Test
    @UseTestFile
    public void storeStringToStream_FormatterError(Path testFile) throws IOException {
        StorableCustomCompiler compiler = new TestStorableCustomCompiler();
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringErrorValueFormatter());

        OutputStream outputStream = new FileOutputStream(testFile.toFile());


        try (outputStream) {
            FormattingException exception = assertThrows(
                FormattingException.class,
                () -> storage.store(compiler, outputStream)
            );
            assertInstanceOf(ExampleException.class, exception.getCause());
        }
    }

    @Test
    @UseTestFile
    public void loadStringFromInputStream(Path testFile) throws IOException {
        StorableCustomCompiler expectedCompiler = new TestStorableCustomCompiler();
        String testString = expectedCompiler.getStorageRepresentation().toString();
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringValueFormatter());

        Files.writeString(testFile, testString);

        InputStream inputStream = new FileInputStream(testFile.toFile());


        StorableCustomCompiler actualcompiler = storage.load(inputStream);


        assertEquals(expectedCompiler, actualcompiler);
    }

    @Test
    @UseTestFile
    public void loadStringFromInputStream_IncorrectFormatter(Path testFile) throws IOException {
        StorableCustomCompiler expectedCompiler = new TestStorableCustomCompiler();
        String testString = expectedCompiler.getStorageRepresentation().toString();
        CompilerStorage storage = new CompilerStorage()
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
        StorableCustomCompiler expectedCompiler = new TestStorableCustomCompiler();
        String testString = expectedCompiler.getStorageRepresentation().toString();
        CompilerStorage storage = new CompilerStorage()
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
        StorableCustomCompiler expectedCompiler = new TestStorableCustomCompiler();
        String testString = expectedCompiler.getStorageRepresentation().toString();
        CompilerStorage storage = new CompilerStorage()
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
    public void storeStorableOfNull(Path testFile) throws IOException {
        StorableCustomCompiler compiler = null;
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringValueFormatter());


        assertThrows(NullStorageObjectException.class, () -> storage.store(compiler));
    }

    @Test
    @UseTestFile
    public void storeStorableOfString(Path testFile) throws IOException {
        StorableCustomCompiler compiler = new TestStorableCustomCompiler();
        String expectedString = compiler.getStorageRepresentation().toString();
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringValueFormatter());


        storage.store(compiler);
        String actualString = Files.readString(testFile);


        assertEquals(expectedString, actualString);
    }

    @Test
    @UseTestFile
    public void storeStorableOfString_IncorrectFormatter(Path testFile) throws IOException {
        StorableCustomCompiler compiler = new TestStorableCustomCompiler();
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestIntegerValueFormatter());


        assertThrows(StorageFormatMismatchException.class, () -> storage.store(compiler));
    }

    @Test
    @UseTestFile
    public void storeStorableOfString_FileWriteError(Path testFile) throws IOException {
        StorableCustomCompiler compiler = new TestStorableCustomCompiler();
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringErrorStreamEditor())
            .setFormatter(new TestStringValueFormatter());


        StoreFailureException exception = assertThrows(
            StoreFailureException.class,
            () -> storage.store(compiler)
        );
        assertInstanceOf(ExampleException.class, exception.getCause());
    }

    @Test
    @UseTestFile
    public void storeStorableOfString_FormatterError(Path testFile) throws IOException {
        StorableCustomCompiler compiler = new TestStorableCustomCompiler();
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringErrorValueFormatter());


        FormattingException exception = assertThrows(
            FormattingException.class,
            () -> storage.store(compiler)
        );
        assertInstanceOf(ExampleException.class, exception.getCause());
    }

    @Test
    @UseTestFile
    public void storeStorableOfNullAtFilepath(Path testFile) throws IOException {
        StorableCustomCompiler compiler = null;
        CompilerStorage storage = new CompilerStorage()
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringValueFormatter());


        assertThrows(NullStorageObjectException.class, () -> storage.store(compiler, testFile.toString()));
    }

    @Test
    @UseTestFile
    public void storeStorableOfStringAtFilepath(Path testFile) throws IOException {
        StorableCustomCompiler compiler = new TestStorableCustomCompiler();
        String expectedString = compiler.getStorageRepresentation().toString();
        CompilerStorage storage = new CompilerStorage()
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringValueFormatter());


        storage.store(compiler, testFile.toString());
        String actualString = Files.readString(testFile);


        assertEquals(expectedString, actualString);
    }

    @Test
    @UseTestFile
    public void storeStorableOfStringAtFilepath_IncorrectFormatter(Path testFile) throws IOException {
        StorableCustomCompiler compiler = new TestStorableCustomCompiler();
        CompilerStorage storage = new CompilerStorage()
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestIntegerValueFormatter());


        assertThrows(StorageFormatMismatchException.class, () -> storage.store(compiler, testFile.toString()));
    }

    @Test
    @UseTestFile
    public void storeStorableOfStringAtFilepath_FileWriteError(Path testFile) throws IOException {
        StorableCustomCompiler compiler = new TestStorableCustomCompiler();
        CompilerStorage storage = new CompilerStorage()
            .setFileEditor(new TestStringErrorStreamEditor())
            .setFormatter(new TestStringValueFormatter());


        StoreFailureException exception = assertThrows(
            StoreFailureException.class,
            () -> storage.store(compiler, testFile.toString())
        );
        assertInstanceOf(ExampleException.class, exception.getCause());
    }

    @Test
    @UseTestFile
    public void storeStorableOfStringAtFilepath_FormatterError(Path testFile) throws IOException {
        StorableCustomCompiler compiler = new TestStorableCustomCompiler();
        CompilerStorage storage = new CompilerStorage()
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringErrorValueFormatter());


        FormattingException exception = assertThrows(
            FormattingException.class,
            () -> storage.store(compiler, testFile.toString())
        );
        assertInstanceOf(ExampleException.class, exception.getCause());
    }

    @Test
    @UseTestFile
    public void storecompilerOfNull(Path testFile) throws IOException {
        StorableCustomCompiler compiler = null;
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringValueFormatter());


        assertThrows(NullStorageObjectException.class, () -> storage.store(compiler));
    }

    @Test
    @UseTestFile
    public void storecompilerOfString(Path testFile) throws IOException {
        StorableCustomCompiler compiler = new TestStorableCustomCompiler();
        String expectedString = compiler.getStorageRepresentation().toString();
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringValueFormatter());


        storage.store(compiler);
        String actualString = Files.readString(testFile);


        assertEquals(expectedString, actualString);
    }

    @Test
    @UseTestFile
    public void storecompilerOfString_IncorrectFormatter(Path testFile) throws IOException {
        StorableCustomCompiler compiler = new TestStorableCustomCompiler();
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestIntegerValueFormatter());


        assertThrows(StorageFormatMismatchException.class, () -> storage.store(compiler));
    }

    @Test
    @UseTestFile
    public void storecompilerOfString_FileWriteError(Path testFile) throws IOException {
        StorableCustomCompiler compiler = new TestStorableCustomCompiler();
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringErrorStreamEditor())
            .setFormatter(new TestStringValueFormatter());


        StoreFailureException exception = assertThrows(
            StoreFailureException.class,
            () -> storage.store(compiler)
        );
        assertInstanceOf(ExampleException.class, exception.getCause());
    }

    @Test
    @UseTestFile
    public void storecompilerOfString_FormatterError(Path testFile) throws IOException {
        StorableCustomCompiler compiler = new TestStorableCustomCompiler();
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringErrorValueFormatter());


        FormattingException exception = assertThrows(
            FormattingException.class,
            () -> storage.store(compiler)
        );
        assertInstanceOf(ExampleException.class, exception.getCause());
    }

    @Test
    @UseTestFile
    void LoadStringInto(Path testFile) throws IOException {
        LoadableTestStorableCustomCompiler expectedCompiler = new LoadableTestStorableCustomCompiler();
        LoadableTestStorableCustomCompiler actualCompiler = new LoadableTestStorableCustomCompiler();
        String expectedString = expectedCompiler.getStorageRepresentation().toString();
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFormatter(new TestStringValueFormatter())
            .setFileEditor(new TestStringStreamEditor());

        Files.writeString(testFile, expectedString);
        

        storage.loadInto(actualCompiler);

    
        assertEquals(expectedCompiler, actualCompiler);
    }

    @Test
    @UseTestFile
    public void LoadStringInto_IncorrectFormatter(Path testFile) throws IOException {
        LoadableTestStorableCustomCompiler expectedCompiler = new LoadableTestStorableCustomCompiler();
        LoadableTestStorableCustomCompiler actualCompiler = new LoadableTestStorableCustomCompiler();
        String expectedString = expectedCompiler.getStorageRepresentation().toString();
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFormatter(new TestStringValueFormatter())
            .setFileEditor(new TestIntegerStreamEditor());

        Files.writeString(testFile, expectedString);


        assertThrows(StorageFormatMismatchException.class, () -> storage.loadInto(actualCompiler));
    }

    @Test
    @UseTestFile
    public void LoadStringInto_FileWriteError(Path testFile) throws IOException {
        LoadableTestStorableCustomCompiler expectedCompiler = new LoadableTestStorableCustomCompiler();
        LoadableTestStorableCustomCompiler actualCompiler = new LoadableTestStorableCustomCompiler();
        String expectedString = expectedCompiler.getStorageRepresentation().toString();
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFormatter(new TestStringValueFormatter())
            .setFileEditor(new TestStringErrorStreamEditor());

        Files.writeString(testFile, expectedString);


        LoadFailureException exception = assertThrows(
            LoadFailureException.class,
            () -> storage.loadInto(actualCompiler)
        );
        assertInstanceOf(ExampleException.class, exception.getCause());
    }

    @Test
    @UseTestFile
    public void LoadStringInto_FormatterError(Path testFile) throws IOException {
        LoadableTestStorableCustomCompiler expectedCompiler = new LoadableTestStorableCustomCompiler();
        LoadableTestStorableCustomCompiler actualCompiler = new LoadableTestStorableCustomCompiler();
        String expectedString = expectedCompiler.getStorageRepresentation().toString();
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFormatter(new TestStringErrorValueFormatter())
            .setFileEditor(new TestStringStreamEditor());

        Files.writeString(testFile, expectedString);


        FormatParseException exception = assertThrows(
            FormatParseException.class,
            () -> storage.loadInto(actualCompiler)
        );
        assertInstanceOf(ExampleException.class, exception.getCause());
    }
    
    @Test
    @UseTestFile
    public void loadStringFromTargetPath(Path testFile) throws IOException {
        StorableCustomCompiler expectedCompiler = new TestStorableCustomCompiler();
        String testString = expectedCompiler.getStorageRepresentation().toString();
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestStringValueFormatter());

        Files.writeString(testFile, testString);


        StorableCustomCompiler actualcompiler = storage.load();

        
        assertEquals(expectedCompiler, actualcompiler);
    }

    @Test
    @UseTestFile
    public void loadStringFromTargetPath_IncorrectFormatter(Path testFile) throws IOException {
        StorableCustomCompiler expectedCompiler = new TestStorableCustomCompiler();
        String testString = expectedCompiler.getStorageRepresentation().toString();
        CompilerStorage storage = new CompilerStorage()
            .setTargetPath(testFile)
            .setFileEditor(new TestStringStreamEditor())
            .setFormatter(new TestIntegerValueFormatter());

        Files.writeString(testFile, testString);


        assertThrows(StorageFormatMismatchException.class, () -> storage.load());
    }

    @Test
    @UseTestFile
    public void loadStringFromTargetPath_FileWriteError(Path testFile) throws IOException {
        StorableCustomCompiler expectedCompiler = new TestStorableCustomCompiler();
        String testString = expectedCompiler.getStorageRepresentation().toString();
        CompilerStorage storage = new CompilerStorage()
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
    public void loadStringFromTargetPath_FormatterError(Path testFile) throws IOException {
        StorableCustomCompiler expectedCompiler = new TestStorableCustomCompiler();
        String testString = expectedCompiler.getStorageRepresentation().toString();
        CompilerStorage storage = new CompilerStorage()
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

}
