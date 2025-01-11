package storage;

import java.io.*;
import java.nio.file.*;

public class FileOperations {

    /**
     * Eases the contents of the provided file
     * @param path The path of the file to be cleared.
     * @throws UncheckedIOException The provided file could not be cleared.
     */
    public static void eraseFileContents(Path path) throws UncheckedIOException {
        try {
            Files.newBufferedWriter(path , StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException("The file " + path.toString() + "could not be cleared.", e);
        }
    }

     /**
     * Checks if the file at the path provided exists
     * @param path The path of the file.
     * @return The existance of the specified file.
     */
    public static boolean fileExists(Path path) {
        return Files.exists(path);
    }

    /**
     * Creates a file for the provided path
     * @param path The path of the file to be created.
     */
    public static void createFile(Path path) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new UncheckedIOException("The file " + path.toString() + "could not be created.", e);
        }
    }

    /**
     * Deletes the file at the provided path
     * @param path The path of the file to be deleted.
     */
    public static void deleteFile(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new UncheckedIOException("The file " + path.toString() + "could not be deleted.", e);
        }
    }

    /**
     * Appends a new line to the specified file with the given contents
     * @param path The path of the file to be edited.
     * @param line The contents to be added on the line.
     */
    public static void appendLine(Path path, String line) {
        line = line + "\n";

        try (FileOutputStream fos = new FileOutputStream(path.toFile(), true);) {
            fos.write(line.getBytes());
        } catch (IOException e) {
            throw new UncheckedIOException("The file " + path.toString() + "could not be appended to.", e);
        }
    }
}
