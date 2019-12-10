package won.bot.skeleton.event;

import won.bot.framework.eventbot.event.BaseEvent;
import won.bot.skeleton.impl.model.EdenredDataPoint;

public class CreateEdenredAtomEvent extends BaseEvent {
    private final EdenredDataPoint datapoint;

    public CreateEdenredAtomEvent(EdenredDataPoint datapoint) {
        this.datapoint = datapoint;
    }

    public EdenredDataPoint getEdenredDatapoint() {
        return this.datapoint;
    }
}