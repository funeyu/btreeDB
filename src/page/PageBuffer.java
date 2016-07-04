package page;

public interface PageBuffer {
    
    public Page getById(int id);
    
    public void pilePage(int id, Page page);
    
    public boolean isPlentished();
    
    
}
