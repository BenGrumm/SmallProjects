import java.util.List;
import java.util.ArrayList;

public class platforms{

	private List<List<Integer>> platformCoords = new ArrayList<List<Integer>>();
	private int rightWall;
	private int leftWall;

	public platforms(List<List<Character>> theMap, int blockSize){
		int widthOfMap = theMap.get(0).size();
		int heightOfMap = theMap.size();
		int blocksFound = 0;
		int start;
		int end;
		//Loop through each element in the 2D array
		for(int i = 0; i < heightOfMap; i++){
			for(int p = 0; p < widthOfMap; p++){
				//If the char is X for platform or ~ for spawn point or O for end point
				if(theMap.get(i).get(p) == 'X' || theMap.get(i).get(p) == '~' || theMap.get(i).get(p) == 'O'){
					//Set the co-ordinates for the start of the platform 
					start = p * blockSize;
					//Set the end of the platform to be the same as the start
					end = start;
					//While the chars are platform pieces increase the end co-ords of the platform
					while(theMap.get(i).get(p) == 'X' || theMap.get(i).get(p) == '~' || theMap.get(i).get(p) == 'O'){
						end += blockSize;
						//Increase the loop
						p++;
					}
					//Add a new list with the start, end and y co-ords
					platformCoords.add(new ArrayList<Integer>());
					platformCoords.get(blocksFound).add(start);
					platformCoords.get(blocksFound).add(end);
					platformCoords.get(blocksFound).add(i * blockSize);
					//Increase the amount of platforms found
					blocksFound++;
				}
			}
		}
		int i = 0;
		//Look for the end of the left wall
		while(theMap.get(0).get(i) == '*'){
			i++;
		}
		//Set left wall to i
		leftWall = i;
		//Continue untill the right wall is found
		while(theMap.get(0).get(i) != '*'){
			i++;
		}
		//Set the right wall to be one less than found
		rightWall = i-1;
	}

	public List<List<Integer>> getPlatforms(){
		return platformCoords;
	}

	public int getLeftWall(){
		return leftWall;
	}

	public int getRightWall(){
		return rightWall;
	}

}