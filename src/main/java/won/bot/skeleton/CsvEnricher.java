package won.bot.skeleton;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;
import fr.dudie.nominatim.client.JsonNominatimClient;
import fr.dudie.nominatim.model.Address;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
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
        //////////// PROCESS ARGS

        if(args.length != 3) {
            logger.info("Usage: csvenricher in.csv out.csv email-for-nominatim@example.org");
            return;
        }
        String filenameIn = args[0];
        String filenameOut = args[1];
        String email = args[2];

        //////////// READ DATA

        List<EdenredDataPoint> data = null;
        try {
            data = EdenredCsvIO.read(filenameIn);
        } catch (IOException e) {
            logger.error("Couldn't find or open CSV-file.");
        } catch (CsvValidationException e) {
            logger.error("Couldn't parse CSV-file.");
        }
        if(data != null) {

            //////////// ENRICH DATA WITH GEO-COORDINATES

            List<EdenredDataPoint> enrichedData = data.stream().map(dp -> enrichDataPoint(dp, email)).collect(Collectors.toList());

            //////////// WRITE ENRICHED DATA
            
            try {
                EdenredCsvIO.write(filenameOut, enrichedData);
            } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException | IOException e) {
                logger.error("Failed to write csv. " + e.getMessage() + ". Stacktrace:\n");
                e.printStackTrace();
            }

            for(EdenredDataPoint dp : enrichedData){
                logger.info("ENRICHED TO: " + dp.toString());
            }
        }
    }

    /**
     * Clone and add lon/lat if the reverse lookup was successful
     * @param datapoint
     * @param email
     * @return
     */
    public static EdenredDataPoint enrichDataPoint(EdenredDataPoint datapoint, String email) {
        Address a = nominatimReverseLookup(datapoint, email);
        if (a != null) {
            return new EdenredDataPoint(datapoint, a.getLongitude(), a.getLatitude());
        } else {
            return new EdenredDataPoint(datapoint);
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