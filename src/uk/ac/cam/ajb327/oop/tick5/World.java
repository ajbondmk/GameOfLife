package uk.ac.cam.ajb327.oop.tick5;

public abstract class World implements Cloneable {

	private Pattern mPattern;

	private int mGeneration = 0;

	public World(Pattern pattern) {
		mPattern = pattern;
	}

	public World(World world) {
		mGeneration = world.mGeneration;
		mPattern = world.mPattern;
	}

	@Override
	public World clone() throws CloneNotSupportedException {
		return (World)super.clone();
	}

	public abstract boolean getCell(int c, int r);

	public abstract void setCell(int c, int r, boolean val);

	public int getWidth() { return mPattern.getWidth(); }

	public int getHeight() { return mPattern.getHeight(); }

	public int getGenerationCount() { return mGeneration; }

	protected void incrementGenerationCount() { mGeneration++; }

	protected Pattern getPattern() { return mPattern; }

	public void nextGeneration() {
		nextGenerationImpl();
		mGeneration++;
	}

	protected abstract void nextGenerationImpl();

	protected int countNeighbours(int x, int y) {
		int runningTotal = 0;
		if (getCell(x - 1, y - 1)) runningTotal++;
		if (getCell(x - 1, y)) runningTotal++;
		if (getCell(x - 1, y + 1)) runningTotal++;
		if (getCell(x, y - 1)) runningTotal++;
		if (getCell(x, y + 1)) runningTotal++;
		if (getCell(x + 1, y - 1)) runningTotal++;
		if (getCell(x + 1, y)) runningTotal++;
		if (getCell(x + 1, y + 1)) runningTotal++;
		return runningTotal;
	}

	protected boolean computeCell(int x, int y) {
		boolean liveCell = getCell(x, y);
		int neighbours = countNeighbours(x, y);
		if (neighbours == 3 || (neighbours == 2 && liveCell)) return true;
		return false;
	}

}