package won.bot.skeleton.impl.model;

import com.opencsv.bean.CsvBindByName;

public class EdenredDataPoint {
    /**  for opencsv-parsing; don't use otherwise */
    public EdenredDataPoint() { }

    public EdenredDataPoint(String name, String streetAddress, String postalCode, String city) {
        this.name = name;
        this.streetAddress = streetAddress;
        this.postalCode = postalCode;
        this.city = city;
    }

    /**
     * Copy constructor.
     * @param dp
     */
    public EdenredDataPoint(EdenredDataPoint dp) {
        this.city = dp.city;
        this.latitude = dp.latitude;
        this.longitude = dp.longitude;
        this.name = dp.name;
        this.postalCode = dp.postalCode;
        this.streetAddress = dp.streetAddress;
        this.telephone = dp.telephone;
        this.type = dp.type;
        this.website = dp.website;
    }

    /**
     * Copies the datapoint and adds lon and lat to the copy.
     * @param dp
     * @param lon
     * @param lat
     */
    public EdenredDataPoint(EdenredDataPoint dp, double lon, double lat) {
        this(dp);
        this.longitude = lon;
        this.latitude = lat;
    }

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

    @CsvBindByName(column = "Longitude", required = false)
    private double longitude;

    public double getLongitude() {
        return this.longitude;
    }

    @CsvBindByName(column = "Latitude", required = false)
    private double latitude;

    public double getLatitude() {
        return this.latitude;
    }

    public String getCountryCode() {
        // only for Austria atm
        return "AT";
    }

    public String getOnelineAddress() {
        return this.getStreetAddress() + ", " + this.getPostalCode() + " "
                        + this.getCity() + ", " + this.getCountryCode();
    }

    @Override
    public String toString() {
        return "EdenredDatePoint@{ " +
            "city: \"" + this.city + "\", " +
            "latitude: " + this.latitude + ", " +
            "longitude: " + this.longitude + ", " +
            "name: \"" + this.name + "\", " +
            "postalCode: \"" + this.postalCode + "\", " +
            "streetAddress: \"" + this.streetAddress + "\", " +
            "telephone: \"" + this.telephone + "\", " +
            "type: \"" + this.type + "\", " +
            "website: \"" + this.website +  "\"" +
            " }";
    }
}
