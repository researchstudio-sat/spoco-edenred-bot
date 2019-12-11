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

public class EdenredCsvReader {
    private static final Logger logger = LoggerFactory.getLogger(EdenredCsvReader.class);

    public static Iterator<EdenredDataPoint> stream(String filePath) throws CsvValidationException, IOException {
        // adapted from https://www.callicoder.com/java-read-write-csv-file-opencsv/
        logger.info("in loadCSV");
        Reader csvReader = Files.newBufferedReader(Paths.get(filePath));
        CsvToBean<EdenredDataPoint> csvToBean = new CsvToBeanBuilder<EdenredDataPoint>(csvReader)
                        .withType(EdenredDataPoint.class).build();
        Iterator<EdenredDataPoint> datapointIter = csvToBean.iterator();
        return datapointIter;
    }
}