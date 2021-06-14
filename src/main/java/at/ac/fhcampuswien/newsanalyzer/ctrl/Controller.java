package at.ac.fhcampuswien.newsanalyzer.ctrl;

import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.beans.Article;
import at.ac.fhcampuswien.newsapi.beans.NewsResponse;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Controller {

	public static final String APIKEY = "2fab5689ec454f02a0a4433f00e18b2e";  //TODO DONE add your api key

	public void process(NewsApi newsApi) {
		System.out.println("Start process");

		//TODO DONE implement Error handling

		//TODO DONE load the news based on the parameters
		NewsResponse newsResponse = null;
		try {
			newsResponse = newsApi.getNews();
		} catch (Exception e){
			System.out.println("Es ist ein Fehler aufgetreten");
			System.err.println("Error "+e.getMessage());
		}


		//TODO DONE implement methods for analysis
		if (newsResponse != null){
			List<Article> articles = newsResponse.getArticles();
			analyze(articles);
			downloadNews(articles);
		}


		System.out.println("End process");
	}

	private void analyze(List<Article> articles){
		System.out.println("Anzahl Nachrichten: " + articles.size());
		if (articles.size() > 0){
			System.out.println("Häufigste Quelle: " + articles.stream()
					.filter(a -> Objects.nonNull(a.getSource().getName()))
					.collect(Collectors.groupingBy(a -> a.getSource().getName(), Collectors.counting()))
					.entrySet().stream()
					.max(Comparator.comparingInt(v -> v.getValue().intValue()))
					.get().getKey());

			System.out.println("Kürzester Autor: " + articles.stream()
					.filter(a -> Objects.nonNull(a.getAuthor()))
					.min(Comparator.comparing(a -> a.getAuthor().length()))
					.get().getAuthor());

			List<Article> sorted = articles.stream()
					.sorted(Comparator.comparing(Article::getTitle))
					.sorted(Comparator.comparingInt(a -> -a.getTitle().length()))
					.collect(Collectors.toList());
			System.out.println("Sortierte Titel:");
			for (Article a : sorted) {
				System.out.println(a.getTitle());
			}
		}

	}

	private void downloadNews(List<Article> articles){
		for (Article a : articles){
			String urlStr = a.getUrl();
			String subFolder = "Downloads";
			String filename = a.getSource().getName() + " - " + a.getTitle().replaceAll("[:\\\\/*?|<>\"]", "") + ".html";
			try {
				Files.copy(new URL(urlStr).openStream(), Paths.get(subFolder + "/" + filename));
			} catch (MalformedURLException e) {
				System.out.println("Die URL \"" + a.getUrl() + "\" ist ungültig und kann nicht abgerufen werden.");
			} catch (FileAlreadyExistsException e) {
				System.out.println("Die Datei \"" + filename + "\" existiert bereits");
			} catch (IOException e) {
				System.out.println("Es ist ein Problem beim Laden der Datei  \"" + filename + "\"  aufgetreten. ");
				System.err.println("Error "+e.getMessage());
			}
		}
	}
	

	public Object getData() {
		
		return null;
	}
}
