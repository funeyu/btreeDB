package btreeJava;

import java.io.IOException;

import page.Page;
import page.WritingPagebuffer;

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
        Page rootPage = Page.createPage((byte)0, 0, -1);
        rootPage.plenish( fs.read(Page.CAPACITY).array() );
        return new Btree(fs, rootPage, BufferSize);
    }
    
    public void store(byte[] data) throws IOException {
        // 初始空的数据库文件
        if (fs.isEmpty() && writingBuffer.now() == null) {
            Page leafPage = Page.createPage((byte)2, 1, 0);
            leafPage.storeObject(data);
            writingBuffer.pilePage(leafPage);
            return;
        }
        
        Page writingNow = writingBuffer.now();
        while(!writingNow.storeObject(data)) {
            
            int parentPageId = writingNow.getParentPageId();
            Page parent = ;
            writingNow.maxObjectId()
            
        }
    }
    
    public byte[] fetch(String objectId) {
        
        return null;
    }
}
