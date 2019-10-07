package abc;

import java.awt.color.ColorSpace;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ByteLookupTable;
import java.awt.image.ColorConvertOp;
import java.awt.image.ConvolveOp;
import java.awt.image.ImageObserver;
import java.awt.image.Kernel;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.Raster;
import java.awt.image.RasterFormatException;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;

import java.awt.AWTException;
import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.CompositeContext;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Robot;
import java.awt.ScrollPane;
import java.awt.Toolkit;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.util.Random;
import java.util.TreeSet;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.jhlabs.composite.ColorDodgeComposite;
import com.jhlabs.image.GaussianFilter;
import com.jhlabs.image.GrayscaleFilter;
import com.jhlabs.image.ImageUtils;
import com.jhlabs.image.InvertFilter;
import com.jhlabs.image.PointFilter;

@SuppressWarnings("serial")
class JavaIDE extends JFrame implements ActionListener, ImageObserver
{
	ScrollPane jsp;
	JMenu file, options, view, help;
	JMenuItem open,rename_file,move_file,copy_file,delete_file,exit;
	JMenuItem screenshot,set_as_screensaver;
	JMenuItem about,keyboard_shortcuts;
	public JPanel mainpanel, imagepanel, shortcutpanel1,shortcutpanel2,shortcutpanel3,editpanel,imagedisplaypanel;
	JMenuBar menubar1;
	final int WIDTH;
	final int HEIGHT;		
	final int APP_WIDTH;
	final int APP_HEIGHT;
	
	BufferedImage image;
	
	JButton jb2,jb8,jb9,jb1,jb4,jb5,jb3,jb6,jb7;
	public static ImgPanel ip;
	
	File imagefile;
	String imagepath;
	String imagename;

