package gitlet;

import java.io.File;
import java.util.Date;

/**
 * Driver class for Gitlet, the tiny stupid version-control system.
 * Collaborated with Chris Wang, Chen Feng Tsai
 *
 * @author Wei-Min Chou
 */
public class Main {
    /**
     * Path to the main .gitlet folder.
     */
    static final File MAIN_FOLDER = Utils.join(".", ".gitlet");

    /**
     * Path to all the commits made before.
     */
    static final File COMMIT = Utils.join(MAIN_FOLDER, "commit");

    /**
     * Path to the Stage File.
     */
    static final File BLOB = Utils.join(MAIN_FOLDER, "blob");

    /**
     * Path to the Adding File.
     */
    static final File ADDDING = Utils.join(MAIN_FOLDER, "add");

    /**
     * Path to the Remove File.
     */
    static final File REMOVE = Utils.join(MAIN_FOLDER, "remove");

    /**
     * Path to the Branch File.
     */
    static final File BRANCH = Utils.join(MAIN_FOLDER, "branch");

    /**
     * curr.
     */
    private static String curr = "curr";

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains
     * <COMMAND> <OPERAND> ....
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            exit(0);
        }
        String command = args[0];
        commandhandler(command, args);
    }

    /**
     * Handles all commands inputed.
     *
     * @param command the first args
     * @param args    the argument inputted
     */
    public static void commandhandler(String command, String[] args) {
        if (command.equals("init")) {
            if (args.length != 1) {
                exit(2);
            } else {
                new Init().init();
            }
        } else if (command.equals("add")) {
            if (args.length != 2) {
                exit(2);
            } else {
                new Add().add(args[1]);
            }
        } else if (command.equals("commit")) {
            if (args.length != 2) {
                exit(2);
            } else if (args[1].isEmpty()) {
                exit(4);
            } else {
                String parent = Utils.readObject(Utils.join(BRANCH, curr),
                        String.class);
                Commit p = Utils.readObject(Utils.join(BRANCH, parent),
                        Commit.class);
                new Commit().commit(args[1], new Date(0), p, null, p.getcont());
            }
        } else if (command.equals("log")) {
            if (args.length != 1) {
                exit(2);
            } else {
                new Log().log();
            }
        } else if (command.equals("checkout")) {
            if (args.length == 2) {
                new Checkout().checkout(args[1], false);
            } else if (args[1].equals("--")) {
                if (args.length != 3) {
                    exit(2);
                } else {
                    new Checkout().checkout(args[2], true);
                }
            } else if (args.length == 4) {
                if (!args[2].equals("--")) {
                    exit(2);
                } else {
                    new Checkout().checkout(args[1], args[3]);
                }
            }
        } else if (command.equals("global-log")) {
            if (args.length != 1) {
                exit(2);
            } else {
                new GloLog().glolog();
            }
        } else {
            commandhandler2(command, args);
        }
    }

    /**
     * Handles all commands inputed.
     *
     * @param command the first args
     * @param args    the argument inputted
     */
    public static void commandhandler2(String command, String[] args) {
        if (command.equals("find")) {
            if (args.length != 2) {
                exit(2);
            } else {
                new Find().find(args[1]);
            }
        } else if (command.equals("status")) {
            if (args.length != 1) {
                exit(2);
            } else {
                new Status().status();
            }
        } else if (command.equals("branch")) {
            if (args.length != 2) {
                exit(2);
            } else {
                new Branch().branch(args[1]);
            }
        } else if (command.equals("reset")) {
            if (args.length != 2) {
                exit(2);
            } else {
                new Reset().reset(args[1]);
            }
        } else if (command.equals("rm-branch")) {
            if (args.length != 2) {
                exit(2);
            } else {
                new Rmbranch().rmbranch(args[1]);
            }
        } else if (command.equals("rm")) {
            if (args.length != 2) {
                exit(2);
            } else {
                new Rm().rm(args[1]);
            }
        } else if (command.equals("merge")) {
            if (args.length != 2) {
                exit(2);
            } else {
                new Merge().merge(args[1]);
            }
        } else {
            exit(1);
        }

    }


    /**
     * Handles commandline errors.
     *
     * @param kind the kind of error
     */
    public static void exit(int kind) {
        if (kind == 0) {
            Utils.message("Please enter a command.");
            System.exit(0);
        } else if (kind == 1) {
            Utils.message("No command with that name exists.");
            System.exit(0);
        } else if (kind == 2) {
            Utils.message("Incorrect operands.");
            System.exit(0);
        } else if (kind == 3) {
            Utils.message("Not in an initialized Gitlet directory.");
            System.exit(0);
        } else if (kind == 4) {
            Utils.message("Please enter a commit message.");
            System.exit(0);
        }
    }
}

