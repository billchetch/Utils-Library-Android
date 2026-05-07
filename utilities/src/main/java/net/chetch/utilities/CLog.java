package net.chetch.utilities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class CLog<T extends CLog.ILogItem> {

    public interface ILogItem{
        int getID();
        LocalDateTime getCreated();
    }

    public interface IFilter<T>{
        boolean matches(T logItem);
    }


    private final T[] data;
    final private int size;
    private int head = 0; // Point to start of data
    private int tail = 0; // Point to next empty spot
    private int count = 0; // Number of items currently stored

    private final  Object addLock = new Object();


    @SuppressWarnings("unchecked")
    public CLog(int size) {
        this.size = size;
        this.data = (T[]) new ILogItem[size];
    }


    public void add(T value) {
        synchronized (addLock) {
            data[tail] = value;
            tail = (tail + 1) % size; // Wraps back to 0 if at the end
            if (count < size) {
                count++;
            } else {
                head = (head + 1) % size; // Overwrite oldest data
            }
        }
    }

    public T get(int index) {
        // Map logical index to physical circular index
        return data[(head + index) % size];
    }

    public T getFirst(){
        return get(0);
    }

    public T getLast(){
        return get(count);
    }

    public int size(){
        return count;
    }

    public boolean matches(T item, Collection<IFilter<T>> filters){
        boolean matches = true;
        if(filters != null) {
            for (IFilter filter : filters) {
                if (filter != null && !filter.matches(item)) {
                    matches = false;
                    break;
                }
            }
        }
        return matches;
    }

    public T find(Collection<IFilter<T>> filters, boolean reverse){
        synchronized (addLock) {
            for (int i = 0; i < count; i++) {
                int idx = reverse ? count - 1 - i : i;
                T item = get(idx);
                if (matches(item, filters)) {
                    return item;
                }
            }
        }
        return null;
    }

    public T find(IFilter<T> filter, boolean reverse){
        ArrayList<IFilter<T>> filters = new ArrayList<>();
        filters.add(filter);
        return find(filters, reverse);
    }


    public void copyTo(List<T> target, boolean reverse, Collection<IFilter<T>> filters, boolean sortResult){
        synchronized (addLock) {
            for (int i = 0; i < count; i++) {
                int idx = reverse ? count - 1 - i : i;
                T item = get(idx);
                if (matches(item, filters)) {
                    target.add(item);
                }
            }
        }

        if(sortResult){
            target.sort((t1, t2) -> {
                if(t1.getCreated().isBefore(t2.getCreated())){
                    return 1;
                } else if(t2.getCreated().isBefore(t1.getCreated())){
                    return -1;
                } else {
                    return 0;
                }
            });
        }
    }

    public void copyTo(List<T> target, boolean sortResult){
        copyTo(target, false, null, sortResult);
    }

}
