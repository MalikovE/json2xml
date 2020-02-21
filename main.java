package converter;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		final Scanner scanner = new Scanner(System.in);
		final String input = scanner.next();

		final Converter converter = new Converter();
		final String output = converter.apply(input);

		System.out.println(output);
		scanner.close();
	}
}

class Converter {
	public String apply(final String input) {
		if (isXML(input)) {
			return convertXMLtoJSON(input);
		}
		if (isJSON(input)) {
			return convertJSONtoXML(input);
		}
		return null;
	}

	private boolean isXML(final String input) {
		if (input.charAt(0) == '<') {
			return true;
		}
		return false;
	}

	private boolean isJSON(final String input) {
		if (input.charAt(0) == '{') {
			return true;
		}
		return false;
	}

	private String convertXMLtoJSON(final String input) {
		final StringBuilder json = new StringBuilder();
		final StringBuilder tag = new StringBuilder();
		final StringBuilder content = new StringBuilder();

		char ch = '<';
		int endOfTag = 0;
		for (int i = 1; ch != '>'; i++) {
			ch = input.charAt(i);
			if (ch == '/') break;
			if (ch != '>') tag.append(ch);
			else endOfTag = i;
		}

		if (ch != '/') {
			for (int i = endOfTag + 1; ch != '<' ; i++) {
				ch = input.charAt(i);
				if (ch != '<') content.append(ch);
			}

			json.append("{\"")
				.append(tag.toString())
				.append("\":\"")
				.append(content.toString())
				.append("\"}");
		} else {
			json.append("{\"")
				.append(tag.toString())
				.append("\": null }");
		}

		return json.toString();
	}

	private String convertJSONtoXML(final String input) { 
		final StringBuilder xml = new StringBuilder();
		final StringBuilder key = new StringBuilder();
		final StringBuilder content = new StringBuilder();

		char ch = ' ';
		int endOfKey = 0;
		for (int i = 2; ch != '\"'; i++) {
			ch = input.charAt(i);
			if (ch != '\"') key.append(ch);
			else endOfKey = i;
		}

		if (input.charAt(endOfKey + 2) == '\"') {
			ch = ' ';
			for (int i = endOfKey + 3; ch != '\"' ; i++) {
				ch = input.charAt(i);
				if (ch != '\"') content.append(ch);
			}

			xml.append("<")
				.append(key.toString())
				.append(">")
				.append(content.toString())
				.append("</")
				.append(key.toString())
				.append(">");
		} else {
			xml.append("<")
				.append(key.toString())
				.append("/>");
		}

		return xml.toString();
	}
}
