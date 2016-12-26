package uk.ac.cam.ajb327.oop.tick5;

public class ArrayWorld extends World {

	private boolean[][] mWorld;
	private boolean[] mDeadRow;

	public ArrayWorld(Pattern pattern) throws PatternFormatException {
		super(pattern);
		mWorld = new boolean[getHeight()][getWidth()];
		mDeadRow = new boolean[getWidth()];
		getPattern().initialise(this);

		for (int a = 0; a < getHeight(); a++) {
			boolean deadRow = true;
			for (int b = 0; b < getWidth(); b++) {
				if (mWorld[a][b]) deadRow = false;
			}
			if (deadRow) mWorld[a] = mDeadRow;
		}
	}

	public ArrayWorld (String string) throws PatternFormatException {
		this(new Pattern(string));
	}

	public ArrayWorld(ArrayWorld world) {
		super(world);
		int height = world.getHeight();
		int width = world.getWidth();
		mWorld = new boolean[height][width];
		mDeadRow = world.mDeadRow;
		for (int a = 0; a < height; a++) {
			boolean[] row = new boolean[width];
			boolean deadRow = true;
			for (int b = 0; b < width; b++) {
				row[b] = world.getCell(b,a);
				if (row[b]) deadRow = false;
			}
			if (deadRow) mWorld[a] = mDeadRow;
			else mWorld[a] = row;
		}
	}

	@Override
	public ArrayWorld clone() throws CloneNotSupportedException {
		ArrayWorld clone = (ArrayWorld)super.clone();
		clone.mDeadRow = mDeadRow;
		int height = getHeight();
		int width = getWidth();
		boolean[][] mWorldClone = new boolean[height][width];
		for (int a = 0; a < height; a++) {
			boolean[] row = new boolean[width];
			boolean deadRow = true;
			for (int b = 0; b < width; b++) {
				row[b] = getCell(b,a);
				if (row[b]) deadRow = false;
			}
			if (deadRow) mWorldClone[a] = mDeadRow;
			else mWorldClone[a] = row;
		}
		clone.mWorld = mWorldClone;
		return clone;
	}

	@Override
	public boolean getCell(int c, int r) {
		return (r >= 0 && c >= 0 && r < getHeight() && c < getWidth()) ? mWorld[r][c] : false;
	}

	@Override
	public void setCell(int c, int r, boolean val) {
		if (r >= 0 && c >= 0 && r < getHeight() && c < getWidth()) mWorld[r][c] = val;
	}

	@Override
	protected void nextGenerationImpl() {
		boolean[][] newWorld = new boolean[getHeight()][getWidth()];
		for (int row = 0; row < getHeight(); row++) {
			for (int col = 0; col < getWidth(); col++) {
				newWorld[row][col] = computeCell(col, row);
			}
		}
		mWorld = newWorld;
	}

}