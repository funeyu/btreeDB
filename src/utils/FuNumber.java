package utils;

public final class FuNumber {
    
    public static int translateBytes (byte[] fourBytes) {
        
        assert(fourBytes.length == 4);
        return fourBytes[0] << 24 +
               fourBytes[1] << 16 +
               fourBytes[2] << 8  +
               fourBytes[3];
    }
}
