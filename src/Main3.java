public class Main3 {

    public static void main(String[] args) {

        System.out.println(intToBinary());
    }


    public static int intToBinary()
    {
        try {
            return 777;
        }
        finally {
            closeResource();
        }
    }

    private static void closeResource() {
        System.out.println("Closed");
    }

    class Node {
        Node  left;
        Node right;
    }
}
