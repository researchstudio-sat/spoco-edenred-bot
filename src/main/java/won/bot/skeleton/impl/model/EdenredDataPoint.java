package won.bot.skeleton.impl.model;

import com.opencsv.bean.CsvBindByName;

public class EdenredDataPoint {
    @CsvBindByName(column = "Einlösestelle", required = true)
    private String name;

    public String getName() {
        return this.name;
    };

    @CsvBindByName(column = "Adresse", required = true)
    private String streetAddress;

    public String getStreetAddress() {
        return this.streetAddress;
    };

    @CsvBindByName(column = "PLZ", required = true)
    private String postalCode;

    public String getPostalCode() {
        return this.postalCode;
    };

    @CsvBindByName(column = "Stadt", required = true)
    private String city;

    public String getCity() {
        return this.city;
    };

    @CsvBindByName(column = "Website", required = false)
    private String website;

    public String getWebsite() {
        return this.website;
    };

    @CsvBindByName(column = "Telefonnummer", required = false)
    private String telephone;

    public String getTelephone() {
        return this.telephone;
    };

    @CsvBindByName(column = "Branche", required = false)
    private String type;

    public String getType() {
        return this.type;
    };
}