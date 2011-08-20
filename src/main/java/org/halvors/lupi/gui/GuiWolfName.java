package org.halvors.lupi.gui;

import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTextField;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.halvors.lupi.Lupi;

public class GuiWolfName extends GenericPopup {
	public GuiWolfName(Lupi plugin) {
		int width = 427;
		int height = 240;
		
		// Create the label.
		GenericLabel label = new GenericLabel("Enter a name: ");
		label.setAlign(WidgetAnchor.CENTER_CENTER);
		label.setX(width / 2);
	    label.setY(height / 2 - 50);
		
		// Create the text field.
		GenericTextField textField = new GenericTextField();
		textField.setWidth(200);
		textField.setHeight(20);
		textField.setX(width / 2);
	    textField.setY(height / 2 - 25);
		
		// Create the button.
		GenericButton button = new GenericButton("Done");
		button.setWidth(200);
	    button.setHeight(20);
	    button.setX(width / 2);
	    button.setY(height / 2 + 5);
	
		attachWidget(plugin, label);
		attachWidget(plugin, textField);
		attachWidget(plugin, button);
	}
}
