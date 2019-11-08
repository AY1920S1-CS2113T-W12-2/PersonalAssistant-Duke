package controlpanel;

import moneycommands.AddIncomeCommand;
import moneycommands.AddExpenditureCommand;
import moneycommands.SettleLoanCommand;
import moneycommands.AddGoalCommand;
import moneycommands.CreateBankAccountCommand;
import moneycommands.DeleteExpenditureCommand;
import moneycommands.DeleteGoalCommand;
import moneycommands.DeleteIncomeCommand;
import moneycommands.ExitMoneyCommand;
import moneycommands.InitCommand;
import moneycommands.ListBankTrackerCommand;
import moneycommands.ListGoalsCommand;
import moneycommands.ListTotalExpenditureCommand;
import moneycommands.ListTotalIncomeCommand;
import moneycommands.StartCommand;
import moneycommands.MoneyCommand;
import moneycommands.CommitGoalCommand;
import moneycommands.DoneGoalCommand;
import moneycommands.FindCommand;
import moneycommands.DeleteBankAccountCommand;
import moneycommands.GraphCommand;
import moneycommands.UndoCommand;
import moneycommands.AddInstalmentCommand;
import moneycommands.DeleteInstalmentCommand;
import moneycommands.ListInstalmentCommand;
import moneycommands.ViewPastExpenditureCommand;
import moneycommands.ViewPastIncomeCommand;
import moneycommands.AddLoanCommand;
import moneycommands.ListLoansCommand;
import moneycommands.DeleteLoanCommand;
import moneycommands.CheckFutureBalanceCommand;
import moneycommands.InternalTransferCommand;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * This class which takes in the user input from command line and identifies the
 * correct command type. Calls the appropriate MoneyCommand from control panel
 */
public class Parser {
    public Parser() throws DukeException, ParseException {

    }

    /**
     * Constructor which runs the parser to parse all commands.
     * @param cmd Original input command from the user
     * @param isNewUser Boolean to identify if the user if a new or returning user
     * @return MoneyCommand to be called according to the user commands
     * @throws DukeException If the user input is invalid
     * @throws ParseException If invalid date is parsed
     */
    public static MoneyCommand moneyParse(String cmd, boolean isNewUser) throws DukeException, ParseException {
        MoneyCommand moneyCommand = null;

        if (cmd.startsWith("start")) {
            moneyCommand = new StartCommand(isNewUser);
        } else if (cmd.startsWith("init")) {
            moneyCommand = new InitCommand(cmd, isNewUser);
        } else if (cmd.equals("bye")) {
            moneyCommand = new ExitMoneyCommand();
        } else if (isNewUser) {
            throw new DukeException("You are a new user, "
                    + "please type: init [existing savings] [Avg Monthly Expenditure]");
        } else if (cmd.startsWith("bank-account")) {
            moneyCommand = new CreateBankAccountCommand(cmd);
        } else if (cmd.equals("list bank trackers")) {
            moneyCommand = new ListBankTrackerCommand();
        } else if (cmd.startsWith("check-balance ")) {
            moneyCommand = new CheckFutureBalanceCommand(cmd);
        } else if (cmd.startsWith("withdraw ") || cmd.startsWith("deposit")) {
            moneyCommand = new InternalTransferCommand(cmd);
        } else if (cmd.startsWith("goal ")) {
            moneyCommand = new AddGoalCommand(cmd);
        } else if (cmd.equals("list goals")) {
            moneyCommand = new ListGoalsCommand();
        } else if (cmd.startsWith("delete goal")) {
            moneyCommand = new DeleteGoalCommand(cmd);
        } else if (cmd.startsWith("commit goal")) {
            moneyCommand = new CommitGoalCommand(cmd);
        } else if (cmd.startsWith("done goal")) {
            moneyCommand = new DoneGoalCommand(cmd);
        } else if (cmd.startsWith("find#")) {
            moneyCommand = new FindCommand(cmd);
        } else if (cmd.startsWith("add income")) {
            moneyCommand = new AddIncomeCommand(cmd);
        } else if (cmd.startsWith("spent")) {
            moneyCommand = new AddExpenditureCommand(cmd);
        } else if (cmd.equals("list all income")) {
            moneyCommand = new ListTotalIncomeCommand();
        } else if (cmd.equals("list all expenditure")) {
            moneyCommand = new ListTotalExpenditureCommand();
        } else if (cmd.startsWith("delete income")) {
            moneyCommand = new DeleteIncomeCommand(cmd);
        } else if (cmd.startsWith("delete expenditure")) {
            moneyCommand = new DeleteExpenditureCommand(cmd);
        } else if (cmd.startsWith("delete bank-account")) {
            moneyCommand = new DeleteBankAccountCommand(cmd);
        } else if (cmd.startsWith("graph") || cmd.equals("change icon")) {
            moneyCommand = new GraphCommand(cmd);
        } else if (cmd.startsWith("undo")) {
            moneyCommand = new UndoCommand();
        } else if (cmd.startsWith("add instalment")) {
            moneyCommand = new AddInstalmentCommand(cmd);
        } else if (cmd.startsWith("delete instalment")) {
            moneyCommand = new DeleteInstalmentCommand(cmd);
        } else if (cmd.startsWith("list all instalment")) {
            moneyCommand = new ListInstalmentCommand();
        } else if (cmd.equals("list month income")) {
            moneyCommand = new ViewPastIncomeCommand("list month");
        } else if (cmd.equals("list month expenditure")) {
            moneyCommand = new ViewPastExpenditureCommand("list month");
        } else if (cmd.startsWith("check income")) {
            moneyCommand = new ViewPastIncomeCommand(cmd);
        } else if (cmd.startsWith("check expenditure")) {
            moneyCommand = new ViewPastExpenditureCommand(cmd);
        } else if (cmd.startsWith("lent") || cmd.startsWith("borrowed")) {
            moneyCommand = new AddLoanCommand(cmd);
        } else if (cmd.startsWith("list") && cmd.contains("loans")) {
            moneyCommand = new ListLoansCommand(cmd);
        } else if ((cmd.startsWith("paid")) || (cmd.startsWith("received"))) {
            moneyCommand = new SettleLoanCommand(cmd);
        } else if (cmd.startsWith("delete loan")) {
            moneyCommand = new DeleteLoanCommand(cmd);
        } else {
            throw new DukeException("OOPS!!! I'm sorry, but I don't know what that means");
        }
        return moneyCommand;
    }

