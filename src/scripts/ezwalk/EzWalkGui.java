package scripts.ezwalk;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import org.tribot.api2007.Player;

import scripts.gui.RSGui;
import scripts.gui.RSGuiBox;
import scripts.gui.RSGuiButton;
import scripts.gui.RSGuiCheckbox;
import scripts.gui.RSGuiDropDown;
import scripts.gui.RSGuiMouseListener;
import scripts.gui.RSGuiPanel;
import scripts.gui.RSGuiTextLabel;
import scripts.gui.font.ChatColor;
import scripts.util.BotTask;
import scripts.util.BotTaskWalk;
import scripts.util.names.Locations;

public class EzWalkGui extends RSGui {
	private RSGuiBox boxTravel;
	private RSGuiBox boxCancel;
	private RSGuiPanel panel;

	private RSGuiTextLabel lastLoc;
	private RSGuiTextLabel toLoc;

	public EzWalkGui(String string) {
		super( string );

		this.setNotification( true );

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


		// Setup destination
		this.boxCancel.add( new RSGuiTextLabel( 0, 0, "Destination:" ).setShadow(true).setBold(true).setColor(ChatColor.GOLD.toColor()) );
		this.toLoc = new RSGuiTextLabel( 0, 16, "N/A" );
		this.toLoc.setShadow(true);
		this.boxCancel.add( toLoc );


		// Setup current location
		this.boxCancel.add( new RSGuiTextLabel( 0, 64, "Current Loation:" ).setShadow(true).setBold(true).setColor(ChatColor.GOLD.toColor()) );
		this.lastLoc = new RSGuiTextLabel( 0, 80, "N/A" );
		this.lastLoc.setShadow(true);
		this.boxCancel.add( lastLoc );


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
			d.addChoice( locs.get(i) );
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

				toLoc.setText( d.getCurrentChoice() );
				EzWalk.plugin.walkTo( d.getCurrentChoice(), check.isChecked() );
				return true;
			}
		});
	}

	public void reset() {
		panel.remove(boxCancel);
		panel.add(boxTravel);
		EzWalk.plugin.cancel();
	}

	@Override
	public void paint(Graphics g) {
		Locations loc = Locations.closestLocation( Player.getPosition() );
		Locations inside = Locations.get( Player.getPosition() );

		this.lastLoc.setText( "Countryside" );
		if ( inside != null ) {
			this.lastLoc.setText( inside.getName() );
		} else {
			int dist = Player.getPosition().distanceTo( loc.getCenter() );
			if ( dist < 24 ) {
				this.lastLoc.setText( loc.getName() );
			}
		}
	}
}
