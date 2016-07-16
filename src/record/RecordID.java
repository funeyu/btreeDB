package record;

import java.util.concurrent.atomic.AtomicInteger;


class RecordID implements Comparable<RecordID>{
    private String idString;
    private static AtomicInteger counter;
    private static final StringBuilder strings = new StringBuilder("");

    private RecordID(String idString) {
        this.idString = idString;
    }

    public static RecordID build() {

        // 递增counter 取后2字节作为计数标记
        if ((counter.get() & 0xff0000) == 0) {
            try {
                Thread.sleep (1);
                counter.set(1);
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

        return new RecordID(strings.toString());
    }

    /**
     * 将byte[]字节数组转换一个RecordID实例
     * @param  bytes 待转成RecordID的字节数组
     * @return       返回一个RecordID实例
     */
    public static RecordID transferedByBytes(byte[] bytes) {

        strings.setLength(0);
        for(int i = 0; i < 10; i ++) {
            strings.append(bytes[i]);
        }
        return new RecordID(strings.toString());
    }

    public static RecordID transferedByString(String id) {
        
        return new RecordID(id);
    }
    
    public String getId() {
        return idString == null ? idString : "";
    }

    /**
     * 比较两个RecordID的大小的函数
     * @param  o 和哪个RecordID比较
     * @return   1：本身的RecordID 大于 o; 0: 两者一样大小； -1: 本身小于o
     */
    @Override public int compareTo(RecordID o) {

        byte[] oBytes = o.toBytes();
        return compareBytes(oBytes);
    }
    
    /**
     * 将该id转换成字节数组
     * @return
     */
    public byte[] toBytes() {

        byte[] bytes = new byte[10];
        for (int a = 0; a < 10; a ++) {
            bytes[a] = (byte) Integer.parseInt(
                    idString.substring(a * 2, a * 2 + 2), 16);
        }
        return bytes;
    }
    
    private int compareBytes(byte[] oBytes) {
        
        byte[] thisBytes = toBytes();
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
