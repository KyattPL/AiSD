/* Klasa 'Slowo' przechowuje informacje o jednym wyrazie występującym w tekście. 
 * Tymi informacjami są: sam wyraz, jego pozycja w tekście (nr wyrazu) oraz kod,
 * który w mojej implementacji otrzymuję w wyniku wywołania metody hashCode() na
 * samym wyrazie.
 */

public class Slowo {
    String slowo;
    int pozycja;
    int kod;

    public Slowo(String s, int p, int k) {
        this.slowo = s;
        this.pozycja = p;
        this.kod = k;
    }

    public String getSlowo() {
        return this.slowo;
    }

    public int getPozycja() {
        return this.pozycja;
    }

    public int getKod() {
        return this.kod;
    }
}