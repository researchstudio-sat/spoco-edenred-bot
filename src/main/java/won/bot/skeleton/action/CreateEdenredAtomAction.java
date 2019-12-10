package won.bot.skeleton.action;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.query.Dataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import won.bot.framework.eventbot.EventListenerContext;
import won.bot.framework.eventbot.action.EventBotActionUtils;
import won.bot.framework.eventbot.action.impl.atomlifecycle.AbstractCreateAtomAction;
import won.bot.framework.eventbot.bus.EventBus;
import won.bot.framework.eventbot.event.Event;
import won.bot.framework.eventbot.event.impl.atomlifecycle.AtomCreatedEvent;
import won.bot.framework.eventbot.event.impl.wonmessage.FailureResponseEvent;
import won.bot.framework.eventbot.listener.EventListener;
import won.bot.skeleton.context.SkeletonBotContextWrapper;
import won.bot.skeleton.event.CreateEdenredAtomEvent;
import won.bot.skeleton.impl.model.EdenredDataPoint;
import won.bot.skeleton.utils.EdenredAtomModelWrapper;
import won.protocol.message.WonMessage;
import won.protocol.service.WonNodeInformationService;
import won.protocol.util.RdfUtils;
import won.protocol.util.WonRdfUtils;

import java.net.URI;

public class CreateEdenredAtomAction extends AbstractCreateAtomAction {
    private static final Logger logger = LoggerFactory.getLogger(CreateEdenredAtomAction.class);

    public CreateEdenredAtomAction(EventListenerContext eventListenerContext) {
        super(eventListenerContext);
    }

    @Override
    protected void doRun(Event event, EventListener executingListener) {
        EventListenerContext ctx = getEventListenerContext();
        if (!(ctx.getBotContextWrapper() instanceof SkeletonBotContextWrapper)
                || !(event instanceof CreateEdenredAtomEvent)) {
            logger.error(
                    "CreateEdenredAtomAction does not work without a SkeletonBotContextWrapper and CreateEdenredAtomEvent");
            throw new IllegalStateException(
                    "CreateEdenredAtomAction does not work without a SkeletonBotContextWrapper and CreateEdenredAtomEvent");
        }
        SkeletonBotContextWrapper botContextWrapper = (SkeletonBotContextWrapper) ctx.getBotContextWrapper();
        CreateEdenredAtomEvent createEdenredAtomEvent = (CreateEdenredAtomEvent) event;

        EdenredDataPoint edenredDataPoint = createEdenredAtomEvent.getEdenredDatapoint();

        /*
         * TODO if (botContextWrapper.getAtomUriForRaid(edenredAtomToCreate) != null) {
         * logger.warn("RaidAtom already exists, URI: " +
         * botContextWrapper.getAtomUriForRaid(edenredAtomToCreate)); return; }
         */

        final URI wonNodeUri = ctx.getNodeURISource().getNodeURI();
        WonNodeInformationService wonNodeInformationService = ctx.getWonNodeInformationService();
        final URI atomURI = wonNodeInformationService.generateAtomURI(wonNodeUri);
        Dataset dataset = new EdenredAtomModelWrapper(atomURI, edenredDataPoint).copyDataset();
        logger.info("about to publish atom with URI: " + atomURI);
        logger.debug("creating atom on won node {} with content {} ", wonNodeUri,
                StringUtils.abbreviate(RdfUtils.toString(dataset), 150));
        WonMessage createAtomMessage = ctx.getWonMessageSender().prepareMessage(createWonMessage(atomURI, dataset));
        EventBotActionUtils.rememberInList(ctx, atomURI, uriListName);
        EventBus bus = ctx.getEventBus();
        EventListener successCallback = new EventListener() {
            @Override
            public void onEvent(Event event) {
                logger.debug("atom creation successful, new atom URI is {}", atomURI);
                bus.publish(new AtomCreatedEvent(atomURI, wonNodeUri, dataset, null));
                // TODO botContextWrapper.addEdenredAtom(edenredDataPoint, atomURI);
            }
        };
        EventListener failureCallback = new EventListener() {
            @Override
            public void onEvent(Event event) {
                String textMessage = WonRdfUtils.MessageUtils
                        .getTextMessage(((FailureResponseEvent) event).getFailureMessage());
                logger.error("atom creation failed for atom URI {}, original message URI {}: {}", atomURI,
                        ((FailureResponseEvent) event).getOriginalMessageURI(), textMessage);
                // TODO botContextWrapper.removeEden(edenredAtomToCreate);
                EventBotActionUtils.removeFromList(ctx, atomURI, uriListName);
            }
        };
        EventBotActionUtils.makeAndSubscribeResponseListener(createAtomMessage, successCallback, failureCallback, ctx);
        logger.debug("registered listeners for response to message URI {}", createAtomMessage.getMessageURI());
        ctx.getWonMessageSender().sendMessage(createAtomMessage);
        logger.debug("atom creation message sent with message URI {}", createAtomMessage.getMessageURI());
    }
}
