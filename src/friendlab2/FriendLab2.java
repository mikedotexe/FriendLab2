/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package friendlab2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author mike
 */
public class FriendLab2 {
        // turn one of the three below to true, so we can use that to make the tree
        // 0 - ENTROPY
        // 1 = GINI 
        // 2 = CLASSIFICATION
        public static int IMPURITY = 0;
        
        public static final boolean debug = true;

	public static void main(String[] args) {

//		File file = new File("car_data.csv");
		File file = new File("fromOnline.csv");
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));

			// Gets all entries
			String temp = in.readLine();
			String[] tokens = temp.split(",");
                        
//                        System.out.println("tokens "+tokens);
//                        for (int i = 0; i < tokens.length; i++){
//                            System.out.println("token "+tokens[i]);
//                        }
                        
                        // how many attributes are there?
                        int numAttr = tokens.length -1;
//                        System.out.println("numattr is "+numAttr);
                        
                        ArrayList<Attr> attrsArr = new ArrayList<Attr>(numAttr);
                        Row r = new Row();
                        
                        //create attr classes and fill in with values of first row read
                        for (int i = 0; i < numAttr; i++) {
                            Attr tmpA = new Attr(i);
                            Option tmpO = new Option(tokens[i], tmpA);
                            tmpA.add(tmpO);
                            
                            // add/update the attributes
                            attrsArr.add(tmpA);
                            
                            // build first row
                            r.values.add(tmpO);
                        }
                        
                        // make classes as an attribute (while it's not an attribute, data structure works the same)
                        Attr classAtt = new Attr(-1);
                        Option tmpO = new Option(tokens[numAttr], classAtt);
                        classAtt.add(tmpO);
                        
                        // set the class of the row
                        r.theClass = tokens[numAttr];
                        
                        // begin building the training data
                        ArrayList<Row> trainingSet = new ArrayList<Row>();
                        trainingSet.add(r);
                                
//                        if (debug){
////                            Option del = new Option("namehere");
//                            System.out.println("row one is:\n"+r);
//                        }

			while ((temp = in.readLine()) != null) {
                            tokens = temp.split(",");
                            r = new Row();
                            //create attr classes and fill in with values of first row read
                            for (int i = 0; i < numAttr; i++) {
//                                Attr tmpA = new Attr(i);
                                Attr tmpA = attrsArr.get(i);
                                tmpO = new Option(tokens[i], tmpA);
                                tmpA.add(tmpO);

                                // add/update the attributes
//                                attrsArr.add(tmpA);

                                // build the next
                                r.values.add(tmpO);
                            }
                            // make classes as an attribute (while it's not an attribute, data structure works the same)
                            tmpO = new Option(tokens[numAttr], classAtt);
                            classAtt.add(tmpO);
                            
                            // set the class of the row
                            r.theClass = tokens[numAttr];

                            // begin building the training data
                            trainingSet.add(r);                            
                                
			}
			in.close();
                        
//                        System.out.println("one of them "+trainingSet.get(2));
//                        for (int i = 0; i < 10; i++) {
//                            if (debug){
//    //                            Option del = new Option("namehere");
////                                System.out.println("here is i and size is "+i+" "+trainingSet.size());
//                                System.out.println(trainingSet.get(i).toString());
//                            }                        
//                        }
                        
                        System.out.println("Classes have "+classAtt);
                        
                        if (debug){
                            for (int i = 0; i < attrsArr.size(); i++) {
                                System.out.println("attrsArr["+i+"] is "+attrsArr.get(i).toString());
                            }
                        }
//                        Run ENTROPY on entire trainingSet
                        double[] setImpurity = getEntropyGiniClass(trainingSet, classAtt);
                        System.out.println("Entropy/Gini/Classification of entire set:");
                        for (int i = 0; i < 3; i++) {
                            System.out.println(setImpurity[i]);
                        }
                        
