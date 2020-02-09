package main;

import gui.MainWindow;
import org.json.JSONArray;
import structures.mods.Client;
import structures.mods.ModTree;
import structures.serverlist.Server;
import structures.serverlist.ServerList;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class GUI {
    private static GUI GUI;

    private final static String SERVERS_URL = "https://raw.githubusercontent.com/RaineBannister/PatcherInfo/master/servers.json";

    private final static String DEFAULT_MOD = "https://raw.githubusercontent.com/RaineBannister/PatcherInfo/master/client-info.json";

    public static GUI getGUI() {
        if(GUI == null) GUI = new GUI();
        return GUI;
    }

    public void setup() {
        this.mainWindow = MainWindow.start();
        this.mainWindow.getStatus().setText("Finding servers");

        this.mainWindow.getPatchButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ip = "";
                if(mainWindow.getComboBox1().getSelectedItem().toString().equals("Custom IP")) {
                    ip = mainWindow.getTextField1().getText();
                } else {
                    ip = ((Server)mainWindow.getComboBox1().getSelectedItem()).getInfo();
                }

                if(client != null) {
                    client.setServerIP(ip);
                }

                client.onBoot();
            }
        });

        URL url = null;
        try {
            url = new URL(SERVERS_URL);
        } catch (MalformedURLException e) {
            JOptionPane.showMessageDialog(getGUI().mainWindow.getFrame(), "The URL provided is not the correct format", "We encountered an error!", JOptionPane.ERROR_MESSAGE);
        }

        URLConnection request = null;
        try {
            request = url.openConnection();
        } catch (IOException | NullPointerException e) {
            JOptionPane.showMessageDialog(getGUI().mainWindow.getFrame(), "We could not find the server list online", "We encountered an error!", JOptionPane.ERROR_MESSAGE);
        }

        try {
            request.connect();
        } catch(IOException | NullPointerException e) {
            JOptionPane.showMessageDialog(getGUI().mainWindow.getFrame(), "We could not connect to the internet", "We encountered an error!", JOptionPane.ERROR_MESSAGE);
        }

        JSONArray jsonArray = new JSONArray();
        try {
            InputStreamReader stream = new InputStreamReader((InputStream) request.getContent());

            java.util.Scanner s = new java.util.Scanner(stream).useDelimiter("\\A");

            jsonArray = new JSONArray(s.hasNext() ? s.next() : "");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(getGUI().mainWindow.getFrame(), "We could not connect to the internet", "We encountered an error!", JOptionPane.ERROR_MESSAGE);
        }

        this.mainWindow.getStatus().setText("Found " + jsonArray.length() + " server(s)");
        for(int i = 0; i < jsonArray.length(); i ++) {
            this.serverList.addServer(jsonArray.getJSONObject(i).getString("name"), jsonArray.getJSONObject(i).getString("ip"));
        }

        this.serverList.getServers().forEach(server -> {
            this.mainWindow.getComboBox1().addItem(server);
        });

        // make sure client is present...
        // TODO: make threaded
        try {
            this.client = Client.getClientFromURL(DEFAULT_MOD);

            if(!this.client.isModInstalled()) {
                if(!this.client.isModDownloaded()) {
                    this.client.getProgressChange().addPropertyChangeListener(new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                            mainWindow.getStatus().setText("Downloading client (" + Math.abs((int) Math.round((double) evt.getNewValue())) + "%)");
                            mainWindow.getProgressBar1().setValue(Math.abs((int) Math.round((double) evt.getNewValue())));
                        }
                    });

                    this.mainWindow.getStatus().setText("Downloading client (0%)");

                    this.client.downloadMod();

                    this.mainWindow.getStatus().setText("Download complete");
                }

                this.client.getProgressChange().addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        mainWindow.getStatus().setText("Extracting client (" + Math.abs((int) Math.round((double) evt.getNewValue())) + "%)");
                        mainWindow.getProgressBar1().setValue(Math.abs((int) Math.round((double) evt.getNewValue())));
                    }
                });
                this.mainWindow.getStatus().setText("Extracting client (0%)");
                this.client.extractMod();
                this.mainWindow.getStatus().setText("Extracting client");
            }
            this.mainWindow.getStatus().setText("Client Installed");
            this.mainWindow.getProgressBar1().setValue(100);
            this.mainWindow.getPatchButton().setEnabled(true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(getGUI().mainWindow.getFrame(), "There was a problem with the client: " + e.getMessage(), "We encountered an error!", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }

    private void checkForClient() {

    }

    private void downloadClient() {

    }

    private void lookForMods() {

    }

    private ServerList serverList;

    private MainWindow mainWindow;

    private ModTree modTree;

    private Client client;

    private GUI() {
        serverList = new ServerList();
        modTree = new ModTree();
    }
}
