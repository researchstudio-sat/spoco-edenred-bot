package won.bot.skeleton.impl;

import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;
import java.lang.invoke.MethodHandles;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import won.bot.framework.eventbot.action.impl.atomlifecycle.AbstractCreateAtomAction;
import won.bot.framework.bot.base.EventBot;
import won.bot.framework.eventbot.EventListenerContext;
import won.bot.framework.eventbot.action.impl.LogAction;
import won.bot.framework.eventbot.bus.EventBus;
import won.bot.framework.extensions.serviceatom.ServiceAtomBehaviour;
import won.bot.framework.extensions.serviceatom.ServiceAtomExtension;
import won.protocol.message.WonMessage;
import won.protocol.message.builder.WonMessageBuilder;
import won.protocol.message.sender.WonMessageSender;
import won.protocol.service.WonNodeInformationService;
import won.protocol.util.DefaultAtomModelWrapper;
import won.protocol.vocabulary.WXCHAT;
import won.bot.framework.eventbot.event.impl.wonmessage.CloseFromOtherAtomEvent;
import won.bot.framework.eventbot.listener.impl.ActionOnEventListener;

import org.apache.jena.query.Dataset;

import won.bot.skeleton.impl.model.EdenredDataPoint;
import won.bot.skeleton.utils.EdenredAtomModelWrapper;

import won.bot.framework.eventbot.action.BaseEventBotAction;
import won.bot.framework.eventbot.action.EventBotActionUtils;
import won.bot.framework.eventbot.behaviour.ExecuteWonMessageCommandBehaviour;
import won.bot.framework.eventbot.bus.EventBus;
import won.bot.framework.eventbot.event.Event;
import won.bot.framework.eventbot.event.impl.command.connect.ConnectCommandEvent;
import won.bot.framework.eventbot.event.impl.command.connect.ConnectCommandResultEvent;
import won.bot.framework.eventbot.event.impl.command.connect.ConnectCommandSuccessEvent;
import won.bot.framework.eventbot.event.impl.wonmessage.CloseFromOtherAtomEvent;
import won.bot.framework.eventbot.event.impl.wonmessage.ConnectFromOtherAtomEvent;
import won.bot.framework.eventbot.filter.impl.AtomUriInNamedListFilter;
import won.bot.framework.eventbot.filter.impl.CommandResultFilter;
import won.bot.framework.eventbot.filter.impl.NotFilter;
import won.bot.framework.eventbot.listener.EventListener;
import won.bot.framework.eventbot.listener.impl.ActionOnFirstEventListener;
import won.bot.framework.extensions.matcher.MatcherBehaviour;
import won.bot.framework.extensions.matcher.MatcherExtension;
import won.bot.framework.extensions.matcher.MatcherExtensionAtomCreatedEvent;
import won.bot.framework.extensions.serviceatom.ServiceAtomBehaviour;
import won.bot.framework.extensions.serviceatom.ServiceAtomExtension;
import won.bot.skeleton.action.CreateEdenredAtomAction;
import won.bot.skeleton.action.MatcherExtensionAtomCreatedAction;
import won.bot.skeleton.context.SkeletonBotContextWrapper;
import won.bot.skeleton.event.CreateEdenredAtomEvent;

public class SkeletonBot extends EventBot implements ServiceAtomExtension {
    private static final Logger logger = LoggerFactory.getLogger(SkeletonBot.class);
    private int registrationMatcherRetryInterval;
    private List<String> ownedAtomURIs = new LinkedList<String>();

    public void setRegistrationMatcherRetryInterval(final int registrationMatcherRetryInterval) {
        this.registrationMatcherRetryInterval = registrationMatcherRetryInterval;
    }

    @Override
    protected void initializeEventListeners() {
        EventListenerContext ctx = getEventListenerContext();
        EventBus bus = getEventBus();
        bus.subscribe(CloseFromOtherAtomEvent.class,
                new ActionOnEventListener(ctx, new LogAction(ctx, "received close message from remote atom.")));

        bus.subscribe(CreateEdenredAtomEvent.class, new ActionOnEventListener(ctx, new CreateEdenredAtomAction(ctx)));
        // adapted from
        // https://github.com/researchstudio-sat/webofneeds/blob/refac__won_messages/
        // final Dataset data = new Data
        // ctx.getAtomProducer().create();
        try {
            Iterator<EdenredDataPoint> datapointIter = loadCSV("data/result_list_3824_shortened.csv");
            while (datapointIter.hasNext()) {
                EdenredDataPoint datapoint = datapointIter.next();
                logger.info("Einlösestelle: " + datapoint.getName());
                // postAtom(datapoint);
                // postAtom2(datapoint);
                bus.publish(new CreateEdenredAtomEvent(datapoint));
                break;
            }
        } catch (IOException e) {
            logger.error("Couldn't find or open CSV-file.");
        } catch (CsvValidationException e) {
            logger.error("Couldn't parse CSV-file.");
        }
    }

