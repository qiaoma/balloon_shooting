package edu.calstatela.cs202.qiao.homework4;

public class App {
	public static void main(String[] args){
		
		try {
			String filename = null;
			if (args.length == 0){
				filename = "input.txt";
			}else{
				filename = args[0];
			}
			ConfigurationManager cm = new ConfigurationManager(filename);		
			GUIComponent component = new GUIComponent(filename);			
			for (String key : cm.getKeys()){
				String[] values = cm.getString(key).split("\\s*!\\s*");
				component.addComponent(key, values[0], values[1], values[2], values[3]);
			}			
			component.addButtons();
			component.setFrame();
			cm.closeFile();
		} catch (Exception e) {
			System.err.println(e);
		}		
	}
}
