package hu.za.pc_remote.desktop_agent;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.log4j.Logger;


/**
 * Created by IntelliJ IDEA.
 * User: Andor
 * Date: 9/27/11
 * Time: 8:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    private static Logger logger = Logger.getLogger(Main.class);

    private TrayIcon trayIcon;
    private BTManager manager;

    public Main() {
        if (SystemTray.isSupported()) {

            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.jpg"));

            ActionListener exitListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    logger.debug("Exiting...");
                    System.exit(0);
                }
            };

            PopupMenu popup = new PopupMenu();
            MenuItem defaultItem = new MenuItem("Exit");
            defaultItem.addActionListener(exitListener);
            popup.add(defaultItem);

            trayIcon = new TrayIcon(image, "Bluetooth remote agent", popup);

            trayIcon.setImageAutoSize(true);

            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                logger.debug("TrayIcon could not be added.");
            }

        } else {

        }

        manager = new BTManager(trayIcon);
        manager.initialize();
        manager.start();
    }

    public static void main(String[] args) {
        new Main();
    }

}
