package structures.Boot.ConfigTypes;

public class String extends Type {
    private java.lang.String data;

    public String(java.lang.String data) {
        this.data = data;
    }

    public java.lang.String toString() {
        return "0:" + this.data;
    }
}
