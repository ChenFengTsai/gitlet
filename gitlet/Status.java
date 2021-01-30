package gitlet;

import java.util.List;

/**
 * Status command.
 * @author Wei-Min Chou
 */
public class Status {

    /**
     * Status command.
     */
    public void status() {
        Utils.message("=== Branches ===");
        String branch = Utils.readObject(Utils.join(Main.BRANCH, "curr"),
                String.class);
        List<String> files = Utils.plainFilenamesIn(".gitlet/branch");
        for (String s : files) {
            if (!s.equals("curr")) {
                if (s.equals(branch)) {
                    Utils.message("*" + s);
                } else {
                    Utils.message(s);
                }
            }
        }
        Utils.message("\n=== Staged Files ===");
        List<String> added = Utils.plainFilenamesIn(".gitlet/add");
        for (String s : added) {
            Utils.message(s);
        }
        Utils.message("\n=== Removed Files ===");
        List<String> removed = Utils.plainFilenamesIn(".gitlet/remove");
        for (String s : removed) {
            Utils.message(s);
        }
        Utils.message("\n=== Modifications Not Staged For Commit ===");
        Utils.message("\n=== Untracked Files ===\n");

    }
}
