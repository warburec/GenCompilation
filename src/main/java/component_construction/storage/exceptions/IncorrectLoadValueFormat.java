package component_construction.storage.exceptions;

public class IncorrectLoadValueFormat extends RuntimeException {

    public IncorrectLoadValueFormat(String expectedFormat, String actualFormat) {
        super("A load value was provided in an incorrect format. Expected - " + expectedFormat + ", Actual - " + actualFormat);
    }
}
