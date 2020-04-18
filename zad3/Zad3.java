import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.util.ArrayList;

public class Zad3 {
    // Lista przechowująca wszystkie słowa odpowiadającego pliku (u nas
    // 'z3data1.txt') w kolejności ich występowania
    static ArrayList<String> dane;
    // Zmienna na potrzeby porównania wydajności algorytmów wyszukujących
    static int liczbaPorownan;

    // Główna pętla programu
    public static void main(String[] args) {
        dane = wczytajDane("z3data1.txt");
        System.out.println("------------Sekwencyjne------------");
        System.out.println("mars: " + wyszukiwanieSekwencyjne("mars") + ", porownan: " + liczbaPorownan);
        System.out.println("ogilvy: " + wyszukiwanieSekwencyjne("ogilvy") + ", porownan: " + liczbaPorownan);
        System.out.println("sky: " + wyszukiwanieSekwencyjne("sky") + ", porownan: " + liczbaPorownan);
        System.out.println("meteor: " + wyszukiwanieSekwencyjne("meteor") + ", porownan: " + liczbaPorownan);
        // Tworzy listę obiektów 'Slowo', na które składa się: wyraz (String), jego
        // pozycja w tekście (int) oraz kod hash (int)
        ArrayList<Slowo> slowaZKodami = policzKody();
        // Obiekty te następnie są sortowane przy pomocy algorytmu combSort
        ArrayList<Slowo> posortowaneSlowa = combSort(slowaZKodami);
        System.out.println("--------------Binarne--------------");
        System.out.println(
                "mars: " + wyszukiwanieBinarne(posortowaneSlowa, "mars".hashCode()) + ", porownan: " + liczbaPorownan);
        System.out.println("ogilvy: " + wyszukiwanieBinarne(posortowaneSlowa, "ogilvy".hashCode()) + ", porownan: "
                + liczbaPorownan);
        System.out.println(
                "sky: " + wyszukiwanieBinarne(posortowaneSlowa, "sky".hashCode()) + ", porownan: " + liczbaPorownan);
        System.out.println("meteor: " + wyszukiwanieBinarne(posortowaneSlowa, "meteor".hashCode()) + ", porownan: "
                + liczbaPorownan);

    }

