package com.conference.backend.data.utils.base;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public interface SpellChecker {
    /**
     * The guessed word
     * @param word The word given
     * @return the guessed word; otherwise, return {@code word}
     */
    String corrections(String word);
}
