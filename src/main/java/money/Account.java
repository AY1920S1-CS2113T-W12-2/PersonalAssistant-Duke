package money;

import controlpanel.DukeException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class Account {
    private ArrayList<Income> incomeListTotal;
    private ArrayList<Expenditure> expListTotal;
    private ArrayList<Income> incomeListCurrMonth;
    private ArrayList<Expenditure> expListCurrMonth;
    private ArrayList<Goal> shortTermGoals;
    private ArrayList<Instalment> instalments;
    private ArrayList<BankTracker> bankTrackerList;
    private float totalSavings;
    private float currMonthSavings;
    private float baseSavings;
    private float goalSavings;
    private boolean toInitialize;

    public Account() {
        incomeListTotal = new ArrayList<>();
        expListTotal = new ArrayList<>();
        incomeListCurrMonth = new ArrayList<>();
        expListCurrMonth = new ArrayList<>();
        shortTermGoals = new ArrayList<>();
        instalments = new ArrayList<>();
        bankTrackerList = new ArrayList<>();
        toInitialize = true;
    }

    public Account(Account account) {
        incomeListTotal = account.getIncomeListTotal();
        expListTotal = account.getExpListTotal();
        incomeListCurrMonth = account.getIncomeListCurrMonth();
        expListCurrMonth = account.getExpListCurrMonth();
        shortTermGoals = account.getShortTermGoals();
        instalments = account.getInstalments();
        bankTrackerList = account.getBankTrackerList();
        toInitialize = account.isToInitialize();
        baseSavings = account.getBaseSavings();
        updateSavings();
        populateCurrentMonthLists();
//        if (account.isInitialised()) {
//            toInitialize = false;
//        } else { toInitialize = true; }
    }

    public void initialize(float userSavings, float avgExp) {
        LocalDate nowDate = LocalDate.now();
        Income initialSavings;
        if (userSavings > avgExp * 6) {
            this.baseSavings = avgExp * 6;
            this.goalSavings = userSavings - baseSavings;
            initialSavings = new Income(userSavings, "Initial Savings", nowDate);
            incomeListTotal.add(initialSavings);
        } else {
            initialSavings = new Income(userSavings, "Initial Savings", nowDate);
            incomeListTotal.add(initialSavings);
            this.baseSavings = totalSavings;
            this.goalSavings = 0;
        }
        toInitialize = false;
    }

    public ArrayList<Income> getIncomeListTotal() {
        return incomeListTotal;
    }

    public ArrayList<Expenditure> getExpListTotal() {
        return expListTotal;
    }

    public ArrayList<Income> getIncomeListCurrMonth() {
        return incomeListCurrMonth;
    }

    public ArrayList<Expenditure> getExpListCurrMonth() {
        return expListCurrMonth;
    }

    public ArrayList<Goal> getShortTermGoals() {
        return shortTermGoals;
    }

    public ArrayList<Instalment> getInstalments() {
        return instalments;
    }

    public ArrayList<BankTracker> getBankTrackerList() {
        return bankTrackerList;
    }

    public float getTotalIncome() {
        float total = 0;
        for (Income i : incomeListTotal) {
            total += i.getPrice();
        }
        return total;
    }

    public float getTotalExp() {
        float total = 0;
        for (Expenditure i : expListTotal) {
            total += i.getPrice();
        }
        return total;
    }

    public float getCurrMonthIncome() {
        float total = 0;
        for (Income i : incomeListCurrMonth) {
            total += i.getPrice();
        }
        return total;
    }

    public float getCurrMonthExp() {
        float total = 0;
        for (Expenditure i : expListCurrMonth) {
            total += i.getPrice();
        }
        return total;
    }

    public float getTotalSavings() {
        totalSavings = getTotalIncome() - getTotalExp();
        return totalSavings;
    }

    public float getCurrMonthSavings() {
        currMonthSavings = getCurrMonthIncome() - getCurrMonthExp();
        return currMonthSavings;
    }

    public float getBaseSavings() {
        return baseSavings;
    }

    public float getGoalSavings() {
        goalSavings = getTotalSavings() - getBaseSavings();
        return goalSavings;
    }

    public void updateSavings() {
        totalSavings = getTotalIncome() - getTotalExp();
        currMonthSavings = getCurrMonthIncome() - getCurrMonthExp();
        goalSavings = getTotalSavings() - getBaseSavings();
    }

    public boolean isToInitialize() {
        return toInitialize;
    }

    public boolean isInitialised() {
        return getIncomeListTotal().isEmpty() || getExpListTotal().isEmpty() ||
                getShortTermGoals().isEmpty() || getInstalments().isEmpty() ||
                getBankTrackerList().isEmpty();
    }

    /**
     * This method is run upon initialisation to fill the Month Income List and
     * Month Expenditure List.
     */
    public void populateCurrentMonthLists() {
        getIncomeListCurrMonth().clear();
        getExpListCurrMonth().clear();
        Calendar dateNow = Calendar.getInstance();
        int currMonth = dateNow.get(Calendar.MONTH) + 1;
        int currYear  = dateNow.get(Calendar.YEAR);
        for (Income i : incomeListTotal) {
            if (i.getPayday().getMonthValue() == currMonth && i.getPayday().getYear() == currYear) {
                incomeListCurrMonth.add(i);
            }
        }
        for (Expenditure e : expListTotal) {
            if (e.getDateBoughtDate().getMonthValue() == currMonth && e.getDateBoughtDate().getYear() == currYear) {
                expListCurrMonth.add(e);
            }
        }
    }


    public void setToInitialize(boolean initStatus) {
        this.toInitialize = initStatus;
    }

    public void setBaseSavings(float baseSavings) {
        this.baseSavings = baseSavings;
    }

    /**
     * This method helps to find the corresponding bank account tracker by given description(name)
     * @param name The given description
     * @return The corresponding tracker
     * @throws DukeException Handle the case when there is no such account
     */
    public BankTracker findTrackerByName(String name) throws DukeException {
        BankTracker bankTracker = null;
        for (BankTracker b : bankTrackerList) {
            if (b.getDescription().equals(name)) {
                bankTracker = b;
                break;
            }
        }
        if (bankTracker == null) {
            throw new DukeException("Sorry, FG does not find this account...");
        }
        return bankTracker;
    }
}
