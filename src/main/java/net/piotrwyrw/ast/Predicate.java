package net.piotrwyrw.ast;

import net.piotrwyrw.tokenizer.Token;
import net.piotrwyrw.tokenizer.TokenType;

public enum Predicate {

    DEFINED,
    UNDEFINED;

    public static Predicate fromToken(Token t) {
        return fromTokenType(t.type());
    }

    public static Predicate fromTokenType(TokenType type) {
        switch (type) {
            case IDEN_DEFINED: return DEFINED;
            default: return UNDEFINED;
        }
    }

}
