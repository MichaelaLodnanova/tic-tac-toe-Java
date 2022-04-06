package cz.muni.fi.pb162.hw01.impl;

import com.beust.jcommander.Parameter;
import cz.muni.fi.pb162.hw01.Utils;
import cz.muni.fi.pb162.hw01.cmd.CommandLine;
import cz.muni.fi.pb162.hw01.cmd.Messages;

/**
 * Application class represents the command line interface of this application.
 *
 * You are expected to implement the {@link Application#run()} method
 *
 * @author jcechace
 */
public class Application {
    @Parameter(names = { "--size", "-s" })
    private int boardSize = 3;

    // Implement additional command line flags

    @Parameter(names = "--help", help = true)
    private boolean showUsage = false;

    @Parameter(names = {"--win", "-w"})
    private int winSize = 3;

    @Parameter(names = {"--history", "-h"})
    private int historySize = 1;

    @Parameter(names = {"--playes", "-p"})
    private String players = "xo";

    /**
     * Application entry point
     *
     * @param args command line arguments of the application
     */
    public static void main(String[] args) {
        Application app = new Application();

        CommandLine cli = new CommandLine(app);
        cli.parseArguments(args);

        if (app.showUsage) {
            cli.showUsage();
        } else {
            app.run();
        }
    }

    /**
     * Application runtime logic
     */
    private void run() {

        //initialize and validate game configuration
        Configuration configuration = new Configuration(boardSize, winSize, historySize, players);
        if (configuration.isValid()) {
            //create the TicTacToeManager object that controls the game
            GameManager ticTacToe = new Manager(configuration);
            ticTacToe.playGame();
        } else {
            Utils.error(Messages.ERROR_INVALID_COMMAND);
            System.exit(1);
        }

    }
}
