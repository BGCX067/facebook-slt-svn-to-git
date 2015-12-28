package set.comm;

import java.util.EventListener;

public interface GameEventListener extends EventListener {
    public abstract void eventReceived(GameEvent e);
}
