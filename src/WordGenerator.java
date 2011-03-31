
import java.util.ArrayList;

/**
 * This is a class to generate anagrams from a given string of characters.
 *
 */

public class WordGenerator
{
    //the set of words that are the dictionary words
    private ArrayList<String> wordlist = new ArrayList<String>();
    //the scanner needed to read the file containind the dictionary
    private java.util.Scanner scanner;
    //the hashtable to store the dictionary words
    private LexDict dictionary;
    //the language for the dictionary
    private String language;

    //an array of characters that are given as input
    //it can only contain alphabet alphabet and a '?',
    //no punctuation is allowed
    private char[] alphabet;

    private String source;


    /**
     * This constructor converts the given alphabet to a char array and creates
     * the dictionary.
     *
     * @require A string of alphabet
     * @ensure Will create a new generater to generate words from the given
     *          alphabet.
     * @param String
     */
    public WordGenerator(String letters, String language, LexDict dictionary)
    {
        //TODO use the parameter for the language to select the right txt file

        //gets the language for the dicitionary
        this.language = language;

        this.source = letters.toUpperCase();
        this.dictionary = dictionary;
        initLanguage();
        //Create dicitonary to compare genertaed words with
        //createDict();
    }

    /**
     * Will start the process of generating the the anagrams
     *
     * @ensure Will generate the words based on the given alphabet
     */
    public void generate()
    {
        //initalize the wild flag to false
        boolean containsWild = false;

        //check the array for the wildcard
        if(arrayContains(source.toCharArray(),'?') != -1)
            containsWild = true;

        if(!containsWild)
            generateNormal();
        else
            generateWild();
    }

    /**
     * Convert the set of anagrams to an array of Strings.
     *
     * @ensure Will return the generated dictionary words as an array
     * @return String[] An array containg the generated dictionary words
     */
    public String[] getResult()
    {
        String result[] = new String[wordlist.size()];
        
        for(int i = 0; i < wordlist.size(); i++)
            result[i] = wordlist.get(i); 
        //create the array with the size of the set of words
        return result;
    }

    private void generateNormal()
    {
        /*
         * return -1 if word is not found and is not a prefix
         * return 0 if word is not found but is a prefix
         * return 1 if word is found and a prefix
         * return 2 if word is found and not a prefix
         */
        
        
        GenThread[] threads = new GenThread[source.length()];
        
        for(int i = 0; i < threads.length;i++)
        {
            String anchor  = ""+source.charAt(i);
            String remaining = removeChar(source,i);
            char[] pool = remaining.toCharArray();

            threads[i] = new GenThread(anchor,pool);
            threads[i].start();
        }

        for (int i=0; i < threads.length; i++)
        {
            try {
                threads[i].join();
             }
             catch (InterruptedException e) {
                System.out.print("Join interrupted\n");
             }
        }


        /*for(int i = 0; i < source.length(); i++)
        {
            String anchor  = ""+source.charAt(i);
            String remaining = removeChar(source,i);
            
            char[] pool = remaining.toCharArray();
            
            generateWords(anchor,pool);
            
        }*/
    }

    private void generateWild()
    {
        int wild = arrayContains(source.toCharArray(),'?');
        GenWildThread[] threads = new GenWildThread[alphabet.length];

        for(int i = 0; i < threads.length;i++)
        {
            char[] temp = (new String(source)).toCharArray();
            temp[wild] = alphabet[i];
            String modifiedSource = new String(temp);

            threads[i] = new GenWildThread(modifiedSource);
            threads[i].start();
        }

        for (int i=0; i < threads.length; i++)
        {
            try {
                threads[i].join();
             }
             catch (InterruptedException e) {
                System.out.print("Join interrupted\n");
             }
        }
    }

    private String removeChar(String string, int index)
    {
        StringBuilder temp = new StringBuilder();
        for(int i = 0; i < string.length();i++)
        {
            if(i != index)
                temp.append(string.charAt(i));
        }

        return temp.toString();
    }

    private void generateWords(String anchor, char[] pool)
    {
        /*
         * return -1 if word is not found and is not a prefix
         * return 0 if word is not found but is a prefix
         * return 1 if word is found and a prefix
         * return 2 if word is found and not a prefix
         */
        
        int search = dictionary.lookup(anchor);
        
        //if the anchor is a prefix
        if(search == LexDict.NOT_WORD_IS_PREFIX || search == LexDict.IS_WORD_IS_PREFIX)
        {
            //add the found word to the wordlist
            if(search == 1)
            {
                if(!wordlist.contains(anchor))
                    wordlist.add(anchor);
            }
            
                        
            for(int i = 0; i < pool.length; i++)
            {
                String newAnchor = new String(anchor);
                newAnchor = newAnchor + pool[i];
                String temp = new String(pool);
                char[] newpool = (removeChar(temp,i)).toCharArray();
                //reduce pool by one
                generateWords(newAnchor, newpool);
            }
        }
        
        //if the anchor is not a prefix
        if(search == -1 || search == 2)
        {
            //add the found word to the wordlist
            if(search == 2){
                 if(!wordlist.contains(anchor))
                     wordlist.add(anchor);
            }
                
        }
        
    }

    /**
     * Will read a file that contains words for a dictionary then create a
     * dictionary to compare the generated words to.
     *
     * @ensure Will create a dictionay from a text file.
     */
    private void createDict()
    {
       // dictionary = new LexDict(alphabet,language);

    }

    private void initLanguage()
    {
        if(language.equals("ENG"))
        {
            alphabet = (new String("ABCDEFGHIJKLMNOPQRSTUVWXYZ")).toCharArray();
        }
        if(language.equals("GER"))
        {
            alphabet = (new String("ABCDEFGHIJKLMNOPQRSTUVWXYZÜÄÖ")).toCharArray();
        }
        if(language.equals("FRE"))
        {
            alphabet = (new String("ABCDEFGHIJKLMNOPQRSTUVWXYZ")).toCharArray();
        }
    }

    /**
     * Simple method to see if the array contains a certain charcter and return
     * the index of that character. If the chracter is not found then it will
     * return -1
     *
     * @param word
     * @param item
     * @return
     */
    private int arrayContains(char[] word, char item)
    {
        //TODO: rewite as boolean return type

        //index to return
        int result = -1;

        //itertes of the array to search for the chracter
        for(int i = 0; i < word.length; i++)
        {
            //if the character is found then set the index
            if(word[i] == item)
                result = i;
        }

        return result;
    }

    private class comparator implements java.util.Comparator<String>
    {
        public comparator()
        {
            //left blank
        }
        public int compare(String first, String second)
        {
            return first.compareTo(second);
        }
    }

    private class GenWildThread extends Thread
    {
        private String source;

        public GenWildThread(String source)
        {
            this.source = source;
        }

        public void run()
        {
            GenThread[] threads = new GenThread[source.length()];

            for(int i = 0; i < threads.length;i++)
            {
                String anchor  = ""+source.charAt(i);
                String remaining = removeChar(source,i);
                char[] pool = remaining.toCharArray();

                threads[i] = new GenThread(anchor,pool);
                threads[i].start();
            }

            for (int i=0; i < threads.length; i++)
            {
                try {
                    threads[i].join();
                 }
                 catch (InterruptedException e) {
                    System.out.print("Join interrupted\n");
                 }
            }
        }
    }
    private class GenThread extends Thread
    {
        private String anchor;
        private char[] pool;

        public GenThread(String anchor, char[] pool)
        {
            this.anchor = anchor;
            this.pool = pool;
        }

        public void run()
        {            
            generateWords(anchor,pool);
        }
    }
}
