import java.util.HashMap;

public class InterpreterDataType {

    private String value;
    private HashMap<String, InterpreterDataType> hashMap;

    public InterpreterDataType(){
        value="";
        hashMap = null;
    }

    public InterpreterDataType(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }



    public boolean isEmpty() {
        return value == null || value.isEmpty();
    }

    //unsure if this actually means false...
    public boolean isFalse() {
        return "0".equals(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
