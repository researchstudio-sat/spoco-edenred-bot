package won.bot.skeleton.utils;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import won.bot.skeleton.impl.model.EdenredDataPoint;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EdenredCsvIO {
    private static final Logger logger = LoggerFactory.getLogger(EdenredCsvIO.class);

    public static Iterator<EdenredDataPoint> readStream(String filePath) throws CsvValidationException, IOException {
        // adapted from https://www.callicoder.com/java-read-write-csv-file-opencsv/
        logger.info("in loadCSV");
        try (
                        Reader csvReader = Files.newBufferedReader(Paths.get(filePath));) {
            CsvToBean<EdenredDataPoint> csvToBean = new CsvToBeanBuilder<EdenredDataPoint>(csvReader)
                            .withType(EdenredDataPoint.class).build();
            Iterator<EdenredDataPoint> datapointIter = csvToBean.iterator();
            return datapointIter;
        }
    }

    public static void write(String filename, List<EdenredDataPoint> datapoints) throws IOException,
                    CsvDataTypeMismatchException,
                    CsvRequiredFieldEmptyException {
        // adapted from
        // https://www.callicoder.com/java-read-write-csv-file-opencsv/#generate-csv-file-from-list-of-objects
        try (
                        Writer writer = Files.newBufferedWriter(Paths.get(filename));) {
            StatefulBeanToCsv<EdenredDataPoint> beanToCsv = new StatefulBeanToCsvBuilder<EdenredDataPoint>(writer)
                            .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                            .build();
            beanToCsv.write(datapoints);
        }
    }
}