	public JavaIDE()
	{
		// Create the JFrame, set icon, set size and default properties.
		
		// Set Position & Size in screen
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		WIDTH = toolkit.getScreenSize().width;
		HEIGHT = toolkit.getScreenSize().height;		
		APP_WIDTH = WIDTH;
		APP_HEIGHT = HEIGHT;
		setLocation((WIDTH - APP_WIDTH)/2, (HEIGHT - APP_HEIGHT)/2);
		setSize(APP_WIDTH, APP_HEIGHT);
		
		setResizable(true);
		setIconImage(getToolkit().getImage("images/icon.png"));
		setTitle("PicFiX");
		setVisible(true);
		
		// Design of GUI using swing components
		// Menu Names
		file = new JMenu("File");
		options = new JMenu("Options");
		help = new JMenu("Help");
		
		// File Menu
		open = new JMenuItem("Open");
		open.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		file.add(open);
		open.addActionListener(this);
		
		rename_file = new JMenuItem("Rename File");
		rename_file.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_R, ActionEvent.CTRL_MASK));
		rename_file.addActionListener(this);
		file.add(rename_file);
		
		move_file = new JMenuItem("Move File");
		move_file.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_M, ActionEvent.CTRL_MASK));
		move_file.addActionListener(this);
		file.add(move_file);
		
		copy_file= new JMenuItem("Copy Image");
		file.add(copy_file);
		copy_file.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		copy_file.addActionListener(this);
		
		delete_file = new JMenuItem("Delete");
		delete_file.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_D, ActionEvent.CTRL_MASK));
		delete_file.addActionListener(this);
		file.add(delete_file);
		
		exit = new JMenuItem("Exit");
		exit.addActionListener(this);
		file.add(exit);
		
		//option Menu
		screenshot = new JMenuItem("Screen Shot");
		screenshot.addActionListener(this);
		screenshot.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_W, ActionEvent.CTRL_MASK));
	    options.add(screenshot);
		
	    set_as_screensaver = new JMenuItem("Set as screensaver");
		//options.add(set_as_screensaver);
	
		// Help Menu
		about = new JMenuItem("About ImageEditor");
		about.addActionListener(this);
		help.add(about);
		
		keyboard_shortcuts=new JMenuItem("Keyboard Shortcuts");
		keyboard_shortcuts.addActionListener(this);
		help.add(keyboard_shortcuts);
        
		//Adding menus to the menubar
		menubar1 = new JMenuBar();
        this.setJMenuBar(menubar1);
        menubar1.add(file);
        menubar1.add(options);
        menubar1.add(help);
        
        // Dividing the GUI into panels
        mainpanel = new JPanel();
        mainpanel.setLayout(null);
        getContentPane().add(mainpanel);

        //Shortcut1 Panel
        shortcutpanel1=new JPanel();
        shortcutpanel1.setLayout(new FlowLayout(FlowLayout.LEFT));
        shortcutpanel1.setBackground(Color.LIGHT_GRAY);
        shortcutpanel1.setBounds(0,0,2000,55);
        mainpanel.add(shortcutpanel1);
        
        Icon Open=new ImageIcon("images/open.png");
        jb1=new JButton(Open);
        jb1.setActionCommand("Open");
        jb1.setPreferredSize(new Dimension(50,50));
        jb1.setIcon(Open);
        shortcutpanel1.add(jb1);
        jb1.addActionListener(this);
        jb1.setToolTipText("OPEN");

        Icon Delete=new ImageIcon("images/delete.jpg");
        jb2=new JButton(Delete);
        jb2.setPreferredSize(new Dimension(50,50));
        jb2.setIcon(Delete);
        jb2.addActionListener(this);
        jb2.setActionCommand("Delete");
        shortcutpanel1.add(jb2);
        jb2.setToolTipText("DELETE");
        
        Icon Cut=new ImageIcon("images/cut.png");
        jb3=new JButton(Cut);
        jb3.setActionCommand("Cut Image");
        jb3.setPreferredSize(new Dimension(50,50));
        jb3.setIcon(Cut);
        jb3.setMnemonic(KeyEvent.VK_X);
        jb3.addActionListener(this);
        shortcutpanel1.add(jb3);
        jb3.setToolTipText("CUT");
        
        Icon Copy=new ImageIcon("images/copy.png");
        jb4=new JButton(Copy);
        jb4.setActionCommand("Copy Image");
        jb4.setPreferredSize(new Dimension(50,50));
        jb4.setIcon(Copy);
        jb4.addActionListener(this);
        shortcutpanel1.add(jb4);
        jb4.setToolTipText("COPY");
        
        Icon Paste=new ImageIcon("images/paste.jpg");
        jb5=new JButton(Paste);
        jb5.setActionCommand("Paste Image");
        jb5.setPreferredSize(new Dimension(50,50));
        jb5.setIcon(Paste);
        jb5.addActionListener(this);
        //shortcutpanel1.add(jb5);
        jb5.setToolTipText("PASTE");
        
        Icon Undo=new ImageIcon("images/undo.jpg");
        jb6=new JButton(Undo);
        jb6.setActionCommand("Undo");
        jb6.setPreferredSize(new Dimension(50,50));
        jb6.setIcon(Undo);
        jb6.addActionListener(this);
        shortcutpanel1.add(jb6);
        jb6.setToolTipText("UNDO");
        
        Icon Redo=new ImageIcon("images/redo.png");
        jb7=new JButton(Redo);
        jb7.setActionCommand("Redo");
        jb7.setPreferredSize(new Dimension(50,50));
        jb7.setIcon(Redo);
        jb7.addActionListener(this);
        shortcutpanel1.add(jb7);
        jb7.setToolTipText("REDO");
        
        // Image Panel 
        imagepanel = new JPanel();
        imagepanel.setLayout(null);
        imagepanel.setBackground(Color.gray);
        imagepanel.setBounds(0,65,2000,500); 
        mainpanel.add(imagepanel);
        
        // Shortcut2 Panel
        shortcutpanel2 = new JPanel();
        shortcutpanel2.setLayout(null);
        shortcutpanel2.setBackground(Color.black);
        shortcutpanel2.setBounds(0,450,2000,250);
        mainpanel.add(shortcutpanel2);
        
        Icon zoomin=new ImageIcon("images/zoom_in.png");
        jb8=new JButton(zoomin);
        jb8.setActionCommand("Zoom In");
        jb8.setPreferredSize(new Dimension(10,10));
        jb8.setBounds(780,140,60,40);
        jb8.setIcon(zoomin);
        jb8.addActionListener(this);
        shortcutpanel2.add(jb8);
        
        Icon zoomout=new ImageIcon("images/zoom_out.png");
        jb9=new JButton(zoomout);
        jb9.setActionCommand("Zoom Out");
        jb9.setPreferredSize(new Dimension(10,10));
        jb9.setBounds(840,140,60,40);
        jb9.setIcon(zoomout);
        jb9.addActionListener(this);
        shortcutpanel2.add(jb9);
        
        //Edit Panel
        editpanel=new JPanel();
        editpanel.setLayout(new GridLayout(1,1));
        editpanel.setBackground(Color.blue);
        editpanel.setBounds(0,0,350,550);
        imagepanel.add(editpanel);
        
        //Image display Panel (Important area)
        imagedisplaypanel=new JPanel();
        imagedisplaypanel.setLayout(new FlowLayout());
        imagedisplaypanel.setBackground(Color.GRAY);
        imagedisplaypanel.setBounds(350,0,1000,550);	
        imagepanel.add(imagedisplaypanel);
        
        JTabbedPane TabMenus= new JTabbedPane();
        TabMenus.addTab("Tools", new ToolsPanel());
        TabMenus.addTab("Effects", new EffectPanel());
        editpanel.add(TabMenus);
        
        
        image = null;		
		imagefile = null;
		imagepath = null;
		imagename = null;
	} // end of JavaIDE()
	
        @Override
		public void actionPerformed(ActionEvent e)
        {
        	String acmd = e.getActionCommand();
        	
        	// Start ActionHandler For Open
        	if(acmd == "Open")
        	{
	        	JFileChooser chooser=new JFileChooser();
				FileNameExtensionFilter filter=new FileNameExtensionFilter("JPG & images","jpg","gif","bmp","png","wbmp","jpeg");
				chooser.setFileFilter(filter);
				int returnvalue=chooser.showOpenDialog(this);
				if(returnvalue==JFileChooser.APPROVE_OPTION)
				{
					imagefile=chooser.getSelectedFile();
					imagepath = imagefile.getAbsolutePath();
					imagename = imagefile.getName();
				}
				try
				{
					
					image=ImageIO.read(imagefile);
					ip=new ImgPanel(image, imagepath, imagename);
					if(jsp!=null)
					{
						imagedisplaypanel.remove(jsp);
						imagedisplaypanel.validate();	
					}
					
					jsp=new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
					jsp.setSize(990,540);
					jsp.add(ip);
					Adjustable vadjust = jsp.getVAdjustable();
				    Adjustable hadjust = jsp.getHAdjustable();
				    hadjust.setUnitIncrement(20);
				    vadjust.setUnitIncrement(20);

					imagedisplaypanel.add(jsp);
					this.setVisible(true);
					ip.openImage();
					ip.repaint();
				}
				catch(IOException el)
				{
					System.out.println(el);
				}
				System.out.println(chooser.getSelectedFile().getName());
        	}
        	// End ActionHandler For Open
        	
        	// ActionHandler for zoomin and zoomout
        	if(acmd == "Zoom In") 
        	{
        		ip.increaseSize();
        		ip.repaint();
        	}
            if(acmd == "Zoom Out")
            {
            	ip.decreaseSize();
            	ip.repaint();
            }
            
            //ActionHandler for delete image
            if(acmd == "Delete")
            {
            	ip.deleteImage();
            	ip.repaint();
            }
            
            // Action Handler for Screen Shot
            if(acmd == "Screen Shot")
            {
            	new CopyScreenShot();
            }
            //ActionHandler for rename file
            if(acmd == "Rename File")
            {
            	File oldFile = new File(imagepath);
            	File newFile = null;
            	 try {
            		 String newname = JOptionPane.showInputDialog("Enter the new name of the file ");
            		 String format = JOptionPane.showInputDialog("Enter the extension of the image to be saved: jpg, png, gif, bmp ");
            		 if(newname != null && format != null)
            		 {
	            		 String newfilename = newname + "." + format;
	            		 newFile = new File(oldFile.getParent() + "\\" + newfilename);
	            		 //System.out.println(newFile);
            			 if(ImgPanel.lastOperation == "zoom" || ImgPanel.lastOperation == "rotateleft" || ImgPanel.lastOperation == "rotateright"
	                		 || ImgPanel.lastOperation == "original")
	                	 {
	                		 ImageIO.write(ImgPanel.image, format, newFile);
	                		 JOptionPane.showMessageDialog(null, "Saved Succesfully!!");
	                		 ip.displayRenamedImage(newFile);
	                		 ip.repaint();
	                		 oldFile.delete();
	                	 } 
	                	 else	 
	                	 {
	                		 ImageIO.write(ImgPanel.bi, format, newFile);
	                		 JOptionPane.showMessageDialog(null, "Saved Succesfully!!");
	                		 ip.displayRenamedImage(newFile);
	                		 ip.repaint();
	                		 oldFile.delete();
	                	 }
	                 }
            	 }catch (IOException ex) {
	                	 JOptionPane.showMessageDialog(null, "Not Saved !!");
	                 }
            	
            }
            
            // ActionHandler for moving image file
            if(acmd == "Move File")
            { 
            	File oldFile = new File(imagepath);
            	File newFile = null;
            	JFileChooser chooser = new JFileChooser();
            	FileNameExtensionFilter filter=new FileNameExtensionFilter("JPG & images","jpg","gif","bmp","png","wbmp","jpeg");
     			chooser.setFileFilter(filter);
     	        chooser.setSelectedFile(oldFile);
     	        int rval = chooser.showSaveDialog(null);
     	        if (rval == JFileChooser.APPROVE_OPTION) {
     	            newFile = chooser.getSelectedFile();
     	            String filename = newFile.getName();
     	            String format = filename.substring(filename.lastIndexOf(".")+1, filename.length());
     	            if(ImgPanel.lastOperation == "zoom" || ImgPanel.lastOperation == "rotateleft" || ImgPanel.lastOperation == "rotateright"
              		 || ImgPanel.lastOperation == "original")
	              	 {
	              		 try {
							ImageIO.write(ImgPanel.image, format, newFile);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
	              		 JOptionPane.showMessageDialog(null, "Saved Succesfully!!");
	              		 ip.displayRenamedImage(newFile);
	              		 ip.repaint();
	              		 oldFile.delete();
	              	 }
	              	 else	 
	              	 {
	              		 try {
							ImageIO.write(ImgPanel.bi, format, newFile);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
	              		 JOptionPane.showMessageDialog(null, "Saved Succesfully!!");
	              		 ip.displayRenamedImage(newFile);
	              		 ip.repaint();
	              		 oldFile.delete();
	              	 }
     	        }
            }
            
            // ActionHandler for copying image
            if(acmd == "Copy Image")
            {
            	File imageFile = new File(imagepath);
            	CopyImagetoClipBoard ci = new CopyImagetoClipBoard(imageFile);
            }
            
            // ActionHandler for cut image
            if(acmd == "Cut Image")
            {
            	File imageFile = new File(imagepath);
            	CopyImagetoClipBoard ci = new CopyImagetoClipBoard(imageFile);
            	ip.cutImage();
            	ip.repaint();
            }
            
            // ActionHandler for undo
            if(acmd == "Undo")
            {
            	ip.undo();
            	ip.repaint();
            }
            
            // ActionHandler for redo
            if(acmd == "Redo")
            {
            	ip.redo();
            	ip.repaint();
            }
            
            //ActionHandler for exit
            if(acmd == "Exit")
            {
            	JavaIDE.this.dispose();
            }
            
            // ActionHandler for About
            if(acmd == "About ImageEditor")
            {
            	JFrame about_window = new JFrame("About JavaEditor");
            	JPanel icon_panel = new JPanel();
            	JPanel text_panel = new JPanel();
            	
            	ImageIcon icon_image = new ImageIcon("images/PicFixIcon.png");
            	JLabel icon_label = new JLabel(icon_image);
            	icon_panel.add(icon_label);
            	
    			JTextArea about_display = new JTextArea();
    			about_display.setText("*	ImageEditor PicFix\n" + 
    								  "*	Programmers: Biswajit Panda, Sribidya Girija Mahapatra and Romalisa Samal\n" + 
    								  "*	Development IDE: Eclipse IDE\n" +
    								  "*	Programming Language: Java\n" +
    								  "*	License: You're free to modify and redistribute PiCFiX,\n" +
    								  "*	though PiCFiX is not released under a GPL license.\n" +
    								  "* 	E-Mail ID \n" +
    								  "*	biswajitpanda30@gmail.com \n" +
    								  "*	msribidya@gmail.com \n" +
    								  "* 	romalisas@gmail.com \n");
    			about_display.setBackground(Color.lightGray);
    			about_display.setEditable(false);
    			about_display.setVisible(true);
		
               	JScrollPane scroll = new JScrollPane(about_display);
    			scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
               	scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
               	text_panel.add(scroll);
               	
               	about_window.setLayout(new FlowLayout());
    			about_window.add(icon_panel);
    			about_window.add(text_panel);
    			about_window.setLocation((WIDTH - APP_WIDTH) * 2/3, (HEIGHT - APP_HEIGHT));
    			about_window.setSize(APP_WIDTH * 3/4, APP_HEIGHT/3);
    			about_window.setVisible(true);
    			about_window.setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
            
            // ActionHandler for KeyBoard Shortcuts
            if(acmd == "Keyboard Shortcuts")
            {
            	JFrame about_window = new JFrame("ImageEditor KeyBoard Shortcuts");

    			JTextArea about_display = new JTextArea();
    			about_display.setText("*	KeyBoard Shortcuts\n" +
    								  "*	------------------\n" +
    								  "*	\n" +
    								  "*	Open        \t: CTRL+O \n" + 
    								  "*	Rename File\t: CTRL+R \n" +
    								  "*	Move File  \t: CTRl+M \n" +
    								  "*	Copy Image \t: CTRL+C \n" +
    								  "*	Cut Image  \t: ALT+X \n" + 
    								  "*	Delete Image\t: CTRL+D \n" +
    								  "* 	Screen Shot \t: CTRL+W \n");
    			about_display.setBackground(Color.lightGray);
    			about_display.setEditable(false);
    			about_display.setVisible(true);
    			
               	JScrollPane scroll = new JScrollPane(about_display);
    			scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
               	scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    			about_window.setLayout(new BorderLayout());
    			about_window.add(scroll,BorderLayout.CENTER);
    			about_window.setLocation((WIDTH - APP_WIDTH) * 2/3, (HEIGHT - APP_HEIGHT));
    			about_window.setSize(APP_WIDTH * 3/4, APP_HEIGHT/3);
    			about_window.setVisible(true);
    			about_window.setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
            
            // Actionhandler for pasting image
            if(acmd == "Paste Image")
            {
            	Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
            	Image i;
	        	  if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor))
	        	  {
	        	    try
	        	    {
	        	      i = (Image) transferable.getTransferData(DataFlavor.imageFlavor);
	        	      ip.showPastedImage(i);
	  				  ip.repaint();
	        	    }
	        	    catch (UnsupportedFlavorException ufe)
	        	    {
	        	      // handle this as desired
	        	      ufe.printStackTrace();
	        	    }
	        	    catch (IOException ioe)
	        	    {
	        	      // handle this as desired
	        	      ioe.printStackTrace();
	        	    }
	        	  }
            }
		}
}

// Tools Panel Class
class ToolsPanel extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	JButton jb10;
	JButton jb121;
	JButton jb122;
	
	JComboBox imageformats;
	JLabel saveas;
	Random cropfilenameindex;
	
	ToolsPanel()
	{
		  setLayout(new FlowLayout());
	      setBackground(Color.LIGHT_GRAY);
	      
	      Icon crop=new ImageIcon("images/crop.png");
	      jb10=new JButton("Crop");
	      jb10.setPreferredSize(new Dimension(150,80));
	      jb10.setIcon(crop);
	      jb10.addActionListener(this);
	      this.add(jb10);
	      
	      Icon negative=new ImageIcon("images/negative.png");
		  JButton jb11=new JButton("Negative");
		  jb11.setPreferredSize(new Dimension(150,80));
		  jb11.setIcon(negative);
		  jb11.addActionListener(this);
	      this.add(jb11);
	      
	      Icon contrastinc=new ImageIcon("images/contrast.gif");
	      jb121=new JButton("Contrast ++");
	      jb121.setPreferredSize(new Dimension(150,80));
	      jb121.setIcon(contrastinc);
	      jb121.addActionListener(this);
	      this.add(jb121);
	      
	      Icon contrastdsc=new ImageIcon("images/contrast.gif");
	      jb122=new JButton("Contrast --");
	      jb122.setPreferredSize(new Dimension(150,80));
	      jb122.setIcon(contrastdsc);
	      jb122.addActionListener(this);
	      this.add(jb122);
	      
	      Icon left=new ImageIcon("images/left.gif");
		  JButton jb13=new JButton("Rotate Left");
		  jb13.setPreferredSize(new Dimension(150,80));
		  jb13.setIcon(left);
		  jb13.addActionListener(this);
		  this.add(jb13);
	      
	      Icon right=new ImageIcon("images/right.gif");
		  JButton jb14=new JButton("Rotate Right");
		  jb14.setPreferredSize(new Dimension(150,80));
		  jb14.setIcon(right);
		  jb14.addActionListener(this);
	      this.add(jb14);
	      
		  Icon brightinc=new ImageIcon("images/brightness.png");
		  JButton jb151=new JButton("Brightness ++");
		  jb151.setPreferredSize(new Dimension(150,80));
		  jb151.setIcon(brightinc);
		  jb151.addActionListener(this);
		  this.add(jb151);
		  
		  Icon brightdsc=new ImageIcon("images/brightness.png");
		  JButton jb152=new JButton("Brightness --");
		  jb152.setPreferredSize(new Dimension(150,80));
		  jb152.setIcon(brightdsc);
		  jb152.addActionListener(this);
		  this.add(jb152);
		  
		  saveas = new JLabel("SAVE AS: ");
		  this.add(saveas);
		  imageformats = new JComboBox(getFormats());
		  imageformats.setActionCommand("Set Formats");
	      imageformats.addActionListener(this);
	      this.add(imageformats);
		}
				
		public void actionPerformed(ActionEvent ae)
		{
			String acmd = ae.getActionCommand();
			// ActionHandler for Crop
			if(acmd == "Crop")
			{
				ImageCropper imgcrop = new ImageCropper(ImgPanel.imagepath, ImgPanel.cropHeight, ImgPanel.cropWidth, ImgPanel.cropStartX, ImgPanel.cropStartY);
		  		ImgPanel.selection = null;
		  		JavaIDE.ip.repaint();
			}
			
			// ActionHandler for Contrast
            if(acmd == "Contrast ++")
			{
				JavaIDE.ip.contrastInc = true;
				JavaIDE.ip.changeScaleFactor();
                System.out.println(JavaIDE.ip.scaleFactor + "=scaleF");
                JavaIDE.ip.rescale();
                JavaIDE.ip.repaint();
			}
			
			if(acmd == "Contrast --")
			{
				JavaIDE.ip.contrastInc = false;
				JavaIDE.ip.changeScaleFactor();
                System.out.println(JavaIDE.ip.scaleFactor + "=scaleF");
                JavaIDE.ip.rescale();
                JavaIDE.ip.repaint();
			}
			
			// Action Handler for Brightness
			if(acmd == "Brightness ++")
			{
				JavaIDE.ip.brighten = true;
				JavaIDE.ip.changeOffSet();
                System.out.println(JavaIDE.ip.offset + "=offset");
                JavaIDE.ip.rescale();
                JavaIDE.ip.repaint();
			}
			
			if(acmd == "Brightness --")
			{
				JavaIDE.ip.brighten = false;
				JavaIDE.ip.changeOffSet();
				System.out.println(JavaIDE.ip.offset + "=offset");
				JavaIDE.ip.rescale();
				JavaIDE.ip.repaint();
			}
			
			// ActionHandler for rotation
			if(acmd == "Rotate Left")
			{
				JavaIDE.ip.rotateLeft();
				JavaIDE.ip.repaint();
			}
			
			if(acmd == "Rotate Right")
			{
				JavaIDE.ip.rotateRight();
				JavaIDE.ip.repaint();
			}
			
			// ActionHandler for Save As
			if(acmd == "Set Formats")
			{
				JComboBox cb = (JComboBox)ae.getSource();
				String format = (String)cb.getSelectedItem();
	             /* Use the format name to initialise the file suffix.
	              * Format names typically correspond to suffixes
	              */
	             File saveFile = new File("savedimage."+format);
	             JFileChooser chooser = new JFileChooser();
	             FileNameExtensionFilter filter=new FileNameExtensionFilter("JPG & images","jpg","gif","bmp","png","wbmp","jpeg");
				 chooser.setFileFilter(filter);
	             chooser.setSelectedFile(saveFile);
	             int rval = chooser.showSaveDialog(cb);
	             if (rval == JFileChooser.APPROVE_OPTION) {
	                 saveFile = chooser.getSelectedFile();
	                 /* Write the filtered image in the selected format,
	                  * to the file chosen by the user.
	                  */
	                 try {
	                	 if(ImgPanel.lastOperation == "zoom" || ImgPanel.lastOperation == "rotateleft" || ImgPanel.lastOperation == "rotateright"
	                		 || ImgPanel.lastOperation == "original")
	                	 {
	                		 ImageIO.write(ImgPanel.image, format, saveFile);
	                		 JOptionPane.showMessageDialog(null, "Saved Succesfully!!");
	                	 }
	                	 else	 
	                	 {
	                		 ImageIO.write(ImgPanel.bi, format, saveFile);
	                		 JOptionPane.showMessageDialog(null, "Saved Succesfully!!");
	                	 }
	                 } catch (IOException ex) {
	                	 JOptionPane.showMessageDialog(null, "Not Saved !!");
	                 }
	             }
			}
			
			// ActionHandler for Negative
			if(acmd == "Negative")
			{
				JavaIDE.ip.reverseLUT();
				JavaIDE.ip.applyFilter();
				JavaIDE.ip.repaint();
			}
		}
		
		/* Return the formats sorted alphabetically and in lower case */
        public String[] getFormats() {
            String[] formats = ImageIO.getWriterFormatNames();
            TreeSet<String> formatSet = new TreeSet<String>();
            for (String s : formats) {
                formatSet.add(s.toLowerCase());
            }
            return formatSet.toArray(new String[0]);
        }
        
} // ToolPanel ends