// SPLIT UP INTO DIFFERENT SUBTABLES
                        
                        ArrayList<ArrayList<Row>> subset = new ArrayList<ArrayList<Row>>();
                        ArrayList<double[]> allInfoGains = new ArrayList<double[]>();
                        double[] subsetInfoGain = null;
                        
                        for (int j = 0; j < attrsArr.size(); j++) {
                            System.out.println("--------------------------------------------------------------------------");
//                            j = 2;
// CALL TO makeSubsets(ArrayList<Row> training, int j, ArrayList<Attr> attrs)
//      RETURNS ArrayList<ArrayList<Row>>
                            subset = makeSubsets(trainingSet, j, attrsArr);

                            //show all subTables
                            if (debug){
                                for (int i = 0; i < subset.size(); i++) {
                                    System.out.println("Subtable["+i+"]:");
                                    for (int k = 0; k < subset.get(i).size(); k++) {
                                        System.out.println("\t"+subset.get(i).get(k).values.get(0).value + " => " + subset.get(i).get(k).theClass);
                                    }
                                }
                                System.out.println("");
                            }                       

                            ArrayList<Row> oneSet = null;
                            // combine them into one arraylist after done
                            ArrayList<double[]> allSubsetImp = new ArrayList<double[]>();
                            double[] subsetImpurity = null;
                            for (int i = 0; i < subset.size(); i++) {
                                oneSet = subset.get(i);
                                // getting the subset's IMPURITY MEASURES
// CALL TO getEntropyGiniClass(ArrayList<Row> set, Attr classAtt){
//      RETURNS double[]                                                                 
                                
                                subsetImpurity = getEntropyGiniClass(oneSet, classAtt);
                                System.out.println("Entropy/Gini/Classification of subset:");
                                for (int k = 0; k < 3; k++) {
                                    System.out.println("\tEGC => "+subsetImpurity[k]);
                                }
                                System.out.println();
                                allSubsetImp.add(subsetImpurity);
                            }
                            
                            // get the OVERALL ATTRIBUTE INFORMATION GAIN
// CALL TO getInfoGain(double[] parentEntropy, ArrayList<ArrayList<Row>> subset, ArrayList<double[]> subImpurity)
//      RETURNS double[]                             
                            subsetInfoGain = getInfoGain(setImpurity, subset, allSubsetImp);
                            allInfoGains.add(subsetInfoGain);
                        }
                        
//                        Analyze all the information gains
                        System.out.println("infoGain values:");
                        for (int i = 0; i < allInfoGains.size(); i++) {
//                            System.out.println("i is "+i);
//                            double[] tmp = allInfoGains.get(i);
//                            System.out.println("iSize is "+tmp.toString());
                            for (int j = 0; j < allInfoGains.get(i).length; j++) {
//                                System.out.println("j is "+j);
                                System.out.println("\t"+allInfoGains.get(i)[j]);
                            }
                            System.out.println("");
                        }
                    
//    PHASE TWO - MAKING TREE
                        // The IMPURITY global will determine which measure we use now
                        // this is the index of the attribute to use from attrsArr
                        
                        // going forward we need
                        /*
                         * attrsArr => ArrayList<Attr>
                         * allInfoGains => ArrayList<double[]>
                         * trainingSet => ArrayList<Row>
                         * 
                         * now we need to split trainingset into subtables based on the optimum attribute
                         * then see if the tables are pure, if so we can make leaf nodes
                         * if not, we split
                         * 
                         * we'll need a check for impurity function where we send the attrIdx so it can see if it's pure.
                         *      if it finds a pure / impure maybe return a boolean so we can add a leaf back from where i was called
                         *      build an arraylist of impure tables
                         * 
                         * make a function that removes an entire row from a table based on index
                         */
                        int attrIdx = max(allInfoGains); 
                        System.out.println("yes it can "+attrIdx);
                        
                        
                        
                        
                        
                        
                        
                        

