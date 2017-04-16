package NBPExchangeRates;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

class Currency {
	private String name;
	private String code;
	private int amount;
	private float beginRate; //kurs sredni
	private float currentChange;

	Currency(String name, int amount, String code, float exchangeRate) {
		this.name = name;
		this.code = code;
		this.amount = amount;
		this.beginRate = exchangeRate;
	}
	public String toString() {
		return String.format("%-35s %-20s %-20s %s",
				this.name,
				this.amount + " " + this.code,
				this.formatFloat(this.beginRate),
				this.formatFloat(this.currentChange));
	}

	public String toTableCell() {
		return String.format("\t\t<tr>"
				+ "\n\t\t\t<td>%s</td>"
				+ "\n\t\t\t<td>%s</td>"
				+ "\n\t\t\t<td>%s</td>"
				+ "\n\t\t\t<td><span style=\"color: %s\">%s</span></td>"
				+ "\n\t\t</tr>\n",
				this.name,
				this.amount + " " + this.code,
				this.formatFloat(this.beginRate),
				this.currentChange > 0 ? "green" : "red",
				this.formatFloat(this.currentChange));
	}
	String formatFloat(float num) {
		if (num == 0) {
			return "Brak";
		}

		DecimalFormat df = new DecimalFormat("#.####");
		String myNum = df.format(Math.abs(num));
		return (myNum + "0000").substring(0, 6);
	}
	void findNewExchangeRate(ArrayList<Currency> newCurrencies) {
		for (Currency cur: newCurrencies) {
			if(cur.name.equals(this.name)) {
				this.currentChange = this.beginRate - cur.beginRate;
				return;
			}
		}
	}
}

