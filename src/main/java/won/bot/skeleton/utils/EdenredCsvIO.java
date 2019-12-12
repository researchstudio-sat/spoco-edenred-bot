package won.bot.skeleton.utils;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import won.bot.skeleton.impl.model.EdenredDataPoint;

public class EdenredCsvIO {
    private static final Logger logger = LoggerFactory.getLogger(EdenredCsvIO.class);

    /**
     * Reads a csv-file into a list of edenred-datapoints then closes the file.
     */
    public static List<EdenredDataPoint> read(String filePath) throws CsvValidationException, IOException {
        // adapted from https://www.callicoder.com/java-read-write-csv-file-opencsv/
        logger.info("in loadCSV");
        List<EdenredDataPoint> results = new LinkedList<EdenredDataPoint>();
        try (Reader csvReader = Files.newBufferedReader(Paths.get(filePath));) {
            CsvToBean<EdenredDataPoint> csvToBean = new CsvToBeanBuilder<EdenredDataPoint>(csvReader)
                    .withType(EdenredDataPoint.class).build();
            Iterator<EdenredDataPoint> datapointIter = csvToBean.iterator();
            while (datapointIter.hasNext()) {
                results.add(datapointIter.next());
            }
        }
        return results;
    }

    /**
     * Gives you a writer, that can be used to append individual data-points.
     * 
     * @param filename
     * @return
     * @throws IOException
     */
    public static EdenredCsvAppendWriter getAppendWriter(String filename) throws IOException {
        return new EdenredCsvAppendWriter(filename);
    }

    /**
     * Writes an entire list of datapoints and then closes the file again. If the
     * file already exists, it will overwrite it.
     */
    public static void write(String filename, List<EdenredDataPoint> datapoints)
            throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        // adapted from
        // https://www.callicoder.com/java-read-write-csv-file-opencsv/#generate-csv-file-from-list-of-objects
        try (Writer writer = Files.newBufferedWriter(Paths.get(filename));) {
            StatefulBeanToCsv<EdenredDataPoint> beanToCsv = new StatefulBeanToCsvBuilder<EdenredDataPoint>(writer)
                    .build();
            beanToCsv.write(datapoints);
        }
    }
}
