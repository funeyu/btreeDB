package page;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class WritingPagebuffer implements PageBuffer{

    private ConcurrentHashMap<Integer, Page> writingPages = new ConcurrentHashMap<Integer, Page>();
    private final AtomicInteger counter = new AtomicInteger();
    private final int size;
    
    private WritingPagebuffer (int size) {
        
        this.size = size;
    }
    
    @Override public Page getById(int id) {
        
        return null;
    }

    @Override public void pilePage(int id, Page page) {
        
        if(counter.incrementAndGet() < size) {
            writingPages.put(new Integer(id), page);
        }
    }

    @Override public boolean isPlentished() {
        
        return counter.get() >= size ? true : false;
    }

    public ByteBuffer pourOut () {
        
        ByteBuffer bytes = ByteBuffer.allocate(counter.get() * Page.CAPACITY);
        for (int i = 0, loop = counter.get(); i < loop; i ++) {
            bytes.put(getById(i).getBytes());
        }
        return bytes;
    }
}
