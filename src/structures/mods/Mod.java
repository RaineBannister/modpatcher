package structures.mods;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.json.JSONObject;
import structures.exceptions.DuplicateDependencyException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Mod implements Serializable {
    private String name;
    private String version;
    private String url;
    private String compressedHash;
    private String extractedHash;
    private HashMap<String, Mod> dependencies;
    private PropertyChangeSupport progressChange;
    private double progress;

    public static Mod getModFromURL(String URL) throws IOException {
        return getModFromURL(new URL(URL));
    }

    public static Mod getModFromURL(URL url) throws IOException {
        URLConnection request = url.openConnection();
        request.connect();
        InputStreamReader stream = new InputStreamReader((InputStream) request.getContent());
        Scanner scanner = new Scanner(stream).useDelimiter("\\A");
        return getModFromJSONString(scanner.hasNext() ? scanner.next() : "");
    }

    public static Mod getModFromJSONString(String json) {
        return getModFromJSON(new JSONObject(json));
    }

    public static Mod getModFromJSON(JSONObject json) {
        // TODO: check to make sure all the options are here from JSON...
        return new Mod(
                json.getString("mod-name"),
                json.getString("version"),
                json.getString("url"),
                json.getString("compressed-hash"),
                json.getString("extracted-hash")
        );
    }

    public Mod(String name, String version, String url, String compressedHash, String extractedHash) {
        setName(name);
        setVersion(version);
        setUrl(url);
        setCompressedHash(compressedHash);
        setExtractedHash(extractedHash);

        this.progress = 0;
        this.progressChange = new PropertyChangeSupport(progress);
        this.dependencies = new HashMap<>();
    }

    public String getPath() {
        return this.getName() + "_" + this.getVersion();
    }

    public void addDependency(Mod dependency) throws DuplicateDependencyException {
        if(!this.dependencies.containsKey(dependency.getExtractedHash())) {
            this.dependencies.put(this.extractedHash, dependency);
        } else {
            throw new DuplicateDependencyException("Tried to add duplicate dependency!");
        }
    }

    public boolean isModInstalled() {
        return this.isModInstalled(this.getPath());
    }

    public boolean isModInstalled(String path) {
        File f = new File(path);
        if(f.exists() && f.isDirectory()) {
            return this.doesModMatchHash(f);
        } else {
            return false;
        }
    }

    public boolean isModDownloaded() {
        File f = new File(this.getFileName());
        if(f.exists() && f.isFile()) {
            return this.doesModZipMatchHash(f);
        } else {
            return false;
        }
    }

    public boolean doesModZipMatchHash(File f) {
        // TODO: Implement hashing
        return true;
    }

    private boolean doesModMatchHash(File file) {
        //TODO: actually check hashes
        return true;
    }

    public void downloadMod() throws IOException {
        URL url = new URL(this.getUrl());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        long filesize = connection.getContentLengthLong();
        long downloadedSize = 0;
        BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
        FileOutputStream fileOutputStream = new FileOutputStream(this.getFileName());
        byte dataBuffer[] = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
            downloadedSize += bytesRead;
            fileOutputStream.write(dataBuffer, 0, bytesRead);
            setProgress((((double) downloadedSize) / ((double) filesize) * 100));
        }

        removeAllListeners();
    }

    public void extractMod() throws IOException {
        this.extractMod(this.getFileName(), this.getDirName());
    }

    public void extractMod(String zip, String output) throws IOException {
        this.extractMod(new File(zip), getDirectory(output));
    }

    public void extractMod(File zip, File output) throws IOException {
        byte[] buffer = new byte[1024];
        FileInputStream is = new FileInputStream(zip);
        ZipInputStream zis = new ZipInputStream(is);
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(output, zipEntry);
            if(!newFile.exists()) {
                if(!newFile.isDirectory()) {
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                }
            }
            this.setProgress((((double) is.getChannel().position()) / ((double) zip.length())) * 100);
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();

        removeAllListeners();
    }

    private void removeAllListeners() {
        for (PropertyChangeListener listener : this.progressChange.getPropertyChangeListeners()) {
            this.progressChange.removePropertyChangeListener(listener);
        }
    }

    public void onInstall() {

    }

    public void onEnabled() {

    }

    public void onBoot() {

    }

    private File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        if(zipEntry.isDirectory() && !destFile.exists()) {
            destFile.mkdir();

            return destFile;
        }

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    private File getDirectory() {
        return getDirectory(this.getDirName());
    }

    private File getDirectory(String name) {
        File file = new File(name);
        if(!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    private String getFileName() {
        return this.getName() + "_" + this.getVersion() + ".zip";
    }

    protected String getDirName() {
        return this.getName() + "_" + this.getVersion() + File.separator;
    }

    public HashMap<String, Mod> getDependencies() {
        return this.dependencies;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    private void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    private void setUrl(String url) {
        this.url = url;
    }

    public String getCompressedHash() {
        return compressedHash;
    }

    private void setCompressedHash(String compressedHash) {
        this.compressedHash = compressedHash;
    }

    public String getExtractedHash() {
        return extractedHash;
    }

    private void setExtractedHash(String extractedHash) {
        this.extractedHash = extractedHash;
    }

    private double getProgress() {
        return progress;
    }

    private void setProgress(double progress) {
        double old = this.progress;
        this.progress = progress;
        this.progressChange.firePropertyChange("progress", old, progress);
    }

    public PropertyChangeSupport getProgressChange() {
        return progressChange;
    }

    private void setProgressChange(PropertyChangeSupport progressChange) {
        this.progressChange = progressChange;
    }
}
