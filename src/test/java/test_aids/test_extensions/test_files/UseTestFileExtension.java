package test_aids.test_extensions.test_files;

import java.io.*;
import java.nio.file.*;
import java.util.Comparator;

import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;

public class UseTestFileExtension implements ParameterResolver {

    private static final Namespace STORAGE_NAMESPACE = Namespace.create(UseTestFileExtension.class);

    @Override
    public boolean supportsParameter(ParameterContext pc, ExtensionContext ec) {
        return ec
            .getTestMethod()
            .get()
            .isAnnotationPresent(UseTestFile.class);
    }

    @Override
    public Object resolveParameter(ParameterContext pc, ExtensionContext ec) {
        try {
            ManagedDirectory managedDir = ec
                .getStore(STORAGE_NAMESPACE)
                .getOrComputeIfAbsent(
                    "managedDir", 
                    key -> new ManagedDirectory(), 
                    ManagedDirectory.class
                );
            
            return managedDir.createFile("testFile-" + System.nanoTime());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    protected static class ManagedDirectory implements CloseableResource {

        private final Path dir;

        public ManagedDirectory() {
            try {
                this.dir = Files.createTempDirectory("tests-");
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        public Path createFile(String filename) throws IOException {
            return Files.createFile(dir.resolve(filename));
        }

        @Override
        public void close() throws Throwable {
            if (!Files.exists(dir)) return;

            try (var stream = Files.walk(dir)) {
                stream
                    .sorted(Comparator.reverseOrder())
                    .forEach(p -> p.toFile().delete());
            }
        }
    }
}