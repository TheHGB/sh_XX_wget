package Wget;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 * 
 * @author Ivan Ortiz Escarré
 * @author Hector García Berzosa
 *
 */
public class Wget {

	private static int nUrls = 0;
	private static String urlFile = "urls.txt";

	
	/**
	 * Main functions of the application, read the web urls in the urls file
	 * and the arguments given by the user. Then it runs the process to download
	 * all the given urls.
	 * 
	 * @param args (String[]) input arguments of the app. 
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException{
		
		boolean fFilter = false;
		boolean aFilter = false;
		boolean zipFilter = false;
		boolean gzipFilter = false;
		
		//Creates de downloads folder in case it is not created.
		File directorio=new File("downloads"); 
		directorio.mkdir(); 
		
		//Checks the input arguments
		for(String argument: args){
			if(fFilter){
				urlFile = argument;
				fFilter = false;
			}
			else{
				if(argument.equals("-f")){
					fFilter = true;
				}
				else{
					if(argument.equals("-a")){
						aFilter = true;
					}
					else{
						if(argument.equals("-z")){
							zipFilter = true;
						}
						else{
							if(argument.equals("-gz")){
								gzipFilter = true;
							}
						}
					}
				}
			}
		}
		
		//Opens the input urlFile.
		String line;
		FileReader in = null;
		try {
			in = new FileReader(urlFile);
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: input file " + urlFile + " not found!");
			return;
		}
		
	    BufferedReader br = new BufferedReader(in);
	    
	    //Counts the number of threads we need.
	    nUrls = countLines(urlFile);
	    
	    //Creates and run all the threads.
	    ConnectToUrl[] threads = new ConnectToUrl[nUrls];
	    for(int i = 0; i < nUrls; i++){
	    	line = br.readLine();
	    	threads[i] = new ConnectToUrl(line, aFilter, zipFilter, gzipFilter);
	    	threads[i].start();
	    }
	    
		br.close();
	}
	
	/**
	 * Counts the number of lines of the input file.
	 * 
	 * @throws IOException 
	 * @param file (String) path of the file.
	 * @return nLines (int) number of lines of the input file.
	 */
	private static int countLines(String file) throws IOException{
		
		int nLines = 0;
		BufferedReader br = new BufferedReader(new FileReader(file));
		try {
			while ((br.readLine()) != null) {
			    nLines++;
			}
		} catch (IOException e) {
			System.out.println("ERROR: IOException counting lines!");
		}
		br.close();
		return nLines;
	}
}