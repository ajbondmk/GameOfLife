package uk.ac.cam.ajb327.oop.tick5;

public class PackedWorld extends World {

	private long mWorld;

	public PackedWorld (Pattern pattern) throws PatternFormatException {
		super(pattern);
		if (getWidth() * getHeight() > 64) throw new PatternFormatException("Input too large");
		getPattern().initialise(this);
	}

	public PackedWorld (String string) throws PatternFormatException {
		this(new Pattern(string));
	}

	public PackedWorld(PackedWorld world) {
		super(world);
		mWorld = world.mWorld;
	}

	@Override
	public PackedWorld clone() throws CloneNotSupportedException {
		return (PackedWorld)super.clone();
	}

	@Override
	public boolean getCell(int c, int r) {
		return (c >= 0 && c <= 7 && r >= 0 && r <= 7) ? ((mWorld >> getPosition(c,r)) & 1) == 1 : false;
	}

	@Override
	public void setCell(int c, int r, boolean val) {
		if (c >= 0 && c <= 7 && r >= 0 && r <= 7) {
			if (val) {
				if (!getCell(c,r)) mWorld |= (1L << getPosition(c,r));
			}
			else {
				if (getCell(c,r)) mWorld &= ~(1L << getPosition(c,r));
			}
		}
	}

	@Override
	protected void nextGenerationImpl() {
		long newWorld = mWorld;
		for (int col = 0; col < 8; col++) {
			for (int row = 0; row < 8; row++) {
				if (computeCell(col, row)) {
					if (!getCell(col,row)) newWorld |= (1L << getPosition(col,row));
				}
				else {
					if (getCell(col,row)) newWorld &= ~(1L << getPosition(col,row));
				}
			}
		}
		mWorld = newWorld;
	}

	private int getPosition(int c, int r) {
		return (r * getWidth() + c);
	}

}