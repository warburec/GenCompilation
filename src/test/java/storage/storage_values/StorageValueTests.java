package storage.storage_values;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import storage.storage_values.IntegerStorageValue;
import storage.storage_values.StringStorageValue;

public class StorageValueTests {

    @Test
    public void nonEquality() {
        String strValue = "0";
        Integer intValue = Integer.parseInt(strValue);

        StringStorageValue strStorage = new StringStorageValue(strValue);
        IntegerStorageValue intStorage = new IntegerStorageValue(intValue);

        assertNotEquals(strStorage, intStorage);
    }

    @Test
    public void equality() {
        String strValue = "0";
        Integer intValue = Integer.parseInt(strValue);

        StringStorageValue strStorage1 = new StringStorageValue(strValue);
        IntegerStorageValue intStorage1 = new IntegerStorageValue(intValue);

        StringStorageValue strStorage2 = new StringStorageValue(strValue);
        IntegerStorageValue intStorage2 = new IntegerStorageValue(intValue);

        assertEquals(strStorage1, strStorage2);
        assertEquals(intStorage1, intStorage2);
    }

    @Test
    public void nullNonEquality() {
        String strValue = null;
        Integer intValue = null;

        StringStorageValue strStorage = new StringStorageValue(strValue);
        IntegerStorageValue intStorage = new IntegerStorageValue(intValue);

        assertNotEquals(strStorage, intStorage);
    }

    @Test
    public void nullEquality() {
        String strValue = null;

        StringStorageValue strStorage1 = new StringStorageValue(strValue);
        StringStorageValue strStorage2 = new StringStorageValue(strValue);

        assertEquals(strStorage1, strStorage2);
    }
}
