package structures.mods;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.json.JSONObject;
import structures.Boot.BootConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Scanner;

public class Client extends Mod {
    private BootConfig bootConfig;

    public static Client getClientFromURL(String URL) throws IOException {
        return getClientFromURL(new URL(URL));
    }

    public static Client getClientFromURL(URL url) throws IOException {
        URLConnection request = url.openConnection();
        request.connect();
        InputStreamReader stream = new InputStreamReader((InputStream) request.getContent());
        Scanner scanner = new Scanner(stream).useDelimiter("\\A");
        return getClientFromJSONString(scanner.hasNext() ? scanner.next() : "");
    }

    public static Client getClientFromJSONString(String json) {
        return getClientFromJSON(new JSONObject(json));
    }

    public static Client getClientFromJSON(JSONObject json) {
        // TODO: check to make sure all the options are here from JSON...
        return new Client(
                json.getString("mod-name"),
                json.getString("version"),
                json.getString("url"),
                json.getString("compressed-hash"),
                json.getString("extracted-hash")
        );
    }

    public Client(String name, String version, String url, String compressedHash, String extractedHash) {
        super(name, version, url, compressedHash, extractedHash);

        this.bootConfig = new BootConfig();
        this.bootConfig.addConfig("SERVERNAME", "");
        this.bootConfig.addConfig("PATCHSERVERIP", "");
        this.bootConfig.addConfig("AUTHSERVERIP", "");
        this.bootConfig.addConfig("PATCHSERVERPORT", 80);
        this.bootConfig.addConfig("LOGGING", 100);
        this.bootConfig.addUnsignedIntConfig("DATACENTERID", 150);
        this.bootConfig.addConfig("CPCODE", 89164);
        this.bootConfig.addConfig("AKAMAIDLM", false);
        this.bootConfig.addConfig("PATCHSERVERDIR", "localhost");
        this.bootConfig.addConfig("UGCUSE3DSERVICES", false);
        this.bootConfig.addConfig("UGCSERVERIP", "localhost");
        this.bootConfig.addConfig("UGCSERVERDIR", "3dservices");
        this.bootConfig.addConfig("PASSURL", "");
        this.bootConfig.addConfig("SIGNINURL", "");
        this.bootConfig.addConfig("SIGNUPURL", "");
        this.bootConfig.addConfig("CRASHLOGURL", "");
        this.bootConfig.addConfig("LOCALE", "en_US");
        this.bootConfig.addConfig("TRACK_DSK_USAGE", true);

        System.out.println(this.bootConfig);
    }

    public void setServerIP(String ip) {
        this.bootConfig.addConfig("AUTHSERVERIP", ip);
    }

    @Override
    public void onBoot() {
        // write the bootconfig here
        File file = new File(this.getDirName() + "LCDR Unpacked" + File.separator + "boot.cfg");
        try {
            this.bootConfig.toFile(file);
        } catch (IOException e) {

        }

        this.boot();
    }

    public void boot() {
        File file = new File(this.getDirName() + "LCDR Unpacked" + File.separator);
        ProcessBuilder pb = new ProcessBuilder(file.getAbsolutePath() + File.separator + "legouniverse.exe");
        pb.inheritIO();
        pb.directory(file.getAbsoluteFile());

        try {
            Process p = pb.start();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
