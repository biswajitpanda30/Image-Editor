
/*
 * Part of the Java Image Processing Cookbook, please see
 * http://www.lac.inpe.br/~rafael.santos/JIPCookbook.jsp
 * for information on usage and distribution.
 * Rafael Santos (rafael.santos@lac.inpe.br)
 */

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * This application creates a pure black-and-white image. In this image all pixels values are 
 * either zero (black) or one (white).
 * The JAI API is not used in this example.
 */
public class CreateBWImageNoJAI
  {
  public static void main(String[] args) throws IOException 
    {
    int width = 400; // Dimensions of the image
    int height = 400;
    // Let's create a BufferedImage for a binary image.
    BufferedImage im = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_BINARY);
    // We need its raster to set the pixels' values.
    WritableRaster raster = im.getRaster();
    // Put the pixels on the raster. Note that only values 0 and 1 are used for the pixels.
    // You could even use other values: in this type of image, even values are black and odd
    // values are white.
    for(int h=0;h<height;h++)
      for(int w=0;w<width;w++)
        if (((h/50)+(w/50)) % 2 == 0) raster.setSample(w,h,0,0); // checkerboard pattern.
        else raster.setSample(w,h,0,1);
    // Store the image using the PNG format.
    ImageIO.write(im,"PNG",new File("checkboard.png"));
    }

  }