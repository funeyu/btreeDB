package record;

import java.util.Arrays;

public class Record {
    
    private RecordID recordID;
    private byte[] rawData;
    
    private Record(RecordID recordID, byte[] rawData) {
        
        this.recordID = recordID;
        this.rawData = rawData;
    }
    
    public static Record build(byte[] value) {
        
        RecordID recordID = RecordID.build();
        return new Record(recordID, value);
    }
    
    /*
     * 从磁盘里读取字节数组转换成相应的Record对象
     */
    public static Record open(byte[] fullData) {
        
        if (fullData.length <= 10) {
            return null;
        }
        byte[] recordIDBytes = Arrays.copyOfRange(fullData, 0, 10);
        RecordID record = RecordID.transferedByBytes(recordIDBytes);
        
        byte[] rawBytes = Arrays.copyOfRange(fullData, 10, fullData.length);
        return new Record(record, rawBytes);
    }
    
    
    /*
     * 获取该条记录的recordID 与 rawData数据组成的byte[];
     * 在将该条数据从内存写入到磁盘时候调用
     */
    public byte[] pourOutData() {
        
        byte[] recordIDBytes = recordID.toBytes();
        byte[] fullBytes = new byte[rawData.length + 10];
        System.arraycopy(recordIDBytes, 0, fullBytes, 0, 10);
        System.arraycopy(rawData, 0, fullBytes, 0, rawData.length);
        return fullBytes;
    }
    
    /*
     * 获取该条数据下的id字符串
     */
    public String getIDS() {
        
        return recordID.getId();
    }
    
    public int compare(Record other) {
        
        RecordID record = other.getID();
        return recordID.compareTo(record);
    }
    
    /**
     * 根据记录的RecordID字节组，判断大小
     * @param me 本身的RecordID字节数组
     * @param other 另一个RecordID字节数组
     * @return 1：me > other; 0: me = other; -1: me < other
     */
    public static int compare(byte[] me, byte[] other) {
        
        for (int i = 0, loop = me.length; i < loop; i ++) {
            if (me[i] > other[i]) {
                return 1;
            } else if (me[i] < other[i]) {
                return -1;
            }
        }
        return 0;
    }
    
    /*
     * 获取记录的recordID的字节数组
     */
    public byte[] toIDBytes() {
        
        return recordID.toBytes();
    }
    
    /*
     * note: 获取该条记录的RecordID不是返回其链接，
     *       返回的是copy的RecordID对象
     */
    private RecordID getID() {
        
        return RecordID.transferedByString(recordID.getId());
    }
}
