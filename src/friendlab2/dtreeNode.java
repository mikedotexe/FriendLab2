/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package friendlab2;

/**
 *
 * @author Mike
 */
public class dtreeNode {
    public int dID;
    public dtreeNode ai;
    public dtreeNode aii;
    public dtreeNode aiii;
    
    public dtreeNode(int dID, dtreeNode ai, dtreeNode aii, dtreeNode aiii){
        this.dID = dID;
        this.ai = ai;
        this.aii = aii;
        this.aiii = aiii;
    }
    
    public dtreeNode(int dID){
        this.dID = dID;
    }
}
