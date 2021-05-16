package tokenring.impl.shared;

public class Load {
    public enum LoadLevel{
        LOW, // == ~ 0.2 * nodesSize
        NORMAL, // == ~ 0.5 * nodesSize
        HIGH // >= nodesSize
    }
    private final LoadLevel relativeLoad;

    private final int length;

    @Override
    public String toString() {
        return "Load{" +
                "relativeLoad=" + relativeLoad +
                ", length=" + length +
                '}';
    }

    public int getLength() {
        return length;
    }

    public LoadLevel getRelativeLoad() {
        return relativeLoad;
    }

    public Load(LoadLevel relativeLoad, int length) {
        this.relativeLoad = relativeLoad;
        this.length = length;
    }
}
