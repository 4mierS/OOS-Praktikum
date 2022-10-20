import bank.Payment;
import bank.Transfer;

public class Main {
    public static void main(String[] args) {
        Payment p1 = new Payment("2022-01-01", "P1 description", 100, 0.1, 0.12);
        Payment p2 = new Payment("2022-01-02", "P2 description", 200);
        p2.setIncomingInterest(0.05);
        p2.setOutgoingInterest(0.12);
        Payment p3 = new Payment(p2);
        p3.setDescription("P3 description");
        p3.setDate("2022-01-03");


        System.out.println("Printing Payments");
        System.out.println("-----------------");
        p1.printObject();
        System.out.println("-----------------");
        p2.printObject();
        System.out.println("-----------------");
        p3.printObject();
        System.out.println();

        Transfer t1 = new Transfer("2022-01-01", "T1 description", 100);
        t1.setSender("T1 sender");
        t1.setRecipient("T1 recipient");
        Transfer t2 = new Transfer("2022-01-02", "T2 description", 200, "T2 sender", "T2 recipient");
        Transfer t3 = new Transfer(t2);
        t3.setDescription("T3 description");
        t3.setDate("2022-01-03");

        System.out.println("Printing Transfers");
        System.out.println("-----------------");
        t1.printObject();
        System.out.println("-----------------");
        t2.printObject();
        System.out.println("-----------------");
        t3.printObject();

    }
}
