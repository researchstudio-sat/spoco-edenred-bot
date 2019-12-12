
package won.bot.skeleton.utils;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import won.bot.skeleton.impl.model.EdenredDataPoint;

public class EdenredCsvAppendWriter implements AutoCloseable {
    Writer appendingWriter;
    StatefulBeanToCsv<EdenredDataPoint> beanToCsv;

    public EdenredCsvAppendWriter(String filename) throws IOException {
        this.appendingWriter = new FileWriter(filename, true);
        this.beanToCsv = new StatefulBeanToCsvBuilder<EdenredDataPoint>(appendingWriter).build();
    }

    public void write(EdenredDataPoint dp)
            throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {
        this.beanToCsv.write(dp);
        this.appendingWriter.flush();
    }

    public void write(List<EdenredDataPoint> data)
            throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {
        this.beanToCsv.write(data);
        this.appendingWriter.flush();
    }

    @Override
    public void close() throws IOException {
        this.appendingWriter.close();
    }

}