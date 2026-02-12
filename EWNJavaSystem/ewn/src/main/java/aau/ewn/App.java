package aau.ewn;

import java.awt.EventQueue;

import javax.swing.UIManager;

import aau.ewn.ui.GameFrame;

public class App
{

    public static void main( String[] args )
    {
    	EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// windows主题
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
				} catch (Exception e) {
					e.printStackTrace();
				}
				GameFrame frame = new GameFrame();
				frame.setVisible(true);
			}
		});
    }
}
