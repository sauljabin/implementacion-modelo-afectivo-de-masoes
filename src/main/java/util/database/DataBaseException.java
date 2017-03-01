package util.database;

public class DataBaseException extends Exception {
    private boolean isCloseConnectionNeeded;

    public DataBaseException(String message, boolean isCloseConnectionNeeded) {
        super(message);
        this.isCloseConnectionNeeded = isCloseConnectionNeeded;
    }

    public boolean isCloseConnectionNeeded(){
        return isCloseConnectionNeeded;
    }

}
