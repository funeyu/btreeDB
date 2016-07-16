package page;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class WritingPagebuffer implements PageBuffer{

    private ConcurrentHashMap<Integer, Page> writingPages;
    private ConcurrentHashMap<Integer, Page> parentPages;
    private final AtomicInteger counter = new AtomicInteger();
    private final int size;
    private int nowPageId;
    private int parentPageId;
    
    
    private WritingPagebuffer (int size) {
        
        this.size = size;
        this.nowPageId = 0;
        writingPages = new ConcurrentHashMap<Integer, Page>();
        parentPages = new ConcurrentHashMap<Integer, Page>();
    }
    
    @Override public Page getById(int id) {
        
        return writingPages.get(new Integer(id));
    }

    @Override public void pilePage(Page page) {
        
        this.nowPageId = page.getId();
        writingPages.put(new Integer(page.getId()), page);
    }

    @Override public boolean isPlentished() {
        
        return counter.get() >= size ? true : false;
    }

    @Override public Page now() {
        
        return writingPages.get(new Integer(this.nowPageId));
    }

    public static WritingPagebuffer create(int size) {
        
        return new WritingPagebuffer(size);
    }
    
    public void setParentPageId(int parentPageId) {
        
        this.parentPageId = parentPageId;
    }

    public ByteBuffer pourOut () {
        
        ByteBuffer bytes = ByteBuffer.allocate(counter.get() * Page.CAPACITY);
        for (int i = 0, loop = counter.get(); i < loop; i ++) {
            bytes.put(getById(i).getBytes());
        }
        return bytes;
    }

}

