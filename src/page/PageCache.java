package page;

import java.util.LinkedHashMap;
import java.util.Map;

public class PageCache {
    
    private final LinkedHashMap<Integer, Page> cache;
    private int cachePageSize;
    private int counter;
    private final float factor = 0.75f;
    
    private PageCache (int size) {
        
        cache = new LinkedHashMap<Integer, Page>(size, factor, true) {

            private static final long serialVersionUID = 1L;

            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, Page> eldest){
                
                boolean isFull = cachePageSize >= size;
                if (isFull) {
                    counter -= 1;
                }
                return isFull;
            }
        };
        
        cachePageSize = size;
    }
    
    public static PageCache initPageCache(int size) {
        
        return new PageCache(size);
    }
    
    public Page getPageById(int id) {
        
        Page page = cache.get(new Integer(id));
        return page;
    }
    
    public void putPage(Integer pageId, Page page) {
        
        cache.put(pageId, page);
    }
    
}
