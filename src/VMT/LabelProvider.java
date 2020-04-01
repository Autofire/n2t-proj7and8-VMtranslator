//  Author: Daniel Edwards
//   Class: CS 3650 (Section 1)
// Project: 7 & 8
//     Due: 04/06/2020

package VMT;

/**
 * Used to handle labels. Generates the suckers and makes sure we
 * always end up with unique ones. Make a new one when you are
 * changing files.
 */
public class LabelProvider {
    private String file;
    private int nextJumpUUID = 0;

    public LabelProvider(String file) {
        this.file = file;
    }

    public String nextLabel() {
        nextJumpUUID++;

        return String.join(".", file, String.valueOf(nextJumpUUID));
    }

    public String labelPrefix() {
        return file;
    }

}
