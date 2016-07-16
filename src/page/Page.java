package page;

import java.nio.ByteBuffer;
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

    private Page(byte type, int level, int parentPageID, int childPageID, ObjectId max, ObjectId min) {
        this.type = type;
        this.level = level;
        this.parentPageID = parentPageID;
        this.maxObjectId = max;
        this.minObjectId = min;
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

    public boolean storeObject(byte[] data) {

        ObjectId objectId = maxObjectId = ObjectId.build();
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

    public ObjectId maxObjectId() {

        return maxObjectId;
    }

    public void getHighPage() {

//        this
    }
    public static Page shapeFromBytes(byte[] bytes) {

        ByteBuffer buffers = ByteBuffer.wrap(bytes);
        byte[] objectIdBytes = new byte[10];
        buffers.position(0);
        byte type = buffers.get();

        int level = buffers.getInt();

        buffers.get(objectIdBytes);
        ObjectId maxObjectId = ObjectId.transferedByBytes(objectIdBytes);

        buffers.get(objectIdBytes);
        ObjectId minObjectId = ObjectId.transferedByBytes(objectIdBytes);

        int childPageID = buffers.getInt();
        int parentPageID = buffers.getInt();

        return new Page(type, level, parentPageID, childPageID, maxObjectId, minObjectId);
    }
}
