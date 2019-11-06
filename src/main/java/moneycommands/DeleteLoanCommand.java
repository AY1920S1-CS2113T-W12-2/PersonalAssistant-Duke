package moneycommands;

import controlpanel.DukeException;
import controlpanel.MoneyStorage;
import controlpanel.Ui;
import money.Account;
import money.Item;
import money.Loan;
import moneycommands.MoneyCommand;

/**
 * This command deletes a loan from the Loans List according to index.
 */
public class DeleteLoanCommand extends MoneyCommand {

    private String inputString;
    private int serialNo;

    //@@author chengweixuan
    /**
     * Constructor of the command which initialises the delete expenditure command
     * with the index of the item to be deleted within the user input.
     * @param command delete command inputted from user
     */
    public DeleteLoanCommand(String command) throws DukeException {
        try {
            inputString = command;
            String temp = inputString.replaceFirst("delete loan ", "");
            temp = temp.replaceAll(" ", "");
            serialNo = Integer.parseInt(temp);
        } catch (NumberFormatException e) {
            throw new DukeException("Please enter a numerical number as the index of the loan to be deleted\n");
        }
    }

    @Override
    public boolean isExit() {
        return false;
    }

    /**
     * This method executes the delete loan command. Takes the index of the item
     * to be deleted from the Loans List and checks for the item.
     * Deletes the item from the list if the item is found.
     * Note that the index given is for the Total Loans List and not the Outgoing Loans List
     * or Incoming Loans List.
     * @param account Account object containing all financial info of user saved on the programme
     * @param ui Handles interaction with the user
     * @param storage Saves and loads data into/from the local disk
     * @throws DukeException When the index given is out of bounds of the list
     */
    @Override
    public void execute(Account account, Ui ui, MoneyStorage storage) throws DukeException {
        if (serialNo > account.getLoans().size() || serialNo <= 0) {
            throw new DukeException("The serial number of the loan is Out Of Bounds!");
        }
        Loan deletedEntryLoan = account.getLoans().get(serialNo - 1);
        String typeStr = deletedEntryLoan.getType().toString().toLowerCase();
        ui.appendToOutput(" Noted. I've removed this " + typeStr + " loan:\n");
        ui.appendToOutput("  " + account.getLoans().get(serialNo - 1).toString() + "\n");
        ui.appendToOutput(" Now you have " + (account.getLoans().size() - 1) + " total loans.\n");

        account.getLoans().remove(serialNo - 1);
        storage.addDeletedEntry(deletedEntryLoan);
        storage.writeToFile(account);
    }

    @Override
    //@@author Chianhaoplanks
    public void undo(Account account, Ui ui, MoneyStorage storage) throws DukeException {
        Item deletedEntry = storage.getDeletedEntry();
        if (deletedEntry instanceof Loan){
            account.getLoans().add(serialNo - 1, (Loan)deletedEntry);
            storage.writeToFile(account);
            ui.appendToOutput(" Last command undone: \n");
            ui.appendToOutput(account.getLoans().get(serialNo - 1).toString() + "\n");
            ui.appendToOutput(" Now you have " + account.getLoans().size() + " loans listed\n");
        } else {
            throw new DukeException("u messed up (LOA)");
        }
    }
}