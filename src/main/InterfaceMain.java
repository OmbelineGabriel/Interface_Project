package main;
import peltier.PeltierControl;
import model.Interface;
import view.InterfaceView;

public class InterfaceMain {
public static void main(String[] args) {
		PeltierControl.init();
		Interface model = new Interface();
		InterfaceView view = new InterfaceView(model);
	}
}
