import java.util.ArrayList;
/**
 *
 * @author Daniel Ward
 */
public class Application
{
    //holds the list of generated words
    private String[] words;
    //new WordGenerator objext
    private WordGenerator generator;

    /**
     * Constructor creates the WordGenerator
     *
     * @ensure Will create a new WordGenerator
     *
     * @param letters A String of letters
     */
    public Application(String letters,String language, LexDict dictionary)
    {
        generator = new WordGenerator(letters,language,dictionary);
    }

    /**
     * 
     */
    public void generateAnagrams()
    {
        generator.generate();
        words = generator.getResult();
        //sort(words);
    }

    public String[] contains(String string)
    {
        String tempString = string.toUpperCase();

        ArrayList<String> result = new ArrayList<String>();

        for(int i = 0; i < words.length; i++)
        {
            String tempWord = words[i];

            boolean contain = true;

            for(int j = 0; j < tempString.length() && contain; j++)
            {
                if(tempWord.indexOf(tempString.charAt(j)) == -1)
                   contain = false;

            } 
             
            if(contain)
                result.add(tempWord);
        }

        String[] out = new String[result.size()];

        if (out.length == 0)
            result.add("There were no dictionary anagrams found contaning with "+tempString);

        return result.toArray(out);
    }

    public String[] beginsWith(String string)
    {
        String tempString = string.toUpperCase();

        ArrayList<String> result = new ArrayList<String>();

        for(int i = 0; i < words.length; i++)
        {
            String temp = words[i];

            if(temp.startsWith(tempString))
                result.add(temp);
        }

        String[] out = new String[result.size()];

        if (out.length == 0)
            result.add("There were no dictionary anagrams found begining with "+tempString );

        return result.toArray(out);

    }

    public String[] endsWith(String string)
    {
        String tempString = string.toUpperCase();
        ArrayList<String> result = new ArrayList<String>();

        for(int i = 0; i < words.length; i++)
        {
            String temp = words[i];

            if(temp.endsWith(tempString))
                result.add(temp);
        }

        String[] out = new String[result.size()];

        if (out.length == 0)
            result.add("There were no dictionary anagrams found ending with "+tempString );

        return result.toArray(out);
    }

    public String[] getAnagrams()
    {
        if (words.length == 0)
            words = new String[]{"There were no dictionary anagrams found"};
        return words;
    }
}
