package interview.soft123.invertstring;

public class InvertSubString {

    static void invert(char[] buffer, int first, int last) {
        int qty=last-first+1;
        for (int iter=0; iter<qty/2; iter++) {
            int idx1=first-1+iter;
            int idx2=last-1-iter;
            char v=buffer[idx1];
            buffer[idx1]=buffer[idx2];
            buffer[idx2]=v;
        }
    }

    public static void main(String[] args) {
        char[] chars = {'A', 'B', 'C', 'D', 'E'};
        System.out.println(chars);
        invert(chars, 1, 2);
        System.out.println(chars);
    }
}
