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
	private MatchSort sorter = new MatchSort();

	public void loadData() throws IOException { 
		String url = "http://www.del.org/de/statistiken/livescores/page/260----.html";
		Document doc = Jsoup.connect(url).get();
		Elements names = doc.select(".team .wappen");
		Elements scores = doc.select(".team span");
		Elements time = doc.select("span.time");
		Date date = new Date();

		System.out.println(date.toString());
		System.out.println("Anzahl Spiele: " + time.size() + "\n");
		namesData = new String[names.size()/2][2];
		scoresData = new String[scores.size()/2][2];
		timeData = new String[time.size()];

		for (int i = 0; i < time.size(); i++) {
			namesData[i][0] = names.eq(i + i).attr("alt")
					.replaceAll("\\s", "%20");
			namesData[i][1] = names.eq(i + i + 1).attr("alt")
					.replaceAll("\\s", "%20");
			scoresData[i][0] = scores.eq(i + i).text();
			scoresData[i][1] = scores.eq(i + i + 1).text();
			System.out.println(namesData[i][0].replaceAll("%20", " ") + "-"
					+ namesData[i][1].replaceAll("%20", " "));
			System.out.println(scoresData[i][0] + " " + scoresData[i][1]);

			timeData[i] = time.eq(i).text().replaceAll("\\s", "%20"); //Get time String of the home club and replace blanks
			System.out.println(timeData[i].replaceAll("%20", " ") + "\n"); //Print time
		}
		sorter.sortData(namesData,  scoresData, timeData);
	}

	public String[][] getNames() {
		return sorter.getSortedNames();
	}

	public String[][] getScores() {
		return sorter.getSortedScores();
	}

	public String[] getTime() {
		return sorter.getSortedTime();
	}

	public void sendToVmix(String vmixAPI) throws IOException {
		System.out.println(vmixAPI);
		URL vmixURL = new URL(vmixAPI);
		HttpURLConnection vmixCon = (HttpURLConnection) vmixURL
				.openConnection();
		System.out.println(vmixCon.getResponseMessage());
	}
}