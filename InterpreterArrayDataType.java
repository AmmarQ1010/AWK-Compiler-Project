import java.util.HashMap;

public class InterpreterArrayDataType extends InterpreterDataType{
    private HashMap<String,InterpreterDataType> arrayMap;

    public InterpreterArrayDataType(){
        arrayMap=new HashMap<>();
    }

    public InterpreterArrayDataType(HashMap<String, InterpreterDataType> arrayMap){
        this.arrayMap=arrayMap;
    }

    public void set(String key, InterpreterDataType value) {
        arrayMap.put(key, value);
    }

    public HashMap<String, InterpreterDataType> getArrayMap(){
        return arrayMap;
    }

    @Override
    public String toString() {
        return arrayMap.toString();
    }

}
