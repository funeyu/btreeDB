package btreeJava;

import java.util.concurrent.atomic.AtomicInteger;

public class Page {
    public static final int CAPACITY = 4 * 1024;
    private static AtomicInteger pageCounter;
    private int index;
    private byte type;
    private int level;
    private int freeSpace;
    private byte[] rawData;
    private ObjectId minObjectId;
    private ObjectId maxObjectId;
    private int nextPagePos;
    private int childPagePos;
    private int parentPagePos;
    
    private Page(byte type, int level) {
        
        this.index = pageCounter.getAndIncrement();
        this.type = type;
        this.level = level;
        this.rawData = new byte[CAPACITY];
        this.freeSpace = CAPACITY;
    }
    // 类似于mongo的objectid：507f1f77bcf86cd799439011
    private static class ObjectId implements Comparable<ObjectId>{
        private String idString;
        private static AtomicInteger counter; 
        
        private ObjectId(String idString) {
            this.idString = idString;
        }
        
        public static ObjectId build() {
            
            StringBuilder strings = new StringBuilder("");
            // 递增counter 取后8字节作为计数标记
            if ((counter.get() & 0xff0000) != 0) {
                try {
                    Thread.sleep (1);
                    counter.set(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            int current = counter.incrementAndGet();
            long times = System.currentTimeMillis();
            strings.append(Integer.toHexString((int) (times >> 7 & 0xff)))
                   .append(Integer.toHexString((int) (times >> 6 & 0xff)))
                   .append(Integer.toHexString((int) (times >> 5 & 0xff)))
                   .append(Integer.toHexString((int) (times >> 4 & 0xff)))
                   .append(Integer.toHexString((int) (times >> 3 & 0xff)))
                   .append(Integer.toHexString((int) (times >> 2 & 0xff)))
                   .append(Integer.toHexString((int) (times >> 1 & 0xff)))
                   .append(Integer.toHexString((int) (times & 0xff)))
                   
                   .append(Integer.toHexString((int) (current >> 1 & 0xff)))
                   .append(Integer.toHexString((int) (current & 0xff)));
            
            return new ObjectId(strings.toString());
        }
        
        public String getId() {
            return idString == null ? idString : "";
        }
        
        private byte[] toByte() {
            
            byte[] bytes = new byte[10];
            for (int a = 0; a < 10; a ++) {
                bytes[a] = (byte) Integer.parseInt(idString.substring(a * 2, a * 2 + 2), 16);
            }
            return bytes;
        }
        
        @Override
        public int compareTo(ObjectId o) {
            
            byte[] thisBytes = toByte();
            byte[] oBytes = o.toByte();
            
            for (int i = 0, loop = thisBytes.length; i < loop; i ++) {
                if (thisBytes[i] > oBytes[i]) {
                    return 1;
                } else if (thisBytes[i] < oBytes[i]) {
                    return -1;
                }
            }
            return 0;
        }
    }

    public static Page createPage(byte type, int level) {
        
        return new Page(type, level);
    }
    
    public void setNextPage(int nextPage) {
        
        nextPagePos = nextPage; 
    }
    
    public int getNextPage() {
        
        return nextPagePos;
    }
    
    public void plenish(byte[] data) {
        
        rawData = data;
    }
    
    public boolean storeObject(byte[] data) {
        
        ObjectId objectId = ObjectId.build();
        if(freeSpace > data.length) {
            int start = CAPACITY - freeSpace;
            for (int i = 0, length = data.length; i < length; i ++) {
                rawData[start + i] = data[i];
            }
            return true;
        }
        return false;
    }
    
    public boolean isLeafPage() {
        return type == (byte) 2 ? true : false;
    }
    
    public boolean isEmpty() {
        return freeSpace == CAPACITY ? true : false;
    }
}
