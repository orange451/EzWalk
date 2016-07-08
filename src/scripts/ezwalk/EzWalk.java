package scripts.ezwalk;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

import org.tribot.api.General;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Player;
import org.tribot.api2007.WebWalking;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.EventBlockingOverride;
import org.tribot.script.interfaces.Painting;

import scripts.util.AntiBan;
import scripts.util.BotTask;
import scripts.util.BotTaskWalk;
import scripts.util.names.Locations;
import scripts.util.player.Navigation;

@ScriptManifest(authors = { "orange451" }, category = "Tools", name = "EzWalk", version = 1.00, description = "Walk to almost any F2P location in Runescape!", gameMode = 1)
public class EzWalk extends Script implements Painting,EventBlockingOverride {
	public static ArrayList<String> supportedLocations = new ArrayList<String>();
	public static EzWalk plugin;

	public EzWalkGui gui;
	private BotTask currentTask;

	@Override
	public void run() {
		plugin = this;

		// Update supported locations
		supportedLocations.add( Locations.BARBARIAN_VILLAGE.getName() );
		supportedLocations.add( Locations.EDGEVILLE.getName() );
		supportedLocations.add( Locations.FALADOR.getName() );
		supportedLocations.add( Locations.FALADOR_SQUARE.getName() );
		supportedLocations.add( Locations.FALADOR_BANK_EAST.getName() );
		supportedLocations.add( Locations.FALADOR_BANK_WEST.getName() );
		supportedLocations.add( Locations.FALADOR_CASTLE.getName() );
		supportedLocations.add( Locations.GRAND_EXCHANGE.getName() );
		supportedLocations.add( Locations.VARROK.getName() );
		supportedLocations.add( Locations.VARROK_SQUARE.getName() );
		supportedLocations.add( Locations.VARROK_BANK_EAST.getName() );
		supportedLocations.add( Locations.VARROK_BANK_WEST.getName() );
		supportedLocations.add( Locations.VARROK_MINE_EAST.getName() );
		supportedLocations.add( Locations.VARROK_MINE_WEST.getName() );
		supportedLocations.add( Locations.VARROK_WILDERNESS.getName() );
		supportedLocations.add( Locations.VARROK_SEWER.getName() );
		supportedLocations.add( Locations.LUMBRIDGE.getName() );
		supportedLocations.add( Locations.LUMBRIDGE_BANK.getName() );
		supportedLocations.add( Locations.DRAYNOR_MANOR_START.getName() );
		supportedLocations.add( Locations.DRAYNOR.getName() );
		supportedLocations.add( Locations.DRAYNOR_BANK.getName() );
		supportedLocations.add( Locations.WIZARD_TOWER.getName() );
		supportedLocations.add( Locations.PORT_SARIM.getName() );
		supportedLocations.add( Locations.RIMMINGTON.getName() );
		supportedLocations.add( Locations.ALKHARID.getName() );
		supportedLocations.add( Locations.ALKHARID_BANK.getName() );
		supportedLocations.add( Locations.ALKHARID_MINE.getName() );
		supportedLocations.add( Locations.ALKHARID_PALACE.getName() );
		Collections.sort(supportedLocations);

		// Initiaize gui
		gui = new EzWalkGui("scripts/ezwalk/icon.png");

		// Run script
		while(true) {
			sleep( 50L );

			if ( currentTask != null ) {
				if ( currentTask.isTaskComplete() ) {
					currentTask = currentTask.getNextTask();
					println( "Walking task finished!" );
					gui.setNotification( true );
					gui.reset();
				}
			} else {
				AntiBan.timedActions();
			}
		}
	}

	public void cancel() {
		if ( currentTask == null )
			return;

		currentTask.forceComplete();
		EzWalk.plugin.println("Cancelling walking task...");
	}

	public void walkTo( String location, final boolean run ) {
		if ( currentTask != null )
			return;

		String check = location.replace(" ", "_").toUpperCase();
		final Locations loc = Locations.valueOf(check);
		if ( loc != null ) {
			println("Attempting to walk to: " + loc.getName() );

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
					if ( this.wasForceCompleted() )
						return true;

					WebWalking.setUseRun( run );

					// If we're not inside the location
					if ( !loc.contains(Player.getPosition()) ) {

						// Nagivate to the location
						Navigation.walkTo(loc, new Condition() {
							@Override
							public boolean active() {

								// Do antiban stuff, and AFK player randomly.
								Navigation.doWalkingTasks();

								// Stop when inside or if cancelled.
								return loc.contains(Player.getRSPlayer()) || wasForceCompleted();
							}
						});
					}
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
