package net.biff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solver {
    public List<String> words = new ArrayList<>();
    private List<String> alphabet = new ArrayList<>();
    private short[] duplicates = new short[26];
    private short[] scores = new short[78];
    public Solver() throws IOException, URISyntaxException {
        var url = new URI("https://gist.githubusercontent.com/cfreshman/a03ef2cba789d8cf00c08f767e0fad7b/raw/c46f451920d5cf6326d550fb2d6abb1642717852/wordle-answers-alphabetical.txt").toURL();
        var http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("GET");
        http.setConnectTimeout(5000);
        http.setReadTimeout(5000);
        if (http.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed to access the internet (HTTP error code: " + http.getResponseCode() + ").");
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line);
            }
        }
        for (char c = 'a'; c <= 'z'; c++){
            alphabet.add(String.valueOf(c));
        }
        for (int i = 0; i < 78; i++){
            scores[i] = 0;
        }
        for (String word : words){
            getWordCount(word);
        }
        for (int i = 0; i <26; i++){
            System.out.println(alphabet.get(i) +" "+scores[i]);
        }
        resetDuplicates();
        System.out.println("\n2------------\n");
        List<Character> multiples = multiplicates(2);
        for (char c : multiples){
            alphabet.add(String.valueOf(c)+ c);
            scores[alphabet.size()-1] = duplicates[multiples.indexOf(c)];
            System.out.println(c+" "+duplicates[multiples.indexOf(c)]);
        }
        System.out.println("\n3------------\n");
        resetDuplicates();
        multiples = multiplicates(3);
        for (char c : multiples){
            alphabet.add(String.valueOf(c)+ c);
            scores[alphabet.size()-1] = duplicates[multiples.indexOf(c)];
            System.out.println(c+" "+duplicates[multiples.indexOf(c)]);
        }
    }
    private void getWordCount(String word){
        for (char letter : word.toCharArray()){
            scores[alphabet.indexOf(String.valueOf(letter))]++;
        }
    }
    @Deprecated(since = "Pre-Fruma")
    private List<Character> duplicates(){
        List<Character> repeated = new ArrayList<>();
        for (String word: words){
            List<Character> through = new ArrayList<>();
            for (char c : word.toCharArray()){
                if (!through.contains(c)){
                    through.add(c);
                } else if (!repeated.contains(c)){
                    repeated.add(c);
                }
            }
        }
        return repeated;
    }
    private void resetDuplicates(){
        for (int i = 0; i<26; i++){duplicates[i] = 0;}
    }
    private List<Character> multiplicates(int count){
        List<Character> repeated = new ArrayList<>();
        for (String word: words){
            for (char c : word.toCharArray()){
                Pattern letter = Pattern.compile(String.valueOf(c));
                Matcher matcher = letter.matcher(word);
                short reps = 0;
                while (matcher.find()) {reps++;}
                if (!(reps >= count)){continue;}
                if (!repeated.contains(c)){repeated.add(c);}
                duplicates[repeated.indexOf(c)]++;
            }
        }
        return repeated;
    }
}