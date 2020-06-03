import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Zad5 {

    public static ArrayList<ArrayList<String>> macierz;

    public static void main(String[] args) {
        wczytajPlik("z5data1.csv");
        wypiszMacierz();
        Scanner scan = new Scanner(System.in);
        System.out.println("Podaj miasto startowe: ");
        String start = scan.nextLine();
        scan.close();
        wyznaczTrase(start);
    }

    public static void wczytajPlik(String nazwaPliku) {
        try (BufferedReader br = new BufferedReader(new FileReader(nazwaPliku))) {
            macierz = new ArrayList<ArrayList<String>>();
            String linia;
            linia = br.readLine();
            while ((linia = br.readLine()) != null) {
                String[] temp = linia.split(",");
                ArrayList<String> lista = new ArrayList<String>();
                for (String element : temp) {
                    lista.add(element.trim());
                }
                macierz.add(lista);
            }
        } catch (EOFException e) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void wypiszMacierz() {
        for (ArrayList<String> wiersz : macierz) {
            for (String pole : wiersz) {
                System.out.print(pole + " ");
            }
            System.out.println();
        }
    }

    public static void wyznaczTrase(String start) {
        ArrayList<String> kolejnoscMiast = new ArrayList<String>();
        ArrayList<Integer> kolejneOdleglosci = new ArrayList<Integer>();
        HashSet<Integer> odwiedzoneMiasta = new HashSet<Integer>();
        kolejnoscMiast.add(start);

        int index = 0;
        int returnIndex = 0;
        while (index != macierz.size()) {
            if (start.equals(macierz.get(index).get(0))) {
                break;
            }
            returnIndex += 1;
            index += 1;
        }

        while (odwiedzoneMiasta.size() != macierz.size()) {
            int min = Integer.MAX_VALUE;
            int minIndex = 0;
            int wewnIndex = 1;

            if (odwiedzoneMiasta.size() == macierz.size() - 1) {
                odwiedzoneMiasta.add(returnIndex);
                kolejnoscMiast.add(start);
                kolejneOdleglosci.add(Integer.parseInt(macierz.get(index).get(returnIndex + 1)));
                break;
            }

            while (wewnIndex != macierz.get(index).size()) {
                int temp = Integer.parseInt(macierz.get(index).get(wewnIndex));
                if (!odwiedzoneMiasta.contains(wewnIndex - 1) && temp < min && index != wewnIndex - 1) {
                    if (start.equals(macierz.get(wewnIndex - 1).get(0))
                            && odwiedzoneMiasta.size() != macierz.size() - 1) {
                        wewnIndex += 1;
                        continue;
                    }
                    min = temp;
                    minIndex = wewnIndex - 1;
                }
                wewnIndex += 1;
            }
            index = minIndex;
            kolejnoscMiast.add(macierz.get(minIndex).get(0));
            odwiedzoneMiasta.add(minIndex);
            kolejneOdleglosci.add(min);
        }

        for (String miasto : kolejnoscMiast) {
            System.out.print(miasto + " ");
        }
        System.out.println();

        for (Integer odleglosc : kolejneOdleglosci) {
            System.out.print(odleglosc + " ");
        }
        System.out.println();
    }
}