// Effects Panel Class
class EffectPanel extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	JButton jb16, jb17, jb18;

	EffectPanel()
	{
		setLayout(new FlowLayout(FlowLayout.LEFT));
        setBackground(Color.LIGHT_GRAY);
        
        Icon sharpen=new ImageIcon("images/sharpen.jpg");
	    jb16=new JButton("Sharpen");
	    jb16.setPreferredSize(new Dimension(150,80));
	    jb16.setIcon(sharpen);
	    jb16.addActionListener(this);
		this.add(jb16);
		
		Icon focalbw=new ImageIcon("images/focal bnw.jpg");
	    jb17=new JButton("Blur");
	    jb17.setPreferredSize(new Dimension(150,80));
	    jb17.setIcon(focalbw);
	    jb17.addActionListener(this);
		this.add(jb17);
		
		Icon sepia=new ImageIcon("images/sepia.jpg");
	    jb18=new JButton("Sepia");
	    jb18.setPreferredSize(new Dimension(150,80));
	    jb18.setIcon(sepia);
	    jb18.addActionListener(this);
		this.add(jb18);
		
		Icon bnw=new ImageIcon("images/bnw.jpg");
	    JButton jb19=new JButton("B&W");
	    jb19.setPreferredSize(new Dimension(150,80));
	    jb19.setIcon(bnw);
	    jb19.addActionListener(this);
		this.add(jb19);
		
		Icon warmify=new ImageIcon("images/pencil_sketch.jpg");
	    JButton jb20=new JButton("Pencil Sketch");
	    jb20.setPreferredSize(new Dimension(150,80));
	    jb20.setIcon(warmify);
	    jb20.addActionListener(this);
		this.add(jb20);
		
		Icon original=new ImageIcon("images/original.jpg");
	    JButton jb21=new JButton("Original");
	    jb21.setPreferredSize(new Dimension(150,80));
	    jb21.setIcon(original);
	    jb21.addActionListener(this);
	    this.add(jb21);
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		String acmd = ae.getActionCommand();
		
		//ActionHandler for sharpen and blur
		if(acmd == "Sharpen")
		{
			JavaIDE.ip.sharpen();
			JavaIDE.ip.repaint();
		}
		
		if(acmd == "Blur")
		{
			JavaIDE.ip.blur();
			JavaIDE.ip.repaint();
		}
		
		// ActionHandler for sepia
		if(acmd == "Sepia")
		{
			JavaIDE.ip.sepia();
			JavaIDE.ip.repaint();
		}
		
		// ActionHandler for pencil sketch
		if(acmd == "Pencil Sketch")
		{
			JavaIDE.ip.pencilSketch();
			JavaIDE.ip.repaint();
		}
		
		// ActionHandler for B&W
		if(acmd == "B&W")
		{
			JavaIDE.ip.bNw();
			JavaIDE.ip.repaint();
		}
		
		//ActionHandler for original
		if(acmd == "Original")
		{
			JavaIDE.ip.original();
			JavaIDE.ip.repaint();
		}
	}
}

