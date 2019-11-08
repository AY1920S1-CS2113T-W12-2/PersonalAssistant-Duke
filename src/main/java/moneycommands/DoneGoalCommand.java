package moneycommands;

import controlpanel.DukeException;
import controlpanel.MoneyStorage;
import controlpanel.Ui;
import money.Account;
import money.Expenditure;
import money.Goal;
import money.Item;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DoneGoalCommand extends MoneyCommand {

    private String inputString;
    private DateTimeFormatter dateTimeFormatter;
    private int indexNo;

    //@@author therealnickcheong
    /**
     * Constructor of the command which initialises the add short-term goal command.
     * with the goal data within the user input.
     * @param cmd add command inputted from user.
     */
    public DoneGoalCommand(String cmd) {
        inputString = cmd;
        dateTimeFormatter  = DateTimeFormatter.ofPattern("d/M/yyyy");
    }

    @Override
    public boolean isExit() {
        return false;
    }


    @Override
    public void execute(Account account, Ui ui, MoneyStorage storage) throws ParseException, DukeException {

        try {
            String temp = inputString.replaceAll("[^-?0-9]", "");
            indexNo = Integer.parseInt(temp);
        } catch (NumberFormatException e) {
            throw new DukeException("Please enter in the format: "
                    + "done goal <index>\n");
        }

        if (indexNo > account.getShortTermGoals().size() || (indexNo < 1)) {
            throw new DukeException("The serial number of the Goal is Out Of Bounds!");
        }

        Goal doneGoal =  account.getShortTermGoals().get(indexNo - 1);
        float price = doneGoal.getPrice();
        String desc = doneGoal.getDescription();
        String category = doneGoal.getCategory();
        LocalDate doneDate = LocalDate.now();

        if (account.getGoalSavings() < price) {
            throw new DukeException("Goal Price exceeds Goal Savings");
        }

        Expenditure e = new Expenditure(price, desc, category, doneDate);
        account.getExpListTotal().add(e);
        account.getShortTermGoals().remove(indexNo - 1);
        storage.addDeletedEntry(doneGoal);
        storage.writeToFile(account);

        ui.appendToOutput(" Nice! This Goal is Completed:\n");
        ui.appendToOutput("  " + doneGoal.toString() + "\n");
        ui.appendToOutput(" Now you have " + (account.getShortTermGoals().size()) + " goals in the list.\n");

        MoneyCommand list = new ListGoalsCommand();
        list.execute(account,ui,storage);
    }

    //remove from getExpListTotal, add back to getShortTermGoals()
    @Override
    //@@author Chianhaoplanks
    public void undo(Account account, Ui ui, MoneyStorage storage) throws DukeException {
        account.getExpListTotal().remove(account.getExpListTotal().size() - 1);
        String temp = inputString.replaceAll("[^0-9]", "");
        int indexNo = Integer.parseInt(temp);

        Item deletedEntry = storage.getDeletedEntry();
        if (deletedEntry instanceof  Goal) {
            account.getShortTermGoals().add(indexNo - 1, (Goal)deletedEntry);
            ui.appendToOutput(" Last command undone: \n");
            ui.appendToOutput(account.getShortTermGoals().get(indexNo - 1).toString() + " added to goals\n");
            ui.appendToOutput(" Now you have " + account.getShortTermGoals().size() + " goals listed\nand "
                    + account.getExpListTotal().size()
                    + " expenses listed\n");
        } else {
            throw new DukeException("Messed up (DoneGoal)");
        }
    }
}
