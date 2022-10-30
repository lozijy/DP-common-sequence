import java.util.HashSet;
import java.util.LinkedList;

public class Solution {
    //第一种方法,先用回溯方法生成第一个字符串的所有子序列,再看每一个子序列是否是第二个字符串的子序列
    //回溯生成所有str1的所有子序列放进HashSet-tokens里面
    public static void backtrack(HashSet<String> tokens, String str1, int n,LinkedList<String> list ) {
        if (n == str1.length()) {
            return;
        } else {
            for (String token : tokens) {
                list.add(token + str1.charAt(n));
            }
            for(String token:list){
                tokens.add(token);
            }
            backtrack(tokens, str1, n + 1,list);
        }
    }
    //解决问题，把tokens里面的所有token与str2的字符进行比较，如果是子序列则更新ans
    public static int solve(String str1, String str2) {
        //声明所有会用到的变量
        int ans = 0;
        HashSet<String> tokens = new HashSet<>();
        tokens.add("");//对tokens附上初值
        HashSet<Character> set = new HashSet<>();
        LinkedList<String> list = new LinkedList<>();
        backtrack(tokens, str1, 0,list);//生成str1的所有子序列
        //判断str1的每一个子序列是否是str2的子序列
        for (String token : tokens) {
            int position1 = 0;
            int position2 = 0;
            while(position2!=str2.length()&&position1!=token.length()) {
                position1 = ((token.charAt(position1) == str2.charAt(position2++)) ? (position1 + 1) : position1);
            }
            if (position1 == token.length()) {//如果是，则更新ans
                ans = Math.max(ans, token.length());
            }
        }
        return ans;
    }

    //使用深度优先搜索把所有会生成最终结果的子序列放进hashset里面
    public static void dfs(String mark[][],String str,HashSet<String> set,int i,int j,String str1){
        if(i==0||j==0){
            set.add(str);
            return;
        }
        else if(mark[i][j].equals("↖ ")){
            str+=str1.charAt(i-1);
            dfs(mark,str,set,i-1,j-1,str1);
        }
        else if(mark[i][j].equals("W ")){
            dfs(mark,str,set,i-1,j,str1);
            dfs(mark,str,set,i,j-1,str1);
        }else if(mark[i][j].equals("↑ ")){
            dfs(mark,str,set,i-1,j,str1);
        }else {
            dfs(mark,str,set,i,j-1,str1);
        }
    }
    //普通dp+结果追踪(对mark数组修改值)
    public static int dp01(String str1,String str2){
        int[][] dp = new int[str1.length()+1][str2.length()+1];
        String [][]mark=new String[str1.length()+1][str2.length()+1];
        //对dp数组和mark数组附上初值
        for (int i = 0; i < str1.length()+1; i++) {
            dp[i][0]=0;
            mark[i][0]="0 ";
        }
        for (int i = 0; i < str2.length()+1; i++) {
            mark[0][i]="0 ";
            dp[0][i]=0;
        }
        //dp状态转移和mark标记
        for (int i = 1; i < str1.length()+1; i++) {
            for (int j = 1; j < str2.length()+1; j++) {
                if(str1.charAt(i-1)==str2.charAt(j-1)){
                    dp[i][j]=dp[i-1][j-1]+1;
                    mark[i][j]="↖ ";
                }else{
                    if(dp[i-1][j]<dp[i][j-1]){
                        mark[i][j]="← ";
                    }else if(dp[i-1][j]>dp[i][j-1]){
                        mark[i][j]="↑ ";
                    }else {
                        mark[i][j]="W ";
                    }
                    dp[i][j]=Math.max(dp[i-1][j],dp[i][j-1]);
                }
            }
        }
        //打印mark标记
        for (int i = 0; i < str1.length()+1; i++) {
            for (int j = 0; j < str2.length()+1; j++) {
                System.out.print(mark[i][j]);
            }
            System.out.println();
        }
        System.out.println();
        //dfs把子序列存储在set中
        String str = "";
        HashSet<String> set = new HashSet<>();
        dfs(mark,str,set,mark.length-1,mark[0].length-1,str1);
        int num=0;
        //打印子序列
        for(String ans:set){
            num++;
            System.out.println("第"+num+"个公共子序列是:");
            for (int i = ans.length()-1; i >=0 ; i--) {
                if(i!=0) {
                    System.out.print(ans.charAt(i) + ",");
                }else {
                    System.out.println(ans.charAt(i));
                }
            }
        }
        return dp[str1.length()][str2.length()];
    }
    //第三个方法,优化的dp算法,用一维数组存储状态，引入res和tmp保持数组更新前的状态
    public static int dp02(String str1,String str2){
        int[] dp = new int[str2.length()+1];
        int tmp=0;
        int res=0;
        for (int i = 1; i < str1.length()+1; i++) {
            res=dp[0];
            for (int j = 1; j < str2.length()+1; j++) {
                tmp=dp[j];
                if(str1.charAt(i-1)==str2.charAt(j-1)){
                    dp[j]=res+1;
                }else {
                    dp[j]=Math.max(dp[j-1],dp[j]);
                }
                res=tmp;
            }
        }
        return dp[str2.length()];
    }
    public static void main(String[] args) {
        System.out.println("一共有"+dp01( "137175","19251")+"个公共子序列");
    }
}

