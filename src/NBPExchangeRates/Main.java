package NBPExchangeRates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * NBPExchangeRates
 * @author kaje11
 *
 */
public class Main {
	public static void main(String[] args) {
		kursyWalut("result/textResult.txt", "result/htmlResult.html");
	}
	/*
	 * Napisaæ funkcjê, której zadaniem jest odczytanie danych z plików XML
	 * (mo¿na traktowaæ go jako plik XML lub tekstowy), a nastêpnie zapisanie
	 * ich do nowego pliku tekstowego w postaci tabeli oraz obliczenie ró¿nicy
	 * kursów. Nastêpnie powsta³a tabela ma zostaæ zapisana w postaci HTML. Dla
	 * wersji rozszerzonej nale¿y pobraæ ze strony NBP dwa najnowsze dokumenty
	 * zwi¹zane z wartoœciami kursów walut i dokonywaæ obliczeñ oraz porównania
	 * na pobranych plikach – mo¿na równie¿ pobraæ dane z dni podanych przez
	 * u¿ytkownika.
	 */

	/*
	 * plikWe1: http://rss.nbp.pl/kursy/xml2/2010/a/10a091.xml 
	 * plikWe2: http://rss.nbp.pl/kursy/xml2/2010/a/10a092.xml
	 */
    //Wersja podstawowa
    static void kursyWalut(String plikWe1, String plikWe2, String plikWy, String plikWyHTML) {
    	String siteOne = readHTMLSource(plikWe1);
    	String siteTwo = readHTMLSource(plikWe2);

		XMLCurrencies xmlOne = new XMLCurrencies(siteOne);
		XMLCurrencies xmlTwo = new XMLCurrencies(siteTwo);
    	try {
    		xmlOne.parseXML();
    		xmlTwo.parseXML();
		} catch (Exception e) {
			System.out.println(e);
			return;
		}

    	//Compare
    	for(Currency cur: xmlOne.getCurrencies()) {
    		cur.findNewExchangeRate(xmlTwo.getCurrencies());
    	}

    	//Print tables to file
    	try (PrintWriter tableOutput = new PrintWriter(plikWy);
    			PrintWriter htmlOutput = new PrintWriter(plikWyHTML)) {

    		String head = String.format("Tabela nr %s z dnia %s w porównaniu z tabel¹ nr %s z dnia %s",
    				xmlOne.getID(),
    				xmlOne.getDate(),
    				xmlTwo.getID(),
    				xmlTwo.getDate());

    		tableOutput.printf(head);
    		htmlOutput.printf("<h1>%s</h1>\n", head);

    		tableOutput.printf("\n%-35s %-20s %-20s %6s\n", "Nazwa waluty", "Kod waluty", "Kurs œredni", "Zmiana");
    		htmlOutput.println("<table>\n"
    				+ "	<thead>\n"
    				+ "		<th>Nazwa waluty</th>\n"
    				+ "		<th>Kod waluty</th>\n"
    				+ "		<th>Kurs œredni</th>\n"
    				+ "		<th>Zmiana</th>\n"
    				+ "	</thead>\n"
    				+ "	<tbody>");

    		for (Currency cur: xmlOne.getCurrencies()) {
    			tableOutput.println(cur.toString());
    			htmlOutput.print(cur.toTableCell());
    		}

    		htmlOutput.println("	</tbody>\n"
    				+ "</table>");
    	} catch (Exception e) {
    		System.out.println(e);
		}
    }

    //Wersja rozszerzona:
    static void kursyWalut(String plikWy, String plikWyHTML) {
    	int curYear = Calendar.getInstance().get(Calendar.YEAR);
    	ArrayList<String> tables = getTables(curYear);

    	String urlOne;
    	String urlTwo;
    	int retries = 0;

    	while (tables == null && retries++ < 10) {
	    	tables = getTables(curYear - retries);
    	}

    	if (tables == null) {
    		System.out.println("Nieznaleziono danych");
    		return;
    	}

    	//1st one is link reference
    	if (tables.size() == 2) {
    		urlTwo = tables.get(0) + tables.get(1);
    		try {
    			tables = getTables(curYear - 1);
    			if (tables == null) {
    				System.out.println("Nieznaleziono danych");
    	    		return;
    			}
    			urlOne = tables.get(0) + tables.get(tables.size() - 1);
    		} catch (Exception e) {
    			System.out.println("B³¹d!");
    			return;
    		}
    	} else {
    		urlOne = tables.get(0) + tables.get(tables.size() - 2);
    		urlTwo = tables.get(0) + tables.get(tables.size() - 1);
    	}
    	System.out.println(urlOne);
    	kursyWalut(urlOne, urlTwo, plikWy, plikWyHTML);
    }

   static ArrayList<String> getTables(int year) {
	   String url = "http://rss.nbp.pl/kursy/xml2/" + year + "/a/dir.aspx";
	   String html = readHTMLSource(url);

	   if (!html.matches(".*<title>Dir<\\/title>.*")) {
		   System.out.println(html.matches("<title>Dir<\\/title>"));
		   return null;
	   }
	   Pattern pattern = Pattern.compile("<a href=\"([0-9a-z]+\\.xml)\">");
	   Matcher matcher = pattern.matcher(html);

	   ArrayList<String> tables = new ArrayList<String>();
	   tables.add("http://rss.nbp.pl/kursy/xml2/" + year + "/a/");

	   while (matcher.find()) {
		   tables.add(matcher.group(1));
	   }
	   return tables;
   }

    static String readHTMLSource(String siteURL) {
	    URL url;
	    InputStream is = null;
	    BufferedReader br;
	    String line;
	    StringBuilder output = new StringBuilder();

	    try {
	        url = new URL(siteURL);
	        is = url.openStream();
	        br = new BufferedReader(new InputStreamReader(is));

	        while ((line = br.readLine()) != null) {
	            output.append(line);
	        }
	    } catch (MalformedURLException mue) {
	         mue.printStackTrace();
	    } catch (IOException ioe) {
	         ioe.printStackTrace();
	    } finally {
	        try {
	            if (is != null) {
					is.close();
				}
	        } catch (IOException ioe) {
	            // nothing to see here
	        }
	    }
        return output.toString();
    }
}
