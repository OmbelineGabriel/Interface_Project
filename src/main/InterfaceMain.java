package main;

import model.Interface;
import view.InterfaceView;

public class InterfaceMain {
public static void main(String[] args) {
		Interface model = new Interface();
		InterfaceView view = new InterfaceView(model);
	}
}
