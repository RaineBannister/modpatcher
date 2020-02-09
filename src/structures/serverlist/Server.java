package structures.serverlist;

public class Server {
    private String name;
    private String info;

    public Server(String name, String info) {
        this.name = name;
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public String toString() {
        return this.name;
    }
}
