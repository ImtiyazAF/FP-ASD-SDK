/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #1
 * 1 - 5026231136 - Maulana Muhammad Ad-Dzikri
 * 2 - 5026231172 - Mochamad Zhulmi Danovanz H
 * 3 - 5026231197- Imtiyaz Shafhal Afif
 */
package sudoku;
/**
 * An enumeration of constants to represent the status
 * of each cell.
 */
public enum CellStatus {
    GIVEN,         // clue, no need to guess
    TO_GUESS,      // need to guess - not attempted yet
    CORRECT_GUESS, // need to guess - correct guess
    WRONG_GUESS,    // need to guess - wrong guess
    HINTED         // Cell has been revealed as a hint
    // The puzzle is solved if none of the cells have
    //  status of TO_GUESS or WRONG_GUESS
}