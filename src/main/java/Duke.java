import Commands.Command;
import ControlPanel.DukeException;
import ControlPanel.Parser;
import ControlPanel.Storage;
import ControlPanel.Ui;
import Tasks.TaskList;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;


/**
 * The main class which controls the overall flow, run the program
 */
public class Duke{

    private Ui ui;
    private TaskList tasks;
    private Storage storage;

    /**
     * Duke class acts as a constructor to initialize and setup
     * //@param filePath the path of the tasks.txt which contains the data of the tasks' list
     */
    public Duke(){
        Path currentDir = Paths.get("data/tasks.txt");
        String filePath = currentDir.toAbsolutePath().toString();
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (Exception e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    public String getResponse(String input) {
        try {
            if (input.equals("greeting")) {
                return ui.showWelcome();
            }
            ui.clearOutputString();
            ui.appendToOutput(ui.showLine());
            Command c = Parser.parse(input);
            c.execute(tasks, ui, storage);
        } catch (ParseException | DukeException e) {
            ui.clearOutputString();
            ui.appendToOutput(ui.showError(e.getMessage()));
            return ui.getOutputString();
        } finally {
            ui.appendToOutput(ui.showLine());
        }
        return ui.getOutputString();
    }

}