// Image Panel Class
class ImgPanel extends JPanel implements MouseListener, MouseMotionListener
{
	private static final long serialVersionUID = 1L;
	
	public static BufferedImage image;
	
	Point anchor;
	public static Rectangle selection;
	public static String imagepath;
	String imagename;
	public static String cropimagename;
	int rand;
	boolean exists;
	
	public static int cropHeight;
	public static int cropWidth;
	public static int cropStartX;
	public static int cropStartY;
	
	MediaTracker tracker;
	LookupTable lookupTable;
    Dimension imgSize,iniSize;
    
    public static BufferedImage bi;
    BufferedImage biSrc, biDest;
    BufferedImage bi2;
    public static BufferedImage bi3;
    public static BufferedImage bi4;
    Graphics2D big;
    RescaleOp rescale, rescalesepia;
    float scaleFactor = 1.0f;
    float offset = 10;
    float[] sepiascales;
    float[] sepiaoffsets;
    boolean brighten, contrastInc;
    
    boolean zoom;
    boolean contrast, brightness;
    boolean rotateLeft, rotateRight;
    boolean sharpen, blur;
    boolean sepia;
    boolean pencilsketch;
    boolean bw;
    boolean delete;
    boolean crop;
    boolean original;
    boolean negative;
    boolean rename;
    boolean paste;
    boolean cut;
    boolean open;
    boolean undo;
    boolean redo;
    boolean doRedo;
    boolean lastRotateLeft, lastRotateRight;
    
