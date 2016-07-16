package record;

import java.util.Arrays;

import utils.FuNumber;

public class RecordInfo implements Comparable<RecordInfo>{
    
    private RecordID recordID;
    private int pageId;
    
    /**
     * RecordInfo 由 recordID和其索引结构中指向的下一级Page的id构成；
     * RecordInfo 只在索引的Page中存储不会在数据的Page
     * RecordInfo 占据 10（RecordID） + 4（int） 字节
     * @param recordID
     * @param pageId
     */
    private RecordInfo(RecordID recordID, int pageId) {
        
        this.recordID = recordID;
        this.pageId = pageId;
    }
    
    public static RecordInfo build(RecordID recordID, int pageId) {
        
        return new RecordInfo(recordID, pageId);
    }
    
    /*
     * 根据磁盘读出的14个字节来创建RecordInfo对象
     */
    public static RecordInfo read(byte[] riBytes) {
        
        assert(riBytes.length == 14);
        
        byte[] recordBytes = Arrays.copyOfRange(riBytes, 0, 10);
        RecordID recordID = RecordID.transferedByBytes(recordBytes);
        
        byte[] pageIdBytes = Arrays.copyOfRange(riBytes, 10, 14);
        int pageId = FuNumber.translateBytes(pageIdBytes);
        
        return new RecordInfo(recordID, pageId);
    }
    
    private RecordID getSelfID() {
        
        return this.recordID;
    }
    
    public int getPageID() {
        
        return this.pageId;
    }

    @Override public int compareTo(RecordInfo o) {
        
        RecordID orecordID = o.getSelfID();
        RecordID selfRecordID = getSelfID();
        return selfRecordID.compareTo(orecordID);
    }
}
