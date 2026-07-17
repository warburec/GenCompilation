package test_aids.storage_entities;

public class TestDynamicObjectSubclass extends TestDynamicObject {
    public boolean c;

    public TestDynamicObjectSubclass(int a, String b, boolean c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TestDynamicObjectSubclass)) return false;

        TestDynamicObjectSubclass other = (TestDynamicObjectSubclass)obj;

        if (this.a != other.a) return false;
        if (this.b != other.b) return false;
        if (this.c != other.c) return false;

        return true;
    }
}
