import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class loadFileToLevel{

	//Need a 2D list for each char
	private List<List<Character>> levelList = new ArrayList<List<Character>>();
	private int count = 0;
	private char[] charsInLine;

	public static void main(String[] args){
		loadFileToLevel main = new loadFileToLevel();
	}

	public List<List<Character>> getArrayOfArray(){
		try{
			createArray();
		}catch(IOException e){
			System.out.println(e);
		}
		return levelList;
	}

	public void createArray() throws IOException{
		//Get file location and create instance of the file "converts given pathname string into an abstract pathname"
		File file = new File("level.txt");
		//Create BufferedReader with FileReader
		BufferedReader br = new BufferedReader(new FileReader(file));
		//Initialize empty string
		String line = null;

		//While the br.readLine() does not return null
		while( (line = br.readLine())!= null ){
			//Get a char array from the line
			charsInLine = line.toCharArray();
			//Add the array to array of arrays
			levelList.add(new ArrayList<Character>());
			//Loop through the chars in charsInLine
			for(char c : charsInLine){
				//Add the char to the list
				levelList.get(count).add(c);
			}
			count++;
		}
	}

}