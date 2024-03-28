import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            WordSearch wordSearch = new WordSearch();
            int matches = wordSearch.solvePuzzle();
            System.out.println("Total matches found: " + matches);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
