package arduino;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        String s = scan.nextLine();
        int n = s.length();

        int last = -1;
        int end = -1;
        ArrayList<Borders> queue = new ArrayList<>(n);
        queue.add(++end, new Borders(0,n-1));
        int p=0;
        while (p<=end) {
            Borders borders = queue.get(p++);
            int mid = (borders.l + borders.r) / 2;
            if (mid < last)
                continue;
            if (mid + 1 <= borders.r) queue.add(++end, new Borders(mid + 1, borders.r));

            if (isLetter(s.charAt(mid))){
                last = mid;
                end = p;
                if (mid + 1 <= borders.r) queue.add(++end, new Borders(mid + 1, borders.r));
            }else
                if (last<=mid-1) queue.add(++end, new Borders(last,mid-1));
        }
        System.out.println(end);
        System.out.println(last);
    }

    private static boolean isLetter(char c){
        return ('a'<=c && c<='z');
    }

    static class Borders{
        int l;
        int r;

        public Borders(int l, int r) {
            this.l = l;
            this.r = r;
        }
    }
}
