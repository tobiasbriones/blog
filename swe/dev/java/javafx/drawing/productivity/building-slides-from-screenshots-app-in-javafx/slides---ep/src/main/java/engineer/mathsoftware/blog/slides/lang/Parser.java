// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.lang;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Parser<K extends Enum<?>> {
    private static final Pattern STRING_PATTERN
        = Pattern
        .compile("(['\"])([^'\"]*)(['\"])");
    private static final Pattern SINGLE_LINE_COMMENT_PATTERN
        = Pattern
        .compile("(//)(.*)(\\r\\n|\\r|\\n)");
    private static final String PASCAL_CASE_GROUP_REGEX
        = "([A-Z][a-zA-Z0-9]*)";
    private static final Pattern PASCAL_CASE_TYPE_PATTERN
        = Pattern
        .compile("([ (.,:]{1}|:{2}|::)"
            + PASCAL_CASE_GROUP_REGEX
            + "([ (.,:\\[]{1}|:{2}|::)"
        );

    public static List<String> tokens(String code) {
        var delimiters = List.of(
            "\n",
            " ",
            ";",
            "=",
            "+",
            "-",
            "*",
            "/",
            ",",
            ":",
            "?",
            "(",
            ")",
            "<",
            ">",
            "[",
            "]",
            "."
        );
        var delimiterPatterns = delimiters
            .stream()
            .map(delimiter -> "\\" + delimiter)
            .collect(Collectors.joining("|"));
        var delimiterPattern
            = "(?<=" + delimiterPatterns + ")|(?=" + delimiterPatterns + ")";
        var afterStrings = tokensEnclosedBy(STRING_PATTERN, code);
        var afterComments = new ArrayList<String>();

        for (var token : afterStrings) {
            if (STRING_PATTERN.matcher(token).find()) {
                afterComments.add(token);
                continue;
            }
            afterComments.addAll(tokensEnclosedBy(SINGLE_LINE_COMMENT_PATTERN,
                token
            ));
        }

        var afterTypes = new ArrayList<String>();

        for (var token : afterComments) {
            if (
                STRING_PATTERN.matcher(token).find()
                    || SINGLE_LINE_COMMENT_PATTERN.matcher(token).find()
            ) {
                afterTypes.add(token);
                continue;
            }
            afterTypes.addAll(tokensSurroundedBy(PASCAL_CASE_TYPE_PATTERN,
                token
            ));
        }

        var result = new ArrayList<String>();

        for (var token : afterTypes) {
            if (
                STRING_PATTERN.matcher(token).find()
                    || SINGLE_LINE_COMMENT_PATTERN.matcher(token).find()
                    || token.matches(PASCAL_CASE_GROUP_REGEX)
            ) {
                result.add(token);
                continue;
            }
            result.addAll(List.of(token.split(delimiterPattern)));
        }
        return result;
    }

    private final Map<String, K> keywordMap;

    public Parser(Class<K> keywordType) {
        var keywords = keywordType.getEnumConstants();
        keywordMap = new HashMap<>(keywords.length);

        for (var keyword : keywords) {
            var key = keyword.name().toLowerCase(Locale.ROOT);
            keywordMap.put(key, keyword);
        }
    }

    public Element.TokenParsing parseToken(String value) {
        var token = value.trim();

        if (STRING_PATTERN.matcher(token).find()) {
            return new Element.TokenParsing(new Element.StringLiteral(value));
        }
        if (token.startsWith("//")) {
            return new Element.TokenParsing(new Element.Comment(value));
        }
        if (token.matches(PASCAL_CASE_GROUP_REGEX)) {
            return new Element.TokenParsing(new Element.Type(value));
        }
        if (keywordMap.containsKey(token)) {
            return new Element.TokenParsing(new Element.Keyword(value));
        }
        if (token.matches("[,=+\\-*/;&|?:!<>]+")) {
            return new Element.TokenParsing(new Element.Symbol(value));
        }
        if (token.matches("-?\\d+(\\.\\d+)?")) {
            return new Element.TokenParsing(new Element.Number(value));
        }
        return new Element.TokenParsing(new Element.Other(value));
    }

    private static ArrayList<String> tokensEnclosedBy(
        Pattern pattern,
        String code
    ) {
        var matcher = pattern.matcher(code);
        var result = new ArrayList<String>();
        int lastIndex = 0;

        while (matcher.find()) {
            var startIndex = matcher.start();
            var endIndex = matcher.end();
            var delimiter1 = matcher.group(1);
            var matchedString = matcher.group(2);
            var delimiter2 = matcher.group(3);

            result.add(code.substring(lastIndex, startIndex));
            result.add(delimiter1 + matchedString + delimiter2);

            lastIndex = endIndex;
        }
        result.add(code.substring(lastIndex));
        return result;
    }

    private static ArrayList<String> tokensSurroundedBy(
        Pattern pattern,
        String code
    ) {
        var matcher = pattern.matcher(code);
        var result = new ArrayList<String>();
        int lastIndex = 0;

        while (matcher.find()) {
            var startIndex = matcher.start();
            var endIndex = matcher.end();

            result.add(code.substring(lastIndex, startIndex));

            for (int i = 1; i <= matcher.groupCount(); i++) {
                var matched = matcher.group(i);

                result.add(matched);
            }

            lastIndex = endIndex;
        }
        result.add(code.substring(lastIndex));
        return result;
    }
}
