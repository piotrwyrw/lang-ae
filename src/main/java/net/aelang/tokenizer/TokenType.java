package net.aelang.tokenizer;

public enum TokenType {

    LPAREN,
    RPAREN,
    ADD,
    SUB,
    MUL,
    DIV,
    POW,
    INT_LIT,
    REAL_LIT,
    STR_LIT,
    IDEN,
    IDEN_DEFINE,
    IDEN_UNDEFINE,
    IDEN_NOTE,
    IDEN_LD,
    IDEN_INFO,
    IDEN_IF,
    IDEN_RUN,
    IDEN_DEFINED,
    IDEN_AND,
    IDEN_OR,
    IDEN_XOR,
    IDEN_NOT,
    IDEN_THEN,
    IDEN_FOLLOWING,
    IDEN_VAR,
    IDEN_DEFAULT,
    IDEN_PROMPT,
    IDEN_REPEAT,
    IDEN_TIMES,
    IDEN_RECALL,
    IDEN_COMPLEX,
    IDEN_NEW,
    PLACEHOLDER,
    POINT_RIGHT,
    COLON,
    SEMI,
    QUESTION,
    LBRACKET,
    RBRACKET,

    RGREATER,
    LGREATER,
    DOT,
    UNDEFINED;

    public static TokenType simpleClassify(String str) {
        if (str.matches("\\("))
            return LPAREN;
        if (str.matches("\\)"))
            return RPAREN;
        if (str.matches("\\+"))
            return ADD;
        if (str.matches("\\-"))
            return SUB;
        if (str.matches("\\*"))
            return MUL;
        if (str.matches("\\/"))
            return DIV;
        if (str.matches("\\^"))
            return POW;
        if (str.matches("[0-9]+"))
            return INT_LIT;
        if (str.matches("(([0-9]+(\\.[0-9]+)?))"))
            return REAL_LIT;
        if (str.matches("def"))
            return IDEN_DEFINE;
        if (str.matches("undef"))
            return IDEN_UNDEFINE;
        if (str.matches("note"))
            return IDEN_NOTE;
        if (str.matches("ld"))
            return IDEN_LD;
        if (str.matches("info"))
            return IDEN_INFO;
        if (str.matches("if"))
            return IDEN_IF;
        if (str.matches("run"))
            return IDEN_RUN;
        if (str.matches("defined"))
            return IDEN_DEFINED;
        if (str.matches("and"))
            return IDEN_AND;
        if (str.matches("or"))
            return IDEN_OR;
        if (str.matches("not"))
            return IDEN_NOT;
        if (str.matches("xor"))
            return IDEN_XOR;
        if (str.matches("then"))
            return IDEN_THEN;
        if (str.matches("following"))
            return IDEN_FOLLOWING;
        if (str.matches("var"))
            return IDEN_VAR;
        if (str.matches("default"))
            return IDEN_DEFAULT;
        if (str.matches("prompt"))
            return IDEN_PROMPT;
        if (str.matches("repeat"))
            return IDEN_REPEAT;
        if (str.matches("times"))
            return IDEN_TIMES;
        if (str.matches("recall"))
            return IDEN_RECALL;
        if (str.matches("complex"))
            return IDEN_COMPLEX;
        if (str.matches("new"))
            return IDEN_NEW;
        if (str.matches("[a-zA-Z_]+"))
            return IDEN;
        if (str.matches("\\-\\>"))
            return POINT_RIGHT;
        if (str.matches("\\:"))
            return COLON;
        if (str.matches("\\;"))
            return SEMI;
        if (str.matches("\\?"))
            return QUESTION;
        if (str.matches("\\["))
            return LBRACKET;
        if (str.matches("\\]"))
            return RBRACKET;
        if (str.matches("<"))
            return RGREATER;
        if (str.matches(">"))
            return LGREATER;
        if (str.matches("\\."))
            return DOT;
        return UNDEFINED;
    }

}
