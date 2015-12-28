package set.util;

import java.util.EventListener;

public interface ScheduledEvents extends EventListener
{
    public abstract void checkpoint(TimerEvent e);
}
