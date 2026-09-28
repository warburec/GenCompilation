package test_aids.storage_entities;

public class TestDynamicObject {
    public int a;
    public String b;

    public TestDynamicObject() {}

    public TestDynamicObject(int a) {
        this.a = a;
    }

    public TestDynamicObject(String b) {
        this.b = b;
    }

    public TestDynamicObject(int a, String b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TestDynamicObject)) return false;

        TestDynamicObject other = (TestDynamicObject)obj;

        if (this.a != other.a) return false;
        if (this.b != other.b) return false;

        return true;
    }

    public class InnerTestDynamicObject {
        public int c;
        public String d;

        public InnerTestDynamicObject() {}

        public InnerTestDynamicObject(int c) {
            this.c = c;
        }

        public InnerTestDynamicObject(String d) {
            this.d = d;
        }

        public InnerTestDynamicObject(int c, String d) {
            this.c = c;
            this.d = d;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof InnerTestDynamicObject)) return false;
        
            InnerTestDynamicObject other = (InnerTestDynamicObject)obj;

            if (this.c != other.c) return false;
            if (this.d != other.d) return false;

            return true;
        }
    }

    public static class StaticInnerTestDynamicObject {
        public int e;
        public String f;

        public StaticInnerTestDynamicObject() {}

        public StaticInnerTestDynamicObject(int e) {
            this.e = e;
        }

        public StaticInnerTestDynamicObject(String f) {
            this.f = f;
        }

        public StaticInnerTestDynamicObject(int e, String f) {
            this.e = e;
            this.f = f;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof StaticInnerTestDynamicObject)) return false;
        
            StaticInnerTestDynamicObject other = (StaticInnerTestDynamicObject)obj;

            if (this.e != other.e) return false;
            if (this.f != other.f) return false;

            return true;
        }
    }
}
