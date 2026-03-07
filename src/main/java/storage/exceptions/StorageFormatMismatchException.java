package storage.exceptions;

public class StorageFormatMismatchException extends RuntimeException {
    
    public StorageFormatMismatchException(String type1Name, String type2Name) {
        super(
            "The formatter and stream editor types were mismatched. Formatter - " 
            + type1Name.getClass().getSimpleName()
            + ", Stream - " 
            + type2Name.getClass().getSimpleName()
        );
    }

}