    public static String lastOperation;
    BufferedImage prevImage;
  
	ImgPanel()
	{
		
	}
	
	ImgPanel(BufferedImage image, String imagepath, String imagename)
	{
		// the opened image
		this.image=image;
		zoom = false;
		contrast = false;
		brightness = false;
		rotateLeft = false;
		rotateRight = false;
		sharpen = false;
		blur = false;
		sepia = false;
		pencilsketch = false;
		bw = false;
		delete = false;
		crop = false;
		original = false;
		negative = false;
		rename = false;
		paste = false;
		cut = false;
		open = false;
		undo = false;
		redo = false;
		doRedo = false;
		lastRotateLeft = lastRotateRight = false;
		
		prevImage = null;
		
		sepiascales = new float[] { 1.0f, 1.0f, 1.0f };
		sepiaoffsets = new float[] { 40.0f, 20.0f, -20.0f };
		tracker=new MediaTracker(this);
	    tracker.addImage(image,1);
	    
	    try {
	        tracker.waitForAll();
	    }
	    catch(Exception ie){}
	    
	    setBackground(Color.gray); 
        loadImage();
        setSize(image.getWidth(this),
        		image.getWidth(this));
        createBufferedImages();
        bi = biSrc;
	        
	    imgSize=iniSize=new Dimension(image.getWidth(this),image.getHeight(this));
	    
		ImgPanel.imagepath = imagepath;
		this.imagename = imagename;
		
		setPreferredSize(new Dimension(image.getWidth(),image.getHeight()));	
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	@Override
	public Dimension getPreferredSize()
	{
	    return new Dimension(imgSize);
	}
	
	// methods for zoom in and zoom out
	 public void increaseSize()
	 {
	    zoom = true;
		int x=10*imgSize.width/100; 
	    int y=10*imgSize.height/100;
	    imgSize=new Dimension(imgSize.width+x,imgSize.height+y); 
	    if(imgSize.width>iniSize.width){
	        setSize(imgSize);
	        getParent().doLayout();
	    }
	    repaint();
	 }
	 
     public void decreaseSize()
     {
	     zoom = true;
    	 int x=10*imgSize.width/100;
	     int y=10*imgSize.height/100;
	     imgSize=new Dimension(imgSize.width-x,imgSize.height-y);
	     if(getWidth()>iniSize.width){
	         setSize(imgSize);
	         getParent().doLayout();
	             }
	     repaint();
     }
    
    // methods for mouse area selection
    public void mousePressed(MouseEvent e) 
  	{
  		anchor = e.getPoint();
  		selection = new Rectangle(anchor);
  	}
  	
  	public void mouseDragged(MouseEvent e) 
  	{
  		selection.setBounds((int)Math.min(anchor.x,e.getX()), (int)Math.min(anchor.y,e.getY()),
  								(int)Math.abs(e.getX()-anchor.x), (int)Math.abs(e.getY()-anchor.y));
  		repaint();
  	}
  	
  	public void mouseReleased(MouseEvent e) 
  	{
  		cropHeight = selection.height;
		cropWidth = selection.width;
		cropStartX = selection.x;
		cropStartY = selection.y;
  	}
 	
 	// unused
 	public void mouseMoved(MouseEvent e) {}
 	public void mouseClicked(MouseEvent e) {}
 	public void mouseEntered(MouseEvent e) {}
 	public void mouseExited(MouseEvent e) {}
 	
 	// methods for contrast brightness
 	public void loadImage() {
 		//image = (BufferedImage)Toolkit.getDefaultToolkit().getImage("pic.jpg");
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(image, 1);
        try {
            mt.waitForAll();
        } catch (Exception e) {
            System.out.println("Exception while loading.");
        }
 
        if (image.getWidth(this) == -1) {
            System.out.println("No jpg file");
            System.exit(0);
        }
    }

    public void createBufferedImages() {
        biSrc = new BufferedImage(image.getWidth(this),
        						  image.getHeight(this),
                                  BufferedImage.TYPE_INT_RGB);

        big = biSrc.createGraphics();
        big.drawImage(image, 0, 0, this);

        biDest = new BufferedImage(image.getWidth(this),
        						   image.getHeight(this),
                                   BufferedImage.TYPE_INT_RGB);
        bi = biSrc;
    }

    public void changeOffSet() {
        if (brighten) {
        	brightness = true;
            if (offset < 255)
               offset = offset+5.0f;
        }
        else {
            brightness = true;
        	if (offset > 0)
               offset = offset-5.0f;
        }
    }

    public void changeScaleFactor() {
        if (contrastInc) {
        	contrast = true;
            if (scaleFactor < 2)
                scaleFactor = scaleFactor+0.1f;
        }
        else {
        	contrast = true;
            if (scaleFactor > 0)
                scaleFactor = scaleFactor-0.1f;
        }
    }

    public void rescale() {
        	rescale = new RescaleOp(scaleFactor, offset, null);
        	rescale.filter(biSrc, biDest);
        	bi = biDest;
    }
    
    // methods for sharpen and blur
    public void sharpen() {
    	sharpen = true;
        float data[] = { -1.0f, -1.0f, -1.0f, -1.0f, 9.0f, -1.0f, -1.0f, -1.0f,
            -1.0f };
        Kernel kernel = new Kernel(3, 3, data);
        ConvolveOp convolve = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP,
            null);
        convolve.filter(biSrc, biDest);
        bi = biDest;
      }

