package page;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import nerza.FileStore;

public class WritingPagebuffer implements PageBuffer{

    private ConcurrentHashMap<Integer, Page> writingNewPages;
    private ConcurrentHashMap<Integer, Page> parentPages;
    private final AtomicInteger counter = new AtomicInteger();
    private final int size;
    private int nowPageId;
    private int parentPageId;
    
    
    private WritingPagebuffer (int size) {
        
        this.size = size;
        this.nowPageId = 0;
        writingNewPages = new ConcurrentHashMap<Integer, Page>();
        parentPages = new ConcurrentHashMap<Integer, Page>();
    }
    
    @Override public Page getById(int id) {
        
        return writingNewPages.get(new Integer(id));
    }

    @Override public void pilePage(Page page) {
        
        this.nowPageId = page.getId();
        writingNewPages.put(new Integer(page.getId()), page);
    }

    @Override public boolean isPlentished() {
        
        return counter.get() >= size ? true : false;
    }

    @Override public Page now() {
        
        return writingNewPages.get(new Integer(this.nowPageId));
    }

    public static WritingPagebuffer create(int size) {
        
        return new WritingPagebuffer(size);
    }
    
    public void setParentPageId(int parentPageId) {
        
        this.parentPageId = parentPageId;
    }

    public Page getParentBy(int pageID, FileStore fs) throws IOException {
        
        Page parentPage = parentPages.get(new Integer(pageID));
        if(parentPage == null) {
            // 如果没找到相应的上一级Page，就根据pageID从磁盘里一次i/o
            // 然后建立一个Page到内存缓存起来
            fs.seek(pageID * Page.CAPACITY);
            byte[] onePage = fs.read(Page.CAPACITY).array();
            parentPage = Page.shapeFromBytes(onePage);
            // 将该Page存储到父级Page缓存中
            cacheParent(parentPage);
            
            return parentPage;
        }
        
        return parentPage;
    }
    
    private void cacheParent(Page parentPage) {
        
        int parentID = parentPage.getId();
        parentPages.put(new Integer(parentID), parentPage);
    }
    
    public ByteBuffer pourOut () {
        
        ByteBuffer bytes = ByteBuffer.allocate(counter.get() * Page.CAPACITY);
        for (int i = 0, loop = counter.get(); i < loop; i ++) {
            bytes.put(getById(i).getBytes());
        }
        return bytes;
    }

}