    // Metoda wczytująca słowa z danego pliku wejściowego
    public static ArrayList<String> wczytajDane(String nazwaPliku) {
        ArrayList<String> dane = new ArrayList<String>();

        try (BufferedReader br = new BufferedReader(new FileReader(nazwaPliku))) {
            String linia;
            while ((linia = br.readLine()) != null) {
                String[] temp = linia.split(" ");
                for (String l : temp) {
                    dane.add(l);
                }
            }
        } catch (EOFException e) {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dane;
    }

    /*
     * Metoda 'wyszukiwanieSekwencyjne' przemierza od początku listę wszystkich słów
     * danego tekstu i po kolei sprawdza czy zadany klucz odpowiada aktualnie
     * odczytanej wartości z listy. Jeśli znajdzie zgodność to zwracana jest wartość
     * licznika, który przechowuje informację o pozycji aktualnego słowa w
     * oryginalnym tekście. Gdy przeiteruje przez całą listę i nie znajdzie
     * zgodności to zwracana jest wartość 0.
     */
    public static int wyszukiwanieSekwencyjne(String klucz) {
        liczbaPorownan = 0;
        int licznik = 1;
        for (String word : dane) {
            if (word.equals(klucz)) {
                liczbaPorownan += 1;
                break;
            }
            liczbaPorownan += 1;
            licznik += 1;
        }
        if (licznik == dane.size() + 1) {
            return 0;
        }
        return licznik;
    }

    /*
     * Metoda tworzy obiekty klasy 'Slowo' na podstawie każdego wyrazu z oryginalnej
     * listy 'dane'. Stworzone obiekty są przechowywane w jednej liście
     * 'slowaZKodami'. Obiekty trzymają następujące dane: String wyraz, int pozycja,
     * int kod, gdzie kod jest otrzymywany poprzez wywołanie metody 'hashCode()' na
     * Stringu 'wyraz'.
     */
    public static ArrayList<Slowo> policzKody() {
        int pozycja = 1;
        ArrayList<Slowo> slowaZKodami = new ArrayList<Slowo>();
        for (String w : dane) {
            slowaZKodami.add(new Slowo(w, pozycja, w.hashCode()));
            pozycja += 1;
        }
        return slowaZKodami;
    }

    /*
     * Metoda 'combSort' dalej realizuje ten sam algorytm co w zadaniu 2, natomiast
     * w wyniku podawania innego formatu danych wejściowych, nastąpiły następujące
     * zmiany: 1. porównywane są obiekty klasy 'Slowo', a ściślej ich hash kody,
     * które otrzymuje getterem getKod(), 2. Same kody nie są wystarczające,
     * ponieważ ten sam wyraz może pojawiać się w tekście w różnych momentach, co
     * oznacza, że lista może i będzie posortowana względem kodów, natomiast pozycje
     * słów będą 'porozrzucane'. 3. By uniknąć tego problemu dodatkowo sprawdzam czy
     * porównywane 2 obiekty mają ten sam kod: jeśli tak, to patrzę czy 1-sze słowo
     * jest na dalszej pozycji (pole int pozycja) niż słowo 2-gie. Jeśli jest to
     * również je zamieniam.
     */
    public static ArrayList<Slowo> combSort(ArrayList<Slowo> lista) {
        ArrayList<Slowo> tempList = lista;
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
                if (tempList.get(j).getKod() > tempList.get(j + odleglosc).getKod()) {
                    Slowo tempVal = tempList.get(j);
                    tempList.set(j, tempList.get(j + odleglosc));
                    tempList.set(j + odleglosc, tempVal);
                    czyKoniec = false;
                } else if (tempList.get(j).getKod() == tempList.get(j + odleglosc).getKod()) {
                    if (tempList.get(j).getPozycja() > tempList.get(j + odleglosc).getPozycja()) {
                        Slowo tempVal = tempList.get(j);
                        tempList.set(j, tempList.get(j + odleglosc));
                        tempList.set(j + odleglosc, tempVal);
                        czyKoniec = false;
                    }
                }
            }
            if (czyKoniec) {
                break;
            }
        }
        return tempList;
    }

    /*
     * Metoda 'wyszukiwanieBinarne' realizuje algorytm wyszukiwania klucza w
     * uporządkowanej już tablicy danych. Podstawowo algorytm sprawdza środkowy
     * element tablicy i: 1. Jeśli będzie on większy od danego klucza to
     * poszukiwania zawęża do pierwszej połowy, 2. Jeśli będzie on mniejszy to
     * poszukiwania zawęża do drugiej połowy, 3. Jeśli jest zgodność to zwracany
     * jest indeks danego elementu. W naszym przypadku występuje możliwość istnienia
     * duplikatów kodów, dlatego interesuje nas element zgodny z kluczem znajdujący
     * się najbardziej na lewo (bo combSort gwarantuje, że najmniejsza pozycja
     * wyrazu będzie najbardziej na lewo).
     */
    public static int wyszukiwanieBinarne(ArrayList<Slowo> lista, int klucz) {
        liczbaPorownan = 0;
        int lewo = 0;
        int prawo = lista.size();
        int srodek = 0;
        while (lewo < prawo) {
            srodek = (int) Math.floor((lewo + prawo) / 2);
            if (lista.get(srodek).getKod() < klucz) {
                lewo = srodek + 1;
            } else {
                prawo = srodek;
            }
            liczbaPorownan += 1;
        }
        if (lista.get(lewo).getKod() == klucz) {
            return lista.get(lewo).getPozycja();
        } else {
            return 0;
        }
    }
}