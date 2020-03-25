import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

class Zad1 {

    // Oryginalna lista wczytywana z pliku .csv
    public static ArrayList<Integer> lista;

    // Na potrzebę porównania różnych algorytmów sortowania:
    public static int liczbaPorownan = 0;
    public static int liczbaZamian = 0;

    // Główna pętla programu
    public static void main(String[] args) {
        // Tutaj z poziomu kodu podaje plik do otworzenia
        String nazwaPliku = "z2data12.csv";
        String pelnaSciezka = "zad1/data/" + nazwaPliku;
        wczytajDane(pelnaSciezka);
        // Należy ustawić na cocktailSort() lub combSort() by wybrać algorytm
        ArrayList<Integer> posortowanaLista = combSort();
        // W przypadku cocktailSort'a należy zmienić "comb sort" na "cocktail sort"
        zapiszWyniki(nazwaPliku, posortowanaLista, "comb sort", liczbaPorownan, liczbaZamian);
    }

    // Funkcja służąca do wczytania liczb z pliku .csv. Przyjmowany argument jest
    // nazwą pliku do otworzenia.
    public static void wczytajDane(String plik) {
        lista = new ArrayList<Integer>();
        try (BufferedReader br = new BufferedReader(new FileReader(plik))) {
            String linia;
            while ((linia = br.readLine()) != null) {
                String[] temp = linia.split(",");
                for (String liczba : temp) {
                    lista.add(Integer.parseInt(liczba));
                }
            }
        } catch (EOFException e) {
            // Ten wyjątek jest w zasadzie oczekiwanym rezultatem dlatego nie prowadzi do
            // żadnych komunikatów błędu.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Algorytm sortujący cocktail sort przechodzi przez tablicę porównując 2
     * elementy: n oraz n+1 do momentu osiągnięcia jej końca. W wyniku iteracji
     * "w przód" na ostatniej pozycji na pewno znajdzie się maksymalny element
     * tablicy. Następnie iterowanie przebiega "od końca do początku" i w jego
     * wyniku na początku listy znajdzie się najmniejszy element. I tak dalej
     * "odbija się" w jedną i drugą stronę.
     */
    public static ArrayList<Integer> cocktailSort() {
        // temp od angielskiego temporary, ponieważ lista ta istnieje tylko na
        // potrzeby tej funkcji tj. na potrzebę sortowania
        ArrayList<Integer> tempList = (ArrayList<Integer>) lista.clone();

        int przesuwacz = 0;
        boolean czyKoniec = false;
        for (int i = 0; i <= tempList.size(); i++) {

            czyKoniec = true;

            for (int j = 0; j <= tempList.size() - i - 2; j++) {
                if (i % 2 == 0) {
                    liczbaPorownan += 1;
                    if (tempList.get(przesuwacz) > tempList.get(przesuwacz + 1)) {
                        Integer tempInt = tempList.get(przesuwacz);
                        tempList.set(przesuwacz, tempList.get(przesuwacz + 1));
                        tempList.set(przesuwacz + 1, tempInt);
                        liczbaZamian += 1;
                        czyKoniec = false;
                    }
                    przesuwacz += 1;
                } else {
                    liczbaPorownan += 1;
                    if (tempList.get(przesuwacz) < tempList.get(przesuwacz - 1)) {
                        Integer tempInt = tempList.get(przesuwacz);
                        tempList.set(przesuwacz, tempList.get(przesuwacz - 1));
                        tempList.set(przesuwacz - 1, tempInt);
                        liczbaZamian += 1;
                        czyKoniec = false;
                    }
                    przesuwacz -= 1;
                }
            }
            if (i % 2 == 0) {
                przesuwacz -= 1;
            } else {
                przesuwacz += 1;
            }

            if (czyKoniec) {
                break;
            }
        }
        return tempList;
    }

    /*
     * Algorytm sortujący comb sort, działa jak bubble sort z 2 różnicami: -
     * porównywane są elementy "oddalone" od siebie o pewną odległość, która z każdą
     * kolejną iteracją się zmniejsza (ostatnia iteracja to klasyczy bubble sort) -
     * nie mamy pewności, że po 1 iteracji ostatni element będzie maksymalnym
     * elementem listy, dlatego zakres po którym iterujemy się nie zmniejsza
     */
    public static ArrayList<Integer> combSort() {
        ArrayList<Integer> tempList = (ArrayList<Integer>) Zad1.lista.clone();
        // Specjalnie dobrany (empirycznie) wsp. zapewnia najlepszą wydajność
        double wspZmniejszania = 1.3;
        int odleglosc = tempList.size();
        boolean czyKoniec = false;
        for (int i = 0; i <= tempList.size() - 1; i++) {
            odleglosc = (int) Math.floor(odleglosc / wspZmniejszania);

            if (odleglosc <= 1) {
                odleglosc = 1;
                czyKoniec = true;
            }

            for (int j = 0; j <= tempList.size() - 1 - odleglosc; j++) {
                liczbaPorownan += 1;
                if (tempList.get(j) > tempList.get(j + odleglosc)) {
                    int tempVal = tempList.get(j);
                    tempList.set(j, tempList.get(j + odleglosc));
                    tempList.set(j + odleglosc, tempVal);
                    liczbaZamian += 1;
                    czyKoniec = false;
                }
            }
            if (czyKoniec) {
                break;
            }
        }
        return tempList;
    }

    // Funkcja zwracająca rozmiar listy.
    public static int liczebnoscZbioru(ArrayList<Integer> lista) {
        return lista.size();
    }

    // Funkcja zwracająca maksymalny element listy (ma sens tylko, w przypadku gdy
    // jest posortowana).
    public static int maksymalnyElement(ArrayList<Integer> posortowanaLista) {
        return posortowanaLista.get(posortowanaLista.size() - 1);
    }

    // Funkcja zwracająca minimalny element listy (ma sens tylko, w przypadku gdy
    // jest posortowana).
    public static int minimalnyElement(ArrayList<Integer> posortowanaLista) {
        return posortowanaLista.get(0);
    }

    // Funkcja zwracająca medianę danej listy (ma sens tylko, w przypadku gdy jest
    // posortowana).
    public static double medianaZbioru(ArrayList<Integer> posortowanaLista) {
        if (posortowanaLista.size() % 2 != 0) {
            int index = posortowanaLista.size() / 2 + 1;
            return posortowanaLista.get(index);
        } else {
            int index = posortowanaLista.size() / 2;
            double suma = posortowanaLista.get(index) + posortowanaLista.get(index - 1);
            return suma / 2;
        }
    }

    // W wyniku tej funkcji przyjmowana lista wyświetlana jest w konsoli.
    public static void wyswietlZbior(ArrayList<Integer> lista) {
        if (lista.size() <= 20) {
            for (Integer element : lista) {
                System.out.print(element + " ");
            }
            System.out.println();
        }
    }

    // W wyniku tej funkcji przyjmowana lista zwracana jest jako jeden String
    public static String wydrukZbioru(ArrayList<Integer> lista) {
        String zbior = "";
        ListIterator<Integer> li = lista.listIterator();
        int licznik = 1;
        while (li.hasNext()) {
            zbior += li.next() + " ";
            licznik += 1;
            if (licznik % 10 == 0) {
                zbior += "\n";
            }
        }
        return zbior;
    }

    // Funkcja zapisująca wyniki zadania do pliku 'wyniki.txt'.
    public static void zapiszWyniki(ArrayList<Integer> posortowanaLista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("zad1/wyniki.txt"))) {
            bw.write("Liczebnosc wynosi: " + liczebnoscZbioru(posortowanaLista));
            bw.newLine();
            bw.write("Minimum wynosi: " + minimalnyElement(posortowanaLista));
            bw.newLine();
            bw.write("Maksimum wynosi: " + maksymalnyElement(posortowanaLista));
            bw.newLine();
            bw.write("Mediana wynosi: " + medianaZbioru(posortowanaLista));
            bw.newLine();
            bw.write("Posortowana tablica: \n");
            bw.write(wydrukZbioru(posortowanaLista));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Funkcja zapisująca do pliku 'wyniki2.txt'. Zapisywana jest pojedyncza
    // linia, która wchodzi w skład pliku 'zestawienie.txt'.
    public static void zapiszWyniki(String nazwaPliku, ArrayList<Integer> posortowanaLista, String metodaSortowania,
            int liczbaPor, int liczbaZam) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("zad1/wyniki2.txt"))) {
            bw.write(nazwaPliku + " - metoda: " + metodaSortowania + ", liczebnosc: "
                    + Zad1.liczebnoscZbioru(posortowanaLista) + ", liczba porownan: " + liczbaPor + ", liczba zamian: "
                    + liczbaZam);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}