public class LeafManager {

    private Leaf[] leafArr = new Leaf[5];

    public LeafManager(int amount) {
        leafArr = new Leaf[amount];
    }

    public void leafAdd(Leaf leaf) {

        leafArr[leaf.getId()] = leaf;

    }

    public Leaf leafGet(int x) {
        
        return leafArr[x];

    }

    public void reorganize(int x) {
        for (int i = x; i < leafArr.length; i++) {
            
            if (i < leafArr.length-1) {
                leafArr[i] = leafArr[i+1];
            }
        }
    }
}
