//Needs complete revision

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class Model {

	private String vmixIP;
	private int title1, title2, title3;
	private String[] namesData;
	private String[] scoresData;
	private String[] timeData;
	private Scanner sc = new Scanner(System.in);;

	Model(View _GUI) {
		setup();
		try {
			convertData(); //Ergebnis in Strings wandelen
		} catch (IOException e) {
			
		}
		try {
			textToAPI(); //Strings an vMix senden
		} catch (IOException e1) {
			System.out.println("Fehler beim senden der Daten!");
		}
		while (true) {
			try {
				TimeUnit.MINUTES.sleep(3);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
			try {
				convertData();
			} catch (IOException e) {
				System.out.println("Fehler beim Konvertieren der Daten!");
				continue;
			}
			try {
				textToAPI();
			} catch (IOException e1) {
				System.out.println("Fehler beim senden der Daten!");
				continue;
			}
		}
	}

	public void setup() {
		System.out.println("vMix IP-Adresse und Port eingeben:");
		vmixIP = sc.next();
		System.out.println("Nummer des auszuschließenden Spiel:");
		System.out.println("Input des ersten Titels:");
		title1 = sc.nextInt();
		title2 = title1 + 1;
		title3 = title2 + 1;
	}

	public void convertData() throws IOException {
		String url = "http://www.del.org/de/statistiken/livescores/page/260----.html";
		Document doc = Jsoup.connect(url).get();
		Elements names = doc.select(".team .wappen");
		Elements scores = doc.select(".team span");
		Elements time = doc.select("span.time");
		
		Date date = new Date();

		System.out.println(date.toString());
		System.out.println("Anzahl Spiele: " + time.size() + "\n");
		namesData = new String[names.size() - 2];
		scoresData = new String[scores.size() - 2];
		timeData = new String[time.size() - 1];

		for (int i = 0; i < time.size(); i++) {
			if (i == 0) {
				namesData[i] = names.eq(i).attr("alt").replaceAll("\\s", "%20"); //Get name String of the home club and replace blanks
				namesData[i + 1] = names.eq(i + 1).attr("alt").replaceAll("\\s", "%20"); //Get name String of the away club and replace blanks
				scoresData[i] = scores.eq(i).text(); //Get score String of the home club
				scoresData[i + 1] = scores.eq(i + 1).text(); //Get score String of the away club
				System.out.println(namesData[i] + "-" + namesData[i+1]); //Print names
				System.out.println(scoresData[i] + " " + scoresData[i+1]); //Print score
			} else { //same as above
				namesData[i + i] = names.eq(i+i).attr("alt").replaceAll("\\s", "%20");
				namesData[i + i + 1] = names.eq(i + i + 1).attr("alt").replaceAll("\\s", "%20");
				scoresData[i + i] = scores.eq(i + i).text();
				scoresData[i + i + 1] = scores.eq(i + i + 1)
						.text();
				System.out.println(namesData[i + i] + "-" + namesData[i + i + 1]);
				System.out.println(scoresData[i + i] + " " + scoresData[i + i + 1]);
			}

			timeData[i] = time.eq(i).text().replaceAll("\\s", "%20"); //Get time String of the home club and replace blanks
			System.out.println(time.eq(i).text() + "\n"); //Print time
		}
	}

	private void textToAPI() throws IOException {

		for (int i = 0; i < timeData.length; i++) {
			if (i % 2 == 0) {
				if (i <= 1) {
					sendToVmix("http://" + vmixIP
							+ "/api/?Function=setText&Input=" + title1
							+ "&SelectedName=Heim_1&Value=" + scoresData[i + i]);
					sendToVmix("http://" + vmixIP
							+ "/api/?Function=setText&Input=" + title1
							+ "&SelectedName=Gast_1&Value="
							+ scoresData[i + i + 1]);
					sendToVmix("http://" + vmixIP
							+ "/api/?Function=setText&Input=" + title1
							+ "&SelectedName=Zeitpunkt_1&Value=" + timeData[i]);
				} else if (i <= 3) {
					sendToVmix("http://" + vmixIP
							+ "/api/?Function=setText&Input=" + title2
							+ "&SelectedName=Heim_1&Value=" + scoresData[i + i]);
					sendToVmix("http://" + vmixIP
							+ "/api/?Function=setText&Input=" + title2
							+ "&SelectedName=Gast_1&Value="
							+ scoresData[i + i + 1]);
					sendToVmix("http://" + vmixIP
							+ "/api/?Function=setText&Input=" + title2
							+ "&SelectedName=Zeitpunkt_1&Value=" + timeData[i]);
				} else {
					sendToVmix("http://" + vmixIP
							+ "/api/?Function=setText&Input=" + title3
							+ "&SelectedName=Heim_1&Value=" + scoresData[i + i]);
					sendToVmix("http://" + vmixIP
							+ "/api/?Function=setText&Input=" + title3
							+ "&SelectedName=Gast_1&Value="
							+ scoresData[i + i + 1]);
					sendToVmix("http://" + vmixIP
							+ "/api/?Function=setText&Input=" + title3
							+ "&SelectedName=Zeitpunkt_1&Value=" + timeData[i]);
				}
			} else {
				if (i <= 1) {
					sendToVmix("http://" + vmixIP
							+ "/api/?Function=setText&Input=" + title1
							+ "&SelectedName=Heim_2&Value=" + scoresData[i + i]);
					sendToVmix("http://" + vmixIP
							+ "/api/?Function=setText&Input=" + title1
							+ "&SelectedName=Gast_2&Value="
							+ scoresData[i + i + 1]);
					sendToVmix("http://" + vmixIP
							+ "/api/?Function=setText&Input=" + title1
							+ "&SelectedName=Zeitpunkt_2&Value=" + timeData[i]);
				} else if (i <= 3) {
					sendToVmix("http://" + vmixIP
							+ "/api/?Function=setText&Input=" + title2
							+ "&SelectedName=Heim_2&Value=" + scoresData[i + i]);
					sendToVmix("http://" + vmixIP
							+ "/api/?Function=setText&Input=" + title2
							+ "&SelectedName=Gast_2&Value="
							+ scoresData[i + i + 1]);
					sendToVmix("http://" + vmixIP
							+ "/api/?Function=setText&Input=" + title2
							+ "&SelectedName=Zeitpunkt_2&Value=" + timeData[i]);
				} else {
					sendToVmix("http://" + vmixIP
							+ "/api/?Function=setText&Input=" + title3
							+ "&SelectedName=Heim_2&Value=" + scoresData[i + i]);
					sendToVmix("http://" + vmixIP
							+ "/api/?Function=setText&Input=" + title3
							+ "&SelectedName=Gast_2&Value="
							+ scoresData[i + i + 1]);
					sendToVmix("http://" + vmixIP
							+ "/api/?Function=setText&Input=" + title3
							+ "&SelectedName=Zeitpunkt_2&Value=" + timeData[i]);
				}
			}
		}
	}

	private void sendToVmix(String vmixAPI) throws IOException {
		URL vmixURL = new URL(vmixAPI);
		HttpURLConnection vmixCon = (HttpURLConnection) vmixURL
				.openConnection();
		vmixCon.getResponseMessage();
	}
}