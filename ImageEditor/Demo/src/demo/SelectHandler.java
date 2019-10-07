package demo;
import java.io.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.border.*;
import javax.swing.filechooser.*;

import javax.imageio.*;

//import MainScreen.SelectHnadler;

//import java.iwt.image.*;

class MainScreen
{
	JFrame screen;
	JButton select;
	JScrollPane jsp;
	JLabel filename;
	JPanel frontpanel;
	ImagePanel impanel;
	Container container;
	File imagefile;
	MainScreen()
	{
		screen=new JFrame("Image viwer");
		select=new JButton("select a file");
		select.addActionListener(new SelectHnadler());
		filename=new JLabel("file");
		container=screen.getContentPane();
		container.add(select,BorderLayout.NORTH);
		container.add(filename,BorderLayout.SOUTH);
		screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		screen.setSize(400,400);
		screen.setVisible(true);
	}
	class SelectHnadler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			JFileChooser chooser=new JFileChooser();
			FileNameExtensionFilter filter=new FileNameExtensionFilter("JPG & images","jpg","gif");
			chooser.setFileFilter(filter);
			int returnvalue=chooser.showOpenDialog(screen);
			if(returnvalue==JFileChooser.APPROVE_OPTION)
				imagefile=chooser.getSelectedFile();
			filename.setText("File:"+imagefile.getAbsolutePath());
			try
			{
				BufferedImage image=ImageIO.read(imagefile);
				ImagePanel ip=new ImagePanel(image);
				if(jsp!=null)
				{
					container.remove(jsp);
					container.validate();
					
				}
				int VSB=ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
				int HSB=ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
				jsp=new JScrollPane(ip,VSB,HSB);
				container.add(jsp);
			}
			catch(IOException el)
			{
				System.out.println(el);
			}
			System.out.println(chooser.getSelectedFile().getName());
		}
	}
	public static void main(String args[])
	{
		new MainScreen();
	}
}
class ImagePanel extends JPanel
{
	BufferedImage image;
	ImagePanel()
	{
	}
	ImagePanel(BufferedImage image)
	{
		this.image=image;
		setPreferredSize(new Dimension(image.getWidth(),image.getHeight()));
		setBorder(new EtchedBorder());
	}
	public void paint(Graphics g)
	{
		System.out.println("panel pained.."+image.getWidth());
		g.drawImage(image,0,0,image.getWidth(),image.getHeight(),null);
	}
}
