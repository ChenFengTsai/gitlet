package gitlet;

import java.util.List;

/**
 * Handles the branch command.
 * @author Wei-Min Chou
 */
public class Branch {
    /**
     * Handles the branch command.
     * @param name branch name
     */
    public void branch(String name) {
        List<String> files = Utils.plainFilenamesIn(Main.BRANCH);
        for (String s : files) {
            if (s.equals(name)) {
                Utils.message("A branch with that name already exists.");
                System.exit(0);
            }
        }
        String c = Utils.readObject(Utils.join(Main.BRANCH, "curr"),
                String.class);
        Utils.writeObject(Utils.join(Main.BRANCH, name),
                Utils.readObject(Utils.join(Main.BRANCH, c), Commit.class));
    }
}
