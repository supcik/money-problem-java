// Copyright 2018 Jacques Supcik
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

//---------------------------------------------------------------------------
// Solution of the verbal arithmetic puzzle :
//
// SEND + MORE = MONEY
//
// in "Pure" Java (no import)
//---------------------------------------------------------------------------

package net.supcik.send_more_money;

class puzzle {

    private String[] operands;
    private String result;

    private static final int NUMBER_OF_DIGITS = 10;
    private static final int NUMBER_OF_LETTERS = 26;
    private static final char FIRST_LETTER = 'A';

    puzzle(String[] operands, String result) {
        this.operands = operands;
        this.result = result;
    }

    private static int verbToInt(String verb, int[] mapping) {
        char[] v = verb.toUpperCase().toCharArray();
        if (mapping[v[0] - FIRST_LETTER] == 0) {
            return -1; // a number can't start with zero
        }
        int result = 0;
        for (char ch : v) {
            result = result * NUMBER_OF_DIGITS + mapping[ch - FIRST_LETTER];
        }
        return result;
    }

    private boolean isValidSolution(int[] mapping) {
        int sum = 0;
        for (String a : this.operands) {
            int n = verbToInt(a, mapping);
            if (n < 0) return false;
            sum += n;
        }
        int n = verbToInt(this.result, mapping);
        if (n < 0) return false;
        return sum == n;
    }

    private void recursiveSolve(char[] letters, int letterIndex, boolean[] digits, int[] mapping) {
        if (letterIndex >= letters.length) {
            if (this.isValidSolution(mapping)) {
                System.out.printf("Found   %s%n", this.solutionString(mapping));
            }
        } else {
            for (int i = 0; i < digits.length; i++) {
                if (digits[i]) {
                    mapping[letters[letterIndex] - FIRST_LETTER] = i;
                    digits[i] = false;
                    this.recursiveSolve(letters, letterIndex + 1, digits, mapping);
                    digits[i] = true; // backtracking
                }
            }
        }
    }

    void solve() {
        System.out.printf("Solving %s%n", this);
        // Build a set with all letters
        boolean lettersSet[] = new boolean[NUMBER_OF_LETTERS];
        for (String op : this.operands) {
            for (char ch : op.toUpperCase().toCharArray()) {
                lettersSet[ch - FIRST_LETTER] = true;
            }
        }
        for (char ch : this.result.toUpperCase().toCharArray()) {
            lettersSet[ch - FIRST_LETTER] = true;
        }

        // Build the list of all letters
        int letterCount = 0;
        for (boolean b : lettersSet) {
            if (b) letterCount++;
        }
        if (letterCount > NUMBER_OF_DIGITS) {
            throw new IllegalArgumentException();
        }
        char letters[] = new char[letterCount];
        for (int i = 0, j = 0; j < lettersSet.length; j++) {
            if (lettersSet[j]) {
                letters[i++] = (char) (j + FIRST_LETTER);
            }
        }

        // Build the set of all digits
        boolean digits[] = new boolean[NUMBER_OF_DIGITS];
        for (int i = 0; i < NUMBER_OF_DIGITS; i++) {
            digits[i] = true;
        }

        // create an empty mapping and solve
        int[] mapping = new int[NUMBER_OF_LETTERS];
        recursiveSolve(letters, 0, digits, mapping);
    }


    String solutionString(int[] mapping) {
        String[] args = new String[this.operands.length];
        for (int i = 0; i < this.operands.length; i++) {
            args[i] = String.valueOf(verbToInt(this.operands[i], mapping));
        }
        return String.join(" + ", args) +
                " = " + String.valueOf(verbToInt(this.result, mapping));
    }

    @Override
    public String toString() {
        return String.join(" + ", this.operands) + " = " + this.result;
    }

}

public class app {

    public static void main(String[] args) {
        System.out.println();new puzzle(new String[]{"SEND", "MORE"}, "MONEY").solve();
    }

}
