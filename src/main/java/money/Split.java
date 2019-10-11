package money;

import javafx.util.Pair;

import java.time.LocalDate;
import java.util.ArrayList;

public class Split extends Expenditure {

    private ArrayList<Pair<String, Boolean>> parties;
    private boolean isSettled;
    private float eachOwe;
    private float outstandingAmount;

    /**
     * Constructor of the Expenditure Object to record expenditure.
     * @param price Price of the item spent on
     * @param description info on the item
     * @param category Category the item is grouped under
     * @param boughtDate Date which the item is bought
     */
    public Split(float price, String description, String category, LocalDate boughtDate, ArrayList<Pair<String, Boolean>> parties) {
        super(price, description, category, boughtDate);
        this.parties = parties;
        this.isSettled = false;
        this.eachOwe = price / (parties.size() + 1);
        this.outstandingAmount = price - eachOwe;
    }

    @Override
    public String toString() {
        String people = "";
        String status = isSettled ? "Settled" : "Outstanding ";
        for (Pair<String, Boolean> person : parties) {
            String hasPaid = person.getValue() ? "[\u2713]" : "[\u2718]";
            people += hasPaid + person.getKey() + " and ";
        }
        people = people.substring(0, people.length() - 4);
        return "[SE]" + super.toString() +  " [" + status + getOutstandingAmount() + "]\n Split with " + people;
    }

    public String getOutstandingAmount() {
        if (outstandingAmount == 0) {
            return "";
        } else {
            return "$" + outstandingAmount;
        }
    }

    public ArrayList<Pair<String, Boolean>> getParties() {
        return this.parties;
    }

    public void hasSettledSplit(int settleNo) {
        String nameOfPerson = getNameOfPerson(settleNo);
        Pair<String, Boolean> temp = new Pair<>(nameOfPerson, true);
        parties.set(settleNo, temp);
        outstandingAmount -= eachOwe;
        if (outstandingAmount == 0) {
            isSettled = true;
        }
    }

    public String getNameOfPerson(int settleNo) {
        return parties.get(settleNo).getKey();
    }

    public boolean getStatusOfPerson(int settleNo) {
        return parties.get(settleNo).getValue();
    }

    public float getEachOwe() {
        return eachOwe;
    }

    public String getNamesOfPeople() {
        StringBuilder everyone = new StringBuilder();
        for (Pair<String, Boolean> p : parties) {
            String status = "0";
            if (p.getValue()) {
                status = "1";
            }
            String infoPerPerson = p.getKey() + "#" + status;
            everyone.append(infoPerPerson).append(" ! ");
        }
        return everyone.toString().substring(0, everyone.toString().length() - 3);
    }

}