      public void blur() {
    	  blur = true;
        float data[] = { 0.0625f, 0.125f, 0.0625f, 0.125f, 0.25f, 0.125f,
            0.0625f, 0.125f, 0.0625f };
        Kernel kernel = new Kernel(3, 3, data);
        ConvolveOp convolve = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP,
            null);
        convolve.filter(biSrc, biDest);
        bi = biDest;
      }
    
    // methods for rotate
    public void rotateLeft()
    {
    	rotateLeft = true;
    	
    }
    
    public void rotateRight()
    {
    	rotateRight = true;
    }
    
    // method for sepia effect
    public void sepia()
    {
    	sepia = true;
    	// Create a copy of the original image in grayscale.

        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        BufferedImageOp biop = new ColorConvertOp(cs, null);
        bi2 = biop.filter(bi, null);

        // Convert grayscaled image from TYPE_GRAY to TYPE_RGB.

        bi3 = new BufferedImage(bi.getWidth(), bi.getHeight(),
                                              bi.getType());
        Graphics g2 = bi3.createGraphics();
        g2.drawImage(bi2, 0, 0, null);
        g2.dispose();   
    }
    
    // method for pencil sketch
    public void pencilSketch()
    {
    	pencilsketch = true;
    	BufferedImage src = ImageUtils.convertImageToARGB(image);
    	BufferedImage target = null;
    	//transformations begin=============
    	//gray scale
    	PointFilter grayScaleFilter = new GrayscaleFilter();
    	BufferedImage grayScale = new BufferedImage(src.getWidth(),src.getHeight(),src.getType());
    	grayScaleFilter.filter(src, grayScale);

    	//inverted gray scale
    	BufferedImage inverted = new BufferedImage(src.getWidth(),src.getHeight(),src.getType());
    	PointFilter invertFilter = new InvertFilter();
    	invertFilter.filter(grayScale,inverted);

    	//gaussian blurr
    	GaussianFilter gaussianFilter = new GaussianFilter(20);
    	BufferedImage gaussianFiltered = new BufferedImage(src.getWidth(),src.getHeight(),src.getType());
    	gaussianFilter.filter(inverted, gaussianFiltered);

    	//color dodge
    	ColorDodgeComposite cdc = new ColorDodgeComposite(1.0f);
    	CompositeContext cc = cdc.createContext(inverted.getColorModel(), grayScale.getColorModel(), null);
    	Raster invertedR = gaussianFiltered.getRaster();
    	Raster grayScaleR = grayScale.getRaster();
    	BufferedImage composite = new BufferedImage(src.getWidth(),src.getHeight(),src.getType());
    	WritableRaster colorDodgedR = composite.getRaster();
    	cc.compose(invertedR, grayScaleR , colorDodgedR);

    	//==================================
    	target = composite;
    	bi = target;
    }
    
    // method for black and white
    public void bNw()
    {
    	bw = true;
    	//transformations begin=============
    	//gray scale
    	PointFilter grayScaleFilter = new GrayscaleFilter();
    	BufferedImage grayScale = new BufferedImage(image.getWidth(),image.getHeight(),image.getType());
    	grayScaleFilter.filter(image, grayScale);
    	bi = grayScale;
    }
    
    // method to negative image
    public void reverseLUT() {
        byte reverse[] = new byte[256];
        for (int i = 0; i < 256; i++) {
          reverse[i] = (byte) (255 - i);
        }
        lookupTable = new ByteLookupTable(0, reverse);
        negative = true;
      }
    
    public void applyFilter() {
        LookupOp lop = new LookupOp(lookupTable, null);
        lop.filter(biSrc, biDest);
        bi = biDest;
        negative = true;
      }
    
    // method to delete image
    public void deleteImage()
    {
    	String msg = "Do you want to delete the image : " + imagename + "?";
    	File fileReference = new File(imagepath);
		int reply = JOptionPane.showConfirmDialog(null, msg);
		if(reply == JOptionPane.YES_OPTION)
		{
			fileReference.delete();
			JOptionPane.showMessageDialog(null, "Image deleted successfully..");
			delete = true;
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Image not deleted..!! ");
		}
    }
    
    // method for displaying cropped image
    public void crop(File cropFile)
    {
    	crop = true;
    	try {
			bi = ImageIO.read(cropFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    // method to display original image
    public void original()
    {
    	original = true;
    }
    
    // method to display the image first time
    public void openImage()
    {
    	open = true;
    }
    
    // method to display image after renamed
    public void displayRenamedImage(File imageFile)
    {
    	rename = true;
    	try {
			bi = ImageIO.read(imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    // method to display pasted image from clipboard
    public void showPastedImage(Image i)
    {
    	paste = true;
    	bi = new BufferedImage(i.getWidth(this),
				   i.getHeight(this),
                   BufferedImage.TYPE_INT_RGB);
    }
    
    // remove the image that is cut from the application to the clipboard
    public void cutImage()
    {
    	cut = true;
    }
    
    // method for undo
    public void undo()
    {
    	original = true;
    	doRedo = true;
    }
    
    //method for redo
    public void redo()
    {
    	if(prevImage != null && doRedo == true)
    	{
    		redo = true;
    	}
    }
    
    public void update(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        paint(g);
        //paintComponent(g);
    }
    
    // Paint() method
    @Override
	public void paint(Graphics g)
	{		
		super.paint(g);
	    // if contrast
		if (contrast)
	    {
			lastOperation = "contrast";
			contrast = false;
			g.drawImage(bi,(getWidth()-imgSize.width)/2,(getHeight()-imgSize.height)/2,imgSize.width,imgSize.height,this);
			prevImage = bi;
	    }
		
		// if brightness
		else if(brightness)
		{
			lastOperation = "brightness";
			brightness = false;
			g.drawImage(bi,(getWidth()-imgSize.width)/2,(getHeight()-imgSize.height)/2,imgSize.width,imgSize.height,this);
			prevImage = bi;
		}
		
		// if sharpen
		else if (sharpen)
	    {
			lastOperation = "sharpen";
			sharpen = false;
			g.drawImage(bi,(getWidth()-imgSize.width)/2,(getHeight()-imgSize.height)/2,imgSize.width,imgSize.height,this);
			prevImage = bi;
	    }
		
		else if (blur)
	    {
			lastOperation = "blur";
			blur = false;
			g.drawImage(bi,(getWidth()-imgSize.width)/2,(getHeight()-imgSize.height)/2,imgSize.width,imgSize.height,this);
			prevImage = bi;
	    }
		
		// if crop
		else if (selection!=null)
  		{
  			Graphics2D g2d = (Graphics2D)g;
  			g2d.drawImage(bi,(getWidth()-imgSize.width)/2,(getHeight()-imgSize.height)/2,imgSize.width,imgSize.height,this);
  			g2d.draw(selection);
  		}
		
		// if zoom
		else if(zoom)
		{
			lastOperation = "zoom";
			if(imgSize.width<=iniSize.width)
				imgSize=iniSize;
			Graphics2D g2d = (Graphics2D)g;
			g2d.drawImage(image,(getWidth()-imgSize.width)/2,(getHeight()-imgSize.height)/2,imgSize.width,imgSize.height,this);
			prevImage = image;
			//g.drawImage(bi,(getWidth()-bi.getWidth())/2,(getHeight()-bi.getHeight())/2,bi.getWidth(),bi.getHeight(),this);
		}
		
		// if rotate left
		else if(rotateLeft)
		{
			lastOperation = "rotateleft";
			rotateLeft = false;
			Graphics2D g2d=(Graphics2D)g; // Create a Java2D version of g.
	        g2d.translate(imgSize.width/4, imgSize.height/4); // Translate the center of our coordinates.
	        g2d.rotate(-0.5);  // Rotate the image by 0.5 radian.
	        g2d.drawImage(image,(getWidth()-imgSize.width),(getHeight()-imgSize.height),imgSize.width,imgSize.height,this);
	        //g2d.drawImage(bi,(getWidth()-bi.getWidth())/2,(getHeight()-bi.getHeight())/2,bi.getWidth(),bi.getHeight(),this);
	        //g2d.drawImage(bi,(getWidth()-imgSize.width)/2,(getHeight()-imgSize.height)/2,imgSize.width,imgSize.height,this);
	        prevImage = image;
		}
		
		// if rotate right
		else if(rotateRight)
		{
			lastOperation = "rotateright"; 
			rotateRight = false;
			Graphics2D g2d=(Graphics2D)g; // Create a Java2D version of g.
	        g2d.translate(-imgSize.width/4, -imgSize.height/4); // Translate the center of our coordinates.
	        g2d.rotate(0.5);  // Rotate the image by 0.5 radian.
	        g2d.drawImage(image,(getWidth()-imgSize.width)/2,(getHeight()-imgSize.height)/2,imgSize.width,imgSize.height,this);
	        //g2d.drawImage(bi,(getWidth()-bi.getWidth())/2,(getHeight()-bi.getHeight())/2,bi.getWidth(),bi.getHeight(),this);
	        //g2d.drawImage(bi,(getWidth()-imgSize.width)/2,(getHeight()-imgSize.height)/2,imgSize.width,imgSize.height,this);
	        prevImage = image;
		}
		
		else if(sepia)
		{
			lastOperation = "sepia";
			sepia = false;
			Graphics2D g2d = (Graphics2D) g;
		    g2d.drawImage(bi3, new RescaleOp(sepiascales, sepiaoffsets, null), (getWidth()-imgSize.width)/2, (getHeight()-imgSize.height)/2);
		    prevImage = bi3;
		}
		
		else if(pencilsketch)
		{
			lastOperation = "pencilsketch";
			pencilsketch = false;
			Graphics2D g2d = (Graphics2D)g;
  			g2d.drawImage(bi,(getWidth()-imgSize.width)/2,(getHeight()-imgSize.height)/2,imgSize.width,imgSize.height,this);
  			prevImage = bi;
		}
		
		else if(bw)
		{
			lastOperation = "bNw";
			bw = false;
			Graphics2D g2d = (Graphics2D)g;
			g2d.drawImage(bi,(getWidth()-imgSize.width)/2,(getHeight()-imgSize.height)/2,imgSize.width,imgSize.height,this);
			prevImage = bi;
		}
		
		else if(negative)
		{
			lastOperation = "negative";
			negative = false;
			Graphics2D g2d = (Graphics2D)g;
			g2d.drawImage(bi,(getWidth()-imgSize.width)/2,(getHeight()-imgSize.height)/2,imgSize.width,imgSize.height,this);
			prevImage = bi;
		}
		
		else if(crop)
		{
			lastOperation = "crop";
			crop = false;
			Graphics2D g2d = (Graphics2D)g;
			g2d.drawImage(bi,(getWidth()-bi.getWidth())/2,(getHeight()-bi.getHeight())/2,bi.getWidth(),bi.getHeight(),this);
			//g2d.drawImage(bi,(getWidth()-imgSize.width)/2,(getHeight()-imgSize.height)/2,imgSize.width,imgSize.height,this);
			prevImage = bi;
		}
		
		else if(rename)
		{
			lastOperation = "rename";
			rename = false;
			Graphics2D g2d = (Graphics2D)g;
			g2d.drawImage(bi,(getWidth()-bi.getWidth())/2,(getHeight()-bi.getHeight())/2,bi.getWidth(),bi.getHeight(),this);
			//g2d.drawImage(bi,(getWidth()-imgSize.width)/2,(getHeight()-imgSize.height)/2,imgSize.width,imgSize.height,this);
		}
		
		else if(paste)
		{
			lastOperation = "paste";
			paste = false;
			Graphics2D g2d = (Graphics2D)g;
			g2d.drawImage(bi,(getWidth()-bi.getWidth())/2,(getHeight()-bi.getHeight())/2,bi.getWidth(),bi.getHeight(),this);
			//g2d.drawImage(bi,(getWidth()-imgSize.width)/2,(getHeight()-imgSize.height)/2,imgSize.width,imgSize.height,this);
		}
		
		else if(cut)
		{
			lastOperation = "cut";
			cut = false;
			g.drawImage(null,(getWidth()-imgSize.width)/2,(getHeight()-imgSize.height)/2,imgSize.width,imgSize.height,this);
			
		}
		else if(delete)
		{
			lastOperation = "delete";
			delete = false;
			g.drawImage(null,(getWidth()-imgSize.width)/2,(getHeight()-imgSize.height)/2,imgSize.width,imgSize.height,this);
		}
		
		else if(original)
		{
			if(lastOperation == "rotateleft")
				lastRotateLeft = true;
			else if(lastOperation == "rotateright")
				lastRotateRight = true;
			
			lastOperation = "original";
			original = false;
			Graphics2D g2d = (Graphics2D)g;
			g2d.drawImage(image,(getWidth()-imgSize.width)/2,(getHeight()-imgSize.height)/2,imgSize.width,imgSize.height,this);
		}
		
		else if(open)
		{
			lastOperation = "open";
			open = false;
			g.drawImage(bi,(getWidth()-bi.getWidth())/2,(getHeight()-bi.getHeight())/2,bi.getWidth(),bi.getHeight(),this);
			prevImage = bi;
		}
		
		else if(redo)
		{
			redo = false;
			doRedo = false;
			
			if(lastRotateLeft)
			{
				Graphics2D g2d=(Graphics2D)g; // Create a Java2D version of g.
		        g2d.translate(imgSize.width/4, imgSize.height/4); // Translate the center of our coordinates.
		        g2d.rotate(-0.5);  // Rotate the image by 0.5 radian.
		        g2d.drawImage(image,(getWidth()-imgSize.width),(getHeight()-imgSize.height),imgSize.width,imgSize.height,this);
		        lastOperation = "redo";
		        lastRotateLeft = false;
			}
			else if(lastRotateRight)
			{
				Graphics2D g2d=(Graphics2D)g; // Create a Java2D version of g.
		        g2d.translate(-imgSize.width/4, -imgSize.height/4); // Translate the center of our coordinates.
		        g2d.rotate(0.5);  // Rotate the image by 0.5 radian.
		        g2d.drawImage(image,(getWidth()-imgSize.width)/2,(getHeight()-imgSize.height)/2,imgSize.width,imgSize.height,this);
		        lastOperation = "redo";
		        lastRotateRight = false;
			}
			else
			{
				g.drawImage(prevImage,(getWidth()-prevImage.getWidth())/2,(getHeight()-prevImage.getHeight())/2,prevImage.getWidth(),prevImage.getHeight(),this);
				prevImage = null;
				lastOperation = "redo";
			}
		}
		
		// general
		else
		{
			if(zoom)
			{
				zoom = false;
				g.drawImage(image,(getWidth()-imgSize.width)/2,(getHeight()-imgSize.height)/2,imgSize.width,imgSize.height,this);
			}
			else
			{
				if(!(lastOperation == "cut" || lastOperation == "delete" || lastOperation == "redo" || lastOperation == "crop"))
						//g.drawImage(bi,(getWidth()-bi.getWidth())/2,(getHeight()-bi.getHeight())/2,bi.getWidth(),bi.getHeight(),this);
						//g.drawImage(bi,(getWidth()-imgSize.width)/2,(getHeight()-imgSize.height)/2,imgSize.width,imgSize.height,this);
					    g.drawImage(image,(getWidth()-imgSize.width)/2,(getHeight()-imgSize.height)/2,imgSize.width,imgSize.height,this);
			}
				
		}
	}
}
// End of class ImgPanel

// Image Cropper Class
class ImageCropper {
	 
	static Rectangle clip;
	public String inputFileLocation;
	 
	public ImageCropper(String inputfileloc, int cropHeight, int cropWidth, int cropStartX, int cropStartY) {
	
	inputFileLocation = inputfileloc;
	System.out.println("Reading Original File : " + inputFileLocation);
	 
	BufferedImage originalImage = readImage(inputFileLocation);
	 
	/**
	* Image Cropping Parameters
	*/
	BufferedImage processedImage = null;
	try {
		processedImage = cropMyImage(originalImage, cropWidth,
		cropHeight, cropStartX, cropStartY);
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	writeImage(processedImage);
	System.out.println("...Done");
	}
	 
	public static BufferedImage cropMyImage(BufferedImage img, int cropWidth,
	int cropHeight, int cropStartX, int cropStartY) throws Exception {
	BufferedImage clipped = null;
	Dimension size = new Dimension(cropWidth, cropHeight);
	 
	createClip(img, size, cropStartX, cropStartY);
	 
	try {
	int w = clip.width;
	int h = clip.height;
	 
	System.out.println("Crop Width " + w);
	System.out.println("Crop Height " + h);
	System.out.println("Crop Location " + "(" + clip.x + "," + clip.y
	+ ")");
	 
	clipped = img.getSubimage(clip.x, clip.y, w, h);
	 
	System.out.println("Image Cropped. New Image Dimension: "
	+ clipped.getWidth() + "w X " + clipped.getHeight() + "h");
	} catch (RasterFormatException rfe) {
	System.out.println("Raster format error: " + rfe.getMessage());
	return null;
	}
	return clipped;
	}
	 
	/**
	* This method crops an original image to the crop parameters provided.
	*/
	private static void createClip(BufferedImage img, Dimension size,
	int clipX, int clipY) throws Exception {
	boolean isClipAreaAdjusted = false;
	 
	/**Checking for negative X Co-ordinate**/
	if (clipX < 0) {
	clipX = 0;
	isClipAreaAdjusted = true;
	}
	/**Checking for negative Y Co-ordinate**/
	if (clipY < 0) {
	clipY = 0;
	isClipAreaAdjusted = true;
	}
	 
	/**Checking if the clip area lies outside the rectangle**/
	if ((size.width + clipX) <= img.getWidth()
	&& (size.height + clipY) <= img.getHeight()) {
	 
	/**
	* Setting up a clip rectangle when clip area
	* lies within the image.
	*/
	 
	clip = new Rectangle(size);
	clip.x = clipX;
	clip.y = clipY;
	} else {
	 
	/**
	* Checking if the width of the clip area lies outside the image.
	* If so, making the image width boundary as the clip width.
	*/
	if ((size.width + clipX) > img.getWidth())
	size.width = img.getWidth() - clipX;
	 
	/**
	* Checking if the height of the clip area lies outside the image.
	* If so, making the image height boundary as the clip height.
	*/
	if ((size.height + clipY) > img.getHeight())
	size.height = img.getHeight() - clipY;
	 
	/**Setting up the clip are based on our clip area size adjustment**/
	clip = new Rectangle(size);
	clip.x = clipX;
	clip.y = clipY;
	 
	isClipAreaAdjusted = true;
	 
	}
	if (isClipAreaAdjusted)
	System.out.println("Crop Area Lied Outside The Image."
	+ " Adjusted The Clip Rectangle\n");
	}
	 
	/**
	* This method reads an image from the file
	*/
	public static BufferedImage readImage(String fileLocation) {
	BufferedImage img = null;
	try {
	img = ImageIO.read(new File(fileLocation));
	System.out.println("Image Read. Image Dimension: " + img.getWidth()
	+ "w X " + img.getHeight() + "h");
	} catch (IOException e) {
	e.printStackTrace();
	}
	return img;
	}
	 
	/**
	* This method writes a buffered image to a file
	*/
	public static void writeImage(BufferedImage img) {
		
		String format = JOptionPane.showInputDialog("Enter the extension of the image to be saved: jpg, png, gif, bmp ");
		if(format != "")
		{
			File saveFile = new File("savedimage." + format);
	        JFileChooser chooser = new JFileChooser();
	        FileNameExtensionFilter filter=new FileNameExtensionFilter("JPG & images","jpg","gif","bmp","png","wbmp","jpeg");
			chooser.setFileFilter(filter);
	        chooser.setSelectedFile(saveFile);
	        int rval = chooser.showSaveDialog(null);
	        if (rval == JFileChooser.APPROVE_OPTION) {
	            saveFile = chooser.getSelectedFile();
	            /* Write the filtered image in the selected format,
	             * to the file chosen by the user.
	             */
	            try {
	            	
	            	ImageIO.write(img, format, saveFile);
	            	JOptionPane.showMessageDialog(null, "Saved Succesfully!!");
	            	JavaIDE.ip.crop(saveFile);
	            	JavaIDE.ip.repaint();
	            	
	            } catch (IOException ex) {
	            	ex.printStackTrace();
	            }
	        }
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Cropped Image was not saved !!");
		}
	}
}

// class that copies the image to the clipboard
class CopyImagetoClipBoard implements ClipboardOwner {
    public CopyImagetoClipBoard(File imageFile) {
        try {
        	BufferedImage i = ImageIO.read(imageFile);
            TransferableImage trans = new TransferableImage( i );
            Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
            c.setContents( trans, this );
        }
        catch ( IOException x ) {
            x.printStackTrace();
            System.exit( 1 );
        }
    }

    public void lostOwnership( Clipboard clip, Transferable trans ) {
        System.out.println( "Lost Clipboard Ownership" );
    }
}

// class that copies the screenshot to the clipboard
class CopyScreenShot implements ClipboardOwner {
    public CopyScreenShot() {
        try {
            Robot robot = new Robot();
            Dimension screenSize  = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle screen = new Rectangle( screenSize );
            BufferedImage i = robot.createScreenCapture( screen );
            TransferableImage trans = new TransferableImage( i );
            Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
            c.setContents( trans, this );
        }
        catch ( AWTException x ) {
            x.printStackTrace();
            System.exit( 1 );
        }
    }
    
    public void lostOwnership( Clipboard clip, Transferable trans ) {
        System.out.println( "Lost Clipboard Ownership" );
    }
}

// Class that transfers the image contents to ClipBoard
class TransferableImage implements Transferable {

    Image i;

    public TransferableImage( Image i ) {
        this.i = i;
    }

    public Object getTransferData( DataFlavor flavor )
    throws UnsupportedFlavorException, IOException {
        if ( flavor.equals( DataFlavor.imageFlavor ) && i != null ) {
            return i;
        }
        else {
            throw new UnsupportedFlavorException( flavor );
        }
    }

    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] flavors = new DataFlavor[ 1 ];
        flavors[ 0 ] = DataFlavor.imageFlavor;
        return flavors;
    }

    public boolean isDataFlavorSupported( DataFlavor flavor ) {
        DataFlavor[] flavors = getTransferDataFlavors();
        for ( int i = 0; i < flavors.length; i++ ) {
            if ( flavor.equals( flavors[ i ] ) ) {
                return true;
            }
        }

        return false;
    }
}

// Creating the object of our Image Editor
// The public class
public class ImageEditor{
	// Main method
	public static void main(String[] args) {
	  new JavaIDE();
	}
}