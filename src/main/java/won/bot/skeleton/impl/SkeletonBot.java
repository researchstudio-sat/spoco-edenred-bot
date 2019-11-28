package won.bot.skeleton.impl;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import won.bot.framework.bot.base.EventBot;
import won.bot.framework.eventbot.EventListenerContext;
import won.bot.framework.eventbot.action.impl.LogAction;
// import won.bot.framework.eventbot.action.impl.RandomDelayedAction;
import won.bot.framework.eventbot.bus.EventBus;
// import won.bot.framework.eventbot.event.impl.atomlifecycle.AtomCreatedEvent;
// import won.bot.framework.eventbot.event.impl.lifecycle.ActEvent;
// import won.bot.framework.extensions.matcher.MatcherBehaviour;
// import won.bot.framework.extensions.matcher.MatcherExtension;
// import won.bot.framework.extensions.matcher.MatcherExtensionAtomCreatedEvent;
import won.bot.framework.extensions.serviceatom.ServiceAtomBehaviour;
import won.bot.framework.extensions.serviceatom.ServiceAtomExtension;
import won.bot.skeleton.utils.EdenredAtomModelWrapper;
import won.protocol.message.WonMessage;
import won.protocol.message.WonMessageBuilder;
import won.protocol.service.WonNodeInformationService;
import won.bot.framework.eventbot.event.impl.wonmessage.CloseFromOtherAtomEvent;
// import
// won.bot.framework.eventbot.event.impl.wonmessage.MessageFromOtherAtomEvent;
// import
// won.bot.framework.eventbot.event.impl.wonmessage.OpenFromOtherAtomEvent;
// import won.bot.framework.eventbot.filter.impl.AtomUriInNamedListFilter;
// import won.bot.framework.eventbot.filter.impl.NotFilter;
// import won.bot.framework.eventbot.listener.BaseEventListener;
import won.bot.framework.eventbot.listener.impl.ActionOnEventListener;

import org.apache.jena.query.Dataset;
// import won.protocol.model.SocketType;

public class SkeletonBot extends EventBot implements ServiceAtomExtension {
        private static final Logger logger = LoggerFactory.getLogger(SkeletonBot.class);
        private int registrationMatcherRetryInterval;

        public void setRegistrationMatcherRetryInterval(final int registrationMatcherRetryInterval) {
                this.registrationMatcherRetryInterval = registrationMatcherRetryInterval;
        }

        @Override
        protected void initializeEventListeners() {
                EventListenerContext ctx = getEventListenerContext();
                EventBus bus = getEventBus();
                bus.subscribe(CloseFromOtherAtomEvent.class, new ActionOnEventListener(ctx,
                                                new LogAction(ctx, "received close message from remote atom.")));
                // adapted from
                // https://github.com/researchstudio-sat/webofneeds/blob/refac__won_messages/
                // final Dataset data = new Data
                // ctx.getAtomProducer().create();
                final URI wonNodeUri = ctx.getNodeURISource().getNodeURI(); // TODO check if this gets the env var
                WonNodeInformationService wonNodeInformationService = ctx.getWonNodeInformationService();
                final URI atomURI = wonNodeInformationService.generateAtomURI(wonNodeUri);
                logger.debug("ATOM URI: " + atomURI);
                EdenredAtomModelWrapper atomModelWrapper = new EdenredAtomModelWrapper(atomURI);
                Dataset dataset = atomModelWrapper.copyDataset();
                WonMessageBuilder builder = WonMessageBuilder
                                                .setMessagePropertiesForCreate(
                                                                                // generate an URI for the 'create Atom'
                                                                                // event
                                                                                wonNodeInformationService.generateEventURI(
                                                                                                                wonNodeUri),
                                                                                atomURI, // pass the new Atom URI
                                                                                wonNodeUri) // pass the WoN node URI
                                                .addContent(dataset); // add the Atom's content
                WonMessage message = builder.build(); // build the Message object
                getEventListenerContext().getWonMessageSender().sendWonMessage(message); // send it
        }

        @Override
        public ServiceAtomBehaviour getServiceAtomBehaviour() {
                // TODO Auto-generated method stub
                return null;
        }
}
