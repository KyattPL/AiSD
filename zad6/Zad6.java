import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Zad6 {

    public static ArrayList<ArrayList<Integer>> macierz;
    public static Integer P, R, I;

    public static void main(String[] args) {
        System.out.print("Podaj P: ");
        Scanner scan = new Scanner(System.in);
        P = scan.nextInt();

        System.out.print("Podaj R: ");
        R = scan.nextInt();

        System.out.print("Podaj I: ");
        I = scan.nextInt();
        scan.close();

        wczytajPlik("z6data.csv");
        wypiszMacierz();
        generujPopulacje();
    }

    // Metoda służąca do wczytywania macierzy z pliku
    public static void wczytajPlik(String nazwaPliku) {
        try (BufferedReader br = new BufferedReader(new FileReader(nazwaPliku))) {
            macierz = new ArrayList<ArrayList<Integer>>();
            String linia;
            // Dopóki są kolejne linie to je czytamy
            while ((linia = br.readLine()) != null) {
                String[] temp = linia.split(",");
                ArrayList<Integer> lista = new ArrayList<Integer>();
                for (String element : temp) {
                    if (element.equals("X")) {
                        lista.add(0);
                    } else {
                        lista.add(Integer.parseInt(element));
                    }
                }
                macierz.add(lista);
            }
        } catch (EOFException e) {
            // Wyjątek w zasadzie zamierzony, dlatego nic nie robimy
        } catch (Exception e) {
            // Każdy inny wypisujemy
            e.printStackTrace();
        }
    }

    // Metoda służąca do wypisywania macierzy
    public static void wypiszMacierz() {
        // Dla każdego wiersza w macierzy
        for (ArrayList<Integer> wiersz : macierz) {
            // Dla każdego pola w wierszu
            for (Integer pole : wiersz) {
                // Wypisujemy pole
                System.out.print(pole + " ");
            }
            // Newline dla czytelności
            System.out.println();
        }
    }

    public static ArrayList<ArrayList<Integer>> generujPopulacje() {
        ArrayList<ArrayList<Integer>> populacja = new ArrayList<ArrayList<Integer>>();
        Random rand = new Random();

        int licznik = 0;
        int rozmiar = macierz.size();

        while (licznik != P) {
            int wewnLicznik = rozmiar;
            ArrayList<Integer> osobnik = new ArrayList<Integer>();
            ArrayList<Integer> indeksy = new ArrayList<Integer>();
            int indeksLicznik = 0;
            while (indeksLicznik != rozmiar) {
                indeksy.add(licznik);
                indeksLicznik += 1;
            }

            while (wewnLicznik != 0) {
                int temp = indeksy.remove(rand.nextInt(indeksy.size()));
                osobnik.add(temp);
                wewnLicznik -= 1;
            }
            populacja.add(osobnik);
            licznik += 1;
        }

        return populacja;
    }
}