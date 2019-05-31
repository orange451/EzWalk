package scripts.ezwalk;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.EventBlockingOverride;
import org.tribot.script.interfaces.EventBlockingOverride.OVERRIDE_RETURN;
import org.tribot.script.interfaces.Painting;
import scripts.gui.RSGui;
import scripts.util.misc.AntiBan;
import scripts.util.misc.WorldSwitcher;
import scripts.util.names.Locations;
import scripts.util.task.BotTask;
import scripts.util.task.BotTaskWalk;

@ScriptManifest(authors={"orange451"}, category="Tools", name="EzWalk", version=1.0D, description="Walk to almost any F2P location in Runescape!", gameMode=1)
public class EzWalk extends Script implements Painting, EventBlockingOverride {
	public static ArrayList<String> supportedLocations = new ArrayList();

	public static EzWalk plugin;
	public EzWalkGui gui;
	private BotTask currentTask;

	public void run() {
		plugin = this;

		supportedLocations.add(Locations.BARBARIAN_VILLAGE.getName());
		supportedLocations.add(Locations.EDGEVILLE.getName());
		supportedLocations.add(Locations.FALADOR.getName());
		supportedLocations.add(Locations.FALADOR_SQUARE.getName());
		supportedLocations.add(Locations.FALADOR_BANK_EAST.getName());
		supportedLocations.add(Locations.FALADOR_BANK_WEST.getName());
		supportedLocations.add(Locations.FALADOR_CASTLE.getName());
		supportedLocations.add(Locations.GRAND_EXCHANGE.getName());
		supportedLocations.add(Locations.VARROK.getName());
		supportedLocations.add(Locations.VARROK_SQUARE.getName());
		supportedLocations.add(Locations.VARROK_BANK_EAST.getName());
		supportedLocations.add(Locations.VARROK_BANK_WEST.getName());
		supportedLocations.add(Locations.VARROK_MINE_EAST.getName());
		supportedLocations.add(Locations.VARROK_MINE_WEST.getName());
		supportedLocations.add(Locations.VARROK_WILDERNESS.getName());
		supportedLocations.add(Locations.VARROK_SEWER.getName());
		supportedLocations.add(Locations.LUMBRIDGE.getName());
		supportedLocations.add(Locations.LUMBRIDGE_BANK.getName());
		supportedLocations.add(Locations.DRAYNOR_MANOR_START.getName());
		supportedLocations.add(Locations.DRAYNOR.getName());
		supportedLocations.add(Locations.DRAYNOR_BANK.getName());
		supportedLocations.add(Locations.WIZARDS_TOWER.getName());
		supportedLocations.add(Locations.PORT_SARIM.getName());
		supportedLocations.add(Locations.RIMMINGTON.getName());
		supportedLocations.add(Locations.ALKHARID.getName());
		supportedLocations.add(Locations.ALKHARID_BANK.getName());
		supportedLocations.add(Locations.ALKHARID_MINE.getName());
		supportedLocations.add(Locations.ALKHARID_PALACE.getName());
		Collections.sort(supportedLocations);

		RSGui.initialize();
		this.gui = new EzWalkGui("http://firstrecon.net/public/tribot/iconRun.png");
		RSGui.addTab(this.gui);

		while(true) {
			sleep(50L);

			if (this.currentTask != null) {

				if (this.currentTask.isTaskComplete()) {
					this.currentTask = this.currentTask.getNextTask();
					println("Walking task finished!");
					this.gui.setNotification(true);
					this.gui.reset();
				}

			} else {
				AntiBan.timedActions();
			}
		}
	}
	
	public void cancel() {
		if (this.currentTask == null) {
			return;
		}
		this.currentTask.forceComplete();
		plugin.println("Cancelling walking task...");
	}

	public void walkTo(String location, boolean run) {
		if (this.currentTask != null) {
			return;
		}

		String check = location.replace(" ", "_").toUpperCase();
		Locations loc = Locations.valueOf(check);

		if (loc != null) {
			println("Attempting to walk to: " + loc.getName());

			this.currentTask = new BotTaskWalk(loc.getRandomizedCenter(5.0F), run) {
				@Override
				public BotTask getNextTask() {
					return null;
				}

				@Override
				public void init() {
					//
				}
			};
		}
	}

	public void onPaint(Graphics g) {
		RSGui.getInstance().onPaint(g);
	}

	public EventBlockingOverride.OVERRIDE_RETURN overrideKeyEvent(KeyEvent arg0) {
		return RSGui.getInstance().keyEvent(arg0);
	}

	public EventBlockingOverride.OVERRIDE_RETURN overrideMouseEvent(MouseEvent arg0) {
		return RSGui.getInstance().mouseEvent(arg0);
	}

	public BotTask getCurrentTask() {
		return this.currentTask;
	}
}
