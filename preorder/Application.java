import java.util.ArrayList;
/**
 *
 * @author daniel
 */
public class Application
{
    private String[] anagrams;
    private AnagramGenerator generator;

    public Application(String word)
    {
        generator = new AnagramGenerator(word);
    }

    public void generateAnagrams()
    {
        generator.generate();
        anagrams = generator.getResult();
        //sort(anagrams);
    }

    public String[] containsChar(char letter)
    {
        char tempChar = Character.toLowerCase(letter);
        ArrayList<String> result = new ArrayList<String>();

        for(int i = 0; i < anagrams.length; i++)
        {
            String temp = anagrams[i];

            for(int j = 0; j < temp.length(); j++)
            {
                if(temp.charAt(j) == tempChar)
                    result.add(temp);

            }
        }

        String[] out = new String[result.size()];

        if (out.length == 0)
            result.add("There were no dictionary anagrams found contaning with "+tempChar );

        return result.toArray(out);
    }

    public String[] beginsWithChar(char letter)
    {
        char tempChar = Character.toLowerCase(letter);
        ArrayList<String> result = new ArrayList<String>();

        for(int i = 0; i < anagrams.length; i++)
        {
            String temp = anagrams[i];

            if(temp.charAt(0) == tempChar)
                result.add(temp);
        }

        String[] out = new String[result.size()];

        if (out.length == 0)
            result.add("There were no dictionary anagrams found begining with "+tempChar );

        return result.toArray(out);

    }

    public String[] endWithChar(char letter)
    {
        char tempChar = Character.toLowerCase(letter);
        ArrayList<String> result = new ArrayList<String>();

        for(int i = 0; i < anagrams.length; i++)
        {
            String temp = anagrams[i];

            if(temp.charAt(temp.length()-1) == tempChar)
                result.add(temp);
        }

        String[] out = new String[result.size()];

        if (out.length == 0)
            result.add("There were no dictionary anagrams found ending with "+tempChar );

        return result.toArray(out);
    }

    public String[] getAnagrams()
    {
        if (anagrams.length == 0)
            anagrams = new String[]{"There were no dictionary anagrams found"};
        return anagrams;
    }

    private void sort(String[] array)
    {
        for(int i = 1; i < array.length; i++)
        {
            int j = i;

            while(j >= 1 && array[j].compareTo(array[j-1]) < 0)
            {
                String temp = array[j-1];
                array[j-1] = array[j];
                array[j] = temp;
                --j;
            }
        }
    }
}
