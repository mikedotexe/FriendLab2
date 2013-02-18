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
public class Attr {
    public int idNum;
    public ArrayList<Option> options;
    
    public Attr(int idNum){
        this.idNum = idNum;
        options = new ArrayList<Option>();
    }
    
    public void add(String value){
        Option tmp = new Option(value, this);
        if (!options.contains(tmp)){
            // this option has not been added yet
            options.add(new Option(value, this));
        }
    }
    
    public void add(Option value){
//        System.out.println("trying to add value "+value);
//        if (!options.contains(value)){
//            options.add(value);
//        }
        boolean match = false;
        for (int i = 0; i < options.size(); i++) {
            if (value.value.equals(options.get(i).value)){
                match = true;
                break;
            }
        }
        if (!match){
            options.add(value);
        }        
    }
    
    
    public void add(Option value, boolean verbose){
        System.out.println("this attribute has toString of:\n"+value.parentAttr.toString());
        System.out.println("trying to add value "+value);
        boolean match = false;
        for (int i = 0; i < options.size(); i++) {
            if (value.value.equals(options.get(i).value)){
                match = true;
                break;
            }
        }
        
//        if (!options.contains(value)){
        if (!match){
            System.out.println("NOT in contains");
            options.add(value);
        }
        else{
            System.out.println("IS in contains");
        }
    }    
    
    public String toString(){
        String optionStr = "";
        for (int i = 0; i < options.size(); i++) {
            optionStr += "\t"+options.get(i).toString() + "\n";
        }
        return "ID = "+idNum+"\n"+optionStr;
    }
    

    
}

class Option{
    public String value;
    public Attr parentAttr;

    public Option(String value, Attr parentAttr){
        this.value = value;
        this.parentAttr = parentAttr;
    }
    
    public String toString(){
        return value;
    }
}