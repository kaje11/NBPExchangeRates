# NBPExchangeRates
Data comparision of NBP exchange rates. XML parsing was done using Regular Expressions. This excercise was made by me for Java classes at the UE Katowice.

# Task
Task description, as well as project was written in polish.
The author of the exercise is unknown, although you can find the description here:
http://www.jkozak.pl/przedmioty-ue/programowanie-java/zadanie-2-kursy-walut/

# Example usage
### More complex version
Getting current data automatically
```java
public static void main(String[] args) {
		kursyWalut("result/textResult.txt", "result/htmlResult.html");
	}
```
### Standard version
Getting data from provided urls
```java
public static void main(String[] args) {
		kursyWalut("http://rss.nbp.pl/kursy/xml2/2010/a/10a091.xml", "http://rss.nbp.pl/kursy/xml2/2010/a/10a092.xml", "result/textResult.txt", "result/htmlResult.html");
	}
```
# Output
### Example text output
```
Tabela nr 073/A/NBP/2017 z dnia 2017-04-13 w porównaniu z tabelą nr 074/A/NBP/2017 z dnia 2017-04-14
Nazwa waluty                        Kod waluty           Kurs średni          Zmiana
bat (Tajlandia)                     1 THB                0,1161               0,0001
dolar amerykański                   1 USD                3,9886               0,0025
dolar australijski                  1 AUD                3,0251               0,0048
dolar Hongkongu                     1 HKD                0,5131               0,0003
dolar kanadyjski                    1 CAD                3,0142               0,0173
dolar nowozelandzki                 1 NZD                2,7931               0,0031
dolar singapurski                   1 SGD                2,8572               0,0017
euro                                1 EUR                4,2450               0,0055
forint (Węgry)                      100 HUF              1,3591               0,0041
(...)
```

### Example HTML output
![Here's a screencap of the output](http://i.imgur.com/u5OuSZU.png)
