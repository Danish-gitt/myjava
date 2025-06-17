import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;

import static javax.swing.UIManager.get;


class RealBank {
    HashMap<Integer, String> map = new HashMap<>();


    private String name;
    private static int baseacno=100;
    private int acno;
    private int inbal;

    public int createAcno() {
        int lastAcno=99;
        try{
            File f = new File("bank.csv");
            if(!f.exists()){
                return 100;
            }
            Scanner scf = new Scanner(f);
            while(scf.hasNextLine()){
                String line = scf.nextLine();
                String[] col = line.split(",");
                int cacno= Integer.parseInt(col[0]);
                if(cacno>lastAcno){
                    lastAcno=cacno;
                }
            }
            scf.close();
        }catch(IOException e){
            System.out.println("Error: Failed to read account number from bank.csv");
        }
        return lastAcno + 1;
    }

    public void storeData(int acno,String name,int inbal) {
        try {
            FileWriter fw = new FileWriter("bank.csv", true);
            fw.write(acno + "," + name + "," + inbal +"\r\n");
            fw.close();
        } catch (IOException e) {
            System.out.println("Error: Failed to write into bank.csv");
        }
    }


    public void openAccount(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your name: ");
        name=sc.nextLine();
        System.out.print("Enter initial balance to deposit: ");
        inbal=sc.nextInt();
        //acno=baseacno++;
        acno=createAcno();
        System.out.println("Congratulations! your account is opened in our bank with below details");
        System.out.println("Name: "+name +"  Account No: "+acno +"  Balance: "+inbal);
        storeData(acno,name,inbal);
    }

    public void loadToHash(){
        try{
            File fl = new File("bank.csv");
            Scanner scfl = new Scanner(fl);
            while(scfl.hasNextLine()){
                String line=scfl.nextLine();
                String[] col = line.split(",");
                int acno=Integer.parseInt(col[0]);
                int bal=Integer.parseInt(col[2]);
                map.put(acno,col[1]+","+bal);
            }
            scfl.close();
        }catch (IOException e){
            System.out.println("Error: Failed to load bank.csv into hashmap");
        }
    }



    public void checkBalance() {
        loadToHash();
        Scanner scc = new Scanner(System.in);
        System.out.print("Enter Account Number: ");
        int acno = scc.nextInt();
        if (map.get(acno) == null) {
            System.out.println("Sorry! No Account found");
            return;
        } else {
            String line = map.get(acno);
            String[] col = line.split(",");
            String name = col[0];
            int bal = Integer.parseInt(col[1]);
            //System.out.println("Account Number: "+acno +"  Name: "+name +"  Balance: "+bal );
            System.out.printf("Hi %s, your Available balance is %d", name, bal).println();
        }
    }

    public void checkBalance(int acno) {
        loadToHash();
        Scanner scc = new Scanner(System.in);
        if (map.get(acno) == null) {
            System.out.println("Sorry! No Account found");
            return;
        } else {
            String line = map.get(acno);
            String[] col = line.split(",");
            String name = col[0];
            int bal = Integer.parseInt(col[1]);
            //System.out.println("Account Number: "+acno +"  Name: "+name +"  Balance: "+bal );
            System.out.printf("Hi %s, your Available balance is %d", name, bal).println();
        }
    }

    public int getBalance(int acno){
        loadToHash();
        String line=map.get(acno);
        String[] col = line.split(",");
        return Integer.parseInt(col[1]);
    }

    public void updateBalance(int ac,int newbal){
        File orig = new File("bank.csv");
        File temp = new File("tmp.csv");
        try{
            FileWriter fw = new FileWriter("tmp.csv",true);
            Scanner scfw = new Scanner(orig);
            while(scfw.hasNextLine()){
                String line = scfw.nextLine();
                String[] col = line.split(",");
                int acno= Integer.parseInt(col[0]);
                String name = col[1];
                int bal= Integer.parseInt(col[2]);
                if(acno == ac){
                    fw.write(acno +"," +name +"," +newbal +"\r\n");
                }
                else{
                    fw.write(line +"\r\n");
                }
            }
            fw.close();
            scfw.close();
        }catch(IOException e){
            System.out.println("Error: Failed to update file bank.csv");
        }
        orig.delete();
        temp.renameTo(orig);
    }

    public void deposit(){
        loadToHash();
        Scanner scd = new Scanner(System.in);
        System.out.print("Enter Account Number: ");
        int acno = scd.nextInt();
        if(map.get(acno) == null){
            System.out.println("Sorry! No Account found");
            return;
        }else{
            System.out.print("Enter Amount to deposit: ");
            int dbal = scd.nextInt();
            if(dbal>0){
                int cbal = getBalance(acno);
                int newbal = cbal + dbal;
                updateBalance(acno,newbal);
                System.out.printf("Dear Customer %d, Amount %d has been deposited into your account",acno,dbal).println();
                checkBalance(acno);
            }else{
                System.out.println("Sorry! Invalid Amount");
                return;
            }
        }
    }

    public void withdraw() {
        loadToHash();
        Scanner scw = new Scanner(System.in);
        System.out.print("Enter Account Number: ");
        int acno = scw.nextInt();
        if (map.get(acno) == null) {
            System.out.println("Sorry! No Account found");
            return;
        }else{
            System.out.print("Enter Amount to withdraw: ");
            int wbal = scw.nextInt();
            int cbal = getBalance(acno);
            if(cbal>=wbal){
                int newbal = cbal - wbal;
                updateBalance(acno,newbal);
                System.out.printf("Dear Customer %d, Amount %d has been withdrawl from your account",acno,wbal).println();
                checkBalance(acno);
            }else{
                System.out.println("Sorry! Invalid Amount");
                return;
            }
        }
    }



}


class RealBankRunner{
    public static void main(String[] args){
	    RealBank r1 = new RealBank();
	    Scanner sc = new Scanner(System.in);
        while(true){
          System.out.println("\n====== Welcome to RealBank ======");
          System.out.println("1. Open New Account");
          System.out.println("2. Check Balance");
          System.out.println("3. Deposit");
          System.out.println("4. Withdrawl");
          System.out.println("5. Exit");
          System.out.print("Choose an option: ");

          int ch = sc.nextInt();
          switch (ch){
              case 1:
                  r1.openAccount();
                  break;
              case 2:
                  r1.checkBalance();
                  break;
              case 3:
                  r1.deposit();
                  break;
              case 4:
                  r1.withdraw();
                  break;
              case 5:
                  System.out.println("Thankyou for banking with Real bank");
                  sc.close();
                  System.exit(0);
                  break;
              default:
                  System.out.println("Invalid option. Please choose 1-5.");
          }
      }
    }
}
