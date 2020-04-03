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
    private String function = "NO_FUNC";
    private int nextJumpUUID = 0;

    public LabelProvider(String file) {
        this.file = file;
    }

    public String generatedLabel() {
        nextJumpUUID++;

        return String.join(".", file, String.valueOf(nextJumpUUID));
    }

    public String staticLabel(String name) {
        return String.join(".", file, name);
    }

    public String jumpLabel(String name) {

        // TODO This should only allow certain kinds of labels
        return String.join(".", file, function, name);
    }

    public String functionLabel(String name) {

        // TODO This should only allow certain kinds of labels
        // Also I know this looks kinda dumb but we actually just use
        // the function name itself; we want the function to be callable
        // from anywhere, so we'll just refer to its label.
        return name;
    }

}
