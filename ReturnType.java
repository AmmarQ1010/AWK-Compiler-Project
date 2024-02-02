public class ReturnType {
    private Type type;
    private String value;

    public enum Type {
        NONE,
        BREAK,
        CONTINUE,
        RETURN
    }
    public ReturnType(Type type) {
        this.type = type;
    }

    public ReturnType(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (type == Type.NONE) {
            return "None";
        } else if (type == Type.BREAK) {
            return "Break";
        } else if (type == Type.CONTINUE) {
            return "Continue";
        } else if (type == Type.RETURN) {
            return "Return" + (value != null ? " " + value : "");
        }
        return "Unknown";
    }
}
