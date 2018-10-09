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
        for (int i = 0; i < resMatch.size(); i++) { //loop through resident matches
        	for (int j = 0; j< hosPref.get(resMatch.get(i)).indexOf(i); j++) { //loop through the preferences of that hospital
        		//current hospital preference
        		int currHosPref = hosPref.get(resMatch.get(i)).get(j);
        		//current hospital preference opinion on this hospital
        		int resOpofCurrHosPref = resPref.get(currHosPref).indexOf(resMatch.get(i));
        		//this resident opinion of hospital
        		int thisResOpofCurrHosPref = resPref.get(i).indexOf(resMatch.get(i));
        		//current hospital preference opinion of its match
        		int resOpofMatch = resPref.get(currHosPref).indexOf(resMatch.get(currHosPref));
        		/**
        		 * If the resident the hospital prefers over this current matching
        		 * prefers this hospital more than this current matching's resident preference of this hospital
        		 * and prefers it more than its current match, then there is an instability
        		 */
        		if (resOpofCurrHosPref <= thisResOpofCurrHosPref && resOpofMatch > resOpofCurrHosPref) {
        			return false;
        		}	
        	}
        }
        return true; 
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

        Permutation p = new Permutation(n, slots);
        Matching matching;
        while ((matching = p.getNextMatching(marriage)) != null) {
            if (isStableMatching(matching)) {
                return matching;
            }
        }

        return new Matching(marriage);
    }

    /**
     * Determines a resident optimal solution to the Stable Marriage problem from the given input set.
     * Study the description to understand the variables which represent the input to your solution.
     *
     * @return A stable Matching.
     */
    @Override
    public Matching stableMarriageGaleShapley_residentoptimal(Matching marriage) {
        /* TODO implement this function */
        return null; /* TODO remove this line */
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