    //@@ chengweixuan
    private static LocalDate parseCalToDate(Calendar cal) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        Date parseDate = cal.getTime();
        LocalDate parseLocalDate = parseDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String passDate = dateTimeFormatter.format(parseLocalDate);
        return LocalDate.parse(passDate, dateTimeFormatter);
    }

    /**
     * This method checks if a String contains a numeric or non-numeric value.
     * @param checkStr String to be checked
     * @return True if the String is  numeric, else returns false
     */
    public static boolean isNumeric(String checkStr) {
        try {
            int i = Integer.parseInt(checkStr);
        } catch (NullPointerException | NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Takes user input of date for add commands and checks for shortcut dates.
     * If shortcut is found, converts to the correct date according to shortcut.
     * Returns the formatted Date from user inputted date.
     * @param dateStr user input of date
     * @return formatted Date based on user inputted date
     * @throws ParseException if invalid date is parsed
     */
    public static LocalDate shortcutTime(String dateStr) throws ParseException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        String time = dateStr.replaceAll(" ", "");
        final Calendar cal = Calendar.getInstance();

        switch (time) {
        case "now": {
            LocalDate currDate = LocalDate.now();
            String passDate = dateTimeFormatter.format(currDate);
            return LocalDate.parse(passDate, dateTimeFormatter);
        }
        case "ytd": {
            cal.add(Calendar.DATE, -1);
            return parseCalToDate(cal);
        }
        case "tmr": {
            cal.add(Calendar.DATE, +1);
            return parseCalToDate(cal);
        }
        case "lstwk": {
            cal.add(Calendar.DATE, -7);
            return parseCalToDate(cal);
        }
        case "nxtwk": {
            cal.add(Calendar.DATE, +7);
            return parseCalToDate(cal);
        }
        case "lstmth": {
            cal.add(Calendar.MONTH, -1);
            return parseCalToDate(cal);
        }
        case "nxtmth": {
            cal.add(Calendar.MONTH, +1);
            return parseCalToDate(cal);
        }
        case "lstyr": {
            cal.add(Calendar.YEAR, -1);
            return parseCalToDate(cal);
        }
        case "nxtyr": {
            cal.add(Calendar.YEAR, +1);
            return parseCalToDate(cal);
        }
        default:
            return LocalDate.parse(dateStr, dateTimeFormatter);
        }
    }
}
