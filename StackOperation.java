public class StackOperation {
    private int index;
    private String lastValue;
    private int operationCount;

    public StackOperation(int index, String lastValue, int operationCount)
    {
        this.index = index;
        this.lastValue = lastValue;
        this.operationCount = operationCount;
    }

    public int getIndex() {
        return index;
    }

    public String getLastValue() {
        return lastValue;
    }

    public int getOperationCount() {
        return operationCount;
    }
}
