package moneycommands;

import controlpanel.DukeException;
import controlpanel.MoneyStorage;
import controlpanel.Parser;
import controlpanel.Ui;
import money.Account;
import money.BankTracker;
import money.Expenditure;
import money.Income;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;


public class InternalTransferCommand extends MoneyCommand {

    private boolean add;
    private String description;
    private float amt;
    private LocalDate date;

    //@@author cctt1014
    /**
     * The constructor for this class.
     * @param inputString The input command
     * @throws ParseException The parse exception
     */
    public InternalTransferCommand(String inputString) throws ParseException, DukeException {
        try {
            String status = inputString.split(" ")[0];
            if (status.equals("deposit")) {
                add = true;
                inputString = inputString.replaceFirst("deposit ", "");
            } else if (status.equals("withdraw")) {
                add = false;
                inputString = inputString.replaceFirst("withdraw ", "");
            }

            amt = Float.parseFloat(inputString.split(" ")[0]);
            if (amt < 0) {
                throw new DukeException("The format for the numbers is wrong, please check and type in again.\n");
            }
            date = Parser.shortcutTime(inputString.split(" /at ")[1]);
            String temp = inputString.split(" /at ")[0];
            description = temp.split(" ", 2)[1];
        } catch (DateTimeParseException e) {
            throw new DukeException("Invalid date! Please enter date in the format: d/m/yyyy\n");
        } catch (NumberFormatException e) {
            throw new DukeException("The format for the numbers is wrong, please check and type in again.\n");
        }

    }

    /**
     * This method labels whether this command means ceasing the overall program.
     * @return this command will not cease the overall program.
     */
    @Override
    public boolean isExit() {
        return false;
    }

    @Override
    public void execute(Account account, Ui ui, MoneyStorage storage) throws DukeException {
        BankTracker bankTracker = account.findTrackerByName(description);
        if (add) {
            bankTracker.updateDate(date);
            bankTracker.addAmt(amt);
            Income income = new Income(amt, "Deposit to " + bankTracker.getDescription(), date);
            account.getIncomeListTotal().add(income);
        } else {
            if (bankTracker.predictAmt(date) < amt) {
                throw new DukeException("Sorry, FG only allow non-zero balance. Here is the account info: \n"
                        + bankTracker.getBankAccountInfo());
            }
            bankTracker.updateDate(date);
            bankTracker.addAmt(0 - amt);
            Expenditure expenditure = new Expenditure(amt, "Withdraw from "
                    + bankTracker.getDescription(), "withdraw from bank", date);
            account.getExpListTotal().add(expenditure);
        }
        storage.writeToFile(account);
        ui.appendToOutput("  Got it. Here is the current information about this account:\n"
                + bankTracker.getBankAccountInfo() + "\n");
    }

    @Override
    //@@author Chianhaoplanks
    public void undo(Account account, Ui ui, MoneyStorage storage) throws DukeException {
        throw new DukeException("Command can't be undone!\n");
    }
}
