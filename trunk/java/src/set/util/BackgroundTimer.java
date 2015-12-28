package set.util;

import java.util.Vector;

public class BackgroundTimer
{
    private InternalTimer timer;
    private Vector<ScheduledEvents> listeners;
    private Vector<ScheduledEvents> oldListeners;

    public BackgroundTimer(long period)
    {
        listeners = new Vector<ScheduledEvents>();
        timer = new InternalTimer(period, listeners);
        oldListeners = new Vector<ScheduledEvents>();
    }


    public synchronized void addListener(ScheduledEvents listener)
    {
        listeners.add(listener);
    }
    
    public synchronized void removeListener(ScheduledEvents listener)
    {
        listeners.remove(listener);
    }
    
    /**
     * Marks the specified listener for removal. The listener is removed at the
     * next timer cycle. This allows listeners to be removed even within the
     * <code>checkpoint</code> method without triggering a deadlock.
     * @param listener The timer event listener to remove.
     */
    public synchronized void removeListenerDelayed(ScheduledEvents listener)
    {
        oldListeners.add(listener);
    }
    
    public synchronized void start()
    {
        timer.start();
    }
    
    
    private class InternalTimer extends Thread
    {
        private long period;
        private Vector<ScheduledEvents> listeners;
        
        public InternalTimer(long period, Vector<ScheduledEvents> listeners)
        {
            this.period = period;
            this.listeners = listeners;
            
            setPriority(MIN_PRIORITY);
        }

        @Override
        public void run()
        {
            while (true)
            {
                try
                {
                    synchronized (this)
                    {
                        this.wait(period);
                    }
                }
                catch (InterruptedException e)
                {
                    System.out.println("Warning: timer interrupted.");
                    return;
                }
                
                fireTimerEvent();
            }
        }
        
        private synchronized void fireTimerEvent()
        {
            TimerEvent event = new TimerEvent(this);

            // Clean up old event listeners.
            for (ScheduledEvents l : oldListeners)
            {
                listeners.remove(l);
            }
            oldListeners.clear();
            
            // Fire the event.
            for (ScheduledEvents listener : listeners)
            {
                listener.checkpoint(event);
            }
        }
    }
}
