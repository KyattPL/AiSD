import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Zad5 {

    // Nasz graf pełny ważony miast przechowywany jako macierz
    public static ArrayList<ArrayList<String>> macierz;

    // Główna funkcja programu
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        // Wczytujemy nazwę pliku do otwarcia
        System.out.print("Podaj plik do otwarcia: ");
        String plik = scan.nextLine();
        // wczytajPlik() wczytuje odpowiedni plik .csv i zapisuje
        // dane miasta w macierzy
        wczytajPlik(plik);
        // metoda do wyświetlenia macierzy miast (bez nagłówków kolumn, które są zbędne
        // dla macierzy kwadratowej)
        wypiszMacierz();
        // Wczytywanie miasta startowego przez użytkownika
        System.out.print("Podaj miasto startowe: ");
        String start = scan.nextLine();
        scan.close();
        // Wyznaczamy trasę począwszy od miasta w zmiennej 'start'
        wyznaczTrase(start);
    }

    // Metoda służąca do wczytywania macierzy miast z pliku
    public static void wczytajPlik(String nazwaPliku) {
        try (BufferedReader br = new BufferedReader(new FileReader(nazwaPliku))) {
            // Tworzę pustą macierz
            macierz = new ArrayList<ArrayList<String>>();
            // Zmienna służąca do wczytywania kolejnych linii z pliku.
            // Wczytuje pierwszą linię, ponieważ pierwszy wiersz jest mi niepotrzebny.
            // (nagłówki nie są wymagane przy macierzy kwadratowej)
            String linia = br.readLine();
            // Dopóki są kolejne linie to je czytamy
            while ((linia = br.readLine()) != null) {
                // Rozdzielamy tekst względem przecinka
                String[] temp = linia.split(",");
                // Tworzymy nową listę (która będzie pojedynczym wierszem naszej macierzy)
                ArrayList<String> lista = new ArrayList<String>();
                // Zapełniamy listę pojedynczymi elementami z wiersza
                for (String element : temp) {
                    lista.add(element.trim());
                }
                // Dodajemy do macierzy zapełnioną listę (dodajemy wiersz)
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
        for (ArrayList<String> wiersz : macierz) {
            // Dla każdego pola w wierszu
            for (String pole : wiersz) {
                // Wypisujemy pole
                System.out.print(pole + " ");
            }
            // Newline dla czytelności
            System.out.println();
        }
    }

    // Metoda służąca do wyznaczania najmniejszej trasy z miasta startowego przy
    // wykorzystaniu strategii zachłannej (Odwiedzamy wszystkie miasta i wracamy na
    // start).
    public static void wyznaczTrase(String start) {
        // Lista służąca do przechowywania kolejności odwiedzanych miast (tzn ich nazwy
        // po kolei).
        ArrayList<String> kolejnoscMiast = new ArrayList<String>();
        // Lista służąca do przechowywania pokonywanych odległości przy dojeździe do
        // następnego miasta
        ArrayList<Integer> kolejneOdleglosci = new ArrayList<Integer>();
        // Zbiór służący do przechowywania już odwiedzonych miast
        HashSet<Integer> odwiedzoneMiasta = new HashSet<Integer>();
        // Zaczynamy podróż od miasta startowego
        kolejnoscMiast.add(start);

        // Pętla służąca do sprawdzenia, w którym rzędzie macierzy znajduje się nasze
        // startowe miasto. Wynik przechowywany jest naraz w 'aktualneMiastoIndex' oraz
        // 'startIndex', ponieważ startIndex nie będzie ulegał zmian oczywiście w
        // przeciwieństwie do 'aktualneMiastoIndex'
        int aktualneMiastoIndex = 0;
        int startIndex = 0;
        while (aktualneMiastoIndex != macierz.size()) {
            if (start.equals(macierz.get(aktualneMiastoIndex).get(0))) {
                break;
            }
            startIndex += 1;
            aktualneMiastoIndex += 1;
        }

        // Główna pętla wyznaczająca trasę
        while (odwiedzoneMiasta.size() != macierz.size()) {
            // Szukamy najmniejszej odległości więc inicjalizuję min jak największą liczbą.
            int min = Integer.MAX_VALUE;
            // Index wiersza macierzy (czyli odpowiedniego miasta), które ma najmniejszą
            // odległość od aktualnego miasta (przy uwzględnieniu pozostałych warunków np.
            // miast już odwiedzonych).
            int minIndex = 0;
            // Index służący jedynie jako iterator poszczególnego wiersza macierzy (czyli do
            // sprawdzania odległości między aktualnym miastem a pozostałymi).
            int wewnIndex = 1;

            // Jeśli odwiedziliśmy już (liczbaWszystkichMiast - 1) miast to znaczy, że
            // musimy wrócić do miasta startowego.
            if (odwiedzoneMiasta.size() == macierz.size() - 1) {
                // Dodajemy miasto startowe jako ostatnie odwiedzone miasto.
                odwiedzoneMiasta.add(startIndex);
                // Dodajemy odległość do miasta startowego od aktualnego.
                kolejneOdleglosci.add(Integer.parseInt(macierz.get(aktualneMiastoIndex).get(startIndex + 1)));
                // Kończymy wyznaczanie trasy.
                break;
            }

            // Iterujemy przez wszystkie odległości między aktualnym miastem a pozostałymi.
            while (wewnIndex != macierz.get(aktualneMiastoIndex).size()) {
                // Bierzemy odległość od aktualnego miasta
                int temp = Integer.parseInt(macierz.get(aktualneMiastoIndex).get(wewnIndex));
                // Odrzucamy odległość jeśli:
                // 1. Nie jest mniejsza od póki co najmniejszej znalezionej odległości
                // 2. Miasto zostało już odwiedzone
                // 3. Patrzymy na aktualne miasto (tzn. odległość wynosi 0)
                if (!odwiedzoneMiasta.contains(wewnIndex - 1) && temp < min && aktualneMiastoIndex != wewnIndex - 1) {
                    // Jeżeli najmniejsza odległość pochodzi od miasta startowego to musimy ją
                    // odrzucić (bo musimy najpierw odwiedzić wszystkie pozostałe miasta zanim
                    // wrócimy na start)
                    if (start.equals(macierz.get(wewnIndex - 1).get(0))) {
                        wewnIndex += 1;
                        continue;
                    }
                    // Jeśli wszystko jest w porządku to mamy nową najmniejszą odległość i index
                    // tegoż miasta.
                    min = temp;
                    minIndex = wewnIndex - 1;
                }
                // Inkrementujemy licznik i patrzymy na kolejną odległość.
                wewnIndex += 1;
            }
            // Idziemy do miasta o minimalnej odległości od aktualnego.
            aktualneMiastoIndex = minIndex;
            // Dodajemy nowo odwiedzone miasto do naszej optymalnej trasy
            kolejnoscMiast.add(macierz.get(minIndex).get(0));
            // Dodajemy nowo odwiedzone miasto do zbioru miast odwiedzonych
            odwiedzoneMiasta.add(minIndex);
            // Dodajemy pokonaną odległość do listy pokonywanych na trasie odległości
            kolejneOdleglosci.add(min);
        }

        // Wypisujemy miasta w kolejności ich odwiedzania
        for (String miasto : kolejnoscMiast) {
            System.out.print(miasto + " ");
        }

        System.out.print(" [");
        int suma = 0;
        int licznik = macierz.size();
        // Wypisujemy kolejne pokonywane odległości
        for (Integer odleglosc : kolejneOdleglosci) {
            System.out.print(odleglosc);
            suma += odleglosc;
            if (licznik != 1) {
                System.out.print(" + ");
            }
            licznik -= 1;
        }

        // Wypisujemy sumarycznie pokonaną odległość
        System.out.println(" = " + suma + "]");
    }
}