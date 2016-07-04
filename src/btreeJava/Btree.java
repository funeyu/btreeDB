package btreeJava;

import java.io.IOException;

import page.Page;

public class Btree {
    
    private FileStore fs;
    private Page rootPage;
    
    private Btree(FileStore fs, Page rootPage) {
        
        this.fs = fs;
        this.rootPage = rootPage;
    }
    
    public static Btree open(String fileName) throws Exception {
        
        FileStore fs = FileStore.open(fileName);
        Page rootPage = Page.createPage((byte)0, 0);
        rootPage.plenish( fs.read(Page.CAPACITY).array() );
        return new Btree(fs, rootPage);
    }
    
    public void store(byte[] data) throws IOException {
        // 初始空的数据库文件
        if (fs.isEmpty()) {
            Page leafPage = Page.createPage((byte)2, 1);
            leafPage.storeObject(data);
            return;
        }
        
    }
    
    public byte[] fetch(String objectId) {
        
        return null;
    }
}
