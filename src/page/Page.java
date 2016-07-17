package page;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import record.Record;
import record.RecordInfo;

public class Page {
    public static final int CAPACITY = 4 * 1024;
    private static AtomicInteger pageCounter = new AtomicInteger();
    private int index;
    private byte type;
    private int freeSpace;
    private byte[] rawData;
    private byte[] minRecordIdBytes;
    private byte[] maxRecordIdBytes;
    private int nextPagePos;
    private int childPageID;
    private int parentPageID;

    private Page(byte type, int parentPageID) {

        this.index = pageCounter.getAndIncrement();
        this.type = type;
        this.rawData = new byte[CAPACITY];
        this.freeSpace = CAPACITY - (1 + 4 + 4 + 10 + 10);
        this.parentPageID = parentPageID;
    }

    private Page(byte type, int parentPageID, int childPageID, byte[] max, byte[] min) {
        this.type = type;
        this.parentPageID = parentPageID;
        this.maxRecordIdBytes = max;
        this.minRecordIdBytes = min;
        this.parentPageID = parentPageID;
        this.childPageID = childPageID;
    }

    /**
     * 根据Page类型与父级Page的id创建一个新的Page；
     * 由于每次写数据到磁盘里都要经过Page缓存区，在内存中记录该Page的id，
     * 也即Page的递增数，写入磁盘里的时候根据其递增的id来排序倒出到磁盘；
     * 
     * todo: 将rootPage，internalPage与leafPage杂揉到了一起，需要分离
     * @param type 0 ：rootPage, 1: internalPage, 2: leafPage
     * @param parentPageId 父级Page
     * @return
     */
    public static Page createPage(byte type, int parentPageId) {

        return new Page(type, parentPageId);
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
        if(minRecordIdBytes == null) {
            minRecordIdBytes = Arrays.copyOfRange(data, 19, 29);
        }
        
        maxRecordIdBytes = re.toIDBytes();
        if(freeSpace > data.length) {
            int start = CAPACITY - freeSpace;
            for (int i = 0, length = data.length; i < length; i ++) {
                rawData[start + i] = data[i];
            }

            freeSpace -= data.length;
            return true;
        }
        return false;
    }
    
    /**
     * 为不是leaf节点的添加RecordInfo信息
     * @param info 为RecordInfo的byte内容
     * @return true 该Page添加info成功， false 该Page剩余的空间不能盛下
     */
    public boolean storeRecordInfo(byte[] info) {
        
        if(freeSpace > info.length) {
            for (int i = 0, length = info.length; i < length; i ++) {
                int start = CAPACITY - freeSpace;
                rawData[start + i] = info[i];
            }
            
            freeSpace -= info.length;
            return true;
        }
        
        return false;
    }

    private boolean isMax(Record writing, Record other) {
        
//        return Record.
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
    
    public boolean isRootPage() {
        
        return type == (byte)0 ? true : false;
    }
    
    /**
     * 改变该Page的类型， 只发生在根Page向internalPage转换的过程中
     */
    public void changePageType() {
        
        type = (byte) 1;
    }
    
    public byte[] getMax() {
        
        return maxRecordIdBytes;
    }
    
    public byte[] getMin() {
        
        return minRecordIdBytes;
    }
    
    /**
     * 读取磁盘的二进制字节，建立一个Page存于内存中；
     * @param bytes
     * @return
     */
    public static Page shapeFromBytes(byte[] bytes) {

        assert(bytes.length == Page.CAPACITY);
        ByteBuffer buffers = ByteBuffer.wrap(bytes);
        buffers.position(0);
        byte type = buffers.get();

        byte[] recordIdBytes = new byte[10];
        buffers.get(recordIdBytes);
        byte[] maxRecordIdBytes = recordIdBytes;

        buffers.get(recordIdBytes);
        byte[] minRecordIdBytes = recordIdBytes;

        int childPageID = buffers.getInt();
        int parentPageID = buffers.getInt();

        return new Page(type, parentPageID, childPageID, maxRecordIdBytes, minRecordIdBytes);
    }
}
