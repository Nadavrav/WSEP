package DAL;

public class TestsFlags {
    private static volatile TestsFlags flag;
    private boolean isTests=false;

    public static TestsFlags getInstance() {
        if (flag == null) {
            synchronized (TestsFlags.class) {
                if(flag==null)
                    flag = new TestsFlags();
            }
        }
        return flag;
    }
    public void setTests(){
        isTests=true;
    }
    public boolean isTests(){
        return isTests;
    }

}
