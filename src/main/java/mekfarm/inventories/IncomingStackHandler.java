package mekfarm.inventories;

/**
 * Created by CF on 2016-10-29.
 */
public class IncomingStackHandler extends FilteredStackHandler {
    public IncomingStackHandler(int size) {
        super(size);
    }

    @Override
    protected boolean canExtract(int slot, int amount, boolean internal) {
        return internal;
    }
}
