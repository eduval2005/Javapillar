/*A feisty little caterpillar! */
class Caterpillar
{
    private int[][] position;       //position array, holding (x,y) for head and each body segment
    private int segmentCount;       //caterpillar segmentCount
    private int segDistance;        //distance between segments
    private int stepSize = 30;      //size of each step
    private int bearing;


    //Initialize everything to the given parameters and draw the sprite
    Caterpillar(int x, int y, int sd, int sc, int b){

        int head_x = x;
        int head_y = y;
        segmentCount = sc;
        segDistance = sd;
        bearing = b;

        initializeArray(head_x, head_y);
    }

    private void initializeArray(int head_x, int head_y){
        //Filling the position array
        //The head is at the given x,y and each segment is positioned behind the head
        //in the direction opposite to the bearing
        position = new int[segmentCount][2];
        position[0][0] = head_x;
        position[0][1] = head_y;

        for (int index = 1; index < segmentCount; index++){
            if (bearing == 0){ //Heading NORTH!
                position[index] = new int[]{head_x, head_y + (index*segDistance)};
            }
            else if (bearing == 1){ //Heading EAST!
                position[index] = new int[]{head_x - (index*segDistance), head_y};
            }
            else if (bearing == 2){ //Heading SOUTH!
                position[index] = new int[]{head_x, head_y - (index*segDistance)};
            }
            else if (bearing == 3){ //Heading WEST!
                position[index] = new int[]{head_x + (index*segDistance), head_y};
            }
        }
    }


    public void slither(){ //move along in the direction specified by the bearing

        int[][] new_pos = new int[segmentCount][2];

        switch(bearing){
            case 0: //Heading NORTH!
                new_pos[0][0] = position[0][0];
                new_pos[0][1] = position[0][1] - stepSize;
                break;

            case 1: //Heading EAST!
                new_pos[0][0] = position[0][0] + stepSize;
                new_pos[0][1] = position[0][1];
                break;

            case 2: //Heading SOUTH!
                new_pos[0][0] = position[0][0];
                new_pos[0][1] = position[0][1] + stepSize;
                break;

            case 3: //Heading WEST!
                new_pos[0][0] = position[0][0] - stepSize;
                new_pos[0][1] = position[0][1];
                break;
        }

        for (int index = 1; index < segmentCount; index++){ //move each segment up
            new_pos[index] = position[index-1];
        }
        //if (new_pos[getLength()-1] == new_pos[getLength()-2]){
        //    new_pos[getLength()-1] = position[getLength()-1];
        //}
        position = new_pos;
    }

    public int getLength(){
        return segmentCount;
    }

    public int[][] getPos(){
        return position;
    }

    public int getBearing(){
        return bearing;
    }

    public void setBearing(int br){
        bearing = br;
    }

    public void grow(int[] tailLoc){
        segmentCount++;
        int[][] newArray = new int[segmentCount][2];
        for (int x = 0; x < segmentCount-1; x++){
            newArray[x] = position[x];
        }
        newArray[segmentCount-1] = tailLoc;
        position = newArray;
    }
    

}
