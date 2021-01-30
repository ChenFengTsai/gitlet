package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Handles the reset command.
 * @author Wei-Min Chou
 */
public class Reset {
    /**
     * The current commit.
     */
    private Commit curr = Utils.readObject(Utils.join(Main.COMMIT, "curr"),
            Commit.class);
    /**
     * The commit in the current head.
     */
    private String currb = Utils.readObject(Utils.join(Main.BRANCH, "curr"),
            String.class);

    /**
     * Handles the rest command.
     * @param commitid the commit id.
     */
    public void reset(String commitid) {
        handle(commitid);
        Commit me = Utils.readObject(Utils.join(Main.COMMIT, commitid),
                Commit.class);
        File f = Utils.join(".", "");
        HashMap<String, Blob> cont = me.getcont();
        for (String s : cont.keySet()) {
            new Checkout().checkout(commitid, s);
        }
        for (String s : Utils.readObject(Utils.join(Main.BRANCH, currb),
                Commit.class).getcont().keySet()) {
            if (!me.getcont().containsKey(s)) {
                Utils.join(f, s).delete();
            }
        }
        Utils.writeObject(Utils.join(Main.BRANCH, currb), me);
        List<String> fs = Utils.plainFilenamesIn(Main.ADDDING);
        for (String fd: fs) {
            File fdf = Utils.join(Main.ADDDING, fd);
            fdf.delete();
        }
        List<String> r = Utils.plainFilenamesIn(Main.REMOVE);
        for (String fd: r) {
            File fdf = Utils.join(Main.REMOVE, fd);
            fdf.delete();
        }
    }

    /**
     * Handles all potential errors.
     * @param id the id of the string
     */
    public void handle(String id) {
        List<String> wfiles = Utils.plainFilenamesIn(".");

        List<String> files = Utils.plainFilenamesIn(".gitlet/commit");
        if (!files.contains(id)) {
            Utils.message("No commit with that id exists.");
            System.exit(0);
        }
        Commit currbcommit = Utils.readObject(Utils.join(Main.BRANCH, currb),
                Commit.class);
        List<String> untrackedFiles = new ArrayList<>();
        Commit me = Utils.readObject(Utils.join(Main.COMMIT, id), Commit.class);
        for (String s : me.getcont().keySet()) {
            if (!currbcommit.getcont().containsKey(s)) {
                untrackedFiles.add(s);
            }
        }
        for (String s : untrackedFiles) {
            if (wfiles.contains(s)) {
                Utils.message("There is an untracked file in the way; "
                        + "delete it or add it first.");
                System.exit(0);
            }
        }

    }
}
