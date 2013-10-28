import javax.swing.JFrame;
import controller.ViewController;

public class Main {
	private static JFrame mainWindow;
	private static ViewController mainController;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		mainWindow = new JFrame("Mission Control");
		mainWindow.setSize(800, 400);
		mainWindow.getContentPane().add(getMainController().getView());
		mainWindow.setVisible(true);
	}
	
	public static ViewController getMainController() {
		if (mainController == null) {
			mainController = new controller.Main();
		}
		
		return mainController;
	}
}
