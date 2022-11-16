package net.piotrwyrw.ast;

import net.piotrwyrw.tokenizer.Token;
import net.piotrwyrw.tokenizer.TokenType;

public enum BinaryOperation {

    ADD,
    SUB,
    MUL,
    DIV,
    POW,
    UNDEFINED;

    public static BinaryOperation fromToken(Token token) {
        return fromToken(token.type());
    }

    public static BinaryOperation fromToken(TokenType type) {
        switch (type) {
            case ADD: return ADD;
            case SUB: return SUB;
            case MUL: return MUL;
            case DIV: return DIV;
            case POW: return POW;
            default:
                return UNDEFINED;
        }
    }

}
