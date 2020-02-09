package structures.Boot.ConfigTypes;

public class Integer extends Type {
    private int data;

    public Integer(int data) {
        this.data = data;
    }

    public java.lang.String toString() {
        return "1:" + this.data;
    }
}
