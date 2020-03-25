import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Zad2 {
    
    //Zmienne na potrzebę porównania algorytmów sortujących
    public static int liczbaPorownan = 0;
    public static int liczbaZamian = 0;

    /* Algorytm sortujący comb sort, działa jak bubble sort z 2 różnicami:
       - porównywane są elementy "oddalone" od siebie o pewną odległość, która
       z każdą kolejną iteracją się zmniejsza (ostatnia iteracja to klasyczy bubble sort)
       - nie mamy pewności, że po 1 iteracji ostatni element będzie maksymalnym
       elementem listy, dlatego zakres po którym iterujemy się nie zmniejsza
    */
    public static ArrayList<Integer> combSort() {
        ArrayList<Integer> tempList = (ArrayList<Integer>) Zad1.lista.clone();
        //Specjalnie dobrany (empirycznie) wsp. zapewnia najlepszą wydajność
        double wspZmniejszania = 1.3;
        int odleglosc = tempList.size();
        boolean czyKoniec = false;
        for (int i = 0; i <= tempList.size() - 1; i++){
            odleglosc =  (int) Math.floor(odleglosc / wspZmniejszania);
            
            if (odleglosc <= 1) {
                odleglosc = 1;
                czyKoniec = true;
            }

            for (int j = 0; j <= tempList.size() - 1 - odleglosc; j++){
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

    //Funkcja zapisująca do pliku 'wyniki2.txt'. Zapisywana jest pojedyncza
    //linia, która wchodzi w skład pliku 'zestawienie.txt'.
    public static void zapiszWyniki (String nazwaPliku, ArrayList<Integer> posortowanaLista, String metodaSortowania, int liczbaPor, int liczbaZam) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("zad1/wyniki2.txt"))){
            bw.write(nazwaPliku + " - metoda: " + metodaSortowania + ", liczebnosc: " + Zad1.liczebnoscZbioru(posortowanaLista) + 
            ", liczba porownan: " + liczbaPor + ", liczba zamian: " + liczbaZam);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}