    @Override
    public ServiceAtomBehaviour getServiceAtomBehaviour() {
        // TODO Auto-generated method stub
        return null;
    }

    private Iterator<EdenredDataPoint> loadCSV(String filePath) throws CsvValidationException, IOException {
        // adapted from https://www.callicoder.com/java-read-write-csv-file-opencsv/
        logger.info("in loadCSV");
        Reader csvReader = Files.newBufferedReader(Paths.get(filePath));
        CsvToBean<EdenredDataPoint> csvToBean = new CsvToBeanBuilder<EdenredDataPoint>(csvReader)
                .withType(EdenredDataPoint.class).build();
        Iterator<EdenredDataPoint> datapointIter = csvToBean.iterator();
        return datapointIter;
    }

    private void postAtom(EdenredDataPoint datapoint) {
        EventListenerContext ctx = getEventListenerContext();
        final URI wonNodeUri = ctx.getNodeURISource().getNodeURI(); // TODO check if this gets the env var
        WonNodeInformationService wonNodeInformationService = ctx.getWonNodeInformationService();
        final URI atomUri = wonNodeInformationService.generateAtomURI(wonNodeUri);
        logger.debug("ATOM URI: " + atomUri);
        EdenredAtomModelWrapper atomModelWrapper = new EdenredAtomModelWrapper(atomUri, datapoint);
        Dataset dataset = atomModelWrapper.copyDataset();
        // WonMessageBuilder builder = WonMessageBuilder.setMessagePropertiesForCreate(
        // // generate an URI for the 'create Atom'
        // // event
        // wonNodeInformationService.generateEventURI(wonNodeUri), atomUri, // pass the
        // new Atom URI
        // wonNodeUri) // pass the WoN node URI
        // .addContent(dataset); // add the Atom's content
        // WonMessage message = builder.build(); // build the Message object
        // getEventListenerContext().getWonMessageSender().sendWonMessage(message); //
        // send it
        /*
         * WonMessageSender sender = ctx.getWonMessageSender();
         * 
         * WonMessage createAtomMessage = ctx.getWonMessageSender()
         * .prepareMessage(createWonMessage(atomUri, wonNodeUri, dataset, false,
         * false)); getEventListenerContext().getWonMessageSender().sendWonMessage(
         * createAtomMessage); // send it
         */

    }

    // private void postAtom2(EdenredDataPoint datapoint) {
    // EventListenerContext ctx = getEventListenerContext();
    // //// DefaultAtomModelWrapper atomModelWrapper = new
    // //// DefaultAtomModelWrapper(atomURI);
    // WonNodeInformationService wonNodeInformationService =
    // ctx.getWonNodeInformationService();
    // URI wonNodeUri = ctx.getNodeURISource().getNodeURI();
    // URI atomURI = wonNodeInformationService.generateAtomURI(wonNodeUri);
    // logger.debug("ATOM URI: " + atomURI);
    // DefaultAtomModelWrapper atomModelWrapper = new
    // DefaultAtomModelWrapper(atomURI);
    // atomModelWrapper.setTitle("[Edenred] " + datapoint.getName());
    // atomModelWrapper.setDescription("your description");
    // atomModelWrapper.addSocket(atomURI.toString() + "#socket0",
    // WXCHAT.ChatSocketString);
    // //// atomModelWrapper.addSocket(atomURI.toString() + "#socket1",
    // //// SocketType.HoldableSocket.getURI());
    // //// this.setDefaultSocket("#socket0");
    // Dataset atomDataset = atomModelWrapper.copyDataset();
    // WonMessage createAtomMessage = createWonMessage(wonNodeInformationService,
    // atomURI, wonNodeUri, atomDataset);
    // EventListener successCallback = eventS -> { logger.info("Successfully
    // published atom with URI: " + atomURI); };
    // EventListener failureCallback = eventF -> { logger.info("Failed to published
    // atom with URI: " + atomURI)`; };
    // EventBotActionUtils.rememberInList(ctx, atomURI, "ownedAtomURIs");
    // EventBotActionUtils.makeAndSubscribeResponseListener(createAtomMessage,
    // successCallback, failureCallback, c
    // getEventListenerContext().getWonMessageSender().sendWonMessage(createAtomMessage);
    // }
}

// public class SkeletonBot extends EventBot implements MatcherExtension,
// ServiceAtomExtension {
// private static final Logger logger =
// LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
// private int registrationMatcherRetryInterval;
// private MatcherBehaviour matcherBehaviour;
// private ServiceAtomBehaviour serviceAtomBehaviour;

// // bean setter, used by spring
// public void setRegistrationMatcherRetryInterval(final int
// registrationMatcherRetryInterval) {
// this.registrationMatcherRetryInterval = registrationMatcherRetryInterval;
// }

