package uk.ac.cam.ajb327.oop.tick5;

import java.awt.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.*;

public class GUILife extends JFrame implements ListSelectionListener {

	private World mWorld;
	private PatternStore mStore;
	private java.util.List<World> mCachedWorlds = new ArrayList<>();
	private GamePanel mGamePanel;
	private JButton mPlayButton;
	private java.util.Timer mTimer = new java.util.Timer();
	private boolean mPlaying = false;

	public static void main(String[] args) throws IOException {
		PatternStore ps = new PatternStore("http://www.cl.cam.ac.uk/teaching/1617/OOProg/ticks/life.txt");
		GUILife gui = new GUILife(ps);
		gui.setVisible(true);
	}

	public GUILife(PatternStore ps) {
		super("Game of Life");
		mStore = ps;
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1024,768);
		add(createPatternsPanel(), BorderLayout.WEST);
		add(createControlPanel(), BorderLayout.SOUTH);
		add(createGamePanel(), BorderLayout.CENTER);
	}

	private void addBorder(JComponent component, String title) {
		Border etch = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		Border tb = BorderFactory.createTitledBorder(etch,title);
		component.setBorder(tb);
	}

	private JPanel createGamePanel() {
		mGamePanel = new GamePanel();
		addBorder(mGamePanel,"Game Panel");
		return mGamePanel;
	}

	private JPanel createPatternsPanel() {
		JPanel patt = new JPanel();
		addBorder(patt,"Patterns");
		JList<String> list = new JList(mStore.getPatternsNameSorted().toArray());
		list.addListSelectionListener(this);
		JScrollPane scrollPane = new JScrollPane(list);
		patt.setLayout(new BorderLayout());
		patt.add(scrollPane);
		return patt;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		JList<Pattern> list = (JList<Pattern>) e.getSource();
		Pattern p = list.getSelectedValue();
		try {
			if (mPlaying) runOrPause();
			if (p.getWidth() * p.getHeight() <= 64) mWorld = new PackedWorld(p);
			else mWorld = new ArrayWorld(p);
			mCachedWorlds.clear();
			mCachedWorlds.add(mWorld);
		}
		catch (PatternFormatException ex) {
			System.out.println(ex.getMessage());
			mWorld = null;
		}
		mGamePanel.display(mWorld);
	}

	private JPanel createControlPanel() {
		JPanel ctrl =  new JPanel();
		addBorder(ctrl,"Controls");
		ctrl.setLayout(new GridLayout(0, 3));
		JButton btnBack = new JButton("< Back");
		mPlayButton = new JButton("Play");
		JButton btnForward = new JButton("Forward >");
		btnBack.addActionListener(e -> {
			if (mWorld != null) {
				if (mPlaying) runOrPause();
				moveBack();
			}
		});
		mPlayButton.addActionListener(e -> {
			if (mWorld != null) runOrPause();
		});
		btnForward.addActionListener(e -> {
			if (mWorld != null) {
				if (mPlaying) runOrPause();
				moveForward();
			}
		});
		ctrl.add(btnBack);
		ctrl.add(mPlayButton);
		ctrl.add(btnForward);
		return ctrl;
	}

	private void runOrPause() {
		if (mWorld != null) {
			if (mPlaying) {
				mTimer.cancel();
				mPlaying = false;
				mPlayButton.setText("Play");
			} else {
				mPlaying = true;
				mPlayButton.setText("Stop");
				mTimer = new java.util.Timer(true);
				mTimer.scheduleAtFixedRate(new TimerTask() {
					@Override
					public void run() {
						moveForward();
					}
				}, 0, 500);
			}
		}
	}

	private World copyWorld(boolean useCloning) {
		if (useCloning) {
			try {
				return mWorld.clone();
			}
			catch (CloneNotSupportedException e) {
				System.out.println(e.getMessage());
			}
		}
		else {
			if (mWorld instanceof ArrayWorld) {
				return new ArrayWorld((ArrayWorld) mWorld);
			}
			else if (mWorld instanceof PackedWorld) {
				return new PackedWorld((PackedWorld) mWorld);
			}
		}
		return null;
	}

	private void moveBack() {
		if (mWorld != null) {
			if (mWorld.getGenerationCount() != 0) {
				mWorld = mCachedWorlds.get(mWorld.getGenerationCount() - 1);
			}
			mGamePanel.display(mWorld);
		}
	}

	private void moveForward() {
		if (mWorld != null) {
			if (mWorld.getGenerationCount()+1 == mCachedWorlds.size()) {
				mCachedWorlds.add(copyWorld(true));
				mWorld = mCachedWorlds.get(mCachedWorlds.size()-1);
				mWorld.nextGeneration();
			}
			else {
				mWorld = mCachedWorlds.get(mWorld.getGenerationCount()+1);
			}
			mGamePanel.display(mWorld);
		}
	}

}