package pers.zr.opensource.magic.kit.common;

/**
 * 泊松分布
 * 非均匀返回指定数字左右两边的数
 * 
 * @author zhurong
 *
 */
public class PossionUtil {

    public static void main(String[] args) {

        int k = 90;

        //泊松分布对100以内有效，超过3位数产生的值为固定值
        int t = k;
        int s = 1;
        if(String.valueOf(k).length()>2) {
            t = Integer.parseInt(String.valueOf(k).substring(0, 2));
            s = k / t;
        }

        for(int i=0; i<100; i++)
          System.out.println(getPossionVariable(t) * s);


    }

    private static double getPossionProbability(int k, double lamda) {
        double c = Math.exp(-lamda), sum = 1;
        for (int i = 1; i <= k; i++) {
            sum *= lamda / i;
        }
        return sum * c;
    }

    public static int getPossionVariable(double lamda) {
        int x = 0;
        double y = Math.random(), cdf = getPossionProbability(x, lamda);
        while (cdf < y) {
            x++;
            cdf += getPossionProbability(x, lamda);
        }
        return x;
    }
}
