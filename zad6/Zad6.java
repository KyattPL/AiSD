import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
//import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

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
        // wypiszMacierz();
        ArrayList<ArrayList<Integer>> populacja = generujPopulacje();
        int iteracje = 0;
        int podzialIteracji = I / 10;
        while (iteracje != I) {
            int mfc = MFC(populacja);
            populacja = klonowanie(populacja, mfc);
            generujOsobniki(populacja);
            iteracje += 1;

            if (iteracje % podzialIteracji == 0) {
                System.out.println("Iteracja: " + iteracje);
                System.out.println("MFC: " + mfc);
                najlepsze5(populacja, mfc);
                System.out.println();
            }
        }
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
                indeksy.add(indeksLicznik);
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

    public static void generujOsobniki(ArrayList<ArrayList<Integer>> osobniki) {
        Random rand = new Random();

        int licznik = 0;
        int rozmiar = macierz.size();

        while (licznik != R) {
            int wewnLicznik = rozmiar;
            ArrayList<Integer> osobnik = new ArrayList<Integer>();
            ArrayList<Integer> indeksy = new ArrayList<Integer>();
            int indeksLicznik = 0;
            while (indeksLicznik != rozmiar) {
                indeksy.add(indeksLicznik);
                indeksLicznik += 1;
            }

            while (wewnLicznik != 0) {
                int temp = indeksy.remove(rand.nextInt(indeksy.size()));
                osobnik.add(temp);
                wewnLicznik -= 1;
            }
            osobniki.add(osobnik);
            licznik += 1;
        }
    }

    public static int FC(ArrayList<Integer> osobnik) {
        int suma = 0;
        int index = 0;

        while (index != (osobnik.size() - 1)) {
            int innerIndex = index + 1;
            int room = osobnik.get(index);
            int odleglosc = 1;
            while (innerIndex != osobnik.size()) {
                int nextRoom = osobnik.get(innerIndex);
                suma += odleglosc * macierz.get(room).get(nextRoom);
                innerIndex += 1;
                odleglosc += 1;
            }
            index += 1;
        }
        return suma;
    }

    public static int MFC(ArrayList<ArrayList<Integer>> osobniki) {
        int suma = 0;
        for (ArrayList<Integer> osobnik : osobniki) {
            suma += FC(osobnik);
        }
        return suma / P;
    }

    public static ArrayList<ArrayList<Integer>> klonowanie(ArrayList<ArrayList<Integer>> osobniki, int MFC) {
        ArrayList<Double> mfcWszystkich = new ArrayList<Double>();
        int suma = 0;
        int index = 0;
        for (ArrayList<Integer> osobnik : osobniki) {
            mfcWszystkich.add((double) MFC / (double) FC(osobnik));
        }
        int[] indeksy = IntStream.range(0, mfcWszystkich.size()).boxed()
                .sorted((i, j) -> (int) Math.signum(mfcWszystkich.get(j) - mfcWszystkich.get(i))).mapToInt(ele -> ele)
                .toArray();

        while (suma < (P - R)) {
            suma += (int) Math.round(mfcWszystkich.get(indeksy[index]));
            index += 1;
        }
        index -= 1;
        int ileOstatniemu = (int) Math.round(mfcWszystkich.get(indeksy[index]));
        if (suma > (P - R)) {
            int ileOdjac = suma - (P - R);
            suma -= ileOdjac;
            mfcWszystkich.set(indeksy[index], (double) (ileOstatniemu - ileOdjac));
        }
        ArrayList<ArrayList<Integer>> nowePokolenie = new ArrayList<ArrayList<Integer>>();
        int stopIndex = 0;
        while (stopIndex <= index) {
            int nrOsobnika = indeksy[stopIndex];
            int ileKlonow = (int) Math.round(mfcWszystkich.get(indeksy[stopIndex]));
            while (ileKlonow != 0) {
                nowePokolenie.add(mutujOsobnika(osobniki.get(nrOsobnika)));
                ileKlonow -= 1;
            }
            stopIndex += 1;
        }
        for (ArrayList<Integer> osobnik : nowePokolenie) {
            // wypiszOsobnika(osobnik);
        }
        // System.out.println();

        return nowePokolenie;
    }

    public static ArrayList<Integer> mutujOsobnika(ArrayList<Integer> osobnik) {
        ArrayList<Integer> zmutowany = new ArrayList<Integer>();
        Random rand = new Random();

        for (int pokoj : osobnik) {
            zmutowany.add(pokoj);
        }
        int pierwszyIndex = rand.nextInt(osobnik.size());
        int drugiIndex = rand.nextInt(osobnik.size());
        int pierwszyPokoj = zmutowany.get(pierwszyIndex);
        int drugiPokoj = zmutowany.get(drugiIndex);
        zmutowany.set(pierwszyIndex, drugiPokoj);
        zmutowany.set(drugiIndex, pierwszyPokoj);

        if (FC(zmutowany) < FC(osobnik)) {
            return zmutowany;
        } else {
            return osobnik;
        }
    }

    public static void najlepsze5(ArrayList<ArrayList<Integer>> osobniki, int MFC) {
        ArrayList<Integer> mfcWszystkich = new ArrayList<Integer>();
        HashSet<ArrayList<Integer>> juzWypisane = new HashSet<ArrayList<Integer>>();

        for (ArrayList<Integer> osobnik : osobniki) {
            mfcWszystkich.add((int) Math.round((double) MFC / (double) FC(osobnik)));
        }
        int[] indeksy = IntStream.range(0, mfcWszystkich.size()).boxed()
                .sorted((i, j) -> mfcWszystkich.get(j) - mfcWszystkich.get(i)).mapToInt(ele -> ele).toArray();

        int wypisane = 0;
        int index = 0;
        while (wypisane != 5) {
            ArrayList<Integer> osobnik = osobniki.get(indeksy[index]);
            index += 1;
            if (juzWypisane.contains(osobnik)) {
                continue;
            } else {
                wypiszOsobnika(osobnik);
                juzWypisane.add(osobnik);
            }
            wypisane += 1;
        }
    }

    public static void wypiszOsobnika(ArrayList<Integer> osobnik) {
        for (int pokoj : osobnik) {
            System.out.print(pokoj + " ");
        }
        System.out.println("[" + FC(osobnik) + "]");
    }
}