package uk.ac.cam.ajb327.oop.tick5;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GameOfLife {

	private World mWorld;
	private PatternStore mStore;
	private List<World> mCachedWorlds = new ArrayList<>();

	public GameOfLife(PatternStore ps) {
		mStore = ps;
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

	public void play() throws IOException {

		String response = "";
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Please select a pattern to play (l to list):");
		while (!response.equals("q")) {
			response = in.readLine();
			System.out.println(response);
			if (response.equals("f")) {
				if (mWorld == null) System.out.println("Please select a pattern to play (l to list):");
				else {
					if (mWorld.getGenerationCount()+1 == mCachedWorlds.size()) {
						mCachedWorlds.add(copyWorld(true));
						mWorld = mCachedWorlds.get(mCachedWorlds.size()-1);
						mWorld.nextGeneration();
					}
					else {
						mWorld = mCachedWorlds.get(mWorld.getGenerationCount()+1);
					}
					print();
				}
			} else if (response.equals("b")) {
				if (mWorld.getGenerationCount() != 0) {
					mWorld = mCachedWorlds.get(mWorld.getGenerationCount()-1);
				}
				print();
			} else if (response.equals("l")) {
				List<Pattern> names = mStore.getPatternsNameSorted();
				int i = 0;
				for (Pattern p : names) {
					System.out.println(i + " " + p.getName() + "  (" + p.getAuthor() + ")");
					i++;
				}
			} else if (response.startsWith("p")) {
				List<Pattern> names = mStore.getPatternsNameSorted();
				String[] responses = response.split(" ");
				if (responses.length > 1) {
					try {
						int patternInt = Integer.parseInt(responses[1]);
						Pattern chosenPattern = names.get(patternInt);
						if (chosenPattern.getWidth() * chosenPattern.getHeight() > 64) {
							mCachedWorlds.add(new ArrayWorld(chosenPattern));
						}
						else mCachedWorlds.add(new PackedWorld(chosenPattern));
						mWorld = mCachedWorlds.get(0);
					}
					catch (NumberFormatException e) { }
					catch (PatternFormatException e) {
						System.out.println("Error in pattern: " + e.getMessage());
					}
				}
				print();
			}
		}
	}

	public static void main(String args[]) throws IOException {
		if (args.length != 1) {
			System.out.println("Usage: java GameOfLife <path/url to store>");
			return;
		}
		try {
			PatternStore ps = new PatternStore(args[0]);
			GameOfLife gol = new GameOfLife(ps);
			gol.play();
		}
		catch (IOException ioe) {
			System.out.println("Failed to load pattern store");
		}
	}

	public void print() {
		System.out.println("- " + mWorld.getGenerationCount());
		for (int row = 0; row < mWorld.getHeight(); row++) {
			for (int col = 0; col < mWorld.getWidth(); col++) {
				System.out.print(mWorld.getCell(col, row) ? "#" : "_");
			}
			System.out.println();
		}
	}

}