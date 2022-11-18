package net.aelang.tokenizer;

public class Token {

    private TokenType type;
    private String val;
    private int data;

    public Token(TokenType type, String val) {
        this.type = type;
        this.val = val;
        this.data = 0;
    }

    public Token(TokenType type, String val, int data) {
        this.type = type;
        this.val = val;
        this.data = data;
    }

    public TokenType type() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public String val() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public int data() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", val='" + val + '\'' +
                ", data=" + data +
                '}';
    }
}
