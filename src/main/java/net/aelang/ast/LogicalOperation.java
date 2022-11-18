package net.aelang.ast;

import net.aelang.tokenizer.Token;
import net.aelang.tokenizer.TokenType;

public enum LogicalOperation {

    OR,
    AND,
    XOR,
    NOT,
    UNDEFINED;

    public static LogicalOperation fromToken(Token token) {
        return fromTokenType(token.type());
    }

    public static LogicalOperation fromTokenType(TokenType type) {
        switch (type) {
            case IDEN_OR:
                return OR;
            case IDEN_AND:
                return AND;
            case IDEN_XOR:
                return XOR;
            case IDEN_NOT:
                return NOT;
            default:
                return UNDEFINED;
        }
    }

}
