package btreeJava;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import page.Page;

public class FileStore {
    private final static int META_DATA_LENGTH = 4; 
    private String fileName;
    private RandomAccessFile raf;
    private FileChannel fileChannel;
    private MetaData meta;
    
    private FileStore (File f) throws Exception {
        
        raf = new RandomAccessFile(f, "rw");
        fileChannel = raf.getChannel();
        meta = getMetaData(0);
    }
    
    public static FileStore open (String fileName) throws Exception {
        
        File f = new File(fileName);
        if (!f.exists()) {
            ByteBuffer buffer = ByteBuffer.allocate(4 + Page.CAPACITY);
            buffer.putInt(0);
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.getChannel().write(buffer);
        }
        return new FileStore(f); 
    }
    
    public FileStore seek(long position) throws IOException {
        
        raf.seek(position);
        return this;
    }
    
    public ByteBuffer read(int size) throws IOException {
        
        ByteBuffer buffer = ByteBuffer.allocate(size);
        fileChannel.read(buffer);
        return buffer;
    }
    
    public void write(long position, byte[] content) throws IOException {
        
        raf.seek(position);
        raf.write(content);
    }
    
    public int size() {
        
        return meta.count;
    }
    
    private class MetaData {
        private int count;
        public void restore(ByteBuffer buffer) {
            buffer.position(0);
            this.count = buffer.getInt();
        }
    }
    
    private void fill (ByteBuffer buffer) throws IOException {
        
        fileChannel.read(buffer);
    }
   
    private MetaData getMetaData (int position) throws IOException {
        
        ByteBuffer buffer = ByteBuffer.allocate(META_DATA_LENGTH);
        fill(buffer);
        MetaData meta= new MetaData();
        meta.restore(buffer);
        return meta;
    }
    
    public boolean isEmpty () throws IOException {
        
        return meta.count == 0 ? true : false;
    }
    
    public static void main(String args[]) {
        try {
            FileStore.open("java.data");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
}
