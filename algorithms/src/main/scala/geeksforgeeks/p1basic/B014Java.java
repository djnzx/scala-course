package geeksforgeeks.p1basic;

import java.util.ArrayList;

public class B014Java {
  static boolean[][] dp;

  static void display(ArrayList<Integer> v) {
    System.out.println(v);
  }

  // A recursive function to print all subsets with the help of dp[][]. Vector p[] stores current subset. 
  static void collectR(int[] a, int i, int sum, ArrayList<Integer> p) {
    // If we reached end and sum is non-zero. We print 
    // p[] only if arr[0] is equal to sun OR dp[0][sum] is true. 
    if (i == 0 && sum != 0 && dp[0][sum]) {
      p.add(a[i]);
      display(p);
      p.clear();
      return;
    }

    // If sum becomes 0 
    if (i == 0 && sum == 0) {
      display(p);
      p.clear();
      return;
    }

    // If given sum can be achieved after ignoring current element. 
    if (dp[i-1][sum]) 
      collectR(a, i-1, sum, new ArrayList<>(p));
    

    // If given sum can be achieved after considering current element. 
    if (sum >= a[i] && dp[i-1][sum-a[i]]) {
      p.add(a[i]);
      collectR(a, i-1, sum-a[i], p);
    }
  }

  // Prints all subsets of arr[0..n-1] with sum 0. 
  static void solve(int[] a, int n, int sum) {
    if (n == 0 || sum < 0) return;

    // Sum 0 can always be achieved with 0 elements 
    dp = new boolean[n][sum + 1];
    for (int i=0; i<n; ++i) {
      dp[i][0] = true;
    }

    // Sum arr[0] can be achieved with single element 
    if (a[0] <= sum)
      dp[0][a[0]] = true;

    // Fill rest of the entries in dp[][] 
    for (int i = 1; i < n; ++i)
      for (int j = 0; j < sum + 1; ++j)
        dp[i][j] = (a[i] <= j) ? (dp[i-1][j] || dp[i-1][j-a[i]]) : dp[i - 1][j];
      
    if (!dp[n - 1][sum]) {
      System.out.println("There are no subsets with sum "+ sum);
      return;
    }

    // Now recursively traverse dp[][] to find all paths from dp[n-1][sum] 
    ArrayList<Integer> p = new ArrayList<>();
    collectR(a, n-1, sum, p);
  }

  public static void main(String[] args) {
    int[] a = {1, 2, 3, 4, 5};
    int sum = 10;
    solve(a, a.length, sum);
  }
} 
