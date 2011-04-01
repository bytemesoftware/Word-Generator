/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author daniel
 */
public class Dictionary {
    
    public static final int NOT_WORD_NOT_PREFIX = -1;
    public static final int NOT_WORD_IS_PREFIX = 0;
    public static final int IS_WORD_NOT_PREFIX = 2;
    public static final int IS_WORD_IS_PREFIX = 1;
    

    private DictionaryTree dictionary;
    private char[] alphabet;
    private java.io.File file;
    private String language;

    /**
     *
     * @param file The path to the file containing the word list
     * @param letters An array containing the letters in the alphabet
     */
    public Dictionary(String language)
    {
        this.language = language;
        dictionary = new DictionaryTree(false);
        selectDictionary(language);
        initLanguage();
        readFile();
    }    

    /**
     * 
     * @param word
     * @return
     */
    public int lookup(String word)
    {
        int result = Dictionary.NOT_WORD_NOT_PREFIX;

        DictionaryTree tree = dictionary.getTree(word);
        
        if(tree.isWord() && tree.hasChildren())
            result = Dictionary.IS_WORD_IS_PREFIX;
        
        else if(tree.isWord() && !tree.hasChildren())
            result = Dictionary.IS_WORD_NOT_PREFIX;
        
        else if(!tree.isWord() && !tree.hasChildren())
            result = Dictionary.NOT_WORD_NOT_PREFIX;
        
        else
            result = Dictionary.NOT_WORD_IS_PREFIX;
        
        return result;
    }

    private void readFile()
    {
        try{
            java.util.Scanner scanner = new java.util.Scanner(file);

            while(scanner.hasNext()){
                String temp = scanner.next();
                temp = temp.toUpperCase();
                dictionary.add(temp);
            }
        }
        catch(java.io.FileNotFoundException e){
            System.out.println("Could not open file");
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
    
    private void selectDictionary(String filepath)
    {
        if(filepath.equals("ENG"))
        {
            file = new java.io.File("eng_dict.txt");
        }
        if(filepath.equals("GER"))
        {
            file = new java.io.File("ger_dict.txt");
        }
        if(filepath.equals("FRE"))
        {
            file = new java.io.File("fre_dict.txt");
        }
    }
}
