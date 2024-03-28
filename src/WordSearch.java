import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class WordSearch {

    private int rows;
    private int columns;
    private char[][] theBoard;
    private String[] theWords;
    private BufferedReader puzzleStream;
    private BufferedReader wordStream;
    private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    public WordSearch() throws IOException {
        puzzleStream = openFile("Enter puzzle file");
        wordStream = openFile("Enter dictionary file");
        System.out.println("Reading files...");
        readPuzzle();
        readWords();
    }

    private BufferedReader openFile(String message) {
        String fileName = "";
        FileReader theFile;
        BufferedReader fileIn = null;
        do {
            System.out.println(message + ": ");
            try {
                fileName = in.readLine();
                if (fileName == null)
                    System.exit(0);
                theFile = new FileReader(fileName);
                fileIn = new BufferedReader(theFile);
            } catch (IOException e) {
                System.err.println("Cannot open " + fileName);
            }
        } while (fileIn == null);

        System.out.println("Opened " + fileName);
        return fileIn;
    }

    private void readWords() throws IOException {
        List<String> words = new ArrayList<String>();

        String lastWord = null;
        String thisWord;

        while ((thisWord = wordStream.readLine()) != null) {
            if (lastWord != null && thisWord.compareTo(lastWord) < 0) {
                System.err.println("Dictionary is not sorted... skipping");
                continue;
            }
            words.add(thisWord);
            lastWord = thisWord;
        }

        theWords = new String[words.size()];
        theWords = words.toArray(theWords);
    }

    private void readPuzzle() throws IOException {
        List<String> puzzleLines = new ArrayList<>();

        String line;
        int columnCount = -1; // Initialize to an invalid value

        // Read each line of the puzzle file
        while ((line = puzzleStream.readLine()) != null) {
            // Check if this is the first line
            if (columnCount == -1) {
                columnCount = line.length(); // Set the column count based on the length of the first line
            } else {
                // Check if the length of the current line is different from the expected column count
                if (line.length() != columnCount) {
                    throw new IOException("Puzzle file is not rectangular");
                }
            }

            puzzleLines.add(line);
        }

        // If all lines have been read successfully and they all have the same length, proceed to create the puzzle grid
        rows = puzzleLines.size();
        theBoard = new char[rows][columnCount];

        for (int r = 0; r < rows; r++) {
            String row = puzzleLines.get(r);
            for (int c = 0; c < columnCount; c++) {
                theBoard[r][c] = row.charAt(c);
            }
        }
    }

    private int solveDirection(int baseRow, int baseCol, int rowDelta, int colDelta){
        int matches = 0;
        StringBuilder word = new StringBuilder();
        int row = baseRow;
        int col = baseCol;

            while (row >= 0 && row < rows && col >= 0 && col < columns) {
            word.append(theBoard[row][col]);
            int result = prefixSearch(theWords, word.toString());
            if (result >= 0) {
                matches++;
                System.out.println("Found '" + word.toString() + "' at [" + baseRow + "," + baseCol + "] to [" + row + "," + col + "]");
            }
            if (result < -1) break; // No need to continue if the prefix is not found
            row += rowDelta;
            col += colDelta;
        }
            return matches;
    }

    public int solvePuzzle() {
        int totalMatches = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                for (int rd = -1; rd <= 1; rd++) {
                    for (int cd = -1; cd <= 1; cd++) {
                        if (rd == 0 && cd == 0) continue;
                        totalMatches += solveDirection(row, col, rd, cd);
                    }
                }
            }
        }
        return totalMatches;
    }

    private static int prefixSearch(String[] a, String x) {
        return Arrays.binarySearch(a, x);
    }
}