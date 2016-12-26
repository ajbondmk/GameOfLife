package uk.ac.cam.ajb327.oop.tick5;

import java.io.*;
import java.net.*;
import java.util.*;

public class PatternStore {

    private List<Pattern> mPatterns = new LinkedList<>();
    private Map<String,List<Pattern>> mMapAuths = new HashMap<>();
    private Map<String,Pattern> mMapName = new HashMap<>();

    public PatternStore(String source) throws IOException {
        if (source.startsWith("http://")) {
            loadFromURL(source);
        }
        else {
            loadFromDisk(source);
        }
    }

    public PatternStore(Reader source) throws IOException {
        load(source);
    }

    private void load(Reader r) throws IOException {
        BufferedReader b = new BufferedReader(r);
        String line = b.readLine();
        while (line != null) {
            try {
                Pattern thisPattern = new Pattern(line);
                if (!mPatterns.contains(thisPattern)) {
                    mPatterns.add(thisPattern);
                    if (!mMapAuths.containsKey(thisPattern.getAuthor())) {
                        mMapAuths.put(thisPattern.getAuthor(), new LinkedList<Pattern>());
                    }
                    mMapAuths.get(thisPattern.getAuthor()).add(thisPattern);
                    mMapName.put(thisPattern.getName(), thisPattern);
                }
            }
            catch (PatternFormatException e) {
                System.out.println("Invalid pattern input: " + line);
            }
            line = b.readLine();
        }
    }


    private void loadFromURL(String url) throws IOException {
        URL destination = new URL(url);
        URLConnection conn = destination.openConnection();
        load(new java.io.InputStreamReader(conn.getInputStream()));
    }

    private void loadFromDisk(String filename) throws IOException {
        load(new FileReader(filename));
    }

	public List<Pattern> getPatternsNameSorted() {
		List<Pattern> mPatternsCopy = new LinkedList<Pattern>(mPatterns);
		Collections.sort(mPatternsCopy);
		return mPatternsCopy;
	}

	public List<Pattern> getPatternsAuthorSorted() {
		List<Pattern> mPatternsCopy = new LinkedList<Pattern>(mPatterns);
		Collections.sort(mPatternsCopy, new Comparator<Pattern>() {
			@Override
			public int compare(Pattern p1, Pattern p2) {
				int authorCompared = p1.getAuthor().toLowerCase().compareTo(p2.getAuthor().toLowerCase());
				if (authorCompared == 0) return p1.getName().toLowerCase().compareTo(p2.getName().toLowerCase());
				return authorCompared;
			}
		});
		return mPatternsCopy;
	}

	public List<Pattern> getPatternsByAuthor(String author) throws PatternNotFound {
		if (!mMapAuths.containsKey(author)) throw new PatternNotFound("Author '" + author + "' not found.");
		List<Pattern> authorPatterns = new LinkedList<Pattern>(mMapAuths.get(author));
		Collections.sort(authorPatterns);
		return authorPatterns;
	}

	public Pattern getPatternByName(String name) throws PatternNotFound {
    	if (!mMapName.containsKey(name)) throw new PatternNotFound("Pattern '" + name + "' not found.");
		return mMapName.get(name);
	}

	public List<String> getPatternAuthors() {
		List<String> authors = new LinkedList<String>(mMapAuths.keySet());
		Collections.sort(authors, String.CASE_INSENSITIVE_ORDER);
		return authors;
	}

	public List<String> getPatternNames() {
		List<String> patternNames = new LinkedList<String>(mMapName.keySet());
		Collections.sort(patternNames, String.CASE_INSENSITIVE_ORDER);
		return patternNames;
	}
}