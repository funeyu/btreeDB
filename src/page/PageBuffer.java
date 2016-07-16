package page;

public interface PageBuffer {

    public Page getById(int id);

    public void pilePage(Page page);

    public boolean isPlentished();

    public Page now();
}
