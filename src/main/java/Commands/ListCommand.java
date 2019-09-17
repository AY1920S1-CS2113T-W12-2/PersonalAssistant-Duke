package Commands;
import Tasks.*;
import Interface.*;
/**
 * Represents the command to show the list of tasks on a TaskList object
 */
public class ListCommand extends Command {

    /**
     * Executes the display of all the task in the TaskList object
     * @param list The TaskList object to retrieve the contents from
     * @param ui The Ui object to display the list message
     * @param storage The Storage object to access file to load or save the tasks
     * @return
     */
    @Override
    public String execute(TaskList list, Ui ui, Storage storage) {
        return ui.showList(list);
    }
}
