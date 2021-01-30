package gitlet;

import java.util.List;
import java.io.File;

/**
 * Manages rm command.
 * @author Wei-Min Chou
 */
public class Rm {
    /**
     * Manages the file.
     * @param file the file to remove
     */
    public void rm(String file) {
        String branch = Utils.readObject(Utils.join(Main.BRANCH, "curr"),
                String.class);
        Commit head = Utils.readObject(Utils.join(Main.BRANCH, branch),
                Commit.class);
        List<String> files = Utils.plainFilenamesIn(".gitlet/add");
        List<String> total = Utils.plainFilenamesIn(".");
        if (!head.getcont().containsKey(file) && !files.contains(file)) {
            Utils.message("No reason to remove the file.");
            System.exit(0);
        }
        if (files.contains(file)) {
            File f = Utils.join(Main.ADDDING, file);
            f.delete();
            System.exit(0);
        }
        if (head.getcont().containsKey(file)) {
            Blob b = head.getcont().get(file);
            Utils.writeObject(Utils.join(Main.REMOVE, file), b);
            Utils.join(".", file).delete();
        }
    }
}
