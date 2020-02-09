package gui;


import mdlaf.MaterialLookAndFeel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class MainWindow {
    public MainWindow() {
        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(comboBox1.getSelectedItem().equals("Custom IP")) {
                    textField1.setVisible(true);
                } else {
                    textField1.setVisible(false);
                }
                toolBar1.revalidate();
                toolBar1.repaint();
            }
        });
    }

    public static MainWindow start() {
        try {
            UIManager.setLookAndFeel (new MaterialLookAndFeel());
        }
        catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace ();
        }

        MainWindow mainWindow = new MainWindow();

        mainWindow.comboBox1.addItem("Custom IP");

        JFrame frame = new JFrame("Raine's LU Launcher");
        try {
            frame.setIconImage(ImageIO.read(new File("res/lu-patcher.png")));
        } catch(IOException e) {
            //
        }

        frame.setContentPane(mainWindow.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        mainWindow.frame = frame;

        return mainWindow;
    }
    private JFrame frame;
    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JProgressBar progressBar1;
    private JButton patchButton;
    private JLabel status;
    private JComboBox comboBox1;
    private JTextField textField1;
    private JToolBar toolBar1;
    private JTextArea moddingNotSupportedYetTextArea;

    public JFrame getFrame() {
        return frame;
    }

    public JComboBox getComboBox1() {
        return comboBox1;
    }

    public JPanel getPanel1() {
        return panel1;
    }

    public JTabbedPane getTabbedPane1() {
        return tabbedPane1;
    }

    public JProgressBar getProgressBar1() {
        return progressBar1;
    }

    public JButton getPatchButton() {
        return patchButton;
    }

    public JLabel getStatus() {
        return status;
    }

    public JTextField getTextField1() { return textField1; }
}
