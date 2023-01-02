package comp2402a4;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FastSparrow implements RevengeOfSparrow {
  // TODO: Your data structures go here
  ArrayList<Integer> prim;
  ArrayList<ArrayList<Integer>> max;
  int h;


  public FastSparrow() {
    // TODO: Your code goes here
    prim = new ArrayList<Integer>();
    max =  new ArrayList<ArrayList<Integer>>();
    h = 0;
  }

  public void push(int x) {
    // TODO: Your code goes here
    //NEXT IS FOR MAX
    ArrayList<Integer> temp = new ArrayList<>();
    prim.add(x);
    if (prim.size() == 2) {
      h++;
      temp.add(prim.get(0));
      if (prim.get(0) > x) {
        max.add(temp);
      } else {
        temp.set(0, x);
        max.add(temp);
      }
    } else if (prim.size() > (1 << h)) {
      h++;
      for (int k = 0; k < max.size(); k++) {
        temp = max.get(k);
        temp.add(x);
        if (temp.size() == 2) {
          if (temp.get(0) > x) {
            int gr = temp.get(0);
            max.add(new ArrayList<Integer>());
            temp = max.get(k+1);
            temp.add(gr);
          } else {
            max.add(new ArrayList<Integer>());
            temp = max.get(k+1);
            temp.add(x);
          }
          k = max.size();
        }
      }
    } else if (prim.size() > 2) {
      int curI, prevI, tempVal, curHeight = h;
      curI = (size() - 1) >> 1;
      prevI = (size() - 2) >> 1;
      temp = prim;
      for (int k = 0; k < max.size(); k++) {
        if (curI == prevI) {
          if (temp.get(temp.size() - 1) > temp.get(temp.size() - 2)) {
            tempVal = temp.get(temp.size() - 1);
            temp = max.get(k);
            temp.set(temp.size() - 1, tempVal);
          } else {
            tempVal =  temp.get(temp.size() - 2);
            temp = max.get(k);
            temp.set(temp.size() - 1, tempVal);
          }
        } else {
          tempVal = temp.get(temp.size() - 1);
          temp = max.get(k);
          if (temp.size() - 1 < curI) {
            temp.add(tempVal);
          } else if (tempVal > temp.get(temp.size() - 1)){
            temp.set(temp.size() - 1, tempVal);
          }
        }
        curI = (temp.size() - 1) >> 1;
        prevI = (temp.size() - 2) >> 1;
        curHeight--;
      }
    }
    //NEXT IS FOR KSUM
  }

  public Integer pop() {
    // TODO: Your code goes here
    
    ArrayList<Integer> temp = new ArrayList<>();
    if (prim.isEmpty()) {
      return null;
    } else if (prim.size() == 1) {
      return prim.remove(0);
    } else if (prim.size() == 2) {
      max.remove(max.size() - 1);
      return prim.remove(1);
    } else if (prim.size() == ((1 << (h-1)) + 1)) {
      for (int k = 0; k < max.size(); k++) {
        temp = max.get(k);
        temp.remove(temp.size() - 1);
      }
      max.remove(max.size() - 1);
      h--;
      return prim.remove(size() - 1);
    } else {
      int curI, prevI, curV, prevV, tempStore;
      tempStore = prim.remove(size() - 1);
      curI = (size() - 1) >> 1;
      prevI = (size() - 2) >> 1;
      temp = prim;
      curV =  temp.get(temp.size() - 1);
      prevV = temp.get(temp.size() - 2);
      
      for (int k = 0; k < max.size(); k++) {
        if (curI == prevI) {
          int downLen = temp.size();
          temp = max.get(k);
          if ((temp.size() * 2) == (downLen + 2)) {
            temp.remove(temp.size() - 1);
          } else {
            if (curV > prevV) {
              temp.set(temp.size() - 1, curV);
            } else {
              temp.set(temp.size() - 1, prevV);
            }
          }
        } else {
          temp = max.get(k);
          temp.set(temp.size() - 1, curV);
        }
        if (k != max.size() - 1) {
          curV = temp.get(temp.size() - 1);
          prevV = temp.get(temp.size() - 2);
          curI = (temp.size() - 1) >> 1;
          prevI = (temp.size() - 2) >> 1;
        }
      }
      return tempStore;
    }
  }

  public Integer get(int i) {
    // TODO: Your code goes here
    if (i < 0 || i >= prim.size()) {
      return null;
    }
    return prim.get(i);
  }

  public Integer set(int i, int x) {
    // TODO: Your code goes here
    ArrayList<Integer> temp = new ArrayList<>();
    int oldVal;
    
    if (i >= size() || i < 0) {
      return null;
    } else if (size() == 1) {
      oldVal = prim.get(0);
      prim.set(0, x);
      return oldVal;
    } else if (size() == 2) {
      oldVal = prim.set(i, x);
      temp = max.get(0);
    	if (prim.get(0) > prim.get(1)) {
    	  temp.set(0, prim.get(0));
      } else {
        temp.set(0, prim.get(1));
      }
      return oldVal;
    } else {
      int curI, otherI, curV, otherV;
      curI = i;
      curV = prim.get(i);
      oldVal = prim.set(i, x);
      temp = prim;

      for (int k = 0; k < max.size(); k++) {
        if (curI == 0) {
          otherI = 1;
          curV = temp.get(0);
          otherV = temp.get(1);
          temp = max.get(k);
          if (curV > otherV) {
            temp.set(0, curV);
          } else {
            temp.set(0, otherV);
          }
        } else if (curI == temp.size() - 1) {
          if (((curI - 1) >> 1) == (curI >> 1)) {
            otherI = curI - 1;
            curV = temp.get(curI);
            otherV = temp.get(curI - 1);
            temp = max.get(k);
            if (curV > otherV) {
              temp.set((curI >> 1), curV);
            } else {
              temp.set((curI >> 1), otherV);
            }
            curI = curI >> 1;
          } else {
            curV = temp.get(curI);
            temp = max.get(k);
            temp.set((curI >> 1), curV);
            curI = curI >> 1;
          }
        } else {
          if (((curI - 1) >> 1) == (curI >> 1)) {
            otherI = curI - 1;
            curV = temp.get(curI);
            otherV = temp.get(curI - 1);
            temp = max.get(k);
            if (curV > otherV) {
              temp.set((curI >> 1), curV);
            } else {
              temp.set((curI >> 1), otherV);
            }
            curI = curI >> 1;
          } else {
            otherI = curI + 1;
            curV = temp.get(curI);
            otherV = temp.get(curI + 1);
            temp = max.get(k);
            if (curV > otherV) {
              temp.set((curI >> 1), curV);
            } else {
              temp.set((curI >> 1), otherV);
            }
            curI = curI >> 1;
          }
        }
      }
      return oldVal;
    }
  }

  public Integer max() {
    // TODO: Your code goes here
    if (prim.isEmpty()) {
      return null;
    } else if (prim.size() == 1) {
      return prim.get(0);
    } else {
      ArrayList<Integer> temp = new ArrayList<>();
      temp = max.get(max.size() - 1);
      return temp.get(0);
    }
  }

  public long ksum(int k) {
    long sum = 0;
    for(int i=0; i<k && i<prim.size(); i++)
      sum += prim.get(prim.size() - 1 - i);
    return sum;
  }

  public int size() {
    // TODO: Your code goes here
    return prim.size();
  }

  public Iterator<Integer> iterator() {
    // TODO: Your code goes here
    return prim.iterator();
  }
}
