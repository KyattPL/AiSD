import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Zad4 {
    // Pomocne pola do formatowania liczb dziesiętnych w tabeli liczebności
    // oczekiwanych oraz wartości statystyki chi-kwadrat
    private static DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
    private static DecimalFormat df2;

    public static void main(String[] args) {
        // Inicjalizacja formatowania liczb dziesiętnych w tabeli liczebności
        // oczekiwanych
        otherSymbols.setDecimalSeparator('.');
        df2 = new DecimalFormat("#.##", otherSymbols);
        // Wczytaj dane z zadanego pliku, u nas 'z4data4.csv' z podfolderu 'data'
        String[][] dane = wczytajDane("data/z4data4.csv");
        // Generuj tabelę liczebności obserwowanych wraz z policzonymi sumami
        // częściowymi
        obliczSumeCzesciowa(dane);
        // Wyświetl tablicę O
        wyswietlTablice(dane);
        System.out.println();
        // Utwórz nową tablicę wartości oczekiwanych na podstawie oryginalnej tablicy
        // 'dane'
        String[][] oczekiwane = tabelaOczekiwanych(dane);
        // Wyświetl tablicę wartości oczekiwanych
        wyswietlTablice(oczekiwane);
        System.out.println();
        // Policz wsp. chi-kwadrat na podstawie obu tablic
        double chiKwadrat = chiKwadrat(dane, oczekiwane);
        // Wyznacz liczbę stopni swobody danej tabeli
        int stopnieSwobody = stopnieSwobody(dane);
        // Wypisywanie poszczególnych parametrów do konsoli (chi-kwadrat, liczba stopni
        // swobody oraz prawdopodobienstwo p)
        System.out.println("Chi kwadrat: " + chiKwadrat);
        System.out.println("Stopnie swobody: " + stopnieSwobody);
        System.out.println("Prawdopodobienstwo p: " + prawdopodobienstwoP(chiKwadrat, stopnieSwobody));
    }

    // Metoda służąca do wczytania danych z pliku .csv
    public static String[][] wczytajDane(String plik) {
        String[][] lista = new String[0][0];
        int szerokosc = 0;
        int wysokosc = 1;
        // Rozpoznajemy rozmiary tablicy z pliku oraz rezerwujemy przestrzeń na
        // dodatkową kolumnę/wiersz 'SUMA'
        try (BufferedReader br = new BufferedReader(new FileReader(plik))) {
            String linia;
            if ((linia = br.readLine()) != null) {
                szerokosc = linia.split(",").length;
            }
            while ((linia = br.readLine()) != null) {
                wysokosc += 1;
            }
            lista = new String[wysokosc + 1][szerokosc + 1];
            lista[wysokosc][0] = "SUMA ";
            lista[0][szerokosc] = "SUMA ";
        } catch (EOFException e) {
            // Ten wyjątek jest w zasadzie oczekiwanym rezultatem dlatego nie prowadzi do
            // żadnych komunikatów błędu.
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Wczytujemy wartości z pliku do tablicy 2D o stałych rozmiarach
        try (BufferedReader br = new BufferedReader(new FileReader(plik))) {
            int licznikWys = 0;
            String linia;
            while ((linia = br.readLine()) != null) {
                String temp[] = linia.split(",");
                int licznikSzer = 0;
                while (licznikSzer < temp.length) {
                    lista[licznikWys][licznikSzer] = temp[licznikSzer];
                    licznikSzer += 1;
                }
                licznikWys += 1;
            }
            return lista;
        } catch (EOFException e) {
            // Ten wyjątek jest w zasadzie oczekiwanym rezultatem dlatego nie prowadzi do
            // żadnych komunikatów błędu.
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Zwracamy wczytaną tablicę 2D
        return lista;
    }

    // Metoda służąca do wypisywania tablicy do konsoli
    public static void wyswietlTablice(String[][] tablica) {
        int wysokosc = tablica.length;
        int szerokosc = tablica[0].length;
        int licznikSzer = 0;
        int licznikWys = 0;
        // Oprócz pierwszego sysout'a którym wypisujemy wartości postarałem się o
        // utworzenie liczników spacji (dobranych empirycznie) wraz z odpowiednimi
        // pętlami while by wyświetlane dane były pokazane w przystępny sposób
        while (licznikWys < wysokosc) {
            while (licznikSzer < szerokosc) {
                System.out.print(tablica[licznikWys][licznikSzer]);
                if (licznikSzer == 0) {
                    int counterSpacji = 14 - tablica[licznikWys][licznikSzer].length();
                    while (counterSpacji != 0) {
                        System.out.print(" ");
                        counterSpacji -= 1;
                    }
                } else {
                    int counterSpacji = 15 - tablica[licznikWys][licznikSzer].length();
                    while (counterSpacji != 0) {
                        System.out.print(" ");
                        counterSpacji -= 1;
                    }
                }
                licznikSzer += 1;
            }
            licznikSzer = 0;
            System.out.println();
            licznikWys += 1;
        }
    }

    // Metoda zapełnia wiersz oraz kolumnę 'SUMA' odpowiednimi wartościami sum
    // częściowych tablicy O
    public static void obliczSumeCzesciowa(String[][] tablica) {
        int wysokosc = tablica.length;
        int szerokosc = tablica[0].length;
        int licznikSzer = 1;
        int licznikWys = 1;
        int tempSuma = 0;
        // Pierwsza pętla sumuje wartości w wierszach i zapisuje do ostatnej kolumny
        while (licznikWys < wysokosc - 1) {
            while (licznikSzer < szerokosc - 1) {
                tempSuma += Integer.parseInt(tablica[licznikWys][licznikSzer]);
                licznikSzer += 1;
            }
            tablica[licznikWys][licznikSzer] = Integer.toString(tempSuma);
            tempSuma = 0;
            licznikSzer = 1;
            licznikWys += 1;
        }
        licznikWys = 1;
        // Druga pętla sumuje wartości w kolumnach i zapisuje do ostatniego wiersza
        while (licznikSzer < szerokosc - 1) {
            while (licznikWys < wysokosc - 1) {
                tempSuma += Integer.parseInt(tablica[licznikWys][licznikSzer]);
                licznikWys += 1;
            }
            tablica[licznikWys][licznikSzer] = Integer.toString(tempSuma);
            tempSuma = 0;
            licznikWys = 1;
            licznikSzer += 1;
        }
        licznikSzer = 1;
        licznikWys = wysokosc - 1;
        // Trzecia pętla sumuje wartości w ostatnim wierszu i zapisuje wynik do
        // ostatniej komórki w tablicy (gdzie przecina się kolumna i wiersz 'SUMA')
        while (licznikSzer < szerokosc - 1) {
            tempSuma += Integer.parseInt(tablica[licznikWys][licznikSzer]);
            licznikSzer += 1;
        }
        tablica[licznikWys][licznikSzer] = Integer.toString(tempSuma);
    }

    // Metoda generująca tablicę wartości oczekiwanych na podstawie zadanej tablicy
    // wartości obserwowanych. Nie wczytujemy oczywiście kolumny i wiersza 'SUMA'
    public static String[][] tabelaOczekiwanych(String[][] tablica) {
        int wysokosc = tablica.length - 1;
        int szerokosc = tablica[0].length - 1;
        int licznikSzer = 0;
        int licznikWys = 0;
        String[][] oczekiwane = new String[wysokosc][szerokosc];
        // Pierwsza pętla wczytuje nagłówki kolumn
        while (licznikSzer < szerokosc) {
            oczekiwane[0][licznikSzer] = tablica[0][licznikSzer];
            licznikSzer += 1;
        }
        licznikSzer = 1;
        // Druga pętla wczytuje nagłówki wierszy
        while (licznikWys < wysokosc) {
            oczekiwane[licznikWys][0] = tablica[licznikWys][0];
            licznikWys += 1;
        }
        licznikWys = 1;
        // Trzecia pętla oblicza wartości oczekiwane dla każdej komórki tablicy.
        // Obliczam to wzorem (SUMA_rząd / SUMA_całkowita) * SUMA_kolumna, gdzie
        // SUMA_rząd = wartość sumy częściowej z kolumny SUMA spod wiersza
        // odpowiadającego wierszowi aktualnej komórki (z tablicy O).
        // SUMA_kolumna = analogicznie
        // SUMA_całkowita = suma sum częściowych (czyli ostatnia komórka tablicy O)
        while (licznikWys < wysokosc) {
            while (licznikSzer < szerokosc) {
                double suma_a = Double.parseDouble(tablica[licznikWys][szerokosc]);
                double suma_b = Double.parseDouble(tablica[wysokosc][szerokosc]);
                double suma_c = Double.parseDouble(tablica[wysokosc][licznikSzer]);
                oczekiwane[licznikWys][licznikSzer] = df2.format(suma_c * suma_a / suma_b);
                licznikSzer += 1;
            }
            licznikSzer = 1;
            licznikWys += 1;
        }

        // Zwracamy wygenerowaną tablicę wartości oczekiwanych
        return oczekiwane;
    }

    // Metoda obliczająca współczynnik chi-kwadrat, który jest sumą odchyleń zadaną
    // wzorem ((Oij - Eij)^2)/Eij, gdzie Oij to wartość z tablicy O dla kolumny i
    // oraz rzędu j, natomiast Eij to wartość z tablicy E dla kolumny i oraz rzędu j
    public static double chiKwadrat(String[][] tablica, String[][] oczekiwane) {
        int wysokosc = tablica.length - 1;
        int szerokosc = tablica[0].length - 1;
        int licznikSzer = 1;
        int licznikWys = 1;
        double tempSuma = 0;
        while (licznikWys < wysokosc) {
            while (licznikSzer < szerokosc) {
                double obs = Double.parseDouble(tablica[licznikWys][licznikSzer]);
                double ocz = Double.parseDouble(oczekiwane[licznikWys][licznikSzer]);
                tempSuma += (obs - ocz) * (obs - ocz) / ocz;
                licznikSzer += 1;
            }
            licznikSzer = 1;
            licznikWys += 1;
        }
        return tempSuma;
    }

    // Metoda wyznaczająca liczbę stopni swobody danej tablicy
    // (szerokość - 1) * (wysokość - 1)
    public static int stopnieSwobody(String[][] tablica) {
        int szer = tablica.length - 2;
        int wys = tablica[0].length - 2;
        return (szer - 1) * (wys - 1);
    }

    // Metoda obliczająca prawdopodobieństwo p tego, że dane z porównywanych grup są
    // do siebie podobne. Metoda autorstwa John C. Pezzullo oryginalnie napisana w
    // języku Javascript, przeze mnie przeniosiona na Javę.
    // Źródło: http://statpages.org/scicalc.html
    public static double prawdopodobienstwoP(double chiKwadrat, int stopnieSwobody) {
        if (chiKwadrat > 1000 || stopnieSwobody > 1000) {
            double z = (Math.pow(chiKwadrat / stopnieSwobody, 1 / 3) + 2 / (9.0 * stopnieSwobody) - 1)
                    / Math.sqrt(2 / (9.0 * stopnieSwobody));
            double q = Norm(z) / 2.0;
            if (chiKwadrat > stopnieSwobody) {
                return q;
            } else {
                return 1 - q;
            }
        }
        double p = Math.exp(-0.5 * chiKwadrat);
        if ((stopnieSwobody % 2) == 1) {
            p = p * Math.sqrt(2.0 * chiKwadrat / Math.PI);
        }
        int k = stopnieSwobody;
        while (k >= 2) {
            p = p * chiKwadrat / k;
            k = k - 2;
        }
        double t = p;
        int a = stopnieSwobody;
        while (t > 1e-15 * p) {
            a = a + 2;
            t = t * chiKwadrat / a;
            p = p + t;
        }
        return 1 - p;
    }

    // Metoda pomocnicza dla metody prawdopodobienstwoP()
    public static double Norm(double z) {
        double q = z * z;
        if (Math.abs(z) > 7) {
            return (1 - 1.0 / q + 3.0 / (q * q)) * Math.exp(-q / 2.0) / (Math.abs(z) * Math.sqrt(Math.PI / 2.0));
        } else {
            return prawdopodobienstwoP(q, 1);
        }
    }
}