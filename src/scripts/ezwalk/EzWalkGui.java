package scripts.ezwalk;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import scripts.gui.RSGui;
import scripts.gui.RSGuiBox;
import scripts.gui.RSGuiButton;
import scripts.gui.RSGuiCheckbox;
import scripts.gui.RSGuiDropDown;
import scripts.gui.RSGuiFrame;
import scripts.gui.RSGuiMouseListener;
import scripts.gui.RSGuiPanel;
import scripts.gui.RSGuiTextLabel;
import scripts.util.BotTask;
import scripts.util.BotTaskWalk;

public class EzWalkGui extends RSGui {
	private RSGuiBox boxTravel;
	private RSGuiBox boxCancel;

	private RSGuiPanel panel;

	public EzWalkGui(String string) {
		super( string );

		// Create the main panel
		this.panel = this.getBotPanel();

		// Create main boxes
		CREATE_BOX_TRAVEL();
		CREATE_BOX_CANCEL();
		panel.add(boxTravel);
	}

	private void CREATE_BOX_CANCEL() {
		// Create content box
		this.boxCancel = new RSGuiBox( 0, 0, -1, -1 );
		this.boxCancel.setPadding( 8 );
		this.panel.add( boxCancel );
		this.panel.remove( boxCancel );

		// Start button
		RSGuiButton b = new RSGuiButton( "Cancel" );
		b.setColor(Color.red);
		b.setSelectColor(Color.white);
		b.setLocation( boxCancel.getWidth()/2 - b.getWidth()/2, boxCancel.getHeight() - b.getHeight() );
		boxCancel.add(b);
		b.addMouseListener( new RSGuiMouseListener() {
			@Override public void onMouseDown(int x, int y) { }
			@Override public void onMouseUpdate(int x, int y) { }

			@Override
			public boolean onMousePress(int x, int y) {
				reset();
				return true;
			}
		});
	}

	private void CREATE_BOX_TRAVEL() {
		// Create content box
		this.boxTravel = new RSGuiBox( 0, 0, -1, -1 );
		this.boxTravel.setPadding( 8 );
		this.panel.add( boxTravel );
		this.panel.remove( boxTravel );

		// Create dropdown box
		boxTravel.add( new RSGuiTextLabel(0,0,"Destination:") );
		final RSGuiDropDown d = new RSGuiDropDown( 0, 16, boxTravel.getWidth() );
		ArrayList<String> locs = EzWalk.supportedLocations;
		for (int i = 0; i < locs.size(); i++) {
			String str = locs.get(i).replace("_", " ").toLowerCase();
			str = toTitleCase( str );
			d.addChoice( str );
		}
		boxTravel.add(d);

		// Add run button
		final RSGuiCheckbox check = new RSGuiCheckbox( 0, 48, "Use run where available.");
		boxTravel.add(check);

		// Start button
		RSGuiButton b = new RSGuiButton( "Start!" );
		b.setLocation( boxTravel.getWidth()/2 - b.getWidth()/2, boxTravel.getHeight() - b.getHeight() );
		boxTravel.add(b);
		b.addMouseListener( new RSGuiMouseListener() {
			@Override public void onMouseDown(int x, int y) { }
			@Override public void onMouseUpdate(int x, int y) { }

			@Override
			public boolean onMousePress(int x, int y) {
				panel.remove(boxTravel);
				panel.add(boxCancel);
				close();

				EzWalk.plugin.walkTo( d.getCurrentChoice(), check.isChecked() );
				return true;
			}
		});
	}

	public void reset() {
		panel.remove(boxCancel);
		panel.add(boxTravel);

		BotTask t = EzWalk.plugin.getCurrentTask();
		if ( t != null ) {
			EzWalk.plugin.println("Cancelling walking task...");
			((BotTaskWalk)t).setForceCompleted(true);
		}
	}

	private String toTitleCase(String givenString) {
	    String[] arr = givenString.split(" ");
	    StringBuffer sb = new StringBuffer();

	    for (int i = 0; i < arr.length; i++) {
	        sb.append(Character.toUpperCase(arr[i].charAt(0)))
	            .append(arr[i].substring(1)).append(" ");
	    }
	    return sb.toString().trim();
	}
}
