package money;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Expenditure extends Item {

    private String category;
    private LocalDate boughtDate;
    private DateTimeFormatter dateTimeFormatter;

    /**
     * Constructor of the Expenditure Object to record expenditure.
     * @param price Price of the item spent on
     * @param description info on the item
     * @param category Category the item is grouped under
     * @param boughtDate Date which the item is bought
     */
    public Expenditure(float price, String description, String category, LocalDate boughtDate) {
        super(price, description);
        this.category = category;
        this.boughtDate = boughtDate;
        dateTimeFormatter = DateTimeFormatter.ofPattern("d/M/yyyy");
    }

    @Override
    public String toString() { return "[E]" + super.toString() + "(on: " + getBoughtDate() + ")"; }

    public String getCategory() { return category; }

    public LocalDate getDateBoughtDate() { return boughtDate; }

    public String getBoughtDate() { return boughtDate.format(dateTimeFormatter); }
}
