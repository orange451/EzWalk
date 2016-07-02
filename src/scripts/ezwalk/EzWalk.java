package scripts.ezwalk;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

import org.tribot.api2007.WebWalking;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.EventBlockingOverride;
import org.tribot.script.interfaces.Painting;

import scripts.gui.RSGuiFrame;
import scripts.util.AntiBan;
import scripts.util.BotTask;
import scripts.util.BotTaskWalk;
import scripts.util.Locations;
import scripts.util.Navigation;

@ScriptManifest(authors = { "orange451" }, category = "Navigation", name = "EzWalk", version = 1.00, description = "Walk to almost any F2P location in Runescape!", gameMode = 1)
public class EzWalk extends Script implements Painting,EventBlockingOverride {
	public static ArrayList<String> supportedLocations = new ArrayList<String>();
	public static EzWalk plugin;

	public EzWalkGui gui;
	private BotTask currentTask;

	@Override
	public void run() {
		plugin = this;

		// Update supported locations
		supportedLocations.add( Locations.BARBARIAN_VILLAGE.toString() );
		supportedLocations.add( Locations.EDGEVILLE.toString() );
		supportedLocations.add( Locations.FALADOR.toString() );
		supportedLocations.add( Locations.FALADOR_SQUARE.toString() );
		supportedLocations.add( Locations.FALADOR_BANK_EAST.toString() );
		supportedLocations.add( Locations.FALADOR_BANK_WEST.toString() );
		supportedLocations.add( Locations.FALADOR_CASTLE.toString() );
		supportedLocations.add( Locations.GRAND_EXCHANGE.toString() );
		supportedLocations.add( Locations.VARROK.toString() );
		supportedLocations.add( Locations.VARROK_SQUARE.toString() );
		supportedLocations.add( Locations.VARROK_BANK_EAST.toString() );
		supportedLocations.add( Locations.VARROK_BANK_WEST.toString() );
		supportedLocations.add( Locations.VARROK_MINE_EAST.toString() );
		supportedLocations.add( Locations.VARROK_MINE_WEST.toString() );
		supportedLocations.add( Locations.VARROK_WILDERNESS.toString() );
		supportedLocations.add( Locations.LUMBRIDGE.toString() );
		supportedLocations.add( Locations.LUMBRIDGE_BANK.toString() );
		supportedLocations.add( Locations.DRAYNOR_MANOR_START.toString() );
		supportedLocations.add( Locations.DRAYNOR.toString() );
		supportedLocations.add( Locations.DRAYNOR_BANK.toString() );
		supportedLocations.add( Locations.WIZARD_TOWER.toString() );
		supportedLocations.add( Locations.PORT_SARIM.toString() );
		supportedLocations.add( Locations.RIMMINGTON.toString() );
		supportedLocations.add( Locations.ALKHARID.toString() );
		supportedLocations.add( Locations.ALKHARID_BANK.toString() );
		supportedLocations.add( Locations.ALKHARID_MINE.toString() );
		supportedLocations.add( Locations.ALKHARID_PALACE.toString() );
		Collections.sort(supportedLocations);

		// Initiaize gui
		gui = new EzWalkGui();

		// Run script
		while(true) {
			sleep( 50L );

			if ( currentTask != null ) {
				if ( currentTask.isTaskComplete() ) {
					currentTask = currentTask.getNextTask();
					println( "Walking task finished!" );
					gui.reset();
				}
			} else {
				AntiBan.timedActions();
			}
		}
	}

	public void walkTo( String location, final boolean run ) {
		String check = location.replace(" ", "_").toUpperCase();
		final Locations loc = Locations.valueOf(check);
		if ( loc != null ) {
			println("Attempting to walk to: " + loc.toString() );
			currentTask = new BotTask() {

				@Override
				public String getTaskName() {
					return null;
				}

				@Override
				public BotTask getNextTask() {
					return null;
				}

				@Override
				public boolean isTaskComplete() {
					WebWalking.setUseRun( run );
					Navigation.walkTo(loc, true);
					return true;
				}

				@Override
				public void init() {
					//
				}
			};
		}
	}

	public void onPaint(Graphics g) {
		gui.onPaint(g);
	}

	@Override
	public OVERRIDE_RETURN overrideKeyEvent(KeyEvent arg0) {
		return gui.keyEvent(arg0);
	}

	@Override
	public OVERRIDE_RETURN overrideMouseEvent(MouseEvent arg0) {
		return gui.mouseEvent(arg0);
	}

	public BotTask getCurrentTask() {
		return this.currentTask;
	}

}
