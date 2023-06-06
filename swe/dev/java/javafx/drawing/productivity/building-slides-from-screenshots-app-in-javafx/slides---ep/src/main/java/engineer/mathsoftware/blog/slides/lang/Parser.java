// Copyright (c) 2023 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

package engineer.mathsoftware.blog.slides.lang;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Parser<K extends Enum<?>> {
    public static List<String> tokens(String code) {
        var delimiters = List.of("\n", " ");
        var delimiterPattern = delimiters
            .stream()
            .map(delimiter -> "\\" + delimiter)
            .reduce((a, b) ->
                "(?<=" + a + ")" + "|" + "(?<=" + b + ")"
            )
            .orElse("");
        return List.of(code.split(delimiterPattern));
    }

    private final Map<String, K> keywordMap;

    public Parser(Class<K> keywordType){
        var keywords = keywordType.getEnumConstants();
        keywordMap = new HashMap<>(keywords.length);

        for (var keyword : keywords) {
            var key = keyword.name().toLowerCase(Locale.ROOT);
            keywordMap.put(key, keyword);
        }
    }

    public Element.TokenParsing parseToken(String value) {
        var token = value.trim();

        if (keywordMap.containsKey(token)) {
            return new Element.TokenParsing(new Element.Keyword(value));
        }
        if (token.matches("[,=+\\-*/;&|?:!]+")) {
            return new Element.TokenParsing(new Element.Symbol(value));
        }
        if (token.matches("-?\\d+(\\.\\d+)?")) {
            return new Element.TokenParsing(new Element.Number(value));
        }
        return new Element.TokenParsing(new Element.Other(value));
    }
}
