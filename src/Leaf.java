public class Leaf{
    
    String type = "";
    int id;
    boolean isNormal = true;
    
    public Leaf(int identification, int level) {
       
        id = identification;
        int random = (int) (Math.random()*100 + 0.5);

        if (level > 0) {
            if (random < 85) {
                type = "default";
            } else if (random >= 85) {
                type = "slowDown";
            } 
        }

    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public void setId(int newId) {
        id = newId;
    }

}
