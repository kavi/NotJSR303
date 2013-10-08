package notjsr.testmodel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Country {

	@NotNull
	@Size(min=1, max=50)
	private String name;
	
	@Valid
	private Map<String, CountryName> translations;

	public Country(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void addTranslation(String language, CountryName name) {
		if (translations == null) {
			translations = new HashMap<String, Country.CountryName>();
		}
		translations.put(language, name);
	}
	
	public Map<String, CountryName> getTranslations() {
		if (translations == null) {
			return null;
		}
		return Collections.unmodifiableMap(translations);
	}



	public static class CountryName {
		@Size(min=1, max=50)
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public CountryName(String name) {
			this.name = name;
		}
		
	}
	
	
}