//              Catch stuff
		} catch (FileNotFoundException e) {
                    System.out.println("File not found.\t" + e.toString());
		} catch (IOException e) {
                    System.out.println("Some kind of IOException happened.\t"+e.toString());
		}
	}
        
        public static int max(ArrayList<double[]> allGains){
            int ret = 0;
            double max = 0;
//            ret = 10 + IMPURITY;
            
            for (int i = 0; i < allGains.size(); i++) {
                if (i == 0){
                    ret = i;
                    max = allGains.get(i)[IMPURITY];
                }
                if (allGains.get(i)[IMPURITY] > max){
                    ret = i;
                    max = allGains.get(i)[IMPURITY];
                }
                
            }
            return ret;
        }

        public static double[] getInfoGain(double[] parentEntropy, ArrayList<ArrayList<Row>> subset, ArrayList<double[]> subImpurity){
//            double[] ret = null;
//            We need to get  ParentEntropy - (count/total*impurity + count/total*impurity + count/total*impurity) kind of thing
            int total = 0;
            int[] counts = new int[subset.size()];
            
            for (int i = 0; i < subset.size(); i++) {
//                System.out.println(subset.get(i).size());
                counts[i] = subset.get(i).size();
                total += subset.get(i).size();
            }
            
//            if (debug){
//                System.out.println("Total of this subset is "+total);
//                System.out.println("Counts");
//                for (int i = 0; i < counts.length; i++) {
//                    System.out.println(counts[i]);
//                }
//                System.out.println();
//                System.out.println("Subset Impurity:");
//                for (int i = 0; i < subImpurity.size(); i++) {
//                    for (int j = 0; j < subImpurity.get(i).length; j++) {
//                        System.out.println(subImpurity.get(i)[j]);
//                    }
//                }
//                System.out.println();
//                System.out.println("Parent Entropy/GINI/Class");
//                for (double d : parentEntropy){
//                    System.out.println("d => "+d);
//                }
//                System.out.println();
//            }
            
            
//            double[] infoGain = new double[counts.length];
            double[] infoGain = new double[parentEntropy.length];
//            double[] infoGain = new double[];
            //initialize
//            for (int i = 0; i < counts.length; i++) {
            for (int i = 0; i < parentEntropy.length; i++) {
                infoGain[i] = 0.0;
            }
  

            for (int i = 0; i < parentEntropy.length; i++) {
                for (int j = 0; j < counts.length; j++) {
                    infoGain[i] += (double) counts[j] / total * subImpurity.get(j)[i];
                }
                infoGain[i] = parentEntropy[i] - infoGain[i];
            }
            
//            if (debug){
//                System.out.println("infoGain values:");
//                for (int i = 0; i < infoGain.length; i++) {
//                    System.out.println(infoGain[i]);
//                }
//            }
            
            return infoGain;
        }
        
        public static double[] getEntropyGiniClass(ArrayList<Row> set, Attr classAtt){
            // set up return array
            double[] ret = new double[3]; //[classAtt.options.size()];
            Integer[] count = new Integer[classAtt.options.size()];
            double[] prob = new double[classAtt.options.size()];
            for (int i = 0; i < classAtt.options.size(); i++) {
                count[i] = 0;
//                ret[i] = 0;
            }
            
            Map<String, Integer> mappy = new HashMap<String, Integer>();
            // create map so i can line up the values with their indices
            for (int i = 0; i < classAtt.options.size(); i++) {
                mappy.put(classAtt.options.get(i).value, i);
            }
            
//            Integer test = mappy.get("unacc2");
//            System.out.println("vgood is located as index "+test);
            for (int i = 0; i < set.size(); i++) {
                Row tempRow = set.get(i);
                String tempClass = tempRow.theClass;
                Integer tempIndex = mappy.get(tempClass);
                count[tempIndex]++;
            }
            
            // get total
            int countTotal = 0;
            for (int i = 0; i < count.length; i++) {
                countTotal += count[i];
            }
//            System.out.println("total is "+countTotal);
            
            // get probabilities
            for (int i = 0; i < count.length; i++) {
//                System.out.println("HERE count = "+count[i]);
                prob[i] = (double) count[i] / countTotal;
//                System.out.println("HERE TOO"+count[i] / countTotal);
//                System.out.println("prob["+i+"] = "+prob[i]);
            }
//            System.out.println();
            
            // do sigma algorithm
//            
            double retDbl = 0;
            for (int i = 0; i < classAtt.options.size(); i++) {
                if (prob[i] == 0) continue; // this is needed to avoid NaN becoming an answer
                retDbl -= prob[i] * (Math.log(prob[i])/Math.log(2));
            }
            
            // set Entropy in return array
            ret[0] = retDbl;
            
//            GINI
            double gini = 0;
//            double gini = 1;
            for (int i = 0; i < classAtt.options.size(); i++) {
                gini += Math.pow(prob[i], 2);
            }            
            ret[1] = 1 - gini;
            
//            Classification
            double max = 0;
            for (int i = 0; i < classAtt.options.size(); i++) {
                if (i == 0){
                    max = prob[i];
                }
                else{
                    if (prob[i] > max)
                        max = prob[i];
                }
            }
            ret[2] = 1 - max;
            
            return ret;
        }
        
        public static ArrayList<ArrayList<Row>> makeSubsets(ArrayList<Row> training, int j, ArrayList<Attr> attrs){
            ArrayList<Row> subset = new ArrayList<Row>();
            Attr tmpAttr = new Attr(-2);
            
            // this is reducing the subset to only having 1 attr => class
            for (int i = 0; i < training.size(); i++) {
                Row tmpRow = new Row();
//                tmpRow.theClass = training.get(i).theClass;
//                tmpRow.values.add(training.get(i).values.get(j));
                tmpRow = (Row) training.get(i);
                subset.add(tmpRow);
                
                // keep track of how many different classes are in this set
                tmpAttr.add(new Option(training.get(i).theClass, tmpAttr));                
            }
            
            if (debug) {
                System.out.println("Showing subset:");
                for (int i = 0; i < subset.size(); i++) {
//                    System.out.println(subset.get(i).values.get(0)+" => "+subset.get(i).theClass);
                    System.out.println(subset.get(i).toString());// +" => "+subset.get(i).theClass);
                }
                System.out.println("There are "+attrs.get(j).options.size() +" classes represented here.\n");
            }
            
            // We need to make attrs.get(j).options.size() many subTables
            ArrayList<ArrayList<Row>> subTables = new ArrayList<ArrayList<Row>>(attrs.get(j).options.size());
            for (int i = 0; i < attrs.get(j).options.size(); i++) {
                ArrayList<Row> tmpPopulate = new ArrayList<Row>();
                subTables.add(tmpPopulate);
            }
            // So the first arraylist in subTables corresponds to the first class
                Map<String, Integer> mappy = new HashMap<String, Integer>();
                // create map so i can line up the values with their indices
                for (int i = 0; i < attrs.get(j).options.size(); i++) {
//                    mappy.put(tmpAttr.options.get(i).value, i); // old code
                    mappy.put(attrs.get(j).options.get(i).value, i);
                }        
                
            System.out.println("debug - diff between subset and subTables: "+subset.size()+", "+subTables.size());
            System.out.println("Mappy size is "+mappy.size());
            // loop through whole subset "subset"
//            for (int i = 0; i < subset.size(); i++) {
            for (int i = 0; i < subTables.size(); i++) {
//                String tmpClass = subset.get(i).theClass;
                String tmpVal = subset.get(i).values.get(0).value;
                Integer tmpIndex = mappy.get(tmpVal);
                System.out.println("debug this is probably null "+tmpIndex);
                // add value to proper subtable
                Row tmpRow = new Row();
                tmpRow.theClass = subset.get(i).theClass;
                Option tmpOption = new Option(subset.get(i).values.get(0).value, tmpAttr);
//                tmpRow.values.add(subset.get(i).values.get(0), tmpAttr);
                tmpRow.values.add(tmpOption);
                subTables.get(tmpIndex).add(tmpRow);
            }
            
//            //show all subTables
//            if (debug){
//                for (int i = 0; i < subTables.size(); i++) {
//                    System.out.println("Subtable["+i+"]:");
//                    for (int k = 0; k < subTables.get(i).size(); k++) {
//                        System.out.println(subTables.get(i).get(k).values.get(0).value + " => " + subTables.get(i).get(k).theClass);
//                    }
//                }
//            }
            return subTables;
        }        
}
