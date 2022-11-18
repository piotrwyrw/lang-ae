package net.piotrwyrw.ast;

import net.piotrwyrw.tokenizer.Token;
import net.piotrwyrw.tokenizer.TokenType;

public enum LogicalOperation {

    OR,
    AND,
    XOR,
    NOT,
    LEFT_GREATER,
    RIGHT_GREATER,
    UNDEFINED;

    public static LogicalOperation fromToken(Token token) {
        return fromTokenType(token.type());
    }

    public static LogicalOperation fromTokenType(TokenType type) {
        switch (type) {
            case IDEN_OR: return OR;
            case IDEN_AND: return AND;
            case IDEN_XOR: return XOR;
            case IDEN_NOT: return NOT;
            case RGREATER: return RIGHT_GREATER;
            case LGREATER: return LEFT_GREATER;
            default: return UNDEFINED;
        }
    }

}
