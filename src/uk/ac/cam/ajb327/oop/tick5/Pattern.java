package uk.ac.cam.ajb327.oop.tick5;

public class Pattern implements Comparable<Pattern> {

	private String mName;
	private String mAuthor;
	private int mWidth;
	private int mHeight;
	private int mStartCol;
	private int mStartRow;
	private String mCells;

	public String getName() { return mName; }
	public String getAuthor() { return mAuthor; }
	public int getWidth() { return mWidth; }
	public int getHeight() { return mHeight; }
	public int getStartCol() { return mStartCol; }
	public int getStartRow() { return mStartRow; }
	public String getCells() { return mCells; }

	public Pattern(String format) throws PatternFormatException {
		String[] formatArray = format.split(":");
		if (formatArray.length != 7) throw new PatternFormatException("Invalid pattern format: Incorrect number of fields in pattern (found " + formatArray.length + ").");
		mName = formatArray[0];
		mAuthor = formatArray[1];
		try {
			mWidth = Integer.parseInt(formatArray[2]);
		}
		catch (NumberFormatException e) {
			throw new PatternFormatException("Invalid pattern format: Could not interpret the width field as a number ('" + formatArray[2] + "' given).");
		}
		try {
			mHeight = Integer.parseInt(formatArray[3]);
		}
		catch (NumberFormatException e) {
			throw new PatternFormatException("Invalid pattern format: Could not interpret the height field as a number ('" + formatArray[3] + "' given).");
		}
		try {
			mStartCol = Integer.parseInt(formatArray[4]);
		}
		catch (NumberFormatException e) {
			throw new PatternFormatException("Invalid pattern format: Could not interpret the startX field as a number ('" + formatArray[4] + "' given).");
		}
		try {
			mStartRow = Integer.parseInt(formatArray[5]);
		}
		catch (NumberFormatException e) {
			throw new PatternFormatException("Invalid pattern format: Could not interpret the startY field as a number ('" + formatArray[5] + "' given).");
		}
		mCells = formatArray[6];
	}

	public void initialise(World world) throws PatternFormatException {
		String[] rows = mCells.split(" ");
		for (int r = 0; r < rows.length; r++) {
			char[] row = rows[r].toCharArray();
			for (int c = 0; c < row.length; c++) {
				if (row[c] == '1') world.setCell(mStartCol + c, mStartRow + r, true);
				else if (row[c] != '0') throw new PatternFormatException("Invalid pattern format: Malformed pattern '" + mCells + "'.");
			}
		}
	}

	@Override
	public int compareTo(Pattern o) {
		return this.mName.toLowerCase().compareTo(o.getName().toLowerCase());
	}

	@Override
	public String toString() {
		return mName + " (" + mAuthor + ")";
	}

}