package set.gui;

import javax.swing.JApplet;

public class ClientApplet extends JApplet
{
    /**
     * A version number assigned to the connecting client. Version numbers are
     * used to ensure that the client and server are running the most
     * up-to-date version of the program. The server will reject clients
     * that are not up-to-date to prevent game errors.<br />
     * 
     * Format is "YYYY-MM-DD:TIME".
     */
    public static final String CLIENT_VERSION = "2011-04-24:1549";
    
    
    @Override
    public void init()
    {
        // schedules a job for the event-dispatching thread to create and show the GUI
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                String fid = getParameter("fid");
                String key = getParameter("key");
                int gid = Integer.parseInt(getParameter("gid"));
                
                createAndShowGUI(fid, key, gid);
            }
        });
    }

    private void createAndShowGUI(String fid, String key, int gid)
    {
        // creates and sets up the content pane
        Board newContentPane = new Board(fid, key, gid);
        newContentPane.setOpaque(true); //content panes must be opaque
        setContentPane(newContentPane);

    }
}
