package Wget;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Overrides the input stream read to eliminate commentary and commands between.
 * 
 * @author Ivan Ortiz Escarré
 * @author Hector García Berzosa
 *
 */
public class Html2AsciiInputStream extends FilterInputStream{


    protected Html2AsciiInputStream(InputStream in) {
        super(in);

    }

    /**
    * Eliminates everything that is written between the symbols.
    *
    * @return used (int) Filtered stream
    */
    
    @Override
    public int read() throws IOException {
    	int used = in.read();
    	int erased;
    	int count=0;
        while(used == '<'){
        	count ++;
        	while(count > 0){
        		erased = in.read();
	            while(erased != '>'){
	            	erased = in.read();
	            	if(erased == '<') count ++;
	            }
	            used = in.read();
	            count --;
        	}
        }
        return used;
    }

}