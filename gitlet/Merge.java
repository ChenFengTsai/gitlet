package gitlet;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Handles the merge command.
 * @author Wei-Min Chou
 */
public class Merge {
    /**
     * The curr commit.
     */
    private Commit curr = Utils.readObject(Utils.join(Main.COMMIT, "curr"),
            Commit.class);
    /**
     * The head branch commit.
     */
    private Commit bcom = null;
    /**
     * The given branch name.
     */
    private String bname = null;
    /**
     * A latest common ancestor.
     */
    private Commit split = null;
    /**
     * The current branch.
     */
    private String currb = Utils.readObject(Utils.join(Main.BRANCH, "curr"),
            String.class);
    /**
     * The commit in the current branch.
     */
    private Commit currbcom = Utils.readObject(Utils.join(Main.BRANCH, currb),
            Commit.class);


    /**
     * The merge command.
     * @param branch given branch
     */
    public void merge(String branch) {
        bcom = Utils.readObject(Utils.join(Main.BRANCH, branch),
                Commit.class);
        split = findances(branch);
        bname = branch;
        split = findances(branch);
        handle();
    }

    /**
     * Handles the errors of the merge command.
     */
    public void handle() {
        List<String> add = Utils.plainFilenamesIn(Main.ADDDING);
        List<String> remove = Utils.plainFilenamesIn(Main.REMOVE);
        if (!add.isEmpty() && !remove.isEmpty()) {
            Utils.message("You have uncommitted changes.");
            System.exit(0);
        }
        List<String> branches = Utils.plainFilenamesIn(Main.BRANCH);
        if (branches != null && !branches.contains(bname)) {
            Utils.message("A branch with that name does not exist.");
            System.exit(0);
        }
        if (split.equals(bcom)) {
            Utils.message("Given branch is an ancestor of the current branch.");
            System.exit(0);
        }
        if (curr.equals(bcom)) {
            Utils.message("Cannot merge a branch with itself.");
            System.exit(0);
        }
        if (split.equals(curr)) {
            Utils.writeObject(Utils.join(Main.BRANCH, "curr"), bcom);
            Utils.message("Current branch fast-forwarded.");
            System.exit(0);
        }
        for (String s: bcom.getcont().keySet()) {
            File f = Utils.join(".", s);
            if (!curr.getcont().containsKey(s) && f.exists()) {
                Utils.message("There is an untracked file in the way; "
                        + "delete it or add it first.");
                System.exit(0);
            }
        }
    }

    /**
     * Finds the LCA.
     * @param b1 given branch.
     * @return the LCA commit.
     */
    public Commit findances(String b1) {
        HashSet<Commit> comm = com(bcom);
        HashSet<String> comsha = new HashSet<>();
        for (Commit w : comm) {
            comsha.add(w.getsha());
        }
        LinkedBlockingQueue<Commit> queue = new LinkedBlockingQueue<>();
        queue.add(curr);
        while (!queue.isEmpty()) {
            Commit com = queue.poll();
            if (comsha.contains(com.getsha())) {
                return com;
            }
            if (com.getparent() != null) {
                queue.add(com.getparent());
            }
            if (com.getmerge() != null) {
                queue.add(com.getmerge());
            }
        }
        return null;
    }

    /**
     * Get all commits from com.
     * @param com the commit you want to trace.
     * @return A hashset with all commits.
     */
    public HashSet<Commit> com(Commit com) {
        HashSet<Commit> res = new HashSet<>();
        Commit p = com.getparent();
        Commit m = com.getmerge();
        if (com != null) {
            res.add(com);
            while (p != null) {
                res.add(p);
                p = p.getparent();
            }
            while (m != null) {
                res.add(m);
                m = m.getmerge();
            }
        }
        return res;
    }
}
