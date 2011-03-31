import hashtable.HashTable;
import linkedset.OrderedLinkedSet;
import linkedset.AbstractOrderedSet;
import nhUtilities.containers2.List;
import nhUtilities.containers2.Iterator;
import nhUtilities.containers2.LinkedList;

/**
 * This is a class to generate anagrams from a given string of characters.
 *
 */

public class AnagramGenerator 
{
    //the set of anagrams that are dictionary words
    private AbstractOrderedSet<String> results = new OrderedLinkedSet<String>(new comparator());
    //the scanner neede to read the file
    private java.util.Scanner scanner;
    //the hashtable to store the values
    private HashTable<String,String> dictionary;
    //an array of characters that are given as input
    private char[] source;


    /**
     * This cunstructor converts the given string to a char array adn creates
     * the dictionary.
     *
     * @param string
     */
    public AnagramGenerator(String string)
    {
        //Create dicitonary to comapre anagrams to
        createDict();
        //convert to lower case
        string = string.toLowerCase();
        //change the string into an array of characters
        source = string.toCharArray();
    }

    /**
     * Will start the process of generating the the anagrams
     */
    public void generate()
    {
        //set the wild flag to false
        boolean containsWild = false;

        //check the array for the wildcard
        if(arrayContains(source,'?') != -1)
            containsWild = true;

        if(!containsWild)
            permuteNormal(source);
        else
            permuteWild(source);
    }

    /**
     * Convert the set of anagrams to an array of Strings.
     * @return
     */
    public String[] getResult()
    {
        int size = results.size();

        String[] result = new String[size];
        Iterator<String> iter = results.iterator();
        
        int i = 0;
        while(!iter.done())
        {
            result[i] = iter.get();
            iter.advance();
            i++;
        }

        return result;
    }

    /**
     * This method does basic permutaion of a given array. It uses only the
     * indces of the array to do the permutaion.
     * @param word
     */
    private void permute(char[] word)
    {
        int[] indices;
        PermutationGenerator x = new PermutationGenerator (word.length);
        StringBuffer permutation;

        while (x.hasMore ())
        {
            permutation = new StringBuffer();
            indices = x.getNext ();
            for (int i = 0; i < indices.length; i++)
            {
                    permutation.append (word[indices[i]]);
            }

            //if the anagram is a dictionary word then add it to the result set
            if(dictionary.get(permutation.toString()) != null)
            {
                results.add(permutation.toString());
            }

        }
    }

    /**
     * Will start creating the process of creating permutations and the
     * decresing permutations.
     * @param word
     */
    private void permuteNormal(char[] word)
    {
        //do the permutation of all the letters given
        permute(word);

        //remove one letter then do the decreasing permutaion of the remaining
        //letters, then continue the proces still only one letter remains.
        for(int i = word.length-1; i >= 0; i--)
        {
            //word = removeElement(word,i);

            char[] temp = (new String(word)).toCharArray();
            decreasingPermutation(temp);
            word = removeElement(word,i);
        }
    }

    /**
     * Will substitue the wild card spot and create the different permutations
     * that arise form the varience of the wild card.
     * @param word
     */
    private void permuteWild(char[] word)
    {
        //copies the original array to a local temporary one
        char[] original = (new String(word)).toCharArray();

        //find the index of the wild card
        int indexOfWild = arrayContains(original,'?');

        //replace the wild card with each letter in the alphabet
        for(char i = 'a'; i <= 'z';i++)
        {
            //create a new array to alter
            char[] altered = (new String(original)).toCharArray();
            //replace the wild card
            altered[indexOfWild] = i;
            //permute as normal
            permuteNormal(altered);
        }

    }

    /**
     * Create the decreasing permutation by removing a letter then calculating
     * the permutaions of the remaining array, until array.length == 1
     * @param word
     */
    private void decreasingPermutation(char[] word)
    {
        //will remove a letter then permute the smaller array until there is
        //just one letter in the array
        for(int i = word.length-1; i >= 0; i--)
        {
            //permute as normal
            permute(word);
            //will remove the letter then recursivly call the function
            decreasingPermutation(removeElement(word,i));
        }
    }

    /**
     * Will remove a element formt he array and return the altered array
     * @param array
     * @param index
     * @return
     */
    private char[] removeElement(char[] array, int index)
    {
        //swap the elemnt to be removed with the elment at the beginning
        char temp = array[index];
        array[index] = array[0];
        array[0] = temp;

        //create a new substring that excludes for the first element
        String substring = new String(array,1,array.length-1);

        //return the substring as an array
        return substring.toCharArray();
    }

    /**
     * Will read a file that contains words for a dictionary then create a
     * dictionary to comare the anagrams to.
     */
    private void createDict()
    {
        try
        {
            //using Scanner to read the file
            scanner = new java.util.Scanner(new java.io.File("dict.txt"));
        }
        catch(java.io.FileNotFoundException e)
        {
            //if ther is an error reading the file, notify then exit
            System.out.println("File not found or cannot be opened");
            System.exit(1);
        }

        //new linked list to hold the counted strings
        List<String> list = new LinkedList<String>();

        //will traverse the file and add the strings found in the file till
        //the end of the file is reached
        while(scanner.hasNext())
        {
            String temp = scanner.next();//get the next string
            list.add(0,temp);//add to beginng to make the counting linear
        }//end while

        //created the dictionary based on the size of the list of strings
        dictionary = new HashTable<String,String>(list.size());

        //create iterator to travers the list of strings
        Iterator<String> iter = list.iterator();

        //will traverse the list and add each string to the hash table
        while(!iter.done())
        {
            //add the strings to the hashtable and use the string for
            //both the key and value
            dictionary.add(iter.get(), iter.get());

            iter.advance();
        }//end while

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
}
