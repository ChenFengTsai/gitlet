package gitlet;

import java.util.HashMap;
import java.io.File;
import java.util.List;

/**
 * Manages the checkout command.
 * @author Wei-Min Chou
 */
public class Checkout {
    /**
     * Content.
     */
    private HashMap<String, Blob> cont = new HashMap<>();

    /**
     * Name of current branch.
     */
    private String w = Utils.readObject(Utils.join(Main.BRANCH, "curr"),
            String.class);
    /**
     * The commit of the current branch.
     */
    private Commit parent = Utils.readObject(Utils.join(Main.BRANCH, w),
            Commit.class);

    /**
     * The checkout command with only one operand.
     * @param file the name of the file
     * @param isfile whether or not this is a file
     */
    public void checkout(String file, Boolean isfile) {
        if (isfile) {
            cont = parent.getcont();
            Blob b = cont.get(file);
            File f = Utils.join(".", "");
            if (b != null) {
                Utils.writeContents(Utils.join(f, file), b.getfile());
            } else {
                Utils.message("File does not exist in that commit.");
                System.exit(0);
            }
        } else {
            checkforfailures(file);
            Commit branchinfo = Utils.readObject(Utils.join(Main.BRANCH, file),
                    Commit.class);
            cont = branchinfo.getcont();
            File f = Utils.join(".", "");
            for (String s : cont.keySet()) {
                Utils.writeContents(Utils.join(f, s), cont.get(s).getfile());
            }
            for (String s : parent.getcont().keySet()) {
                if (!cont.containsKey(s)) {
                    Utils.join(f, s).delete();
                }
            }
            Utils.writeObject(Utils.join(Main.BRANCH, "curr"), file);
        }
    }

    /**
     * Checkout command for a file and commit id.
     * @param id commit id
     * @param file file name
     */
    public void checkout(String id, String file) {
        String fid = null;
        List<String> sid = Utils.plainFilenamesIn(Main.COMMIT);
        for (String k : sid) {
            if (!k.equals("curr")) {
                String j = k.substring(0, id.length());
                if (id.equals(j)) {
                    fid = k;
                    break;
                }
            }
        }
        if (fid == null) {
            Utils.message("No commit with that id exists. ");
            System.exit(0);
        }
        Commit me = Utils.readObject(Utils.join(Main.COMMIT, fid),
                Commit.class);
        cont = me.getcont();
        Blob b = cont.get(file);
        File f = Utils.join(".", "");
        if (b != null) {
            Utils.writeContents(Utils.join(f, file), b.getfile());
        } else {
            Utils.message("File does not exist in that commit.");
            System.exit(0);
        }
    }

    /**
     * Handles all errors for the checkout command.
     * @param name the name of the branch/file/commit
     */
    public void checkforfailures(String name) {
        List<String> files = Utils.plainFilenamesIn(".gitlet/branch");
        String branch = Utils.readObject(Utils.join(Main.BRANCH, "curr"),
                String.class);
        if (!files.contains(name)) {
            Utils.message("No such branch exists.");
            System.exit(0);
        } else if (untracked(name)) {
            Utils.message("There is an untracked file in the way; "
                    + "delete it or add it first.");
            System.exit(0);
        } else if (branch.equals(name)) {
            Utils.message("No need to checkout the current branch.");
            System.exit(0);
        }
    }

    /**
     * Checks if the file is untracked.
     * @param name Name of the file
     * @return boolean whether or not the file is untracked
     */
    public boolean untracked(String name) {
        boolean res = false;
        List<String> files = Utils.plainFilenamesIn(".");
        Commit c = Utils.readObject(Utils.join(Main.BRANCH, name),
                Commit.class);
        for (String s : files) {
            if (!parent.getcont().containsKey(s)
                    && c.getcont().containsKey(s)) {
                res = true;
            }
        }
        return res;
    }
}
