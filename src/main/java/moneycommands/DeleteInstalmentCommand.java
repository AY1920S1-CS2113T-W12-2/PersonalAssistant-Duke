package moneycommands;

import controlpanel.DukeException;
import controlpanel.MoneyStorage;
import controlpanel.Ui;
import money.Account;
import money.Instalment;
import money.Item;

import java.text.ParseException;

/**
 * This command deletes an Instalment from the Instalment List according to index extracted from the user input.
 */
public class DeleteInstalmentCommand extends MoneyCommand {
    private String inputString;
    private int serialNo;

    /**
     * Constructor of the command which initialises the delete instalment command.
     * with the index of the item to be deleted within the user input
     * @param command delete command inputted from user
     */
    //@@author ChenChao19
    public DeleteInstalmentCommand(String command) {
        inputString = command;
        serialNo = Integer.parseInt(inputString.replaceAll("[^0-9]", ""));
    }

    @Override
    public boolean isExit() {
        return false;
    }

    /**
     * This method executes the delete instalment command. Takes the index of the item
     * to be deleted from the Instalment List and checks for the item
     * Deletes the item from the list if the item is found
     * @param account Account object containing all financial info of user saved on the programme
     * @param ui Handles interaction with the user
     * @param storage Saves and loads data into/from the local disk
     * @throws DukeException When the index given is out of bounds of the list
     */
    @Override
    public void execute(Account account, Ui ui, MoneyStorage storage) throws DukeException, ParseException {
        if (serialNo > account.getInstalments().size()) {
            throw new DukeException("The serial number of the Instalments is Out Of Bounds!");
        }
        Instalment deletedEntryIns = account.getInstalments().get(serialNo - 1);
        ui.appendToOutput(" Noted. I've removed this Instalment:\n");
        ui.appendToOutput("  " + deletedEntryIns.toString() + "\n");
        ui.appendToOutput(" Now you have " + (account.getInstalments().size() - 1) + " instalments in the list.\n");

        account.getInstalments().remove(serialNo - 1);
        storage.addDeletedEntry(deletedEntryIns);
        storage.writeToFile(account);

        MoneyCommand list = new ListInstalmentCommand();
        list.execute(account,ui,storage);
    }

    @Override
    //@@author Chianhaoplanks
    public void undo(Account account, Ui ui, MoneyStorage storage) throws DukeException {
        Item deletedEntry = storage.getDeletedEntry();
        if (deletedEntry instanceof Instalment) {
            account.getInstalments().add(serialNo - 1, (Instalment)deletedEntry);
            storage.writeToFile(account);
            ui.appendToOutput(" Last command undone: \n");
            ui.appendToOutput(account.getInstalments().get(serialNo - 1).toString() + "\n");
            ui.appendToOutput(" Now you have " + account.getInstalments().size() + " instalments listed\n");
        } else {
            throw new DukeException("u messed up (INS)");
        }
    }
}