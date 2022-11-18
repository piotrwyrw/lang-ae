package net.aelang.tokenizer;

import net.aelang.LexerError;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {

    public static final String REGEX_PATTERN = "^(([0-9]+(\\.[0-9]+)?))|^(\\+|\\->?|\\*|\\/|\\^)|^(\\(|\\))|^([a-zA-Z_]+)|^(\\$[0-9]+)|^(\\:)|^(\\;)|^\"(.*?)\"|^\\?|^\\[|^\\]|^\\<|^\\>";

    private Pattern pattern;

    private String input;

    public Tokenizer(String input) {
        this.input = input.replaceAll("\\s{2,}", "");
        this.pattern = Pattern.compile(REGEX_PATTERN);
    }

    public boolean hasNext() {
        return input.length() > 0;
    }

    public Token nextToken() throws LexerError {
        if (input.length() == 0) {
            return null;
        }

        Matcher matcher = pattern.matcher(input);
        if (!matcher.find())
            throw new LexerError("The input does not resemble any expected token: " + input);

        String str = matcher.group();

        input = input.substring(str.length()).trim();

        TokenType type = TokenType.simpleClassify(str);

        int data = 0;

        if (type == TokenType.UNDEFINED && str.matches("\\$[0-9]+")) {
            type = TokenType.PLACEHOLDER;
            data = Integer.parseInt(str.substring(1));
        } else if (type == TokenType.UNDEFINED && str.matches("\"(.*?)\"")) {
            type = TokenType.STR_LIT;
            str = str.trim().substring(1, str.length() - 1);
        }

        str = str.trim();

        if (type == TokenType.UNDEFINED)
            throw new LexerError("Failed to determine token type: " + str);

        return new Token(type, str, data);
    }

}
