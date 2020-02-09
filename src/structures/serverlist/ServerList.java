package structures.serverlist;

import java.util.ArrayList;

public class ServerList {
    private ArrayList<Server> servers;

    public ServerList() {
        this.servers = new ArrayList<>();
    }

    public void addServer(String name, String ip) {
        this.servers.add(new Server(name, ip));
    }

    public ArrayList<Server> getServers() {
        return this.servers;
    }
}
