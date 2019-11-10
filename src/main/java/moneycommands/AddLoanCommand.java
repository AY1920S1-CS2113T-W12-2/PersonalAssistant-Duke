package moneycommands;

import controlpanel.Parser;
import controlpanel.MoneyStorage;
import controlpanel.Ui;
import controlpanel.DukeException;
import money.Account;
import money.Loan;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * This command adds a loan to the Loan List.
 */
public class AddLoanCommand extends MoneyCommand {

    private String inputString;
    private Loan.Type type;

    private float amount;
    private String description;
    private LocalDate startDate;

    private static final int PLACEHOLDER_INT = -1;

    //@@author chengweixuan
    /**
     * Constructor of the command which initialises the add loan command
     * with the loan data within the user input.
     * Differentiates between an add outgoing loan or incoming loan according to
     * data in user input.
     * @param command add command inputted from user
     */
    public AddLoanCommand(String command) {
        if (command.startsWith("lent")) {
            inputString = command.replaceFirst("lent ", "");
            type = Loan.Type.OUTGOING;
        } else if (command.startsWith("borrowed")) {
            inputString = command.replaceFirst("borrowed ", "");
            type = Loan.Type.INCOMING;
        }
    }

    @Override
    public boolean isExit() {
        return false;
    }

    /**
     * This method executes the add loan command. Takes the input data from user and
     * adds an incoming or outgoing loan to the Loan List.
     * @param account Account object containing all financial info of user saved on the programme
     * @param ui Handles interaction with the user
     * @param storage Saves and loads data into/from the local disk
     * @throws ParseException if invalid date is parsed
     * @throws DukeException When the command is invalid
     */
    @Override
    public void execute(Account account, Ui ui, MoneyStorage storage) throws ParseException, DukeException {
        try {
            String[] splitStr = inputString.split(" /amt ", 2);
            description = splitStr[0];
            String[] furSplit = splitStr[1].split("/on ", 2);
            amount = Float.parseFloat(furSplit[0]);
            startDate = Parser.shortcutTime(furSplit[1]);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            throw new DukeException("Please enter in the format: "
                    + "lent/borrowed <person> /amt <amount> /on <date>\n");
        } catch (DateTimeParseException e) {
            throw new DukeException("Invalid date! Please enter date in the format: d/m/yyyy\n");
        }
        if (amount <= 0) {
            throw new DukeException("Loan must be more than zero!\n");
        }

        Loan l = new Loan(amount, description, startDate, type);
        account.getLoans().add(l);
        storage.writeToFile(account);

        int loanTypeSize = PLACEHOLDER_INT;
        if (type == Loan.Type.INCOMING) {
            loanTypeSize = account.getIncomingLoans().size();
        } else if (type == Loan.Type.OUTGOING) {
            loanTypeSize = account.getOutgoingLoans().size();
        }

        ui.appendToOutput(" Got it. I've added this " + l.getType().toString().toLowerCase() + " loan: \n");
        ui.appendToOutput("     ");
        ui.appendToOutput(account.getLoans().get(account.getLoans().size() - 1).toString()
                + "\n");
        ui.appendToOutput(" Now you have " + account.getLoans().size() + " loans listed");
        ui.appendToOutput(" and " + loanTypeSize + " " + l.getType().toString().toLowerCase() + " loans\n");
    }

    @Override
    //@@author Chianhaoplanks
    public void undo(Account account, Ui ui, MoneyStorage storage) {
        int lastIndex = account.getLoans().size() - 1;
        Loan l = account.getLoans().get(lastIndex);
        account.getLoans().remove(l);
        storage.writeToFile(account);

        ui.appendToOutput(" Last command undone: \n");
        ui.appendToOutput(l.toString() + "\n");
        ui.appendToOutput(" Now you have " + account.getLoans().size() + " loans listed\n");
    }
}
