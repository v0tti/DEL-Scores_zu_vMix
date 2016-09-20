import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class Model {

	private String[][] namesData;
	private String[][] scoresData;
	private String[] timeData;

	public void loadData() throws IOException { 
		String url = "https://www.telekomeishockey.de/live-ticker/tickerleiste.html";
		Document doc = Jsoup.connect(url).get();
		Elements matchdata = doc.select(".team");
		Elements time = doc.select("time");
		Date date = new Date();

		System.out.println(date.toString());
		System.out.println("Anzahl Spiele: " + time.size() + "\n");
		namesData = new String[matchdata.size()/2][2];
		scoresData = new String[matchdata.size()/2][2];
		timeData = new String[time.size()];
		
		for (int i = 0; i < time.size(); i++) {
			String[] splitResult1 = matchdata.eq(i + i).text().split(" ");
			String[] splitResult2 = matchdata.eq(i + i + 1).text().split(" ");
			
			namesData[i][0] = splitResult1[0];
			namesData[i][1] = splitResult2[0];
			
			System.out.println(namesData[i][0] + " - " + namesData[i][1]);
			
			if (splitResult1.length == 2 && splitResult2.length == 2) {
				scoresData[i][0] = splitResult1[1];
				scoresData[i][1] = splitResult2[1];
			} else {
				scoresData[i][0] = "-";
				scoresData[i][1] = "-";
			}
			System.out.println("  " + scoresData[i][0] + " - " + scoresData[i][1]);

			timeData[i] = time.eq(i).text(); //Get time String of the home club and replace blanks
			System.out.println(timeData[i] + "\n"); //Print time
		}
	}

	public String[][] getNames() {
		return namesData;
	}

	public String[][] getScores() {
		return scoresData;
	}

	public String[] getTime() {
		return timeData;
	}

	public void sendToVmix(String vmixAPI) throws IOException {
		System.out.println(vmixAPI);
		URL vmixURL = new URL(vmixAPI);
		HttpURLConnection vmixCon = (HttpURLConnection) vmixURL
				.openConnection();
		System.out.println(vmixCon.getResponseMessage());
	}
}