// @Override
// public ServiceAtomBehaviour getServiceAtomBehaviour() {
// return serviceAtomBehaviour;
// }

// @Override
// public MatcherBehaviour getMatcherBehaviour() {
// return matcherBehaviour;
// }

// @Override
// protected void initializeEventListeners() {
// EventListenerContext ctx = getEventListenerContext();
// if (!(getBotContextWrapper() instanceof SkeletonBotContextWrapper)) {
// logger.error(getBotContextWrapper().getBotName() + " does not work without a
// SkeletonBotContextWrapper");
// throw new IllegalStateException(
// getBotContextWrapper().getBotName() + " does not work without a
// SkeletonBotContextWrapper");
// }
// EventBus bus = getEventBus();
// SkeletonBotContextWrapper botContextWrapper = (SkeletonBotContextWrapper)
// getBotContextWrapper();
// // register listeners for event.impl.command events used to tell the bot to
// send
// // messages
// ExecuteWonMessageCommandBehaviour wonMessageCommandBehaviour = new
// ExecuteWonMessageCommandBehaviour(ctx);
// wonMessageCommandBehaviour.activate();
// // activate ServiceAtomBehaviour
// serviceAtomBehaviour = new ServiceAtomBehaviour(ctx);
// serviceAtomBehaviour.activate();
// // set up matching extension
// // as this is an extension, it can be activated and deactivated as needed
// // if activated, a MatcherExtensionAtomCreatedEvent is sent every time a new
// // atom is created on a monitored node
// matcherBehaviour = new MatcherBehaviour(ctx, "BotSkeletonMatchingExtension",
// registrationMatcherRetryInterval);
// matcherBehaviour.activate();
// // create filters to determine which atoms the bot should react to
// NotFilter noOwnAtoms = new NotFilter(
// new AtomUriInNamedListFilter(ctx,
// ctx.getBotContextWrapper().getAtomCreateListName()));
// // filter to prevent reacting to serviceAtom<->ownedAtom events;
// NotFilter noInternalServiceAtomEventFilter =
// getNoInternalServiceAtomEventFilter();
// bus.subscribe(ConnectFromOtherAtomEvent.class,
// noInternalServiceAtomEventFilter, new BaseEventBotAction(ctx) {
// @Override
// protected void doRun(Event event, EventListener executingListener) {
// EventListenerContext ctx = getEventListenerContext();
// ConnectFromOtherAtomEvent connectFromOtherAtomEvent =
// (ConnectFromOtherAtomEvent) event;
// try {
// String message = "Hello i am the BotSkeletor i will send you a message
// everytime an atom is created...";
// final ConnectCommandEvent connectCommandEvent = new ConnectCommandEvent(
// connectFromOtherAtomEvent.getRecipientSocket(),
// connectFromOtherAtomEvent.getSenderSocket(), message);
// ctx.getEventBus().subscribe(ConnectCommandSuccessEvent.class, new
// ActionOnFirstEventListener(ctx,
// new CommandResultFilter(connectCommandEvent), new BaseEventBotAction(ctx) {
// @Override
// protected void doRun(Event event, EventListener executingListener) {
// ConnectCommandResultEvent connectionMessageCommandResultEvent =
// (ConnectCommandResultEvent) event;
// if (!connectionMessageCommandResultEvent.isSuccess()) {
// logger.error("Failure when trying to open a received Request: "
// + connectionMessageCommandResultEvent.getMessage());
// } else {
// logger.info(
// "Add an established connection " +
// connectCommandEvent.getLocalSocket()
// + " -> "
// + connectCommandEvent.getTargetSocket()
// +
// " to the botcontext ");
// botContextWrapper.addConnectedSocket(
// connectCommandEvent.getLocalSocket(),
// connectCommandEvent.getTargetSocket());
// }
// }
// }));
// ctx.getEventBus().publish(connectCommandEvent);
// } catch (Exception te) {
// logger.error(te.getMessage(), te);
// }
// }
// });
// // listen for the MatcherExtensionAtomCreatedEvent
// bus.subscribe(MatcherExtensionAtomCreatedEvent.class, new
// MatcherExtensionAtomCreatedAction(ctx));
// bus.subscribe(CloseFromOtherAtomEvent.class, new BaseEventBotAction(ctx) {
// @Override
// protected void doRun(Event event, EventListener executingListener) {
// EventListenerContext ctx = getEventListenerContext();
// CloseFromOtherAtomEvent closeFromOtherAtomEvent = (CloseFromOtherAtomEvent)
// event;
// URI targetSocketUri = closeFromOtherAtomEvent.getSocketURI();
// URI senderSocketUri = closeFromOtherAtomEvent.getTargetSocketURI();
// logger.info("Remove a closed connection " + senderSocketUri + " -> " +
// targetSocketUri
// + " from the botcontext ");
// botContextWrapper.removeConnectedSocket(senderSocketUri, targetSocketUri);
// }
// });
// }
// }
