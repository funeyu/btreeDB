package nerza;

import java.io.IOException;
import java.util.Arrays;

import page.Page;
import page.WritingPagebuffer;
import record.RecordInfo;

public class Btree {
    
    private FileStore fs;
    private Page rootPage;
    private WritingPagebuffer writingBuffer;
    
    private Btree(FileStore fs, Page rootPage, int BufferSize) {
        
        this.fs = fs;
        this.rootPage = rootPage;
        this.writingBuffer = WritingPagebuffer.create(BufferSize);
        
    }
    
    public static Btree open(String fileName, int BufferSize) throws Exception {
        
        FileStore fs = FileStore.open(fileName);
        Page rootPage = Page.createPage((byte)0, -1);
        rootPage.plenish( fs.read(Page.CAPACITY).array() );
        return new Btree(fs, rootPage, BufferSize);
    }
    
    public void store(byte[] data) throws IOException {
        if (fs.isEmpty() && writingBuffer.now() == null) {
            Page leafPage = Page.createPage((byte)2, 0);
            leafPage.storeRecord(data);
            writingBuffer.pilePage(leafPage);
            int nowid = writingBuffer.now().getId();
            byte[] recordInfo = new byte[14];
            
            byte[] fourBytes = new byte[4];
            fourBytes[0] = (byte)((nowid >> 24) & 0xFF);
            fourBytes[1] = (byte)((nowid >> 16) & 0xFF);
            fourBytes[2] = (byte)((nowid >> 8) & 0xFF);
            fourBytes[3] = (byte)(nowid & 0xFF);
            
            System.arraycopy(data, 0, recordInfo, 0, 10);
            System.arraycopy(fourBytes, 0, recordInfo, 10, 14);
            
            rootPage.storeRecordInfo(recordInfo);
            return;
        }
        
        Page writingNow = writingBuffer.now();
        if(!writingNow.storeRecord(data)) {
            
            int parentPageId = writingNow.getParentPageId();
            Page parentPage = writingBuffer.getParentBy(parentPageId, fs);
            // 从要写入的data与parentPageId提取出RecordInfo
            byte[] ribytes = new byte[14];
            
            byte[] fourBytes = new byte[4];
            int nowid = writingNow.getId();
            fourBytes[0] = (byte)((nowid >> 24) & 0xFF);
            fourBytes[1] = (byte)((nowid >> 16) & 0xFF);
            fourBytes[2] = (byte)((nowid >> 8) & 0xFF); 
            fourBytes[3] = (byte)(nowid & 0xFF);
            
            System.arraycopy(data, 0, ribytes, 0, 10);
            System.arraycopy(fourBytes, 0, ribytes, 10, 14);
            RecordInfo ri = RecordInfo.read(fourBytes);
            
            while(!parentPage.storeRecordInfo(ribytes)) {
                
                if(parentPage.isRootPage()) {
                    // 到达根Page，将原根Page变为internalPage,并且生成新的根Page
                    parentPage.changePageType();
                    byte[] min = parentPage.getMin();
                    Page rootPage  = Page.createPage((byte)0, -1);
                    Page internalPage = Page.createPage((byte)1, rootPage.getId());
                    
                }
            }
            
            Page dataPage = Page.createPage((byte)2, parentPage.getId());
            dataPage.storeRecord(data);
            writingBuffer.pilePage(dataPage);
        }
        
    }
    
    public byte[] fetch(String objectId) {
        
        return null;
    }
}
