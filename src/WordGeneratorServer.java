// $Id$

import java.io.*;
import java.net.*;

public class WordGeneratorServer implements Runnable
{
    private int port;
    private LexDict dictionary;

    protected WordGeneratorServer( int port )
    {
        this.dictionary = new LexDict("ENG");
        this.port = port;
        new Thread( this ).start();
    }//end WordGeneratorServer constructor

    public class Handler implements Runnable
    {
        private Socket socket;

        public Handler( Socket socket )
        {
            this.socket = socket;
            new Thread( this ).start();
        }//end handler constructor

        public void run()
        {
            try
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
                String input = in.readLine();
                //long start = System.nanoTime();
                //System.out.println("start "+start);
                String result = createAnagram(input);
                //long end = System.nanoTime();;
                //System.out.println("end "+end);
                //long total = end-start;
                //System.out.println("total "+total);
                out.println(result);
                in.close();
                out.close();
            }//end try
            catch( EOFException ee )
            {
                System.out.println( "Disconnected "+socket );
            }//end catch
            catch( IOException ie )
            {
                ie.printStackTrace();
            }//end catch
            
            finally
            {
                try
                {
                    socket.close();
                }//end try
                catch( IOException ie ) {}
            }//end finally
        }//end run
      }//end Handler Class

    public void run()
    {
        try
        {
            ServerSocket serversocket = new ServerSocket( port );
            while (true)
            {
                // Accept a connection.
                System.out.println( "Listening on "+serversocket );
                Socket clientsocket = serversocket.accept();
                System.out.println( "Connected "+clientsocket );

                // Process the connection.
                new Handler( clientsocket );
            }//end while
        }//end while
        catch( IOException ie )
        {
          ie.printStackTrace();
        }//end catch
    }//end run

    public String createAnagram( String formatedString )
    {
        //TODO will need to get the lagauge for the dictionary
        String[] input = formatedString.split(",");
        
        Application generator = new Application(input[0],"ENG",WordGeneratorServer.this.dictionary);

        generator.generateAnagrams();

        String[] result = {};

        String out = "";

         for(int i = 0; i < 4; i++)
            System.out.println(input[i]);
        
        if(!input[1].equals("."))
        {

            result = generator.endsWith(input[1]);
        }
        else if(!input[2].equals("."))
        {
            result = generator.beginsWith(input[2]);
        }
        else if(!input[3].equals("."))
        {
            result = generator.contains(input[3]);
        }
        else
        {
            result = generator.getAnagrams();
        }

        for(int i = 0; i < result.length; i++)
            out += result[i] + ",";

       
        return out;
    }    

    static public void main( String args[] ) throws Exception 
    {
        new WordGeneratorServer( 4000 );
    }
}