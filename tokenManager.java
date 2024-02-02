import java.util.LinkedList;
import java.util.Optional;

public class tokenManager {

    private LinkedList<Token> list;

    public tokenManager(LinkedList<Token> list) {
        this.list = list;
    } //default constructor

    public Optional<Token> Peek(int j) { //check j tokens into the token list

        if (j < list.size()) {
            return Optional.of(list.get(j)); //if j is not out of bounds then return the token at that position
        } else {
            return Optional.empty(); //else return optional empty
        }
    }

    public boolean MoreTokens() { //check if list is empty. if not, return true.
        return !list.isEmpty();
    }

    public Optional<Token> MatchAndRemove(Token.TokenType t) { //check for token type of the head of the token list.
        if(MoreTokens()){
            if (list.get(0).getType() == t) {
                return Optional.of(list.removeFirst()); //if found, remove it and return it
            } else
                return Optional.empty(); //else return optional empty
        }
        /*else {
            throw new IndexOutOfBoundsException("List of tokens cannot be accessed, it is empty.");
        }*/
        return Optional.empty();
    }

}
