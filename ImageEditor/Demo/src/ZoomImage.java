
import java.awt.*;
import java.awt.event.*;

class zoomPanel extends Panel{
    MediaTracker tracker;
    Image img;
    Dimension imgSize,iniSize;

    public zoomPanel(){
    setSize(400,275);
    img=Toolkit.getDefaultToolkit().getImage("E:/Project/Demo/bin/Untitled.jpg");
    tracker=new MediaTracker(this);
    tracker.addImage(img,1);

    try{
    tracker.waitForAll();
    }
    catch(Exception ie){}
    imgSize=iniSize=new Dimension(img.getWidth(this),img.getHeight(this));
    }

    public Dimension getPreferredSize(){
    return new Dimension(imgSize);
    }

    public void paint(Graphics g){
    if(imgSize.width<=iniSize.width)
        imgSize=iniSize; 
        g.drawImage(img,(getWidth()-imgSize.width)/2,(getHeight()-imgSize.height)/2,imgSize.width,imgSize.height,this);
    }
    public void increaseSize(){
        int x=10*imgSize.width/100; 
        int y=10*imgSize.height/100;
        imgSize=new Dimension(imgSize.width+x,imgSize.height+y); 
        if(imgSize.width>iniSize.width){
            setSize(imgSize);
            getParent().doLayout();
        }
        repaint();
     }
        public void decreaseSize(){
        int x=10*imgSize.width/100;
        int y=10*imgSize.height/100;
        imgSize=new Dimension(imgSize.width-x,imgSize.height-y);
        if(getWidth()>iniSize.width){
            setSize(imgSize);
            getParent().doLayout();
                }
        repaint();
        }
    }
    public class ZoomImage extends Frame implements ActionListener{
    Button zoomIn,zoomOut;
    Panel pS;
    zoomPanel pN;
    ScrollPane sp;

    public ZoomImage(){
    zoomIn=new Button("Zoom In");
    zoomOut=new Button("Zoom Out");

    zoomIn.addActionListener(this);
    zoomOut.addActionListener(this);
    pN=new zoomPanel();
    pS=new Panel();
    pS.setBounds(0,275,400,25);

    pS.add(zoomIn);
    pS.add(zoomOut);
    setLayout(null);

    sp=new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
    sp.setSize(400,275);

    sp.add(pN);

    Adjustable vadjust = sp.getVAdjustable();
    Adjustable hadjust = sp.getHAdjustable();
    hadjust.setUnitIncrement(10);
    vadjust.setUnitIncrement(10);

    add(sp);
    add(pS);
    setVisible(true);
    setSize(400,320);
    }
    public void paint(Graphics g){ }
    public void actionPerformed(ActionEvent ae){
    if(ae.getSource()==zoomIn) pN.increaseSize();
    if(ae.getSource()==zoomOut)pN.decreaseSize();

    }
    public static void main(String[]args){
        new ZoomImage();
    }
    }
