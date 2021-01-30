package gitlet;

import java.util.List;

/**
 * Manages the rm branch command.
 * @author Wei-Min Chou
 */
public class Rmbranch {
    /**
     * Rm branch.
     * @param bname the name of the branch to be removed
     */
    public void rmbranch(String bname) {
        handle(bname);
        Utils.join(Main.BRANCH, bname).delete();
    }

    /**
     * Handles all potential errors.
     * @param name the error.
     */
    public void handle(String name) {
        List<String> bnames = Utils.plainFilenamesIn(".gitlet/branch");
        String currb = Utils.readObject(Utils.join(Main.BRANCH, "curr"),
                String.class);
        if (!bnames.contains(name)) {
            Utils.message("A branch with that name does not exist.");
            System.exit(0);
        }
        if (currb.equals(name)) {
            Utils.message("Cannot remove the current branch.");
            System.exit(0);
        }
    }
}
