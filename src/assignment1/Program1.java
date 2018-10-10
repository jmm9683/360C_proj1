package assignment1;

/*
 * Name: Jake Morrissey
 * EID: jmm9683
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Your solution goes in this class.
 * 
 * Please do not modify the other files we have provided for you, as we will use
 * our own versions of those files when grading your project. You are
 * responsible for ensuring that your solution works with the original version
 * of all the other files we have provided for you.
 * 
 * That said, please feel free to add additional files and classes to your
 * solution, as you see fit. We will use ALL of your additional files when
 * grading your solution.
 */
public class Program1 extends AbstractProgram1 {
    /**
     * Determines whether a candidate Matching represents a solution to the Stable Marriage problem.
     * Study the description of a Matching in the project documentation to help you with this.
     */
    @Override
    public boolean isStableMatching(Matching marriage) {
        ArrayList<Integer> resMatch = marriage.getResidentMatching();
        ArrayList<ArrayList<Integer>> hosPref = marriage.getHospitalPreference();
        ArrayList<ArrayList<Integer>> resPref = marriage.getResidentPreference();
        int matchCount = 0;
        for (int i = 0; i < resMatch.size(); i++) { //loop through resident matches
        	/* an unmatched resident can be skipped and instability will be caught later
        	 * since there is no hospital to check if it is unmatched
        	 */
        	if (resMatch.get(i) == -1)
        		continue;
        	matchCount++;
        	for (int j = 0; j< hosPref.get(resMatch.get(i)).indexOf(i); j++) { //loop through the preferences of that hospital
        		//current hospital's preference
        		int currHosPref = hosPref.get(resMatch.get(i)).get(j); //3
        		//current hospital's preference's opinion on this hospital
        		int resOpofCurrHosPref = resPref.get(currHosPref).indexOf(resMatch.get(i)); //0
        		//this resident opinion of hospital
        		int thisResOpofCurrHosPref = resPref.get(i).indexOf(resMatch.get(i)); //0
        		//current hospital preference opinion of its match
        		int resOpofMatch = resPref.get(currHosPref).indexOf(resMatch.get(currHosPref)); //3
        		//match of current hospital's preference
        		int currHosPrefMatch = resMatch.get(currHosPref);
        	
        		/* if current hospitals preference over its match do not have the same hospital
        		 * the res the hospital prefers, prefers this hospital more than this current match
        		 * and res the hospital prefers likes this hospital more than its current match
        		 * then instability
        		 */
        		if (currHosPrefMatch == -1 
        				||(currHosPrefMatch != resMatch.get(i)
        					&& resOpofCurrHosPref <= thisResOpofCurrHosPref 
        					&& resOpofMatch > resOpofCurrHosPref
        					&& resOpofCurrHosPref != -1
        					&& thisResOpofCurrHosPref != -1
        					&& resOpofMatch != -1
        					)
        				) {
        			return false;
        		}	
        	}
        }
        return (matchCount == marriage.totalHospitalSlots());
	    }

    /**
     * Determines a resident optimal solution to the Stable Marriage problem from the given input set.
     * Study the project description to understand the variables which represent the input to your solution.
     *
     * @return A stable Matching.
     */
    @Override
    public Matching stableMarriageBruteForce_residentoptimal(Matching marriage) {
        int n = marriage.getResidentCount();
        int slots = marriage.totalHospitalSlots();
        int matchRankMin = -1;
        Matching resOptimal = new Matching(marriage);

        Permutation p = new Permutation(n, slots);
        Matching matching;
        while ((matching = p.getNextMatching(marriage)) != null) {
        	if (isStableMatching(matching)) {
                int matchRankSum = 0;
                for (int i = 0; i < matching.getResidentMatching().size(); i++) {
                	if (matching.getResidentPreference().get(i).indexOf(matching.getResidentMatching().get(i)) != -1)
                		matchRankSum += matching.getResidentPreference().get(i).indexOf(matching.getResidentMatching().get(i));
                }

                if (matchRankMin < 0 || matchRankMin > matchRankSum) {
                	matchRankMin = matchRankSum;
                	resOptimal = matching;
                }

            }
        }

        return resOptimal;
    }

    /**
     * Determines a resident optimal solution to the Stable Marriage problem from the given input set.
     * Study the description to understand the variables which represent the input to your solution.
     *
     * @return A stable Matching.
     */
    @Override
    public Matching stableMarriageGaleShapley_residentoptimal(Matching marriage) {
        ArrayList hosSpots = new ArrayList(marriage.getHospitalSlots());
        int totalSlots = marriage.totalHospitalSlots();
        ArrayList<ArrayList<Integer>> hosPref = marriage.getHospitalPreference();
        ArrayList<ArrayList<Integer>> resPref = marriage.getResidentPreference();
        LinkedList<Integer> resQ = new LinkedList<>();
        ArrayList<Integer> matches = new ArrayList<>();
        for (int i = 0; i <resPref.size(); i++) { //creates queue and final match output
        	resQ.add(i);
        	matches.add(-1);
        }
        int[][] hosRes =  new int[hosSpots.size()][totalSlots]; //holds the list of residents paired to each hospital

        while(resQ.size() != 0){ //i is the resident
        	int i = resQ.pop();
//        	matches.set(i, -1);
        	for (int j = 0; j < resPref.get(i).size(); j++) { //j holds the index preference
        		int thisPrefHos = resPref.get(i).get(j);
        		int openSlots = (int) hosSpots.get(thisPrefHos);
        		if (openSlots == 0) {
        			//if hospital is full need to check all current residents
        			int thisPrefHos_opCurRes = hosPref.get(thisPrefHos).indexOf(i); //compare to least favorite resident
        			int leastFaveLoc = -1;
        			int leastFaveRes = 0;
        			int leastFaveR = 0;
        			for (int r = 0; r < marriage.getHospitalSlots().get(thisPrefHos); r++) { //r is the resident in thisPrefHos
        				int thisRes = hosRes[thisPrefHos][r];
        				int thisPrefHos_opthisRes = hosPref.get(thisPrefHos).indexOf(thisRes);
        				if (leastFaveLoc == -1 || thisPrefHos_opthisRes > leastFaveLoc) {
        					leastFaveLoc = thisPrefHos_opthisRes;
        					leastFaveRes = thisRes;
        					leastFaveR = r;
        				}
        			}
        			if (leastFaveLoc > thisPrefHos_opCurRes) {
        				hosRes[thisPrefHos][leastFaveR] = i;
        				resQ.add(leastFaveRes);
        				matches.set(i, thisPrefHos);
       					matches.set(leastFaveRes, -1);
       					break;
        			}
        		}
        		else {
        			hosRes[thisPrefHos][marriage.getHospitalSlots().get(thisPrefHos) - openSlots] = i;
        			hosSpots.set(thisPrefHos, (int)hosSpots.get(thisPrefHos)-1);
        			matches.set(i, thisPrefHos);
        			break;
        		}
        	}
        }
    	
    	
    	
    	
        return new Matching(marriage, matches);
    }

    /**
     * Determines a hospital optimal solution to the Stable Marriage problem from the given input set.
     * Study the description to understand the variables which represent the input to your solution.
     *
     * @return A stable Matching.
     */
    @Override
    public Matching stableMarriageGaleShapley_hospitaloptimal(Matching marriage) {
        /* TODO implement this function */
        return null; /* TODO remove this line */
    }
}
