package structures.Boot.ConfigTypes;

public class UnsignedInteger extends Type {
    private int data;

    public UnsignedInteger(int data) {
        this.data = data;
    }

    public java.lang.String toString() {
        return "5:" + this.data;
    }
}
