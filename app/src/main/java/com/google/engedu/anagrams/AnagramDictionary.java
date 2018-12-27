/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();

    private int wordLength = DEFAULT_WORD_LENGTH;

    private ArrayList<String> wordList = new ArrayList<>();
    private HashSet<String> wordSet = new HashSet<>();
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<>();
    private HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<>();

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;

        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            String sortedWord = sortLetters(word);
            wordSet.add(sortedWord);

            if (!lettersToWord.containsKey(sortedWord)) {
                lettersToWord.put(sortedWord, new ArrayList<String>());
            }
            lettersToWord.get(sortedWord).add(word);

            int wordLen = word.length();
            if (!sizeToWords.containsKey(wordLen)) {
                sizeToWords.put(wordLen, new ArrayList<String>());
            }
            sizeToWords.get(wordLen).add(word);

        }
    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(sortLetters(word)) && !word.contains(base);
    }

    public List<String> getAnagrams(String targetWord) {
        return lettersToWord.get(sortLetters(targetWord));
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < 26; i++) {
            String temp = sortLetters(word + ((char) ('a' + i)));
            if (wordSet.contains(temp)) {
                for (int j = 0; j < lettersToWord.get(temp).size(); j++) {
                    if (!lettersToWord.get(temp).contains(word)) {
                        result.add(lettersToWord.get(temp).get(j));
                    }
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        while (true) {
            ArrayList<String> temp = sizeToWords.get(wordLength);

            int rand = random.nextInt(temp.size());

            String randomWord = temp.get(rand);

            if (getAnagramsWithOneMoreLetter(randomWord).size() > MIN_NUM_ANAGRAMS) {
                if (wordLength < MAX_WORD_LENGTH) {
                    wordLength++;
                }
                return randomWord;
            }
        }
    }

    public String sortLetters(String word) {
        char[] wordToChars = word.toCharArray();
        Arrays.sort(wordToChars);
        return new String(wordToChars);
    }
}
