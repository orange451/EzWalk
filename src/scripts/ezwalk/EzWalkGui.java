package scripts.ezwalk;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSTile;
import scripts.gui.RSGuiTab;
import scripts.gui.backend.RSGuiBox;
import scripts.gui.backend.RSGuiButton;
import scripts.gui.backend.RSGuiCheckbox;
import scripts.gui.backend.RSGuiDropDown;
import scripts.gui.backend.RSGuiMouseListener;
import scripts.gui.backend.RSGuiPanel;
import scripts.gui.backend.RSGuiTextLabel;
import scripts.gui.font.ChatColor;
import scripts.util.names.Locations;

public class EzWalkGui extends RSGuiTab {
	private RSGuiBox boxTravel;
	private RSGuiBox boxCancel;
	private RSGuiPanel panel;
	private RSGuiTextLabel lastLoc;
	private RSGuiTextLabel toLoc;

	public EzWalkGui(String string) {
		setIconFromUrl(string);

		setNotification(true);


		this.panel = getBotPanel();


		CREATE_BOX_TRAVEL();
		CREATE_BOX_CANCEL();
		this.panel.add(this.boxTravel);
	}

	private void CREATE_BOX_CANCEL() {
		this.boxCancel = new RSGuiBox(0, 0, -1, -1);
		this.boxCancel.setPadding(8);
		this.panel.add(this.boxCancel);
		this.panel.remove(this.boxCancel);

		this.boxCancel.add(new RSGuiTextLabel(0, 0, "Destination:").setShadow(true).setBold(true).setColor(ChatColor.GOLD.toColor()));
		this.toLoc = new RSGuiTextLabel(0, 16, "N/A");
		this.toLoc.setShadow(true);
		this.boxCancel.add(this.toLoc);

		this.boxCancel.add(new RSGuiTextLabel(0, 64, "Current Loation:").setShadow(true).setBold(true).setColor(ChatColor.GOLD.toColor()));
		this.lastLoc = new RSGuiTextLabel(0, 80, "N/A");
		this.lastLoc.setShadow(true);
		this.boxCancel.add(this.lastLoc);

		RSGuiButton b = new RSGuiButton("Cancel");
		b.setColor(Color.red);
		b.setSelectColor(Color.white);
		b.setLocation(this.boxCancel.getWidth() / 2 - b.getWidth() / 2, this.boxCancel.getHeight() - b.getHeight());
		this.boxCancel.add(b);
		b.addMouseListener(new RSGuiMouseListener() {
			@Override
			public boolean onMousePress(int x, int y) {
				reset();
				return true;
			}

			@Override
			public void onMouseDown(int x, int y) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onMouseUpdate(int x, int y) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void CREATE_BOX_TRAVEL() {
		this.boxTravel = new RSGuiBox(0, 0, -1, -1);
		this.boxTravel.setPadding(8);
		this.panel.add(this.boxTravel);
		this.panel.remove(this.boxTravel);

		this.boxTravel.add(new RSGuiTextLabel(0, 0, "Destination:"));
		RSGuiDropDown d = new RSGuiDropDown(0, 16, this.boxTravel.getWidth());
		ArrayList<String> locs = EzWalk.supportedLocations;
		for (int i = 0; i < locs.size(); i++) {
			d.addChoice((String)locs.get(i));
		}
		this.boxTravel.add(d);


		RSGuiCheckbox check = new RSGuiCheckbox(0, 48, "Use run where available.");
		this.boxTravel.add(check);


		RSGuiButton b = new RSGuiButton("Start!");
		b.setLocation(this.boxTravel.getWidth() / 2 - b.getWidth() / 2, this.boxTravel.getHeight() - b.getHeight());
		this.boxTravel.add(b);
		b.addMouseListener(new RSGuiMouseListener() {
			@Override
			public boolean onMousePress(int x, int y) {
				panel.removeAll();
				panel.add(boxCancel);

				EzWalk.plugin.walkTo(d.getCurrentChoice(), check.isChecked());
				return true;
			}

			@Override
			public void onMouseDown(int x, int y) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onMouseUpdate(int x, int y) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void reset() {
		this.panel.removeAll();
		this.panel.add(this.boxTravel);
		EzWalk.plugin.cancel();
	}

	public void paint(Graphics g) {
		Locations loc = Locations.closestLocation(null, Player.getPosition());
		Locations inside = Locations.get(Player.getPosition());

		this.lastLoc.setText("Countryside");
		if (inside != null) {
			this.lastLoc.setText(inside.getName());
		} else {
			int dist = Player.getPosition().distanceTo(loc.getCenter());
			if (dist < 24) {
				this.lastLoc.setText(loc.getName());
			}
		}
	}
}
