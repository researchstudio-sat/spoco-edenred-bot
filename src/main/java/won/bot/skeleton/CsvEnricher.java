package won.bot.skeleton;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;
import fr.dudie.nominatim.client.JsonNominatimClient;
import fr.dudie.nominatim.model.Address;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import won.bot.skeleton.impl.model.EdenredDataPoint;
import won.bot.skeleton.utils.EdenredCsvIO;

/**
 * Class to e.g. do the nominatim reverse lookup and store the results in the
 * CSV for later consumption. Run this before running the bot.
 */
public class CsvEnricher {
    private static final Logger logger = LoggerFactory.getLogger(CsvEnricher.class);

    public static void main(String[] args) {
        readTest();
    }

    public static void nominatimTest() {
        String email = "rsinger+nominatim@researchstudio.at";
        EdenredDataPoint dp = new EdenredDataPoint("A place", "Thurngasse 1", "1090", "Vienna");
        Address a = nominatimReverseLookup(dp, email);
        logger.info("Nominatim result: " + a.getDisplayName() + " " + a.getLongitude() + " - " + a.getLatitude());
    }


    public static void readTest() {
        String filenameIn = "data/result_list_3824_shortened.csv";
        String email = "rsinger+nominatim@researchstudio.at";
        try {
            List<EdenredDataPoint> datapoints = EdenredCsvIO.read(filenameIn);
            for(EdenredDataPoint datapoint : datapoints) {
                logger.info("Einlösestelle: " + datapoint.getName());
                // Address a = nominatimReverseLookup(datapoint, email);
                // if(a != null) {
                //     logger.info("Nominatim result: " + a.getDisplayName() + " " + a.getLongitude() + " - " + a.getLatitude());
                // }
            }
        } catch (IOException e) {
            logger.error("Couldn't find or open CSV-file.");
        } catch (CsvValidationException e) {
            logger.error("Couldn't parse CSV-file.");
        }
    }
    public static void writeTest() {

        String filenameOut = "data/test.csv";
        try {
            List<EdenredDataPoint> data = new LinkedList<EdenredDataPoint>();
            data.add(new EdenredDataPoint("A place", "Yellow Brick Rd 1", "1234", "Oz"));
            EdenredCsvIO.write(filenameOut, data);
        } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException | IOException e) {
            logger.error("Failed to write csv. " + e.getMessage() + "\n");
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @param datapoint
     * @param email
     * @return the first nominatim address, if there's at least one result
     */
    public static Address nominatimReverseLookup(EdenredDataPoint datapoint, String email) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        JsonNominatimClient nominatim = new JsonNominatimClient(httpClient, email);
        List<Address> reverseLookupResults;
        try {
            reverseLookupResults = nominatim.search(datapoint.getOnelineAddress());
            if(reverseLookupResults.size() > 0) {
                Address a = reverseLookupResults.get(0);
                return a;
            } else {
                logger.debug("No results found on nominatim for: " + datapoint.getOnelineAddress());
            }

        } catch (IOException e) {
            logger.error("Couldn't establish connection to nominatim");
            e.printStackTrace();
        }
        return null;
    }
}