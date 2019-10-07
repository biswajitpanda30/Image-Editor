import java.util.List;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
/**
15.
* Simple demonstration of the SepiaTone Effect.
16.
*
17.
* @author Dustin
18.
*/
public class SepiaEffect extends Application
{
/** Default width of displayed photographs/images. */
private final static int DEFAULT_WIDTH = 540;
/**
25.
* Overridden (from parent Application class) method.
26.
*
27.
* @param stage Primary stage.
28.
* @throws Exception JavaFX 2.0 application exception or file I/O exception.
29.
*/
@Override
public void start(final Stage stage) throws Exception
{
// Get command-line parameters and access first argument as image URL.
final Parameters params = getParameters();
final List<String> parameters = params.getRaw();
final String imageUrl = !parameters.isEmpty() ? parameters.get(0) : "";
 
// The third-to-last 'true' preserves height/width ratio and the next
// 'true' argument indicates better quality (smooth) filtering should be
// used and the final 'true' indicates that background loading should be
// used. The imageUrl must really be a URL and begin with a protocol
// such as file:\\ or http:\\
final Image loadedImage = new Image(imageUrl, DEFAULT_WIDTH, 405, true, true, true);
final ImageView originalView = new ImageView(loadedImage);
final ImageView sepiaView = new ImageView(loadedImage);
sepiaView.setEffect(new SepiaTone());  // default is full (1.0) effect
final HBox horizontalBox = new HBox();
horizontalBox.getChildren().add(originalView);
horizontalBox.getChildren().add(sepiaView);
stage.setTitle("Demonstration of JavaFX 2.0 Sepia Effect");
final Group rootGroup = new Group();
final Scene scene = new Scene(rootGroup, DEFAULT_WIDTH*2, 405, Color.WHITE);
rootGroup.getChildren().add(horizontalBox);
stage.setScene(scene);
stage.show();

}
/**
61.
* Main function for running demonstration of JavaFX 2.0 SepiaTone Effect.
62.
*
63.
* @param arguments Command-line arguments: none expected.
64.
*/
public static void main(final String[] arguments)
{
Application.launch(arguments);
}
}