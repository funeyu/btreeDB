package page;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

import record.Record;

public class Page {
    public static final int CAPACITY = 4 * 1024;
    private static AtomicInteger pageCounter = new AtomicInteger();
    private int index;
    private byte type;
    private int level;
    private int freeSpace;
    private byte[] rawData;
    private byte[] minRecordIdBytes;
    private byte[] maxRecordIdBytes;
    private int nextPagePos;
    private int childPageID;
    private int parentPageID;

    private Page(byte type, int level, int parentPageID) {

        this.index = pageCounter.getAndIncrement();
        this.type = type;
        this.level = level;
        this.rawData = new byte[CAPACITY];
        this.freeSpace = CAPACITY;
        this.parentPageID = parentPageID;
    }

    private Page(byte type, int level, int parentPageID, int childPageID, byte[] max, byte[] min) {
        this.type = type;
        this.level = level;
        this.parentPageID = parentPageID;
        this.maxRecordIdBytes = max;
        this.minRecordIdBytes = min;
        this.parentPageID = parentPageID;
        this.childPageID = childPageID;
    }

    public static Page createPage(byte type, int level, int parentPageId) {

        return new Page(type, level, parentPageId);
    }

    public int getId() {

        return index;
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

    public boolean storeRecord(byte[] data) {

        Record re = Record.build(data);
        maxRecordIdBytes = re.toIDBytes();
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

    public byte[] getBytes() {

        return rawData.clone();
    }

    public int getParentPageId() {

        return this.parentPageID;
    }

    public byte[] maxRecordIdBytes() {

        return maxRecordIdBytes;
    }

    public void getHighPage() {
        
        
    }
    
    /**
     * 读取磁盘的二进制字节，建立一个Page存于内存中；
     * @param bytes
     * @return
     */
    public static Page shapeFromBytes(byte[] bytes) {

        ByteBuffer buffers = ByteBuffer.wrap(bytes);
        byte[] recordIdBytes = new byte[10];
        buffers.position(0);
        byte type = buffers.get();

        int level = buffers.getInt();

        buffers.get(recordIdBytes);
        byte[] maxRecordIdBytes = recordIdBytes;

        buffers.get(recordIdBytes);
        byte[] minRecordIdBytes = recordIdBytes;

        int childPageID = buffers.getInt();
        int parentPageID = buffers.getInt();

        return new Page(type, level, parentPageID, childPageID, maxRecordIdBytes, minRecordIdBytes);
    }
}
