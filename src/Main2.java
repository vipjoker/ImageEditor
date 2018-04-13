public class Main2 {
    public static void main(String[] args) {
        System.out.println("hello world");
        int [] array = {8,24,3,20,1,17};
        int [] array2 = {7,21,3,42,3,7};

        System.out.println(check(new int[]{0, 1,2,2,2, 2,4}, 4));
        System.out.println(check(new int[]{1, 1,  3}, 2));
    }



    private static boolean check(int[] A,int K){
        int n = A.length;
        for (int i = 0; i < n - 1; i++) {
            if (A[i] + 1 < A[i + 1] )
                return false;
        }
        if (A[0] != 1 || A[n - 1] != K)
            return false;
        else
            return true;
    }

    private static int countMinimumDistance(int[] array) {
        int min = -1;
        for (int i = 0; i < array.length; i++) {
            for (int j = array.length -1; j > i; j--) {
                int diff = Math.abs(array[i] - array[j]);
                if(diff < min || min == -1)min = diff;
            }
        }
        return min;
    }
}
