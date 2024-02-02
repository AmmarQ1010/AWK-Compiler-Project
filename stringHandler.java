public class stringHandler {
	
    private String file; //file read in
    private int index; //how far you are in the file

    public stringHandler(String file) { //constructor
        this.file = file;
        this.index = 0;
    }

    public char peek(int i) { //see what character is at this index in the file
    	return file.charAt(index + i);
    }

    public String peekString(int i) { //returns a string of all values between index and i
            return file.substring(index, index + i);
    }
    
    public char getChar() {//returns char at index value then moves the index one forwards
            char currentChar = file.charAt(index);
            index++;
            return currentChar;
    }
    
    public void swallow(int i) {//moves the index by i
        index += i;
    }

    public boolean isDone() {//checks if index is at the end of the file
        return index >= file.length();
    }

    public String remainder() {//returns a string of what is remaining in the document
        if (!isDone()) {
            return file.substring(index);
        }
        else {
        	return ""; // Return empty string if at the end
        }
    }
}
