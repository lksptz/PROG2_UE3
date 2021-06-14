package at.ac.fhcampuswien.newsanalyzer.ui;


import at.ac.fhcampuswien.newsanalyzer.ctrl.Controller;
import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.NewsApiBuilder;
import at.ac.fhcampuswien.newsapi.enums.Category;
import at.ac.fhcampuswien.newsapi.enums.Country;
import at.ac.fhcampuswien.newsapi.enums.Endpoint;
import at.ac.fhcampuswien.newsapi.enums.Language;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserInterface 
{
	private Controller ctrl = new Controller();

	public void getDataFromCtrl1(){
		System.out.println("Corona in Österreich");
		NewsApi newsApi = new NewsApiBuilder()
				.setApiKey(Controller.APIKEY)
				.setQ("corona")
				.setEndPoint(Endpoint.TOP_HEADLINES)
				.setSourceCountry(Country.at)
				.setSourceCategory(Category.health)
				.setPageSize("100")
				.createNewsApi();

		ctrl.process(newsApi);
	}

	public void getDataFromCtrl2(){
		System.out.println("WWDC");
		NewsApi newsApi = new NewsApiBuilder()
				.setApiKey(Controller.APIKEY)
				.setQ("wwdc")
				.setEndPoint(Endpoint.TOP_HEADLINES)
				.setSourceCategory(Category.technology)
				.setLanguage(Language.en)
				.setPageSize("100")
				.createNewsApi();

		ctrl.process(newsApi);
	}

	public void getDataFromCtrl3(){
		System.out.println("EURO 2020");
		NewsApi newsApi = new NewsApiBuilder()
				.setApiKey(Controller.APIKEY)
				.setQ("euro 2020".replaceAll(" ", "%20"))
				.setEndPoint(Endpoint.TOP_HEADLINES)
				.setSourceCategory(Category.sports)
				.setLanguage(Language.en)
				.setPageSize("100")
				.createNewsApi();

		ctrl.process(newsApi);
	}
	
	public void getDataForCustomInput() {
		System.out.print("Zu welchem Thema wollen Sie Nachrichten lesen?: ");
		String query = readLine();
		if (query.replaceAll("\\s", "").equals("")){
			System.out.println("Es wurde kein Suchtext eingegeben!");
			return;
		}
		NewsApi newsApi = new NewsApiBuilder()
				.setApiKey(Controller.APIKEY)
				.setQ(query.replaceAll(" ", "%20"))
				.setEndPoint(Endpoint.TOP_HEADLINES)
				.setPageSize("100")
				.createNewsApi();

		ctrl.process(newsApi);
	}


	public void start() {
		Menu<Runnable> menu = new Menu<>("User Interface");
		menu.setTitle("Wählen Sie aus:");
		menu.insert("a", "Corona in Österreich", this::getDataFromCtrl1);
		menu.insert("b", "WWDC (EN)", this::getDataFromCtrl2);
		menu.insert("c", "EURO 2020 (EN)", this::getDataFromCtrl3);
		menu.insert("d", "Choice User Input:",this::getDataForCustomInput);
		menu.insert("q", "Quit", null);
		Runnable choice;
		while ((choice = menu.exec()) != null) {
			 choice.run();
		}
		System.out.println("Program finished");
	}


    protected String readLine() {
		String value = "\0";
		BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
		try {
			value = inReader.readLine();
        } catch (IOException ignored) {
		}
		return value.trim();
	}

	protected Double readDouble(int lowerlimit, int upperlimit) 	{
		Double number = null;
        while (number == null) {
			String str = this.readLine();
			try {
				number = Double.parseDouble(str);
            } catch (NumberFormatException e) {
                number = null;
				System.out.println("Please enter a valid number:");
				continue;
			}
            if (number < lowerlimit) {
				System.out.println("Please enter a higher number:");
                number = null;
            } else if (number > upperlimit) {
				System.out.println("Please enter a lower number:");
                number = null;
			}
		}
		return number;
	}
}
