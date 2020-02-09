package structures.Boot.ConfigTypes;

public class Boolean extends Type {
    private boolean data;

    public Boolean(boolean data) {
        this.data = data;
    }

    public java.lang.String toString() {
        return "7:" + (this.data ? "1" : "0");
    }
}
