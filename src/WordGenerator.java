
import java.util.ArrayList;

/**
 * This is a class to generate anagrams from a given string of characters.
 *
 */

public class WordGenerator
{
    //the set of words that are the dictionary words
    private ArrayList<String> wordlist = new ArrayList<String>();
    //the hashtable to store the dictionary words
    private Dictionary dictionary;
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
    public WordGenerator(String letters, String language, Dictionary dictionary)
    {
        //TODO use the parameter for the language to select the right txt file

        //gets the language for the dicitionary
        this.language = language;

        this.source = letters.toUpperCase();
        this.dictionary = dictionary;
        initLanguage();
    }

    /**
     * Will start the process of generating the the anagrams
     *
     * @ensure Will generate the words based on the given alphabet
     */
    public void generate()
    {
        if(source.indexOf("?") == -1)
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
        //create the array with the size of the set of words
        String result[] = new String[wordlist.size()];
        
        for(int i = 0; i < wordlist.size(); i++)
            result[i] = wordlist.get(i);         
        
        return result;
    }

    private void generateNormal()
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

    private void generateWild()
    {
        int wild = source.indexOf("?");
        int wild2 = source.lastIndexOf("?");
        GenWildThread[] threads = new GenWildThread[alphabet.length];
         
        //two wilds
        if((wild2 != wild) && (wild != -1)){
            
            threads = new GenWildThread[alphabet.length * alphabet.length];
            
            System.out.println("found two ");
            int count = 0;
            for(int i = 0; i < alphabet.length; i++){
                
                for(int j = 0; j < alphabet.length; j++){
                   
                    char[] temp = (new String(source)).toCharArray();
                    temp[wild] = alphabet[i];
                    temp[wild2] = alphabet[j];
                    String modifiedSource = new String(temp);

                    threads[count] = new GenWildThread(modifiedSource);
                    threads[count].start();
                    count++;
                }              
                
            }
            
            for (int i = 0; i < count; i++)
            {
                try {
                    threads[i].join();
                 }
                 catch (InterruptedException e) {                     
                    System.out.print("Join interrupted\n");
                 }
            }
        }
        else{
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

        
        
    }
    
    private synchronized void addWord(String word){
        if(!wordlist.contains(word))
            wordlist.add(word);
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
        int search = dictionary.lookup(anchor);
        
        //if the anchor is a prefix
        if(search == Dictionary.NOT_WORD_IS_PREFIX || search == Dictionary.IS_WORD_IS_PREFIX)
        {
            //add the found word to the wordlist
            if(search == Dictionary.IS_WORD_IS_PREFIX)
            {
                addWord(anchor);
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
        else
        {
            //add the found word to the wordlist
            if(search == Dictionary.IS_WORD_NOT_PREFIX){
                 addWord(anchor);
            }
        }        
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
