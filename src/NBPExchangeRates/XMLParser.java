package NBPExchangeRates;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class XMLCurrencies {
	private String headRegex = "<numer_tabeli>([0-9A-Z\\/]+)<\\/numer_tabeli>\\s+<data_publikacji>([0-9]{4}\\-[0-9]{2}\\-[0-9]{2})<\\/data_publikacji>";
	private String bodyRegex = "<pozycja>\\s+<nazwa_waluty>(.*?)<\\/nazwa_waluty>\\s+<przelicznik>(\\d+)<\\/przelicznik>\\s+<kod_waluty>([A-Z]+)<\\/kod_waluty>\\s+<kurs_sredni>([0-9,]+)<\\/kurs_sredni>\\s+<\\/pozycja>";
	private Pattern headPattern = Pattern.compile(headRegex), bodyPattern = Pattern.compile(bodyRegex);

	private String tableID;
	private String date;
	private ArrayList<Currency> currencies = new ArrayList<Currency>();

	private Matcher head, body;

	XMLCurrencies(String xmlCode) {
    	this.head = this.headPattern.matcher(xmlCode);
    	this.body = this.bodyPattern.matcher(xmlCode);
	}

	ArrayList<Currency> getCurrencies() {
		return this.currencies;
	}

	String getID() { return this.tableID; }
	String getDate() { return this.date; }

	void parseXML() throws Exception {
    	if (!this.head.find() || !this.body.find()) {
    		throw new Exception("Nie mo¿na przeczytaæ pliku");
    	}

    	this.tableID = head.group(1);
    	this.date = head.group(2);

    	int amount;
    	float rate;
    	do {
    		amount = Integer.parseInt(body.group(2));
    		rate = Float.parseFloat(body.group(4).replace(',','.'));

    		this.currencies.add(new Currency(body.group(1), amount, body.group(3), rate));
    	} while (body.find());
	}
}
