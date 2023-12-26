import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;

public class App {
    App gp = new App();
    static final String driver = "com.mysql.jdbc.Driver";
    static final String url = "jdbc:mysql://localhost:3306/jdbc";
    static final String user = "root";
    static final String pass = "";

    static Connection conn;
    static Statement stat;
    static ResultSet result;

    public static void main(final String[] args) throws Exception {

        faktur gipa = new faktur();
        gipa.login();

        Scanner scanner = new Scanner(System.in);

        try {
            Class.forName(driver);

            conn = DriverManager.getConnection(url, user, pass);
            stat = conn.createStatement();

            System.out.print("Masukkan Nomor Faktur: ");
            String nomorFaktur = scanner.nextLine();

            System.out.print("Masukkan Nama Pelanggan: ");
            String namaPelanggan = scanner.nextLine();

            System.out.print("Masukkan Nomor HP: ");
            String nomorHp = scanner.nextLine();

            System.out.print("Masukkan Alamat: ");
            String alamat = scanner.nextLine();

            String sql = "INSERT INTO dapel (noFaktur, nama, noHp, alamat) VALUE ('%s', '%s', '%s', '%s' )";
            sql = String.format(sql, nomorFaktur, namaPelanggan, nomorHp, alamat);
            stat.execute(sql);

            System.out.print("Masukkan Kode Barang: ");
            String kodeBarang = scanner.nextLine();

            System.out.print("Masukkan Nama Barang: ");
            String namaBarang = scanner.nextLine();

            System.out.print("Masukkan Harga Barang: ");
            Integer hargaBarang = Integer.parseInt(scanner.nextLine());
            if (hargaBarang > 10000) {
                throw new IllegalArgumentException("Harga barang tidak boleh lebih dari 500000");
            }

            System.out.print("Masukkan Jumlah Beli: ");
            Integer jumlahBeli = Integer.parseInt(scanner.nextLine());
            if (jumlahBeli > 10000) {
                throw new IllegalArgumentException("Jumlah beli tidak boleh lebih dari 100");
            }

            while (!conn.isClosed()) {
                showMenu();
            }
            stat.close();
            conn.close();

        } catch (NumberFormatException e) {
            System.out.println("Input harus berupa angka");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        scanner.close();
    }

    static Scanner scanner = new Scanner(System.in);

    static void showMenu() {
        System.out.println("\n        MENU UTAMA ");
        System.out.println("==============================");
        System.out.println("1. Show Data");
        System.out.println("2. Edit Data");
        System.out.println("3. Delete Data");
        System.out.println("0. Keluar");
        System.out.println("");
        System.out.print("PILIHAN> ");

        try {
            Integer pilihan = Integer.parseInt(scanner.nextLine());

            switch (pilihan) {
                case 0:
                    System.exit(0);
                    break;
                case 1:
                    showData();
                    break;
                case 2:
                    updateData();
                    break;
                case 3:
                    deleteData();
                    break;
                default:
                    System.out.println("Pilihan salah!");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void showData() {
        String sql = "SELECT * FROM dapel";

        try {
            result = stat.executeQuery(sql);

            System.out.println("   DATA PELANGGAN GIP STORE  ");
            System.out.println("------------------------------");

            while (result.next()) {
                String no_faktur = result.getString("noFaktur");
                String nama = result.getString("nama");
                String no_hp = result.getString("noHp");
                String alamat = result.getString("alamat");

                System.out.println(String.format("%s. %s -- %s -- (%s)", no_faktur, nama, alamat, no_hp));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static void updateData() {
        try {

            // ambil input dari user
            System.out.print("Faktur yang mau diedit : ");
            String no_faktur = scanner.nextLine();
            System.out.print("Nama Pelanggan : ");
            String nama = scanner.nextLine().trim();
            System.out.print("Nomor HP : ");
            String no_hp = scanner.nextLine().trim();
            System.out.print("Alamat : ");
            String alamat = scanner.nextLine().trim();

            // query update
            String sql = "UPDATE dapel SET nama='%s', noHp='%s', alamat='%s' WHERE noFaktur='%s'";
            sql = String.format(sql, nama, no_hp, alamat, no_faktur);

            // update data Data
            stat.execute(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void deleteData() {
        try {

            // ambil input dari user
            System.out.print("Masukkan kode faktur yang mau di hapus : ");
            String no_faktur = (scanner.nextLine());

            // buat query hapus
            String sql = String.format("DELETE FROM dapel WHERE noFaktur='%s'", no_faktur);

            // hapus data
            stat.execute(sql);

            System.out.println("Data terhapus");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
