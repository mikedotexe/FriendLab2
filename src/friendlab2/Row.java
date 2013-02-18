/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package friendlab2;

import java.util.ArrayList;

/**
 *
 * @author mike
 */
public class Row {
    
    public ArrayList<Option> values;
    public String theClass;

    public Row(){
        values = new ArrayList<Option>();
    }
    
    public String toString(){
        String ret = "";
        for (int i = 0; i < values.size(); i++) {
            ret += values.get(i).toString() + "\t";
        }
        return ret + " => "+theClass;
    }
}
