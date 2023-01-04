import bank.*;
import bank.exceptions.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.*;

public class PrivateBankTest {

    private PrivateBank bank;
    private PrivateBank bank2;

    private List<Transaction> transactionLisa;
    private List<Transaction> transactiontest;
    static UUID uuid = UUID.randomUUID();

    @BeforeEach
    void init() {
        try {
            bank = new PrivateBank("Meine Bank", 0.2, 0.2);

            transactionLisa = new ArrayList<>();
            bank.createAccount("Lisa", transactionLisa);


            //Fail because of wrong amount balance
            //If there is a Transfer to another account, the amount need to get subtracted from the balance
            //If there is a Transfer from another account, the amount need to get added to the balance
            //Watch @getAccountBalanceTest for more information
            Payment a = new Payment("01.01.2020", "Gehalt", 2000, 0.3, 0.4);
            bank.addTransaction("Lisa", a);
            bank.addTransaction("Lisa", new Transfer("01.01.2020", "Miete", 10, "Lisa", "Hans"));
            bank.addTransaction("Lisa", new  Transfer("01.01.2020", "Miete", 10, "Lisa", "Franz"));
            transactionLisa.add(a);

            transactionLisa.add(new IncomingTransfer("01.01.2019", "Ausgeliehen", 2000, "Herr Mustermann", "Lisa"));
            transactionLisa.add(new OutgoingTransfer("01.01.2019", "Miete", 2000, "Lisa", "Frank"));

            bank2 = new PrivateBank(bank);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void testConstructors() throws NumericValueInvalidException {
        System.out.println("Test Constructors");

        assertEquals(bank, bank2);
        bank2.setOutgoingInterest(0.3);
        assertNotEquals(bank, bank2);

    }

    @Test
    public void toStringTest() throws ArithmeticException, NumericValueInvalidException {
        // test toString
        System.out.println("Test toString");

        String bankString = bank.toString();
        String bank2String = bank2.toString();
        assertEquals(bankString, bank2String);

        bank2.setOutgoingInterest(0.1);
        String bank3String = bank2.toString();
        assertNotEquals(bankString, bank3String);
    }


    @Test
    public void createAccountTest() throws AccountAlreadyExistsException, NumericValueInvalidException, TransactionAlreadyExistException, TransactionAttributeException {
        System.out.println("Test createAccount");

        Payment a = new Payment("01.01.2020", "Miete", 1000, 0.3, 0.4);
        // test createAccount
        assertThrows(AccountAlreadyExistsException.class, () -> bank.createAccount("Lisa", transactionLisa));
        assertThrows(TransactionAlreadyExistException.class, () -> bank.addTransaction("Lisa", transactionLisa.set(0, a)));
    }

    @Test
    public void addTransactionTest() throws AccountAlreadyExistsException, NumericValueInvalidException, TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException {
        System.out.println("Test addTransaction");

        assertTrue(bank.containsTransaction("Lisa",transactionLisa.get(0)));
        assertEquals(3, bank.getTransactions("Lisa").size());
    }

    @Test
    public void removeTransactionTest() throws AccountAlreadyExistsException, NumericValueInvalidException, TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException, TransactionDoesNotExistException {
        System.out.println("Test removeTransaction");

        assertTrue(bank.containsTransaction("Lisa",transactionLisa.get(0)));
        bank.removeTransaction("Lisa", transactionLisa.get(0));
        assertFalse(bank.containsTransaction("Lisa",transactionLisa.get(0)));
    }

    @Test
    public void containsTransactionTest() throws TransactionAlreadyExistException, NumericValueInvalidException, AccountDoesNotExistException, TransactionAttributeException, AccountAlreadyExistsException {
        System.out.println("Test containsTransaction");

        try{
        assertTrue(bank.containsTransaction("Lisa", transactionLisa.get(0)));

        //tests if new Transfers are added to the list with a negative amount
        //test the setAmount of the Transfer class as well
        assertFalse(bank.containsTransaction("Lisa", new Transfer("01.01.2020", "Miete", -1000, "Lisa", "Hans")));
        }catch (NumericValueInvalidException e){
        }
    }

    @Test
    public void getAccountBalanceTest() throws TransactionAlreadyExistException, NumericValueInvalidException, AccountDoesNotExistException, TransactionAttributeException, AccountAlreadyExistsException {
        System.out.println("Test getAccountBalance");

        //excepted 1380 because: (2000 * 0.7 "Gehalt") + (-10 - 10 "friends") + ((-2000 * 0.2) + (2000 * 0.2) "Payment") = 1380
        assertEquals(1620, bank.getAccountBalance("Lisa"));
    }

    @Test
    void getTransactionsTest() throws NumericValueInvalidException, AccountAlreadyExistsException, TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException {
        System.out.println("Test getTransactions");

        transactiontest = new ArrayList<>();
        Payment a = new Payment("01.01.2020", "Gehalt", 2000, 0.2, 0.2);
        transactiontest.add(a);
        transactiontest.add(new Transfer("01.01.2020", "Miete", 10, "Lisa", "Hans"));
        transactiontest.add(new Transfer("01.01.2020", "Miete", 10, "Lisa", "Franz"));

        assertEquals(transactiontest, bank.getTransactions("Lisa"));

        OutgoingTransfer t3 = new OutgoingTransfer("12.01.2019", "Bad", 200, "Lisa", "Frank");
        transactiontest.add(t3);
        assertNotEquals(transactiontest, bank.getTransactions("Lisa"));
    }

    @Test
    void getTransactionSortedTest() throws NumericValueInvalidException, TransactionAlreadyExistException, AccountAlreadyExistsException, TransactionAttributeException {
        System.out.println("Test getTransactionSorted");

        IncomingTransfer t1 = new IncomingTransfer("01.01.2019", "Gehalt", 2300, "Herr Mustermann", "Lisa");
        OutgoingTransfer t2 = new OutgoingTransfer("01.01.2019", "Miete", 1, "Lisa", "Frank");

        try {
            bank.addTransaction("Lisa", t1);
            bank.addTransaction("Lisa", t2);

            assertEquals(t1, bank.getTransactionsSorted("Lisa", false).get(0));
            assertEquals(t2, bank.getTransactionsSorted("Lisa", true).get(0));

        OutgoingTransfer t3 = new OutgoingTransfer("12.01.2019", "Bad", 200, "Lisa", "Frank");
        transactionLisa.add(t3);
        assertNotEquals(transactionLisa, bank.getTransactions("Lisa"));

        List<Transaction> sorted = new ArrayList<>();
        sorted.add(t2);
        sorted.add(t1);

        List<Transaction> sortedResult1 = bank.getTransactionsSorted("Lisa", true);
        List<Transaction> sortedResult2 = bank.getTransactionsSorted("Lisa", false);

        assertNotEquals(sorted, sortedResult1);
        assertNotEquals(sorted, sortedResult2);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Test
    void getTransactionsByType() throws NumericValueInvalidException, TransactionAlreadyExistException, AccountAlreadyExistsException, TransactionAttributeException {
        System.out.println("Test getTransactionsByType");

        List<Transaction> positiveResult = bank.getTransactionsByType("Lisa", true);
        List<Transaction> negativeResult = bank.getTransactionsByType("Lisa", false);

        assertEquals(3, positiveResult.size());
        assertEquals(0, negativeResult.size());
    }

    @Test
    public void WriteReadTest() throws IOException {
        bank.setDirectoryName("test2");
        bank.readAccounts();

        bank.setDirectoryName("test_Prak"+uuid.toString());

        bank.writeAccount("Hans");
        bank.writeAccount("Anna");
        bank.writeAccount("Lisa");

        assertFalse(bank.getTransactions("Hans").isEmpty());
        assertTrue(bank.getTransactions("Anna").isEmpty());
    }


    @ParameterizedTest
    @ValueSource(strings = {"Anna", "Frank"})
    public void paramterizedTest(String pName){
        assertDoesNotThrow(() -> bank.createAccount(pName));
    }

    @Test
    public void exceptionTest() throws  NumericValueInvalidException, TransactionAlreadyExistException, AccountAlreadyExistsException, AccountDoesNotExistException, TransactionAttributeException {
        System.out.println("Test exception");

        try {
        bank.createAccount("Hans");
        assertThrows(AccountAlreadyExistsException.class, () -> bank.createAccount("Hans"));
        assertThrows(AccountDoesNotExistException.class, () -> bank.addTransaction("Mensch", new Transfer("01.01.2020", "Miete", 10, "Lisa", "Franz")));
        assertThrows(TransactionAttributeException.class, () -> bank.addTransaction("Hans", new Transfer("01.01.2020", "Miete", 0, "Lisa", "Franz")));

        bank.addTransaction("Hans", new Transfer("01.01.2020", "Miete", 10, "Hans", "Franz"));
        assertThrows(TransactionAlreadyExistException.class, () -> bank.addTransaction("Hans", new Transfer("01.01.2020", "Miete", 10, "Hans", "Franz")));

        assertThrows(NumericValueInvalidException.class, () -> bank.addTransaction("Hans", new Transfer("01.01.2020", "Miete", -10, "Hans", "Franz")));
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @AfterEach
    void deleteBank(){
        bank = null;
        bank2 = null;

        transactionLisa = null;
        transactiontest = null;
    }

    @AfterAll
    static void deleteTestFiles(){
        File file = new File("C:\\Users\\JCC\\IdeaProjects\\OOS-Praktikum\\persist\\test_Prak"+uuid.toString());;
        if(file.exists()){
            for(File f : file.listFiles()){
                f.delete();
            }
            file.delete();
        }
    }
}
