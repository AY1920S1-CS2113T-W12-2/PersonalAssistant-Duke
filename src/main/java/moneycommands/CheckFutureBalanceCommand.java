package moneycommands;

import controlpanel.DukeException;
import controlpanel.MoneyStorage;
import controlpanel.Parser;
import controlpanel.Ui;
import money.Account;
import money.BankTracker;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * This class allow user to estimate their future balance in an account.
 */
public class CheckFutureBalanceCommand extends MoneyCommand {

    private String description;
    private LocalDate futureDate;

    //@@author cctt1014
    /**
     * The constructor parses the input command and gets the description and the date.
     * @param inputString The command typed in by the user
     */
    public CheckFutureBalanceCommand(String inputString) throws DukeException {
        try {
            description = inputString.split(" /at ")[0];
            description = description.replaceFirst("check-balance ", "");
            futureDate = Parser.shortcutTime(inputString.split(" /at ")[1]);
        } catch (DateTimeParseException e) {
            throw new DukeException("Invalid date! Please enter date in the format: d/m/yyyy\n");
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

    /**
     * This method firstly find the account based on the description then calculate the number
     * of months between the future date and the latest update date. Based on the interest rate,
     * it can get the future balance.
     * @param account The class record all the financial information of the user
     * @param ui The user interface
     * @param storage The class used to store the information to the local disk
     * @throws DukeException The self-defined exceptions to handle the invalid future date and the non-existing account
     */
    @Override
    public void execute(Account account, Ui ui, MoneyStorage storage) throws DukeException {
        BankTracker bankTracker = account.findTrackerByName(description);
        LocalDate currDate = bankTracker.getLatestDate();
        if (futureDate.isBefore(currDate)) {
            throw new DukeException("The input date is invalid! It should be a "
                    + "date later then the latest update date.");
        }
        Period period = Period.between(currDate, futureDate);
        int length = period.getMonths() + period.getYears() * 12;
        float balance = bankTracker.getAmt();
        double rate = bankTracker.getRate();
        balance *=  Math.pow((1 + rate),length);
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String stringBalance = decimalFormat.format(balance);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        ui.appendToOutput("  The future balance in " + description + " :\n    " + stringBalance + " at "
                + dateTimeFormatter.format(futureDate) + "\n");
    }

    @Override
    //@@author Chianhaoplanks
    public void undo(Account account, Ui ui, MoneyStorage storage) throws DukeException {
        throw new DukeException("Command can't be undone!\n");
    }
}
