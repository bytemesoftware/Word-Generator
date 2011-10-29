import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author daniel
 */
public class DictionaryTree {

    private boolean word;
    private DictionaryTree children[];
    
    public DictionaryTree(boolean isword){
        word = false; 
        children = new DictionaryTree[26];
    }
    
    public void add(String word){
        //System.out.println(word);
        if(word.length() > 0){
            int index = indexOf(word.charAt(0));
            
            if(children[index] == null)
                children[index] = new DictionaryTree(false);  
            
            if(word.length() == 1)
                children[index].setWord(true);
            
            if(word.length() > 1)
                children[index].add(word.substring(1));
           
        }
            
    }//end add
    
    public DictionaryTree getTree(String word){
        DictionaryTree result;
        int index = indexOf(word.charAt(0));
                  
        if(word.length() > 1){
            if(children[index] != null){
                result = children[index].getTree(word.substring(1));
            }
            else
                result = new DictionaryTree(false);
        }
        else{
            if(children[index] != null){
                result = children[index];
            }
            else
                result = new DictionaryTree(false);
        }
                
        
        return result;
    }//get getTree
    
    public boolean hasChildren()
    {
        boolean result = false;
        for(int i = 0; i < children.length && !result; i++){
            if(children[i] != null)
                result = true;
        }
        return result;
    }//end getChildren 
    
    public boolean isWord(){
        return word;
    }
    
    public void setWord(boolean value)
    {
        word = value;
    }
    
    private int indexOf(char letter){
        return letter - 'A';
    }
   
    
}//end LexTree
