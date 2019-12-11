package won.bot.skeleton;

import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import won.bot.framework.bot.base.EventBot;
import won.bot.framework.eventbot.EventListenerContext;
import won.bot.framework.eventbot.action.impl.LogAction;
import won.bot.framework.eventbot.bus.EventBus;
import won.bot.framework.extensions.serviceatom.ServiceAtomBehaviour;
import won.bot.framework.extensions.serviceatom.ServiceAtomExtension;
import won.protocol.message.WonMessage;
// import won.protocol.message.WonMessageBuilder;
import won.protocol.service.WonNodeInformationService;
import won.bot.framework.eventbot.event.impl.wonmessage.CloseFromOtherAtomEvent;
import won.bot.framework.eventbot.listener.impl.ActionOnEventListener;

import org.apache.jena.query.Dataset;

import won.bot.skeleton.impl.model.EdenredDataPoint;
import won.bot.skeleton.utils.EdenredAtomModelWrapper;
import won.bot.skeleton.utils.EdenredCsvIO;

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

/**
 * Class to e.g. do the nominatim reverse lookup and store the results in the
 * CSV for later consumption. Run this before running the bot.
 */
public class CsvEnricher {
    private static final Logger logger = LoggerFactory.getLogger(CsvEnricher.class);

    public static void main(String[] args) {
        try {
            List<EdenredDataPoint> data = new LinkedList<EdenredDataPoint>();
            data.add(new EdenredDataPoint("A place", "Yellow Brick Rd 1", "1234", "Oz"));
            EdenredCsvIO.write("data/test.csv", data);
        } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException | IOException e) {
            logger.error("Failed to write csv. " + e.getMessage() + "\n");
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}