package Wget;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Extends the Thread class to connect and download from an url his content.
 * 
 * @author Ivan Ortiz Escarré
 * @author Hector García Berzosa
 *
 */
public class ConnectToUrl extends Thread{
	
	private static int index = 0;
	private String urlToConnect;
	private boolean aFilter;
	private boolean zipFilter;
	private boolean gzipFilter;
	
	/**
	 * Constructor from class ConnectToUrl, initialize the basic information needed from the url download
	 * process. Including the url, the ascii filter, the zip filter and the gzip filter.
	 * 
	 * @param urlToConnect (String) 
	 * @param aFilter (boolean)
	 * @param zipFilter (boolean)
	 * @param gzipFilter (boolean)
	 */
	public ConnectToUrl(String urlToConnect, boolean aFilter, boolean zipFilter, boolean gzipFilter){
		this.urlToConnect = urlToConnect;
		this.aFilter = aFilter;
		this.zipFilter = zipFilter;
		this.gzipFilter = gzipFilter;
	}
	
	/**
	 * Overrides the run process from thread to use call the connectToUrl function
	 */
	
	@Override
	public void run(){
		try {
			connectToURL();
		} catch (Exception e) {
			System.out.println("ERROR: downloading url " + this.urlToConnect + "!");
		}
	}

	/**
	 * Connects and downloads applying the filters the url given.
	 * 
	 * @throws Exception
	 */
	private void connectToURL() throws Exception  {
	
		URL url = new URL(this.urlToConnect);
		//Obtains the url file name (example: index.html, hello.img...)
		Path p = Paths.get(url.getPath());
		String fileName = indexFileName(p.getFileName().toString());
		
		InputStream is = null;
		try { //Trying if the url gives a stream.
			is = url.openStream();
		} catch (IOException e) {
			System.out.println("ERROR: Receiving the stream from " + this.urlToConnect + "!"); //Error message.
			return;
		}
	
		//ASCII FILTER
		if (this.aFilter && (getExtension(fileName)).equals("html") || getExtension(fileName).equals("txt")){
			is = new Html2AsciiInputStream(is);
			fileName = fileName + ".asc";
		}
				
		String fileDir = fileName;
		if (this.zipFilter){
			fileDir = fileDir + ".zip";
		}
				
		if (this.gzipFilter){	
			fileDir = fileDir + ".gz";
		}
				
		//Writing process to a file starts.
		OutputStream outputFile = new FileOutputStream(new File("downloads/" + fileDir));
		
		if(this.gzipFilter){
			outputFile = new GZIPOutputStream(outputFile);
		}
		
		if(this.zipFilter){
			ZipEntry ze = new ZipEntry(fileName);
			outputFile = new ZipOutputStream(outputFile);
			((ZipOutputStream) outputFile).putNextEntry(ze);
		}
		
		int i;
		while ((i = is.read()) != -1){
			outputFile.write(i); //The file is written byte per byte.
		}
		
		outputFile.close();
		//Writing process to a file finishes. 
		
		System.out.println(this.urlToConnect + " downloaded correctly!"); //Information message. 
	}
	

	/**
	 * In case the filename is known the index number is just added.
	 * In case the filename isn't known we download the indexI.html with the index 
	 * number I.
	 *
	 * @param fileName (String) initial name of the file.
	 * @return fileName (String) indexed name of the file.
	 */
	private String indexFileName(String fileName){

		String fileAux = fileName;
		int pos = fileAux.lastIndexOf(".");
		
		//Beginning of name changing process. (Example index.html --> index1.html, photo.img --> photo2.img)
		if (fileName.isEmpty() || pos == -1){ //In case the filename isn't known we download the indexI.html with the index number I.
			fileName = "index" + index++ + ".html";
		}
		else //In case the filename is known the index number is just added.
		{
			fileName = fileAux.substring(0, pos) + index++ + fileAux.substring(pos, fileAux.length());
		}
		//End of name changing process.
		
		return fileName;
	}

	/**
	 * In case the filename is known the index number is just added.
	 * In case the filename isn't known we download the indexI.html with the index 
	 * number I.
	 *
	 * @param fileName (String) initial name of the file.
	 * @return fileName (String) indexed name of the file.
	 */
	private String getExtension(String fileName){
		return fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());
	}
}