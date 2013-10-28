import javax.swing.JFrame;


public class Main {
	private static JFrame mainWindow;
	private static controller.Main mainController;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		mainWindow = new JFrame("Mission Control");
		mainWindow.setSize(800, 400);
		mainWindow.getContentPane().add(getMainController().getView());
		mainWindow.setVisible(true);
	}
	
	public static controller.Main getMainController() {
		if (mainController == null) {
			mainController = new controller.Main();
		}
		
		return mainController;
	}
}
